package com.everis.reactivex.operators.transforming;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
* Podemos posponer las emisiones utilizando el operador delay (). Retendrá cualquier emisión recibida
* y retrasar cada uno por el período de tiempo especificado. Si quisiéramos retrasar las emisiones en tres
* segundos, podríamos hacerlo así:
 */
public class DelayOperator {

    /*
    * Debido a que delay () opera en un planificador diferente (como Observable.interval ()), nosotros
    * necesita aprovechar un método sleep () para mantener la aplicación viva el tiempo suficiente para ver esto
    * ocurrir. Cada emisión se retrasará tres segundos. Puedes pasar un tercero opcional
    * Argumento booleano que indica si también desea retrasar las notificaciones de error.
    * Para casos más avanzados, puede pasar otro Observable como su argumento delay (),
    * y esto retrasará las emisiones hasta que el otro Observable emita algo.
    * Tenga en cuenta que hay un operador delaySubscription (), que retrasará
    * suscribirse al Observable que lo precede en lugar de retrasar cada
    * emisión individual
     */
    public static void main(String[] args) {
        Observable.just("Alpha", "Beta", "Gamma" ,"Delta",
                "Epsilon")
                .delay(3, TimeUnit.SECONDS)
                .subscribe(s -> System.out.println("Received: " + s));
        sleep(5000);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
