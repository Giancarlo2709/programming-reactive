package com.everis.reactivex.combining.merging;

import io.reactivex.rxjava3.core.Observable;

public class MergeAndMergeWithExample {

    public static void main(String[] args) {

        System.out.println("****************** merge ******************");
        merge();

        System.out.println("****************** mergeWith ******************");
        mergeWith();

    }

    /*
    * El operador Observable.merge () tomará dos o más fuentes Observables <T>
    * emitiendo el mismo tipo T y luego consolidándolos en un solo Observable <T>.
    * Si solo tenemos dos o cuatro fuentes Observables <T> para fusionar, puede pasar cada una como un
    * argumento a la fábrica Observable.merge (). En el siguiente fragmento de código, tengo
    * fusionó dos instancias Observable <String> en una Observable <String>:
     */
    private static void merge() {
        Observable<String> source1 = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observable<String> source2 = Observable.just("Zeta", "Eta", "Theta");

        Observable.merge(source1, source2)
                .subscribe(i -> System.out.println("Received: " + i));

    }

    private static void mergeWith() {
        Observable<String> source1 = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observable<String> source2 = Observable.just("Zeta", "Eta", "Theta");

        source1.mergeWith(source2)
                .subscribe(i -> System.out.println("Received: " + i));
    }
}
