package com.everis.reactivex.disposable;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/*
* Si tiene varias suscripciones que deben administrarse y eliminarse, puede ser útil
* para usar CompositeDisposable. Implementa Desechable, pero internamente contiene un
* colección de elementos desechables, que puede agregar y luego eliminar todos a la vez:
 */
public class CompositeDisposableExample {

    private static final CompositeDisposable disposables = new CompositeDisposable();

    public static void main(String[] args) {
        Observable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS);

        //subscribe and capture disposables
        Disposable disposable1 = seconds.subscribe(l -> System.out.println("Observer 1: " + l));

        Disposable disposable2 = seconds.subscribe(l -> System.out.println("Observer 2: " + l));

        //put both disposables into CompositeDisposable
        disposables.addAll(disposable1, disposable2);

        sleep(5000);

        //dispose all disposables
        disposables.dispose();

        //sleep 5 seconds to prove
        //there are no more emissions
        sleep(5000);

    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
