package com.everis.reactivex.operators.transforming;

import io.reactivex.rxjava3.core.Observable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/*
Para un Observable <T> dado, el operador map () transformará una emisión T en una R
emisión utilizando la función proporcionada <T, R> lambda. Ya hemos usado este operador
muchas veces, convirtiendo cuerdas en largos. Aquí hay un nuevo ejemplo: podemos tomar la fecha sin procesar
cadenas y use el operador map () para convertir cada una en una emisión LocalDate, como se muestra
en el siguiente fragmento de código:
 */
public class MapOperator {
    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy");

        Observable.just("1/3/2016", "5/9/2016", "10/12/2016")
                .map(s-> LocalDate.parse(s, dtf))
                .subscribe(i -> System.out.println("RECEIVED: " + i));

    }
}
