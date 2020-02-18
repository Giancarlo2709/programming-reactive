package com.everis.reactivex.debugging.and.test;

import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class BlockingGetTest {

    @Test
    public void testSingle() {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Zeta");

        List<String> allWithLengthFour = source.filter(s -> s.length() == 4)
                .toList()
                .blockingGet();

        assertTrue(allWithLengthFour.equals(Arrays.asList("Beta", "Zeta")));
    }
}
