package com.everis.reactivex.operators.suppressing;

import io.reactivex.rxjava3.core.Observable;


public class TakeAndSkipWhileOperator {
    public static void main(String[] args) {

        System.out.println("takeWhileOperator");
        takeWhileOperator();

        System.out.println("skipWhileOperator");
        skipWhileOperator();

    }

    /*
    Otra variante del operador take () es el operador takeWhile (), que toma
    emisiones mientras que una condición derivada de cada emisión es verdadera. El siguiente ejemplo será
    siga tomando emisiones mientras las emisiones son menores a 5. En el momento en que encuentra una que es
    no, llamará a la función onComplete () y eliminará esto:
     */
    private static void takeWhileOperator() {
        Observable.range(1, 100)
                .takeWhile(i -> i < 5)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
    }

    /*
    * Al igual que la función takeWhile (), hay una función skipWhile (). Seguirá saltando
    * emisiones mientras califican con una condición. En el momento en que esa condición ya no
    * califica, las emisiones comenzarán a pasar. En el siguiente código, omitimos las emisiones como
    * siempre que sean menores o iguales a 95. En el momento en que se encuentra una emisión que lo hace
    * Si no cumple con esta condición, permitirá todas las emisiones posteriores en el futuro:
     */
    private static void skipWhileOperator() {
        Observable.range(1,100)
                .skipWhile(i -> i <= 95)
                .subscribe(s-> System.out.println("RECEIVED: " + s));
    }
}
