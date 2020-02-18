package com.everis.reactivex.debugging.and.test;

import com.everis.reactivex.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BlockingIterableTest {

    @Test
    public void testSingle() {
        Observable<String> source = ObservableUtil.GREEK_ALPHABET_TEST;

        Iterable<String> allWithLengthFive = source.filter(s -> s.length() == 5)
                .blockingIterable();

        allWithLengthFive.forEach(x -> assertTrue(x.length() == 5));
    }
}
