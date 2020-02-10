package com.everis.reactivex.chapter5;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

import java.util.concurrent.ThreadLocalRandom;

public class MultiCastingExample {

    public static void main(String[] args) {
        Observable<Integer> threeIntegers = Observable.range(1,3);

        threeIntegers.subscribe(i -> System.out.println("Observer One: " + i));

        threeIntegers.subscribe(i -> System.out.println("Observer Two: " + i));

        System.out.println("************** castingConnectableObservable **************");
        castingConnectableObservable();

        System.out.println("************** multiCasting **************");
        multiCasting();

        System.out.println("************** multiCastingConnectable **************");
        multiCastingConnectable();

        System.out.println("************** connectableObservableRandom **************");
        connectableObservableRandom();

        System.out.println("************** multiCastingWithReduce **************");
        multiCastingWithReduce();

    }

    /*
    * Aquí, Observer One recibió las tres emisiones y solicitó onComplete (). Después de esto,
    * El observador dos recibió las tres emisiones (que se regeneraron nuevamente) y llamó
    * onComplete (). Estas fueron dos corrientes separadas de datos generados para dos
    * suscripciones Si quisiéramos consolidarlos en una sola secuencia de datos que empuje
    * cada emisión a ambos observadores simultáneamente, podemos llamar a publicar () en Observable,
    * que devolverá un ConnectableObservable. Podemos configurar los observadores de antemano
    * y luego llame a connect () para comenzar a disparar las emisiones para que ambos Observadores reciban lo mismo
    * emisiones simultáneamente. Esto se indicará mediante la impresión de cada observador.
    * intercalando aquí:
     */
    private static void castingConnectableObservable() {
        ConnectableObservable<Integer> threeIntegers =
                Observable.range(1, 3).publish();

        threeIntegers.subscribe(i -> System.out.println("Observer One: " + i));
        threeIntegers.subscribe(i -> System.out.println("Observer Two: " + i));

        threeIntegers.connect();

        /*
        * El uso de ConnectableObservable hará que las emisiones de la fuente se calienten,
        * empujando un solo flujo de emisiones a todos los Observadores al mismo tiempo en lugar de dar un
        * flujo separado a cada observador. Esta idea de consolidación de flujo se conoce como
        * multidifusión, pero hay matices, especialmente cuando los operadores se involucran. Incluso
        * cuando llamas a Publish () y usas un ConnectableObservable, cualquier operador que siga
        * puede crear secuencias separadas nuevamente. Echaremos un vistazo a este comportamiento y cómo gestionarlo.
        * siguiente.
         */
    }

    /*
    * Comencemos por emitir los números del 1 al 3 y asigne cada uno a un entero aleatorio
    * entre 0 y 100,000. Si tenemos dos observadores, podemos esperar enteros diferentes para cada
    * uno. Tenga en cuenta que su producción será diferente a la mía debido al número aleatorio
    * generación y simplemente reconocen que ambos observadores están recibiendo diferentes aleatorios
    * enteros:
     */
    private static void multiCasting() {
        Observable<Integer> threeRandoms = Observable.range(1,3)
                .map(i -> randomInt());

        threeRandoms.subscribe(i -> System.out.println("Observer 1: "+ i));

        threeRandoms.subscribe(i -> System.out.println("Observer 2: "+ i));
    }

    private static int randomInt() {
        return ThreadLocalRandom.current().nextInt(100000);
    }

    private static void multiCastingConnectable() {
        ConnectableObservable<Integer> threeInts = Observable.range(1,3)
                .publish();

        Observable<Integer> threeRandoms = threeInts.map(i -> randomInt());

        threeRandoms.subscribe(i -> System.out.println("Observer 1: "+ i));

        threeRandoms.subscribe(i -> System.out.println("Observer 2: "+ i));

        threeInts.connect();
    }

    /*
    * Si queremos evitar que el operador map () produzca dos secuencias separadas para cada
    * Observador, debemos llamar a publicar () después de map () en su lugar:
    */
    private static void  connectableObservableRandom() {
        ConnectableObservable<Integer> threeRandoms = Observable.range(1,3)
                .map(i -> randomInt()).publish();

        threeRandoms.subscribe(i -> System.out.println("Observer 1: " + i));

        threeRandoms.subscribe(i -> System.out.println("Observer 2: " + i));

        threeRandoms.connect();
    }

    /*
    * puede tener un observador que imprima los enteros aleatorios pero otro
    * que encuentra la suma con reduce (). En este punto, ese flujo único debería, de hecho, bifurcarse en
    * dos flujos separados porque ya no son redundantes y realizan un trabajo diferente, como
    * se muestra en el siguiente fragmento de código:
     */
    private static void multiCastingWithReduce() {
        ConnectableObservable<Integer> threeRandoms = Observable.range(1, 3)
                .map(i -> randomInt()).publish();

        //Observer 1 - print each random integer
        threeRandoms.subscribe(i -> System.out.println("Observer 1: " + i));

        threeRandoms.reduce(0, (total, next) -> total + next)
                .subscribe(i -> System.out.println("Observer 2: " + i));

        threeRandoms.connect();
    }
}
