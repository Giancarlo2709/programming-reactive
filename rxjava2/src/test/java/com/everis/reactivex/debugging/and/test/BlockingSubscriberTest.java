package com.everis.reactivex.debugging.and.test;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class BlockingSubscriberTest {

    @Test
    public void testBlockingSubscribe() {
        AtomicInteger hitCount = new AtomicInteger();

        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS)
                .take(5);

        source.blockingSubscribe(i -> hitCount.incrementAndGet());

        assertTrue(hitCount.get() == 5);

    }

}