package com.everis.reactivex.observable;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ObservableFactory {

    private static int start = 1;
    private static int count = 5;

    public static void main(String[] args) {

        System.out.println("******************** createFromRange ********************");
        //createFromRange();

        System.out.println("******************** createFromInterval ********************");
        //createFromInterval();

        System.out.println("******************** createFromIntervalColdOrHot ********************");
        //createFromIntervalColdOrHot();

        System.out.println("******************** intervalConnectable ********************");
        //intervalConnectable();

        System.out.println("******************** future ********************");
        //future();

        System.out.println("******************** createEmpty ********************");
        //createEmpty();

        System.out.println("******************** createNever ********************");
        //createNever();

        System.out.println("******************** createError ********************");
        //createError();

        System.out.println("******************** createDefer ********************");
        //createDeferProblem();

        System.out.println("******************** createDeferResolveProblem ********************");
        createDeferResolveProblem();
    }

    /*
    * Para emitir un rango consecutivo de enteros, puede usar Observable.range (). Esto emitirá
    * cada número desde un valor inicial e incremente cada emisión hasta que el recuento especificado sea
    * alcanzado. Todos estos números se pasan a través del evento onNext (), seguido del
    * Evento onComplete ():
     */
    public static void createFromRange() {
        Observable.range(5,10)
                .subscribe(s -> System.out.println("RECEIVED: " + s), Throwable::printStackTrace, () -> System.out.println("Done!"));
    }

    /*
    * Como hemos visto, los Observables tienen un concepto de emisiones a lo largo del tiempo. Las emisiones son entregadas
    * desde la fuente hasta el observador secuencialmente. Pero estas emisiones pueden espaciarse
    * tiempo dependiendo de cuando la fuente los proporcione. Nuestro ejemplo JavaFX con
    * ToggleButton demostró esto, ya que cada clic daba como resultado una emisión de verdadero o falso.
    * Pero veamos un ejemplo simple de un Observable basado en el tiempo usando
    * Observable.interval (). Emitirá una emisión larga consecutiva (a partir de 0) en cada
    * intervalo de tiempo especificado. Aquí, tenemos un Observable <Long> que emite cada segundo:
     */
    public static void createFromInterval(){
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(s-> System.out.println(s + " Mississippi"));

        sleep(5000);
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /*
    * El interval es frio o caliente, observemos el sgte ejemplo:
    * */
    public static void createFromIntervalColdOrHot() {
        Observable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS);

        //observer 1
        seconds.subscribe(l -> System.out.println("Observer 1: " + l));

        sleep(5000);

        //observer 2
        seconds.subscribe(l-> System.out.println("Observer 2: " + l));

        sleep(5000);
    }

    public static void intervalConnectable() {
        ConnectableObservable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS).publish();

        //Observer 1
        seconds.subscribe(l -> System.out.println("Observer 1: " + l));
        seconds.connect();

        sleep(5000);

        //observer 2
        seconds.subscribe(l -> System.out.println("Observer 2: " + l));

        sleep(5000);

    }

    public static void future() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Future<String> futureValue = executor.submit(() -> {
                                        TimeUnit.SECONDS.sleep(5);
                                        System.out.println("Ending processVeryLong...");
                                        return "result";
                                    });

        Observable.fromFuture(futureValue)
                .map(String::length)
                .subscribe(System.out::println);

        futureValue.cancel(true);
    }

    /* Un Observable empty es el concepto de optional de Java 8 */
    private static void createEmpty() {
        Observable<String> empty = Observable.empty();

        empty.subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("Done!"));
    }

    /*
    * Un primo cercano de Observable.empty () es Observable.never (). La unica diferencia
    * entre ellos es que nunca llama a Complete (), dejando para siempre a los observadores esperando
    * emisiones pero nunca dando ninguna:
    */
    private static void createNever() {
        Observable<String> empty = Observable.never();

        empty.subscribe(System.out::println, Throwable::printStackTrace, () -> System.out.println("Done!"));

        sleep(5000);
    }

    /*
    * Esto también es algo que probablemente solo harás con las pruebas, pero puedes crear un
    * Observable que llama inmediatamente a onError () con una excepción especificada:
     */
    private static void createError() {
        Observable.error(() -> new Exception("Crash and burn!"))
        .subscribe(i -> System.out.println("RECEIVED: " + i),
                Throwable::printStackTrace,
                () -> System.out.println("Done!"));
    }

    /*
    * Observable.defer () es una fábrica poderosa debido a su capacidad de crear un estado separado para
    * cada observador Al usar ciertas fábricas observables, puede encontrar algunos matices
    * si su fuente tiene estado y desea crear un estado separado para cada Observador. Tu
    * fuente Observable puede no capturar algo que ha cambiado sobre sus parámetros y
    * Enviar emisiones que son obsoletas. Aquí hay un ejemplo simple: tenemos un
    * Observable.range () construido a partir de dos propiedades int estáticas, start y count.
     */
    private static void createDeferProblem() {

        Observable<Integer> source = Observable.range(start, count);
        source.subscribe(i -> System.out.println("Observer 1: " + i));

        //modify count;

        count = 10;

        source.subscribe(i -> System.out.println("Observer 2: " + i));

    }

    /*
    * Para solucionar este problema de fuentes observables que no capturan cambios de estado, puede crear
    * Un nuevo observable para cada suscripción. Esto se puede lograr usando
    * Observable.defer (), que acepta una lambda que indica cómo crear un Observable
    * por cada suscripción Como esto crea un nuevo Observable cada vez, reflejará cualquier
    * Cambios que conducen sus parámetros:
    * */
    private static void createDeferResolveProblem() {

        Observable<Integer> source = Observable.defer( () ->
                Observable.range(start, count));
        source.subscribe(i -> System.out.println("Observer 1: " + i));

        //modify count;

        count = 10;

        source.subscribe(i -> System.out.println("Observer 2: " + i));

    }

    private static void fromCallable() {

    }

}
