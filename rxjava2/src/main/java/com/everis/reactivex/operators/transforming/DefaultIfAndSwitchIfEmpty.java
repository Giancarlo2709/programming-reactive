package com.everis.reactivex.operators.transforming;

import io.reactivex.rxjava3.core.Observable;

public class DefaultIfAndSwitchIfEmpty {

    public static void main(String[] args) {

        System.out.println("************** defaultIfEmpty ************** ");
        defaultIfEmpty();

        System.out.println("************** switchIfEmpty ************** ");
        switchIfEmpty();

    }

    public static void defaultIfEmpty() {
        Observable<String> items =
                Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        items.filter(s -> s.startsWith("B"))
                .defaultIfEmpty("None")
                .subscribe(System.out::println);
    }

    /*
    * Similar a defaultIfEmpty (), switchIfEmpty () especifica un Observable diferente para
    * emitir valores desde si la fuente Observable está vacía. Esto le permite especificar un diferente
    * secuencia de emisiones en caso de que la fuente esté vacía en lugar de emitir solo una
    * valor.
     */
    public static void switchIfEmpty() {
        Observable<String> items =
                Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        items.filter(s-> s.startsWith("Z"))
                .switchIfEmpty(Observable.just("Zeta", "Eta", "Theta"))
                .subscribe(i -> System.out.println("Received: " + i),
                        e -> System.out.println("Received Error: " + e));
    }
}
