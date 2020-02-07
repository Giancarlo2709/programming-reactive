package com.everis.reactivex.single;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/*
* 1 emit or an error
* */
public class SingleExample {

    public static void main(String[] args) {
        Single.just("Hello")
            .map(String::length)
            .subscribe(System.out::println,
                    Throwable::printStackTrace);

        System.out.println("************* returnSingleWithFirst ************* ");
        returnSingleWithFirst();
    }

    private static void returnSingleWithFirst(){
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta");

        source.filter(x-> x.equals("Gian")).first("Nil")
        .subscribe(System.out::println);
    }
}
