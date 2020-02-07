package com.everis.reactivex.operators.transforming;

import io.reactivex.rxjava3.core.Observable;

/*
El operador repeat () repetirá la suscripción aguas arriba después de onComplete () un especificado
numero de veces.
Por ejemplo, podemos repetir las emisiones dos veces para un Observable dado pasando un largo 2
como argumento para repeat (), como se muestra en el siguiente fragmento de código:
 */
public class RepeatOperator {

    public static void main(String[] args) {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .repeat()
                .subscribe(s-> System.out.println("Received: " + s));
    }
}
