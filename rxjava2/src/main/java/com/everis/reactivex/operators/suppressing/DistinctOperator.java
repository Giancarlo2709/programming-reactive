package com.everis.reactivex.operators.suppressing;

import io.reactivex.rxjava3.core.Observable;

import javax.print.DocFlavor;

/*
El operador distinct () emitirá cada emisión única, pero suprimirá cualquier
duplicados que siguen. La igualdad se basa en la implementación de hashCode () / equals () de
objetos emitidos Si quisiéramos emitir las distintas longitudes de una secuencia de cadena, podría ser
hecho de la siguiente manera:
 */
public class DistinctOperator {

    public static void main(String[] args) {
        Observable.just("Alpha", "Beta", "Gamma", "Delta",
                "Epsilon")
                .map(String::length)
                .distinct()
                .subscribe(i -> System.out.println("RECEIVED: " + i));

        System.out.println("*********** distinctWithLambda ***********");
        distinctWithLambda();

        System.out.println("*********** distinctUntilChanged ***********");
        distinctUntilChanged();

        System.out.println("*********** distinctUntilChangedWithLambda ***********");
        distinctUntilChangedWithLambda();
    }

    /*
    * Tenga en cuenta que si tiene un espectro amplio y diverso de valores únicos, distintos () pueden
    * usa un poco de memoria. Imagine que cada suscripción da como resultado un HashSet que rastrea
    * valores únicos previamente capturados.
    * También puede agregar un argumento lambda que asigne cada emisión a una clave utilizada para la igualdad
    * lógica. Esto permite que las emisiones, pero no la clave, avancen mientras se usa la tecla para
    * lógica distinta Por ejemplo, podemos quitar la longitud de cada cadena y usarla para unicidad, pero
    * emitir las cadenas en lugar de sus longitudes:
     */
    private static void distinctWithLambda() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .distinct(String::length)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
    }

    /*
    * La función distinctUntilChanged () ignorará las emisiones duplicadas consecutivas. Es
    * Una forma útil de ignorar las repeticiones hasta que cambien. Si se emite el mismo valor
    * repetidamente, todos los duplicados serán ignorados hasta que se emita un nuevo valor. Duplicados de la
    * el siguiente valor será ignorado hasta que cambie nuevamente, y así sucesivamente. Observe la salida para el
    * código siguiente para ver este comportamiento en acción:
     */
    private static void distinctUntilChanged() {
        Observable.just(1, 1, 1, 2, 2, 3, 3, 2, 1, 1)
                .distinctUntilChanged()
                .subscribe(i -> System.out.println("RECEIVED: " + i));
    }

    private static void distinctUntilChangedWithLambda() {
        Observable.just("Alpha", "Beta", "Zeta", "Eta", "Gamma",
                "Delta")
                .distinctUntilChanged(String::length)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
    }
}
