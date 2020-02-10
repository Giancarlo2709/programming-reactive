package com.everis.reactivex.chapter4.combining.concatenating;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
* La fábrica Observable.concat () es la concatenación equivalente a
* Observable.merge (). Combinará las emisiones de múltiples Observables, pero disparará
* cada uno de forma secuencial y solo pasar al siguiente después de que se llame a onComplete ().
* En el siguiente código, tenemos dos observables de origen que emiten cadenas. Nosotros podemos usar
* Observable.concat () para disparar las emisiones del primero y luego disparar las emisiones
* del segundo:
 */
public class ConcatAndConcatWithExample {

    public static void main(String[] args) {

        System.out.println("*************** concat ***************");
        concat();

        System.out.println("*************** concatWith ***************");
        concatWith();

        System.out.println("*************** concatWithObservableInfinite ***************");
        //concatWithObservableInfinite();

        System.out.println("*************** concatMap ***************");
        concatMap();
    }

    private static void concat() {
        Observable<String> source1 = Observable.just("Alpha", "Beta", "Gamma", "Delta",
                        "Epsilon");

        Observable<String> source2 = Observable.just("Zeta", "Eta", "Theta");

        Observable.concat(source1, source2)
                .subscribe(i -> System.out.println("Received: " + i));
    }

    private static void concatWith() {
        Observable<String> source1 = Observable.just("Alpha", "Beta", "Gamma", "Delta",
                "Epsilon");

        Observable<String> source2 = Observable.just("Zeta", "Eta", "Theta");

        source1.concatWith(source2)
                .subscribe(i -> System.out.println("Received: " + i));
    }

    /*
    *  Si usamos Observable.concat () con observables infinitos, siempre emitirá desde
    * el primero que encuentra y evita que se disparen los siguientes Observables. Si alguna vez queremos
    * para poner un observable infinito en cualquier parte de una operación de concatenación, probablemente sería
    * especificado al final. Esto asegura que no contenga ningún Observable que lo siga porque
    * no hay ninguno. También podemos usar los operadores take () para hacer que los Observables infinitos sean finitos.
    * Aquí, disparamos un Observable que emite cada segundo, pero solo toma dos emisiones.
    * Después de eso, llamará a onComplete () y lo eliminará. Entonces un segundo
    * Observable concatenado después de que se emitirá para siempre (o en este caso, cuando la aplicación
    * se cierra después de cinco segundos). Dado que este segundo Observable es el último especificado en
    * Observable.concat (), no retendrá ningún Observable posterior al ser infinito:
     */
    private static void concatWithObservableInfinite() {
        //emit every second, but only take 2 emissions
        Observable<String> source1 = Observable.interval(1, TimeUnit.SECONDS)
                .take(5)
                .map(l -> l + 1)//emited elapsed seconds
                .map(l -> "Source1: " + l + " seconds");

        //emit every 300 milliseconds
        Observable<String> source2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 300) // emited elapsed milliseconds
                .map(l -> "Source2: " + l + " milliseconds");

        Observable.concat(source1, source2)
                .subscribe(i -> System.out.println("Received: " + i));

        sleep(7000);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * deberíamos usar concatMap () cuando explícitamente nos importa
    *  manteniendo el orden y desea procesar cada Observable mapeado secuencialmente:
     */
    private static void concatMap() {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta",
                        "Epsilon");

        source.concatMap(s -> Observable.fromArray(s.split("")))
                .subscribe(System.out::println);
    }
}
