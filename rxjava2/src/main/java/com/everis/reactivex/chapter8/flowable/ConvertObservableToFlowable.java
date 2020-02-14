package com.everis.reactivex.chapter8.flowable;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ConvertObservableToFlowable {

    public static void main(String[] args) {
        /*Observable<Integer> source = Observable.range(1, 1000);
        source.toFlowable(BackpressureStrategy.BUFFER)
                .observeOn(Schedulers.io())
                .subscribe(System.out::println);
        ThreadUtil.sleep(1000);*/

        System.out.println("*************** convertFlowableToObservable ***************");
        convertFlowableToObservable();
    }

    /*
     * El Flowable también tiene un operador toObservable (), que convertirá un Flowable <T> en
     * un <T> observable. Esto puede ser útil para hacer que un Flowable se pueda usar en un Observable
     * cadena, especialmente con operadores como flatMap (), como se muestra en el siguiente código:
     */
    private static void convertFlowableToObservable() {
        Flowable<Integer> integers = Flowable.range(1, 1000)
                .subscribeOn(Schedulers.computation());
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .flatMap(s-> integers.map(i -> i + " - " + s).toObservable())
                .subscribe(System.out::println);
        ThreadUtil.sleep(5000);

    }
}
