package com.everis.reactivex.debugging.and.test;

import com.everis.reactivex.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockingFirstTest {

    @Test
    public void testFirst() {
        Observable<String> source = ObservableUtil.GREEK_ALPHABET;

        String firstWithLengthFour = source.filter(s -> s.length() == 4)
                .blockingFirst();

        assertTrue(firstWithLengthFour.equals("Beta"));
    }
}
