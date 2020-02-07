package com.everis.reactivex.operators.transforming;

import io.reactivex.rxjava3.core.Observable;

/*
El método scan () es un agregador continuo. Para cada emisión, lo agrega a un
acumulación. Entonces, emitirá cada acumulación incremental.
Por ejemplo, puede emitir la suma acumulativa para cada emisión pasando una lambda a
Método thescan () que agrega cada próxima emisión al acumulador:
 */
public class ScanOperator {

    public static void main(String[] args) {
        Observable.just(5, 3, 7, 10, 2, 14)
                .scan((accumulator, next) -> accumulator + next)
                .subscribe(s -> System.out.println("Received: " + s));

        System.out.println("************** scanInitialValue *****************");
        scanInitialValue();
    }

    private static  void scanInitialValue() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .scan(0, (total, next) -> total + 1)
                .subscribe(s -> System.out.println("Received: " + s));
    }
}
