package com.everis.reactivex.chapter5;

import io.reactivex.rxjava3.core.Observable;

/*
Cuando desea almacenar en caché todas las emisiones indefinidamente a largo plazo y no necesita
controle el comportamiento de la suscripción a la fuente con ConnectableObservable, puede
use el operador cache (). Se suscribirá a la fuente en el primer observador aguas abajo
que suscribe y mantiene todos los valores indefinidamente. Esto lo convierte en un candidato poco probable para
Observables infinitos o grandes cantidades de datos que podrían gravar su memoria:
 */
public class CacheExample {
    public static void main(String[] args) {

        System.out.println("****************** cache ******************");
        cache();

        System.out.println("****************** cacheWithInitialCapacity ******************");
        cacheWithInitialCapacity();

    }

    private static void cache() {
        Observable<Integer> cachedRollingTotals =
                Observable.just(6, 2, 5, 7, 1, 4, 9, 8, 3)
                        .scan(0, (total, next) -> total + next)
                        .cache();

        cachedRollingTotals.subscribe(System.out::println);
    }

    private static void cacheWithInitialCapacity() {
        Observable<Integer> cachedRollingTotals =
                Observable.just(6, 2, 5, 7, 1, 4, 9, 8, 3)
                        .scan(0, (total, next) -> total + next)
                        .cacheWithInitialCapacity(9);

        cachedRollingTotals.subscribe(System.out::println);
    }
}
