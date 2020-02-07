package com.everis.reactivex.operators.reducing;

import io.reactivex.rxjava3.core.Observable;

/*
El operador reduce () es sintácticamente idéntico a scan (), pero solo emite el final
acumulación cuando la fuente invoca onComplete (). Dependiendo de qué sobrecarga
uso, puede producir Single o Maybe. Si desea emitir la suma de todas las emisiones enteras, usted
puede tomar cada uno y agregarlo al total rodante. Pero solo emitirá una vez que esté finalizado:
*/
public class ReduceOperator {

    public static void main(String[] args) {
        Observable.just(5, 3, 7, 10, 2, 14)
                .reduce((total, next) -> total + next)
                .subscribe(s -> System.out.println("Received: " + s));

        System.out.println("****************** reduceInitialValue ******************");
        reduceInitialValue();
    }

    /*
    * Similar a scan (), hay un argumento semilla que puede proporcionar que servirá como
    * valor inicial para acumular. Si quisiéramos convertir nuestras emisiones en una sola coma separada
    * cadena de valor, podríamos usar reduce () de esta manera, como se muestra a continuación:
     */
    private static void reduceInitialValue() {
        Observable.just(5, 3, 7, 10, 2, 14)
                .reduce("", (total, next) -> total + (total.equals("") ?
                        "" :
                        ",") + next)
                .subscribe(s -> System.out.println("Received: " + s));
    }

}
