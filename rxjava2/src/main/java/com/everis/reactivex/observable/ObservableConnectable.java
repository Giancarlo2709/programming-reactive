package com.everis.reactivex.observable;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

/*
* Una forma útil de Observable caliente es ConnectableObservable. Tomará cualquier
* Observable, incluso si hace frío, y caliéntelo para que todas las emisiones se reproduzcan para todos
* Observadores a la vez. Para hacer esta conversión, simplemente debe llamar a publicar () en cualquier
* Observable, y producirá un ConnectableObservable. Pero la suscripción no comenzará
* las emisiones todavía. Debe llamar a su método connect () para comenzar a disparar las emisiones. Esta
* le permite configurar todos sus observadores de antemano. Echa un vistazo al siguiente código
* retazo:
 */
public class ObservableConnectable {

    /*
    * Observe cómo un observador recibe la cadena mientras que el otro recibe la longitud y
    * los dos los imprimen de forma intercalada. Ambas suscripciones están configuradas
    * de antemano, y luego se llama a connect () para disparar las emisiones. En lugar de observador 1
    * procesando todas las emisiones antes del observador 2, cada emisión va a cada observador
    * simultaneamente. El observador 1 recibe Alpha y el observador 2 recibe 5 y luego
    * Beta y 4, y así sucesivamente. Usando ConnectableObservable para forzar que cada emisión vaya a todos
    * Los observadores simultáneamente se conocen como multicasting, que cubriremos en detalle en
    * Capítulo 5, Multicasting.
     */
    public static void main(String[] args) {
        ConnectableObservable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon").publish();

        //set up observer 1
        source.subscribe(s-> System.out.println("Observer 1: " + s));

        //set up observer 2
        source.map(String::length).filter(integer -> integer >= 5)
                .subscribe(i -> System.out.println("Observer 2: " + i));

        //fire!
        source.connect();

    }
}
