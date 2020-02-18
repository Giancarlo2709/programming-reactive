package com.everis.reactivex.debugging.and.test;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class UsingTestObserver {

    @Test
    public void usingTestObserver() {
        //An Observable with 5 one-second emissions
        Observable<Long> source = Observable.interval(1,
                TimeUnit.SECONDS)
                .take(5);

        //Declare TestObserver
        TestObserver<Long> testObserver = new TestObserver<>();

        //Afirmar que aún no se ha suscrito
        assertTrue(!testObserver.hasSubscription());

        //Suscríbase TestObserver a la fuente
        source.subscribe(testObserver);

        //Afirmar que se ha suscrito
        assertTrue(testObserver.hasSubscription());

        // Bloquea y espera a que Observable termine
        testObserver.assertComplete();

        // Afirmar que no hubo errores
        testObserver.assertNoErrors();

        // Se recibieron 5 valores.
        testObserver.assertValueCount(5);

        // Afirmar que las emisiones recibidas fueron 0, 1, 2, 3, 4
        testObserver.assertValues(0L, 1L, 2L, 3L, 4L);
    }
}
