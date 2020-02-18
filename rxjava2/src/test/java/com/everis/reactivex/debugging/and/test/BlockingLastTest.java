package com.everis.reactivex.debugging.and.test;

import com.everis.reactivex.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BlockingLastTest {

    @Test
    public void testSingle() {
        Observable<String> source = ObservableUtil.GREEK_ALPHABET_TEST;

        String lastWithLengthFour = source.filter(s -> s.length() == 4)
                .blockingLast();

        assertTrue(lastWithLengthFour.equals("Zeta"));
    }
}
