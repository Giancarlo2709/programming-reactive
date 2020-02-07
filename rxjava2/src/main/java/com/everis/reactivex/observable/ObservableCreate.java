package com.everis.reactivex.observable;

import io.reactivex.rxjava3.core.Observable;

public class ObservableCreate {

    /*
    * El método onComplete () se utiliza para comunicar la cadena al Observador que no
    * Vienen más artículos. Los observables pueden ser infinitos, y si este es el caso, el
    * El evento onComplete () nunca se llamará. Técnicamente, una fuente podría dejar de emitir
    * onNext () llama y nunca llama a onComplete (). Sin embargo, esto probablemente sería un mal diseño si
    * la fuente ya no planea enviar emisiones.
    */
    public static void main( String[] args ){
        Observable<String> source = Observable.create(emitter -> {
            emitter.onNext("Alpha");
            emitter.onNext("Beta");
            emitter.onNext("Gamma");
            emitter.onNext("Delta");
            emitter.onNext("Epsilon");
            emitter.onComplete();
        });

        source.subscribe(s -> System.out.println("RECEIVED: " + s));

        System.out.println("**************** onError ****************");
        managementsErrors();

        System.out.println("**************** mapAndFilterWithObservable ****************");
        mapAndFilterWithObservable();
    }

    /*
    * Aunque es poco probable que este ejemplo en particular arroje un error, podemos detectar errores que pueden
    * ocurrir dentro de nuestro bloque Observable.create () y emitirlos a través de onError (). Esta
    * De esta manera, el error puede ser empujado hacia arriba en la cadena y manejado por el observador. Este particular
    * El observador que hemos configurado no maneja las excepciones, pero puede hacerlo, como se muestra
    * aquí:
    */
    public static void managementsErrors() {
        Observable<String> source = Observable.create(emitter -> {
            try {
                emitter.onNext("Alpha");
                emitter.onNext("Beta");
                emitter.onNext("Gamma");
                emitter.onNext("Delta");
                emitter.onNext("Epsilon");
                emitter.onComplete();
            } catch(Throwable e) {
                emitter.onError(e);
            }
        });

        source.subscribe(s -> System.out.println("RECEIVED: " + s), Throwable::printStackTrace);
    }

    /*
    * Tenga en cuenta que onNext (), onComplete () y onError () no necesariamente empujan directamente a
    * El observador final. También pueden empujar a un operador que sirve como el siguiente paso en la cadena.
    * En el siguiente código, derivamos nuevos Observables con los operadores map () y filter (),
    * que actuará entre la fuente Observable y el Observador final que imprime los elementos:
     */
    public static void mapAndFilterWithObservable() {
        Observable<String> source = Observable.create(emitter -> {
            try {
                emitter.onNext("Alpha");
                emitter.onNext("Beta");
                emitter.onNext("Gamma");
                emitter.onNext("Delta");
                emitter.onNext("Epsilon");
                emitter.onComplete();
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });

        source.map(String::length)
            .filter(s -> s >= 5)
            .subscribe(s -> System.out.println("RECEIVED: " + s));
    }

}
