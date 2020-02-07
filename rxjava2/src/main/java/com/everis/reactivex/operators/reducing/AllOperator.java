package com.everis.reactivex.operators.reducing;

import io.reactivex.rxjava3.core.Observable;

public class AllOperator {

    /*
    * El operador all () verifica que cada emisión califique con una condición específica y
    * devolver un Single <Boolean>. Si todos pasan, emitirá True. Si encuentra uno que falla,
    * emitirá inmediatamente False. En el siguiente fragmento de código, emitimos una prueba contra seis
    * enteros, verificando que todos son menores de 10:
     */
    public static void main(String[] args) {
        Observable.just(5, 3, 7, 11, 2, 14)
                .all(i -> i < 15)
                .subscribe(s -> System.out.println("Received: " + s));
    }
}
