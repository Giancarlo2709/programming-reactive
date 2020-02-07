package com.everis.reactivex.observable;

import io.reactivex.rxjava3.core.Observable;

/*
* Cold Observables son muy parecidos a un CD de música que puede reproducirse para cada oyente, por lo que cada
* La persona puede escuchar todas las pistas en cualquier momento. De la misma manera, los Observables fríos se reproducirán
* las emisiones a cada observador, asegurando que todos los observadores obtengan todos los datos. La mayoría de los datos
* Los observables son fríos, y esto incluye el Observable.just () y
* Observable.fromIterable () fábricas.
* En el siguiente ejemplo, tenemos dos observadores suscritos a un observable. los
* Observable primero reproducirá todas las emisiones al primer observador y luego llamará
* onComplete (). Luego, reproducirá todas las emisiones nuevamente para el segundo Observador y
* llame a OnComplete (). Ambos reciben los mismos conjuntos de datos al obtener dos secuencias separadas
* cada uno, que es el comportamiento típico de un resfriado Observable:
* Conjunto de datos finitos son generalmente frios
*
 */

public class ObservableCold {

    public static void main(String[] args) {
        Observable<String> source = Observable.just("Alpha","Beta","Gamma","Delta","Epsilon");

        //first Observer
        source.subscribe(s-> System.out.println("Observer 1 Received: " + s));

        //second Observer
        source.subscribe(s-> System.out.println("Observer 2 Received: " + s));

        System.out.println("********************** natureCold **********************");
        natureCold();
    }

    /*
    * Incluso si el segundo observador transforma sus emisiones con los operadores, seguirá obteniendo sus propias emisiones.
    * flujo de emisiones. Uso de operadores como map () y filter () contra un resfriado
    * Observable aún mantendrá la naturaleza fría de los Observables cedidos:
     */
    public static void natureCold() {
        Observable<String> source = Observable.just("Alpha","Beta","Gamma","Delta","Epsilon");

        //first Observer
        source.subscribe(s-> System.out.println("Observer 1 Received: " + s));

        //second Observer
        source.map(String::length).filter(i -> i >= 5)
                .subscribe(s-> System.out.println("Observer 2 Received: " + s));
    }
}
