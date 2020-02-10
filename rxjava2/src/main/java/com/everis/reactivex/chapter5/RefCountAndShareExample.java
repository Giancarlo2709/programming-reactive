package com.everis.reactivex.chapter5;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
El operador refCount () en ConnectableObservable es similar a
autoConnect (1), que se activa después de obtener una suscripción. Pero hay uno importante
diferencia; cuando ya no tenga observadores, se deshacerá de sí mismo y comenzará de nuevo cuando
entra uno nuevo. No persiste la suscripción a la fuente cuando no tiene más
Observadores, y cuando otro observador los sigue, esencialmente "comenzará de nuevo".

 */
public class RefCountAndShareExample {
    public static void main(String[] args) {

        //System.out.println("***************** refCount *****************");
        //refCount();

        System.out.println("***************** share *****************");
        share();
    }

    /*
    * Mire este ejemplo: tenemos Observable.interval () emitiendo cada segundo, y es
    * multidifusión con refCount (). El observador 1 toma cinco emisiones, y el observador 2 toma
    * Dos emisiones. Escalonamos sus suscripciones con nuestra función sleep () para poner tres segundos
    * brechas entre ellos. Debido a que estas dos suscripciones son finitas debido a la toma ()
    * operadores, deben estar terminados para el momento en que Observer 3 entra, y debe haber
    * ya no será ningún observador anterior. Observe cómo Observer 3 ha comenzado de nuevo con un nuevo
    * conjunto de intervalos que comienzan en 0! Echemos un vistazo al siguiente fragmento de código:
     */
    private static void refCount() {
        Observable<Long> seconds =
                Observable.interval(1, TimeUnit.SECONDS)
                        .publish()
                        .refCount();

        //Observer 1
        seconds.take(5)
                .subscribe(l -> System.out.println("Observer 1: " + l));

        sleep(3000);

        //Observer 2
        seconds.take(2)
                .subscribe(l -> System.out.println("Observer 2: " + l));

        sleep(3000);
        //there should be no more observers at this point

        //Observer 3
        seconds.subscribe(l -> System.out.println("Observer 3: " + l));

        sleep(3000);
    }

    private static void share() {
        Observable<Long> seconds =
                Observable.interval(1, TimeUnit.SECONDS).share();

        //Observer 1
        seconds.take(5)
                .subscribe(l -> System.out.println("Observer 1: " + l));

        sleep(3000);

        //Observer 2
        seconds.take(2)
                .subscribe(l -> System.out.println("Observer 2: " + l));

        sleep(3000);
        //there should be no more observers at this point

        //Observer 3
        seconds.subscribe(l -> System.out.println("Observer 3: " + l));

        sleep(3000);


    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
