package com.everis.reactivex.operators.reducing;

import io.reactivex.rxjava3.core.Observable;

import java.time.LocalDate;

/*
El método any () verificará si al menos una emisión cumple con un criterio específico y
devolver un Single <Boolean>. En el momento en que encuentra una emisión que califica, emitirá verdadero
y luego llame a OnComplete (). Si procesa todas las emisiones y descubre que todas son falsas,
emitirá falso y llamará a OnComplete ().
En el siguiente fragmento de código, emitimos cuatro cadenas de fecha, las convertimos en LocalDate
emisiones y prueba para cualquier que esté en el mes de junio o más tarde:
 */
public class AnyOperator {

    public static void main(String[] args) {
        Observable.just("2016-01-01", "2016-05-02", "2016-09-12",
                "2016-04-03")
                .map(LocalDate::parse)
                .any(dt -> dt.getMonthValue() >= 9)
                .subscribe(s -> System.out.println("Received: " + s));
    }
}
