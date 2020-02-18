package com.everis.reactivex.chapter9.transformers;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOperator;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class ObservableOperatorExample {

    public static void main(String[] args) {
        System.out.println("************** base **************");
        base();
    }

    private static void base() {
        Observable.range(1, 5)
                .lift(doOnEmpty( () ->
                        System.out.println("Operation 1 Empty!")))
                .subscribe(v -> System.out.println("Operation 1: " + v));

        Observable.<Integer>empty()
                .lift(doOnEmpty(() ->
                        System.out.println("Operation 2 Empty!")))
                .subscribe(v -> System.out.println("Operation 2: "
                        + v));
    }

    private static <T>ObservableOperator<T, T> doOnEmpty(Action action) {
        return new ObservableOperator<T, T>() {
            @Override
            public @NonNull Observer<? super T> apply(@NonNull Observer<? super T> observer) throws Throwable {
                return new DisposableObserver<T>() {

                    boolean isEmpty = true;

                    @Override
                    public void onNext(@NonNull T t) {
                        isEmpty = false;
                        observer.onNext(t);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        if(isEmpty) {
                            try {
                                action.run();
                            } catch(Throwable throwable) {
                                onError(throwable);
                                return;
                            }
                        }
                        observer.onComplete();
                    }
                };
            }
        };
    }
}
