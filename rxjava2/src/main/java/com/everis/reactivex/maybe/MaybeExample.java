package com.everis.reactivex.maybe;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

/*
* 0 o 1 emit
 */
public class MaybeExample {

    public static void main(String[] args) {
        //has emission
        Maybe<Integer> presentSource = Maybe.just(100);

        presentSource.subscribe(s -> System.out.println("Process 1 received: " + s),
                throwable -> throwable.printStackTrace(),
                () -> System.out.println("Process 1 Done!"));

        //no emission
        Maybe<Integer> emptySource = Maybe.empty();

        emptySource.subscribe(s -> System.out.println("Process 2 received: " + s),
                throwable -> throwable.printStackTrace(),
                () -> System.out.println("Process 2 Done!"));

        System.out.println(" *********** observableToMaybe ***********");
        observableToMaybe();

    }

    /*
    * Ciertos operadores observables sobre los que aprenderemos más adelante producen un Maybe. Un ejemplo es
    * El operador firstElement (), que es similar a first (), pero devuelve un resultado vacío
    * si no se emiten elementos:
    * */
    private static void observableToMaybe() {
        Observable<String> source= Observable.just("Alpha", "Beta", "Gamma", "Delta");

        source.firstElement()
        .subscribe(s-> System.out.println("RECEIVED: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("Done!"));
    }
}
