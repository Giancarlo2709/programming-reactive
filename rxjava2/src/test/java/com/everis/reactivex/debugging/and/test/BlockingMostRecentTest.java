package com.everis.reactivex.debugging.and.test;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class BlockingMostRecentTest {

    @Test
    public void test() {
        Observable<Long> source =
                Observable.interval(10, TimeUnit.MILLISECONDS)
                .take(5);

        Iterable<Long> iterable = source.blockingMostRecent(-1L);

        for(Long i: iterable) {
            System.out.println(i);
        }
    }
}
