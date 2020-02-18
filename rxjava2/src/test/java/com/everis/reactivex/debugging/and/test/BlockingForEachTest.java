package com.everis.reactivex.debugging.and.test;

import com.everis.reactivex.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BlockingForEachTest {

    @Test
    public void testSingle() {
        Observable<String> source = ObservableUtil.GREEK_ALPHABET_TEST;

        source.filter(s -> s.length() == 5)
                .blockingForEach(s -> assertTrue(s.length() == 5));
    }
}
