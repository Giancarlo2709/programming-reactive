package com.everis.reactivex.chapter4.combining.grouping;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.GroupedObservable;

/*
Una operación poderosa que puede lograr con RxJava es agrupar las emisiones por un determinado
clave en observables separados. Esto se puede lograr llamando al operador groupBy (),
que acepta un lambda que asigna cada emisión a una clave. Luego devolverá un
Observable <GroupedObservable <K, T >>, que emite un tipo especial de Observable
llamado GroupedObservable. GroupedObservable <K, T> es como cualquier otro
Observable, pero tiene el valor clave K accesible como una propiedad. Emitirá las emisiones de T
que se asignan para esa clave dada.
Por ejemplo, podemos usar el operador groupBy () para agrupar las emisiones de un
Observable<String> por la longitud de cada cadena. Nos suscribiremos en un momento, pero
así es como lo declaramos:
 */
public class GroupingExample {
    public static void main(String[] args) {

        System.out.println("************** groupBy **************");
        groupBy();

        System.out.println("************** groupByGetKey **************");
        groupByGetKey();

    }

    private static void groupBy() {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observable<GroupedObservable<Integer, String>> byLengths = source.groupBy(String::length);

        byLengths.flatMapSingle(grp -> grp.toList())
                .subscribe(s -> System.out.println("Received: " + s));
    }

    private static void groupByGetKey() {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observable<GroupedObservable<Integer, String>> byLengths =
                source.groupBy(String::length);

        byLengths.flatMapSingle(grp ->
                    grp.reduce("", (x,y) -> x.equals("") ? y : x + ", " + y)
                            .map(s -> grp.getKey() + ": " + s)
                )
                .subscribe(System.out::println);
    }
}
