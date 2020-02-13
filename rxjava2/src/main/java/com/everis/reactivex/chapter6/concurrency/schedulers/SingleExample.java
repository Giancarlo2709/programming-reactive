package com.everis.reactivex.chapter6.concurrency.schedulers;

import com.everis.reactivex.util.ObservableUtil;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.sql.SQLOutput;

/*
Cuando desee ejecutar tareas secuencialmente en un solo hilo, puede invocar
Schedulers.single (). Esto está respaldado por una implementación de subproceso único adecuada
para eventos en bucle. También puede ser útil aislar operaciones frágiles que no sean seguras a un
hilo único:
 */
public class SingleExample {

    public static void main(String[] args) {
        ObservableUtil.GREEK_ALPHABET
                .subscribeOn(Schedulers.single())
                .blockingSubscribe(s -> System.out.println("Received: " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!"));
    }
}
