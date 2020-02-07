package com.everis.reactivex.operators.transforming;

import io.reactivex.rxjava3.core.Observable;

/*
* Para un Observable <T> dado, el operador startWith () le permite insertar una emisión T
* que precede a todas las demás emisiones. Por ejemplo, si tenemos un Observable <String> que
* emite elementos en un menú que queremos imprimir, podemos usar startWith () para agregar un encabezado de título
*  primero:
* */
public class StartWithOperator {

    public static void main(String[] args) {
        Observable<String> menu =
                Observable.just("Coffee", "Tea", "Espresso", "Latte");

        //print menu
        menu.startWithItem("COFFEE SHOP MENU")
            .subscribe(System.out::println);

        //print menu Array
        menu.startWithArray("COFFEE SHOP MENU", "----------------")
                .subscribe(System.out::println);

    }
}
