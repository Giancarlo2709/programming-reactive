package com.everis.reactivex.util;

import io.reactivex.rxjava3.core.Observable;

public class ObservableUtil {

    public static final Observable<String> GREEK_ALPHABET = Observable.just("Alpha", "Beta", "Delta", "Gamma", "Epsilon");
    public static final Observable<String> GREEK_ALPHABET_TEST = Observable.just("Alpha", "Beta","Gamma", "Delta", "Zeta");
}
