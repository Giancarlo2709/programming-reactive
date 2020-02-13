package com.everis.reactivex.chapter6.concurrency.schedulers;

import com.everis.reactivex.util.ObservableUtil;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Puede construir un Programador a partir de un Java ExecutorService estándar. Puedes elegir hacer
esto con el fin de tener un control más personalizado y ajustado sobre su gestión de subprocesos
políticas Por ejemplo, digamos, queremos crear un Programador que use 20 hilos. Podemos crear
un nuevo ExecutorService fijo especificado con este número de subprocesos. Entonces, puedes envolver
dentro de una implementación del Programador llamando a Schedulers.from ():
 */
public class ExecutorServiceExample {

    /*
    * ExecutorService probablemente mantendrá su programa vivo indefinidamente, por lo que debe administrar
    * su eliminación si se supone que su vida es finita. Si solo quisiera apoyar el ciclo de vida de uno
    * Suscripción observable, necesito llamar a su método shutdown (). Por eso llamé a su
    * método shutdown () después de que la operación finalice o se elimine mediante doFinally ()
    * operador.
     */
    public static void main(String[] args) {
        int numberOfThreads = 20;

        ExecutorService executor =
                Executors.newFixedThreadPool(numberOfThreads);

        Scheduler scheduler = Schedulers.from(executor);

        ObservableUtil.GREEK_ALPHABET
                .subscribeOn(scheduler)
                .doFinally(executor::shutdown)
                .subscribe(System.out::println);
    }
}
