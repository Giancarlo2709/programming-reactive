package com.everis.reactivex.chapter6.concurrency.schedulers;

import com.everis.reactivex.util.ObservableUtil;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
Ya vimos el Programador de cálculo, del que puede obtener la instancia global por
llamando a Schedulers.computation (). Esto mantendrá un número fijo de hilos basados
en el recuento de procesadores disponible para su sesión Java, por lo que es apropiado para
tareas computacionales. Tareas computacionales (como matemáticas, algoritmos y lógica compleja)
puede utilizar núcleos en toda su extensión. Por lo tanto, no hay beneficio en tener más trabajadores
hilos que los núcleos disponibles para realizar dicho trabajo, y el Programador computacional
asegurarse de que:
 */
public class ComputationExample {

    public static void main(String[] args) {
        ObservableUtil.GREEK_ALPHABET
                .subscribeOn(Schedulers.computation())
                .blockingSubscribe(s -> System.out.println("Received: " + s));
    }
}
