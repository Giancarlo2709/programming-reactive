package com.everis.reactivex.debugging.and.test;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class BlockingNextTest {

    @Test
    public void test() {
        Observable<Long> source =
                Observable.interval(1, TimeUnit.MICROSECONDS)
                .take(1000);

        Iterable<Long> iterable = source.blockingNext();

        for(Long i: iterable) {
            System.out.println(i);
        }
    }
}
