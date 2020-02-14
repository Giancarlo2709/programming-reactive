package com.everis.reactivex.chapter8.flowable;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/*
Casi todas las fábricas y operadores observables que aprendiste hasta este punto también
Aplicar a Flowable. En el lado de fábrica, hay Flowable.range (), Flowable.just (),
Flowable.fromIterable () y Flowable.interval (). La mayoría de estos implementan
contrapresión para usted, y el uso es generalmente el mismo que el equivalente Observable.
 */
public class UnderstandingFlowableAndSubscriber {

    public static void main(String[] args) {

        System.out.println("***************** missingBackPressureException *****************");
        missingBackPressureException();
    }

    /*
     * Sin embargo, considere Flowable.interval (), que empuja las emisiones basadas en el tiempo a valores fijos
     * intervalos de tiempo. ¿Se puede contrapresión lógica? Contempla el hecho de que cada emisión
     * está sensible al tiempo que emite. Si ralentizamos Flowable.interval (), nuestro
     * las emisiones ya no reflejarían intervalos de tiempo y se volverían engañosas. Por lo tanto,
     * Flowable.interval () es uno de esos pocos casos en la API estándar que puede generar
     * MissingBackpressureException el momento aguas abajo solicita contrapresión. Aquí,
     * si emitimos cada milisegundo contra intenseCalculation() que ocurre después de
     * observeOn (), obtendremos este error:
     */
    private static void missingBackPressureException() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .map(i -> intenseCalculation(i))
                .subscribe(System.out::println, Throwable::printStackTrace);
        ThreadUtil.sleep(Long.MAX_VALUE);
    }

    public static <T> T intenseCalculation(T value) {
        ThreadUtil.sleep(ThreadLocalRandom.current().nextInt(3000));
        return value;
    }
}
