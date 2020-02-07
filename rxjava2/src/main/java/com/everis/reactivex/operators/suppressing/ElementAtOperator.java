package com.everis.reactivex.operators.suppressing;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

/*
* Puede obtener una emisión específica por su índice especificado por Long, comenzando en 0. Después de ese elemento
* se encuentra y se emite, se llamará a onComplete () y se eliminará la suscripción
* de.
* Si desea que la cuarta emisión provenga de un Observable, puede hacerlo como se muestra
* en el siguiente fragmento de código:
 */
public class ElementAtOperator {

    /*
    * Puede que no lo hayas notado, pero elementAt () devuelve Maybe <T> en su lugar
    * de Observable <T>. Esto se debe a que producirá una emisión, pero si hay menos
    * emisiones que el índice buscado, estará vacío.
     */
    public static void main(String[] args) {
        Maybe<String> source = Observable.just("Alpha", "Beta", "Zeta", "Eta", "Gamma",
                "Delta")
                .elementAt(3);

        source.subscribe(s-> System.out.println("RECEIVED: " + s), Throwable::printStackTrace, () -> System.out.println("Done!"));
    }
}
