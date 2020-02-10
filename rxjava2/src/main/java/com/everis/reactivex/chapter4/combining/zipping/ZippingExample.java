package com.everis.reactivex.chapter4.combining.zipping;


import io.reactivex.rxjava3.core.Observable;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/*
Puede pasar hasta nueve instancias de Observable a la fábrica Observable.zip (). Si tu
necesita más que eso, puede pasar un Iterable <Observable <T>> o usar zipArray () para
proporcionar una matriz Observable []. Tenga en cuenta que si una o más fuentes producen emisiones
más rápido que otro, zip () pondrá en cola esas emisiones rápidas mientras esperan más despacio
fuente para proporcionar emisiones. Esto podría causar problemas de rendimiento no deseados ya que cada
colas de origen en la memoria. Si solo le importa comprimir la última emisión de cada
fuente en lugar de recuperar una cola completa, querrás usar combineLatest (),
que cubriremos más adelante en esta sección.
 */
public class ZippingExample {
    public static void main(String[] args) {

        System.out.println("*************** zip ***************");
        zip();

        System.out.println("*************** zipWith ***************");
        zipWith();

        System.out.println("*************** zipWithInterval ***************");
        zipWithInterval();
    }

    /*
    * Zipping le permite tomar una emisión de cada fuente Observable y combinarla en
    * Una sola emisión. Cada Observable puede emitir un tipo diferente, pero puede combinar estos
    * diferentes tipos emitidos en una sola emisión. Aquí hay un ejemplo, si tenemos un
    * Observable <String> y un Observable <Integer>, podemos comprimir cada String y
    * Integer juntos en un emparejamiento uno a uno y concatenarlo con una lambda:
     */
    private static void zip() {
        Observable<String> source1 = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observable<Integer> source2 = Observable.range(1, 6);

        Observable.zip(source1, source2, (s, i) -> s +" - " + i)
                .subscribe(System.out::println);

        /*
        * La función zip () recibió tanto Alpha como 1 y luego los emparejó en un
        * cadena concatenada separada por un guión, y la empujó hacia adelante. Entonces, recibió
        * Beta y 2 y los emitió hacia adelante como una concatenación, y así sucesivamente. Una emisión de
        * un observable debe esperar para emparejarse con una emisión del otro observable. Si
        * uno Observable llama a Complete () y el otro todavía tiene emisiones esperando para llegar
        * emparejado, esas emisiones simplemente disminuirán, ya que no tienen nada con qué asociarse. Esta
        * sucedió con la emisión 6 ya que solo teníamos cinco emisiones de cuerda.
         */
    }

    private static void zipWith() {
        Observable<String> source1 = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observable<Integer> source2 = Observable.range(1, 6);

        source1.zipWith(source2, (s, i) -> s+ " - " + i)
            .subscribe(System.out::println);
    }

    /*
    * La compresión también puede ser útil para ralentizar las emisiones utilizando Observable.interval ().
    * Aquí, comprimimos cada cadena con un intervalo de 1 segundo. Esto ralentizará cada emisión de cuerda por
    * un segundo, pero tenga en cuenta que las emisiones de cinco cadenas probablemente se pondrán en cola mientras esperan
    * un intervalo de emisión para emparejar con:
     */
    private static void zipWithInterval() {
        Observable<String> strings = Observable.just("Alpha", "Beta", "Gamma", "Delta",
                        "Epsilon");

        Observable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS);

        Observable.zip(strings, seconds, (s, l) -> s)
                .subscribe(s -> System.out.println("Received: " + s + " at " + LocalDateTime.now()));

        sleep(6000);
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
