package com.everis.reactivex.util;

import io.reactivex.rxjava3.core.Flowable;

public class FlowableUtil {

    public static final Flowable<String> GREEK_ALPHABET = Flowable.just("Alpha", "Beta", "Delta", "Gamma", "Epsilon");
}
