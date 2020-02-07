package com.everis.reactivex.operators.transforming;

import io.reactivex.rxjava3.core.Observable;

/*
Un simple operador tipo mapa para emitir cada emisión a un tipo diferente es cast (). Si queremos
tomar Observable <String> y emitir cada emisión a un objeto (y devolver un
Observable <Object>), podríamos usar el operador map () de esta manera:
 */
public class CastOperator {

    public static void main(String[] args) {

        //using map
        Observable<Object> items = Observable.just("Alpha", "Beta", "Gamma")
                .map(s -> (Object) s);

        items.subscribe(s-> System.out.println("Received: " + s));

        //using cast
        Observable<Object> items2 = Observable.just("1", "2", "3")
                .cast(Object.class);
        items2.subscribe(s-> System.out.println("Received: " + s));
    }
}
