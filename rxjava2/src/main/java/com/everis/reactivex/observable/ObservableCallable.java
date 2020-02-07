package com.everis.reactivex.observable;

import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
* Si necesita realizar un cálculo o acción y luego emitirlo, puede usar
* Observable.just () (o Single.just () o Maybe.just (), del cual aprenderemos
* luego). Pero a veces, queremos hacer esto de manera perezosa o diferida. Además, si eso
* procedimiento arroja un error, queremos que se emita en la cadena Observable a través de
* onError () en lugar de arrojar el error en esa ubicación de la manera tradicional de Java. por
* instancia, si intenta ajustar Observable.just () alrededor de una expresión que divide 1 por
* 0, se lanzará la excepción, no se emitirá hasta Observer:
* */
public class ObservableCallable {

    public static void main(String[] args) {

        System.out.println(" applyOptionalOfNullable ");
        applyOptionalOfNullable();

        System.out.println("callable");
        //createFromCallableProblem();

        System.out.println("createFromCallableResolveProblem");
        createFromCallableResolveProblem();
    }

    private static void applyOptionalOfNullable() {
        List<String> list = null;

        /*list.add("Giancarlo");
        list.add("Elvis");
        list.add("Yarleque");
        list.add("Juarez");*/

        List<String> list2 = Optional.ofNullable(list)
                .orElseGet(ArrayList::new)
                .stream()
                .map(s -> s + " xxx  ")
                .collect(Collectors.toList());

        System.out.println(list2);
    }

    private static void createFromCallableProblem() {
        Observable.just(1/0)
                .subscribe(i -> System.out.println("RECEIVED: " + i),
                        e -> System.out.println("Error captured: " + e));
    }

    /*
    * Si vamos a ser reactivos en nuestro manejo de errores, esto puede no ser deseable. Quizás tú
    * quisiera que el error se emitiera por la cadena hacia el observador donde estará
    * manejado. Si ese es el caso, use Observable.fromCallable () en su lugar, ya que acepta un
    * proveedor lambda <T> y emitirá cualquier error que ocurra hasta Observer:
     */
    private static void createFromCallableResolveProblem() {
        Observable.fromCallable(() -> 1/0)
                .subscribe(i -> System.out.println("RECEIVED: " + i),
                        e -> System.out.println("Error captured: " + e));
    }
}
