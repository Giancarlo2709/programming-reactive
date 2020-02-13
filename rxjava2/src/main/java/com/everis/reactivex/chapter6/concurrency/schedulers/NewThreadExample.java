package com.everis.reactivex.chapter6.concurrency.schedulers;

import com.everis.reactivex.util.ObservableUtil;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
La fábrica Schedulers.newThread () devolverá un Programador que no agrupa
hilos en absoluto. Creará un nuevo hilo para cada Observador y luego destruirá el hilo
cuando está hecho Esto es diferente de Schedulers.io () porque no intenta
Persistir y almacenar en caché los subprocesos para su reutilización:
 */
public class NewThreadExample {

    /*
   * Esto puede ser útil en los casos en que desee crear, usar y luego destruir un hilo
   * inmediatamente para que no ocupe memoria. Pero para aplicaciones complejas en general, usted
   * querrá usar Schedulers.io () por lo que hay algún intento de reutilizar hilos si es posible.
   * También debe tener cuidado ya que Schedulers.newThread () puede ejecutarse en complejo
   * aplicaciones (como puede Schedulers.io ()) y crear un alto volumen de subprocesos, lo que podría
   * bloquea tu aplicación.
     */
    public static void main(String[] args) {
        long start =  System.currentTimeMillis();
        ObservableUtil.GREEK_ALPHABET
                .subscribeOn(Schedulers.io())
                .blockingSubscribe(s -> System.out.println("Received: " + s));
        long end =  System.currentTimeMillis();
        System.out.println("Time elapsed: " + (end - start));
    }

}
