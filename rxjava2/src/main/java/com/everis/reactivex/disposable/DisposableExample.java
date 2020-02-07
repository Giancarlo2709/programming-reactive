package com.everis.reactivex.disposable;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.ResourceObserver;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class DisposableExample {

    public static void main(String[] args) {
        Observable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS);

        Disposable disposable = seconds.subscribe(l -> System.out.println("Received: " + l));

        sleep(5000);

        //dispose and stop emissions
        disposable.dispose();

        //sleep 5 seconds to prove
        //there are no more emissions
        sleep(5000);

        System.out.println("********************** resourceObserver **********************");
        resourceObserver();
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void handlingADisposableWithinAnObserver() {
        Observer<Integer> myObserver = new Observer<Integer>() {

            private Disposable disposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                this.disposable = disposable;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                //has access to Disposable
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //has access to Disposable
            }

            @Override
            public void onComplete() {
                //has access to Disposable
            }
        };
    }

    private static void resourceObserver() {
        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS);

        ResourceObserver<Long> myObserver = new ResourceObserver<Long>() {
            @Override
            public void onNext(@NonNull Long aLong) {
                System.out.println(aLong);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };

        //capture Disposable
        Disposable disposable = source.subscribeWith(myObserver);
        sleep(3000);

    }
}
