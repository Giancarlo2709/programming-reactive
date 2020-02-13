package com.everis.reactivex.chapter6.concurrency.schedulers;

import com.github.davidmoten.rx.jdbc.Database;
import io.reactivex.rxjava3.core.Observable;
import rx.schedulers.Schedulers;

import java.sql.Connection;

/*
Las tareas de IO como leer y escribir bases de datos, solicitudes web y almacenamiento en disco son menos
caro en la CPU y, a menudo, tiene tiempo de inactividad esperando que los datos se envíen o regresen.
Esto significa que puede crear hilos de forma más liberal, y Schedulers.io () es apropiado para
esta. Mantendrá tantos subprocesos como tareas y crecerá dinámicamente, se almacenará en caché,
y reduzca el número de subprocesos según sea necesario. Por ejemplo, puede usar Schedulers.io ()
para realizar operaciones SQL utilizando RxJava-JDBC (h t t p s: // g i t h u b. c o m / d a v i d m o t e n / r x j a v a
- j d b c):
 */
public class IOExample {

    public static void main(String[] args) {
        /*Database db = Database.from(connection);

        Observable<String> customerNames =
                db.select("SELECT NAME FROM CUSTOMER")
                .getAs(String.class)
                .subscribeOn(Schedulers.io());
                */
    }
}
