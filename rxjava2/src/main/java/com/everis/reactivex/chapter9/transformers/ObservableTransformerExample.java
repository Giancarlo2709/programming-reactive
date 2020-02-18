package com.everis.reactivex.chapter9.transformers;

import com.everis.reactivex.util.ObservableUtil;
import com.google.common.collect.ImmutableList;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;

public class ObservableTransformerExample {

    public static void main(String[] args) {
        System.out.println("************ base ************");
        base();

        System.out.println("************ refactor ************");
        refactor();
    }

    /*
     * Recuperar Google Guava como una dependencia. En el Capítulo 3, Operadores básicos, cubrimos los
     * operador collect () y lo usó para convertir Observable <T> en
     * una sola <ImmutableList <T>>. Efectivamente, queremos recolectar emisiones de T en un Google
     * Lista inmutable de guayaba <T>. Supongamos que hacemos esta operación suficientes veces hasta que comience a
     * sentirse redundante Aquí, usamos esta operación ImmutableList para dos observables diferentes
     * suscripciones:
     */
    private static void base() {
        ObservableUtil.GREEK_ALPHABET
                .collect(ImmutableList::builder, ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .subscribe(System.out::println);

        Observable.range(1, 15)
                .collect(ImmutableList::builder, ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .subscribe(System.out::println);
    }

    /*
    Es un poco redundante invocar dos veces, por lo que es posible que podamos componer estos operadores
    en un solo operador que recolecta emisiones en una Lista Inmutable? Como una cuestión de hecho,
    ¡si! Para apuntar a un Observable <T>, puede implementar ObservableTransformer <T, R>.
    Este tipo tiene un método apply () que acepta un observable <T> ascendente y devuelve
    un <R> observable aguas abajo. En su implementación, puede devolver un Observable
    cadena que agrega cualquier operador a la cadena ascendente, y después de esas transformaciones, devuelve
    un observable <R>.
    Para nuestro ejemplo, apuntaremos a cualquier tipo genérico T para un Observable <T> dado, y R
    ser una ImmutableList <T> emitida a través de una Observable <ImmutableList <T>>. Lo haremos
    empaquete todo esto en un ObservableTransformer <T, ImmutableList <T>>
    implementación, como se muestra en el siguiente fragmento de código:
     */
    private static void refactor() {
        ObservableUtil.GREEK_ALPHABET
                .compose(toImmutableList())
                .subscribe(System.out::println);

        Observable.range(1, 10)
                .compose(toImmutableListWithLambda())
                .subscribe(System.out::println);
    }

    private static <T> ObservableTransformer<T, ImmutableList<T>> toImmutableList() {
        return new ObservableTransformer<T, ImmutableList<T>>() {
            @Override
            public @NonNull ObservableSource<ImmutableList<T>> apply(@NonNull Observable<T> upstream) {
                return upstream.collect(ImmutableList::<T>builder,
                        ImmutableList.Builder::add)
                        .map(ImmutableList.Builder::build)
                        .toObservable(); // Debe convertir Single en Observable
            }
        };
    }

    private static <T> ObservableTransformer<T, ImmutableList<T>> toImmutableListWithLambda() {
        return upstream -> upstream.collect(ImmutableList::<T>builder,
                ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .toObservable(); //Debe convertir Single en Observable
    }
}
