package com.everis.reactivex.operators.suppressing;

import io.reactivex.rxjava3.core.Observable;

/*
Suppress emissions
 */
public class FilterOperator {
    public static void main(String[] args) {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .filter(s-> s.length() != 5)
                .subscribe(s-> System.out.println("RECEIVED: " + s));
    }
}
