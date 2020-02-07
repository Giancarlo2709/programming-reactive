package com.everis.reactivex.operators.reducing;

import io.reactivex.rxjava3.core.Observable;

/*
El operador contains () verificará si un elemento específico (basado en el
hashCode () / equals () implementación) siempre emite desde un Observable. Devolverá un
Single <Boolean> que emitirá verdadero si se encuentra y falso si no lo es.
En el siguiente fragmento de código, emitimos los enteros del 1 al 10000, y verificamos si
el número 9563 se emite usando contiene ():
 */
public class ContainsOperator {

    public static void main(String[] args) {
        Observable.range(1,10000)
                .contains(9563)
                .subscribe(s -> System.out.println("Received: " + s));
    }
}
