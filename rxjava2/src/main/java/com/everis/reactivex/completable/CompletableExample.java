package com.everis.reactivex.completable;

import io.reactivex.rxjava3.core.Completable;

/*
* Completable simplemente se refiere a una acción que se ejecuta, pero no recibe
* cualquier emisión Lógicamente, no tiene onNext () o onSuccess () para recibir emisiones,
* pero tiene onError () y onComplete ():
 */
public class CompletableExample {

    public static void main(String[] args) {
        Completable.fromRunnable(() -> runProcess())
                .subscribe(()-> System.out.println("Done!"));

    }

    public static void runProcess() {
    }
}
