package com.everis.reactivex.chapter6.concurrency.parallelization;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelizationExample {

    public static void main(String[] args) {
        /*System.out.println("************ base ************");
        base();

        System.out.println("************ parallelizationFlatMap ************");
        parallelizationFlatMap();*/

        System.out.println("************ coreNumbersAvailable ************");
        coreNumbersAvailable();

        ThreadUtil.sleep(20000);
    }

    private static void base() {
        Observable.range(1, 10)
                .map(i -> intenseCalculation(i))
                .subscribe(i -> System.out.println("Received " + i + " " + LocalDateTime.now()));
    }

    public static <T> T intenseCalculation(T value) {
        ThreadUtil.sleep(ThreadLocalRandom.current().nextInt(3000));
        return value;
    }

    /*
    * Recuerde, la serialización (emitiendo elementos de uno en uno) solo tiene que suceder en el mismo
    * Observable. El operador flatMap () fusionará múltiples Observables derivados de cada
    * emisión incluso si son concurrentes. Si una bombilla aún no se ha apagado, sigue leyendo. En
    * flatMap (), envuelvamos cada emisión en Observable.just (), use subscribeOn () para
    * emitirlo en Schedulers.computation (), y luego asignarlo a la calculo intenso ().
    * Para una buena medida, imprimamos el hilo actual en el Observador también, como se muestra en el
    * siguiente código:
     */
    private static void parallelizationFlatMap() {
        Observable.range(1, 10)
                .flatMap(i -> Observable.just(i)
                    .subscribeOn(Schedulers.computation())
                    .map(i2 -> intenseCalculation(i2))
                )
                .subscribe(i -> System.out.println("Received " + i + " " +
                        LocalDateTime.now() + " on thread " +
                        Thread.currentThread().getName()));

        /*
        * Esto tardó tres segundos en completarse, y encontrará que esto procesa mucho los artículos
        * Más rápido. Por supuesto, mi computadora tiene ocho núcleos y es por eso que mi salida probablemente indica
        * que hay ocho hilos en uso. Si tiene una computadora con menos núcleos, este proceso
        * toma más tiempo y usa menos hilos. Pero probablemente seguirá siendo más rápido que el de un solo subproceso
        * implementación que ejecutamos antes.
        * Lo que hicimos fue crear un Observable de cada emisión, usamos subscribeOn () para emitir
        * en el Programador de cómputo y luego realizó el cálculo intenso (),
        * que ocurrirá en uno de los hilos de cálculo. Cada instancia solicitará su propio
        * hilo del Programador de cálculo, y flatMap () los combinará de forma segura
        * de nuevo en una secuencia serializada.
         */
    }

    /*
    * Sin embargo, el ejemplo aquí no es necesariamente óptimo. Crear un observable para cada
    * la emisión puede crear algunos gastos indirectos no deseados. Hay una forma más ágil de lograr
    * paralelización, aunque tiene algunas partes móviles más. Si queremos evitar crear
    * instancias observables excesivas, tal vez deberíamos dividir la fuente observable en un
    * número fijo de observables donde las emisiones se dividen y distribuyen uniformemente a través de
    * cada uno. Luego, podemos paralelizarlos y fusionarlos con flatMap (). Aún mejor, ya que yo
    * tengo ocho núcleos en mi computadora, tal vez sería ideal que tenga ocho Observables para
    * Ocho corrientes de cálculos.
    * Podemos lograr esto usando un truco groupBy (). Si tengo ocho núcleos, quiero ingresar cada uno
    * emisión a un número en el rango de 0 a 7. Esto me dará ocho
    * Observables agrupados que dividen limpiamente las emisiones en ocho corrientes. Más
    * específicamente, quiero recorrer estos ocho números y asignarlos como una clave para cada
    * emisión. Los observables agrupados no se ven necesariamente afectados por subscribeOn () (lo hará
    * emitir en el hilo de la fuente con la excepción de las emisiones en caché), por lo que tendré que usar
    * observeOn () para paralelizarlos en su lugar. También puedo usar un io () o
    * planificador newThread () ya que he restringido el número de trabajadores al
    * número de núcleos, simplemente restringiendo el número de observables agrupados.
    * Así es como hago esto, pero en lugar de codificar para ocho núcleos, consulto dinámicamente
    * Número de núcleos disponibles:
     */
    private static void coreNumbersAvailable() {
        int coreCount = Runtime.getRuntime().availableProcessors();
        AtomicInteger assigner = new AtomicInteger(0);
        Observable.range(1, 10)
                .groupBy(i -> assigner.incrementAndGet() % coreCount)
                .flatMap(grp -> grp.observeOn(Schedulers.io())
                        .map(i2 -> intenseCalculation(i2))
                    )
                .subscribe(i -> System.out.println("Received " + i + " " +
                        LocalDateTime.now() + " on thread " +
                        Thread.currentThread().getName()));
    }

}
