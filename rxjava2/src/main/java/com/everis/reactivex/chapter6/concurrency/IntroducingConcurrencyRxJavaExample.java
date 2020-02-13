package com.everis.reactivex.chapter6.concurrency;

import com.everis.reactivex.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import sun.misc.Launcher;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/*
La concurrencia en RxJava es simple de ejecutar, pero algo abstracta de entender. Por
predeterminado, los Observables ejecutan el trabajo en el hilo inmediato, que es el hilo que
declaró el Observador y lo suscribió. En muchos de nuestros ejemplos anteriores, este fue el
hilo principal que inició nuestro método main ().
 */
public class IntroducingConcurrencyRxJavaExample {

    public static void main(String[] args) {

        /*System.out.println("************** mainExample **************");
        mainExample();

        /*System.out.println("************** concurrencyWithIntenseCalculation **************");
        concurrencyWithIntenseCalculation();

        System.out.println("************** concurrencySubscribeOn **************");
        concurrencySubscribeOn();

        /*System.out.println("************** concurrencyWithZip **************");
        concurrencyWithZip();

        System.out.println("************** concurrencyWithSleepMax **************");
        concurrencyWithSleepMax();

        System.out.println("************** blockingSubscribe **************");
        blockingSubscribe();*/

        System.out.println("************** blockingSubscribe **************");
        blockingSubscribeInterval();
    }

    /*
    * Pero como se insinuó en algunos otros ejemplos, no todos los Observables se dispararán en el hilo inmediato.
    * Recuerde esas veces que usamos Observable.interval (), como se muestra a continuación
    * ¿código? Vamos a ver:
     */
    private static void mainExample() {
        Observable.interval(1, TimeUnit.SECONDS)
                .map(i -> i + " Mississipi")
                .subscribe(System.out::println);

        sleep(5000);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * Este Observable realmente se disparará en un hilo que no sea el principal
    * uno. Efectivamente, el
    * el subproceso principal iniciará Observable.interval (), pero no esperará a que se complete
    * porque está operando en su propio hilo separado ahora. Esto, de hecho, hace que sea concurrente
    * aplicación porque está aprovechando dos hilos ahora. Si no llamamos a un método sleep () para
    * pause el hilo principal, se cargará hasta el final del método main () y saldrá del
    * aplicación antes de los intervalos tienen la posibilidad de disparar.
    * Por lo general, la simultaneidad es útil solo cuando tiene una ejecución prolongada o un cálculo intensivo
    * procesos. Para ayudarnos a aprender concurrencia sin crear ejemplos ruidosos, crearemos un
    * método auxiliar llamado intenseCalculation () para emular un proceso de larga duración. Va a
    * simplemente acepte cualquier valor y luego duerma durante 0-3 segundos y luego devuelva el mismo valor.
    * Dormir un hilo o pausarlo es una excelente manera de simular un hilo ocupado que funciona:
     */

    private static <T> T intenseCalculation(T value) {
        sleep(ThreadLocalRandom.current().nextInt(3000));
        return value;
    }

    private static void concurrencyWithIntenseCalculation() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .map(s-> intenseCalculation((s)))
                .subscribe(System.out::println);

        Observable.range(1, 6)
                .map(s -> intenseCalculation((s)))
                .subscribe(System.out::println);
    }

    /*
    * Podemos lograr esto usando el operador subscribeOn (), que sugiere a la fuente
    * emisiones de fuego en un programador específico. En este caso, usemos
    * Schedulers.computation (), que agrupa un número fijo de subprocesos apropiados para
    * operaciones de computación. Proporcionará un hilo para impulsar las emisiones de cada observador.
    * Cuando se llama a onComplete (), el hilo se devolverá al Programador para que pueda ser
    * reutilizado en otro lugar:
     */
    private static void concurrencySubscribeOn() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .subscribeOn(Schedulers.computation())
                .map(s-> intenseCalculation((s)))
                .subscribe(System.out::println);

        Observable.range(1, 6)
                .subscribeOn(Schedulers.computation())
                .map(s -> intenseCalculation((s)))
                .subscribe(System.out::println);

        sleep(20000);
    }

    /*
    * Otra cosa que es emocionante sobre RxJava son sus operadores (al menos los oficiales y el
    * los personalizados construidos correctamente). Pueden trabajar con Observables en diferentes hilos de forma segura.
    * Incluso operadores y fábricas que combinan múltiples Observables, como merge () y
    * zip (), combinará de manera segura las emisiones empujadas por diferentes hilos. Por ejemplo, podemos usar
    * zip () en nuestros dos Observables en el ejemplo anterior, incluso si están emitiendo en dos
    * hilos de cálculo separados:
     */
    private static void concurrencyWithZip() {
        Observable<String> source1 = ObservableUtil.GREEK_ALPHABET
                .subscribeOn(Schedulers.computation())
                .map(s -> intenseCalculation(s));

        Observable<Integer> source2 = Observable.range(1, 6)
                .subscribeOn(Schedulers.computation())
                .map(s -> intenseCalculation(s));

        Observable.zip(source1, source2, (s,i) -> s + '-' + i)
                .subscribe(System.out::println);

        sleep(20000);
    }

    /*
    * Una forma de mantener viva una aplicación indefinidamente es simplemente pasar Long.MAX_VALUE al
    * Método Thread.sleep (), como se muestra en el siguiente código, donde
    * tener emisiones de disparo Observable.interval () para siempre:
     */
    private static void concurrencyWithSleepMax() {
        Observable.interval(1, TimeUnit.SECONDS)
                .map(l-> intenseCalculation(l))
                .subscribe(System.out::println);

        sleep(Long.MAX_VALUE);
    }

    /*
    * Puede usar operadores de bloqueo para detener el hilo de declaración y esperar las emisiones.
    * Por lo general, los operadores de bloqueo se utilizan para las pruebas unitarias (como veremos en el Capítulo 10,
    * Pruebas y depuración), y pueden atraer antipatrones si se usan incorrectamente en la producción.
    * Sin embargo, mantener viva una aplicación basada en el ciclo de vida de un observable finito
    * la suscripción es un caso válido para usar un operador de bloqueo. Como se muestra aquí,
    * blockSubscribe () se puede usar en lugar de subscribe () para detener y esperar
    * onComplete () que se llamará antes de que el hilo principal pueda continuar y salir de
    * solicitud:
     */
    private static void blockingSubscribe() {
        ObservableUtil.GREEK_ALPHABET
                .subscribeOn(Schedulers.computation())
                .map(IntroducingConcurrencyRxJavaExample::intenseCalculation)
                .blockingSubscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!"));
    }

    private static void blockingSubscribeInterval() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .map(IntroducingConcurrencyRxJavaExample::intenseCalculation)
                .blockingSubscribe(System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!"));
    }
}
