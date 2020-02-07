package com.everis.reactivex.operators.reducing;

import io.reactivex.rxjava3.core.Observable;

/*
El operador más simple para consolidar las emisiones en una sola es count (). Va a contar
Se llama a la cantidad de emisiones y emisiones emitidas a través de un solo onComplete (), que se muestra
como sigue:
 */
public class CountOperator {

    public static void main(String[] args) {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .count()
                .subscribe(s -> System.out.println("Received: " + s));
    }
}
