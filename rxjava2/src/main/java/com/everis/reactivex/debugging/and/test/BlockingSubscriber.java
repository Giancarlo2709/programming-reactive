package com.everis.reactivex.debugging.and.test;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class BlockingSubscriber {

    public static void main(String[] args) {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(5)
                .subscribe(System.out::println);

        ThreadUtil.sleep(5000);
    }
}
