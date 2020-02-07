package com.everis.reactivex.operators.suppressing;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
El operador take () tiene dos sobrecargas. Uno tomará un número específico de emisiones y
luego llame a OnComplete () después de que los capture a todos. También dispondrá de la totalidad
suscripción para que no se produzcan más emisiones. Por ejemplo, take (3) emitirá el primer
tres emisiones y luego llame al evento onComplete ():
 */
public class TakeOperator {
    public static void main(String[] args) {
        /*Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .take(3)
                .subscribe(s-> System.out.println("RECEIVED: " + s));*/

        System.out.println("takeIntervalTime");
        takeIntervalTime();
    }

    /*
    * La otra sobrecarga tomará emisiones dentro de un tiempo específico y luego llamará
    * onComplete (). Por supuesto, nuestro Observable frío aquí emitirá tan rápido que podría
    * servir como un mal ejemplo para este caso. Quizás un mejor ejemplo sería usar un
    * Función Observable.interval (). Vamos a emitir cada 300 milisegundos, pero
    * tomar () emisiones durante solo 2 segundos en el siguiente fragmento de código:
    *
    * Es probable que obtenga el resultado que se muestra aquí (cada impresión ocurre cada 300
    * milisegundos). Solo puede obtener seis emisiones en 2 segundos si están espaciadas por 300
    * milisegundos
    * Tenga en cuenta que también hay un operador takeLast (), que tomará el último número especificado de
    * emisiones (o duración de tiempo) antes de que se llame a la función onComplete (). Solo ten en cuenta
    * que pondrá en cola internamente las emisiones hasta que se llame a su función onComplete (), y luego
    * puede identificar y emitir lógicamente las últimas emisiones.
    */
    private static void takeIntervalTime() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .subscribe(i -> System.out.println("RECEIVED: " + i));

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
