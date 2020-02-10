package com.everis.reactivex.chapter4.combining.combinelatest;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
La fábrica Observable.combineLatest () es algo similar a zip (), pero para cada
emisión que se dispara desde una de las fuentes, inmediatamente se unirá con la última
emisión de cualquier otra fuente. No pondrá en cola las emisiones no emparejadas para cada fuente,
sino más bien almacenar en caché y emparejar el último.
 */
public class CombineLatestExample {

    public static void main(String[] args) {
        System.out.println("***************** combineLatest *****************");
        //combineLatest();

        System.out.println("***************** withLatestFrom *****************");
        withLatestFrom();
    }

    /*
    * Aquí, usemos Observable.combineLatest () entre dos Observables de intervalo, el primero
    * emitiendo a 300 milisegundos y el otro cada segundo:
    * Están sucediendo muchas cosas aquí, pero intentemos analizarlo. source1 está emitiendo cada 300
    * milisegundos, pero las dos primeras emisiones aún no tienen nada con lo que emparejarse
    * source2, que emite cada segundo, y aún no se ha producido ninguna emisión. Finalmente despues de uno
    * segundo, source2 empuja su primera emisión 0, y se empareja con la última emisión 2 (la tercera
    * emisión) de la fuente1. Tenga en cuenta que las dos emisiones anteriores 0 y 1 de la fuente1 fueron
    * completamente olvidado porque la tercera emisión 2 es ahora la última emisión. fuente1 entonces
    * empuja 3, 4 y luego 5 a intervalos de 300 milisegundos, pero 0 sigue siendo la última emisión de
    * source2, por lo que los tres se emparejan con él. Entonces, source2 emite su segunda emisión 1, y se empareja
    * con 5, la última emisión de la fuente2.
    * En términos más simples, cuando se dispara una fuente, se combina con las últimas emisiones de las otras.
    * Observable.combineLatest () es especialmente útil para combinar entradas de la interfaz de usuario, como anteriormente
    * las entradas de los usuarios son frecuentemente irrelevantes y solo las últimas son motivo de preocupación
     */
    private static void combineLatest() {
        Observable<Long>  source1 = Observable.interval(300, TimeUnit.MILLISECONDS);

        Observable<Long> source2 = Observable.interval(1, TimeUnit.SECONDS);

        Observable.combineLatest(source1, source2, (l1,l2) -> "Source1 : " + l1 + " Source2:  " + l2)
                .subscribe(System.out::println);

        sleep(3000);
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * Similar a Observable.combineLatest (), pero no exactamente igual, es el
    * withLatestfrom () operador. Mapeará cada emisión T con los últimos valores de otros
    * Los observa y los combina, pero solo tomará una emisión de cada uno de los otros
    * Observables:
     */
    private static void withLatestFrom() {
        Observable<Long>  source1 = Observable.interval(300, TimeUnit.MILLISECONDS);

        Observable<Long> source2 = Observable.interval(1, TimeUnit.SECONDS);

        source2.withLatestFrom(source1, (l1, l2) -> "Source2: " + l1 + " Source1: " + l2)
                .subscribe(System.out::println);

        sleep(3000);
    }
}
