package com.everis.reactivex.chapter8.backpressure.operators;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.BackpressureOverflowStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/*
Si se le proporciona un Flowable que no tiene implementación de contrapresión (incluidas las
derivado de Observable), puede aplicar BackpressureStrategy utilizando
Operadores onBackpressureXXX (). Estos también proporcionan algunas configuraciones adicionales
opciones. Esto puede ser útil si, por ejemplo, tiene un Flowable.interval () que emite
más rápido de lo que los consumidores pueden mantener el ritmo. Flowable.interval () no se puede ralentizar en
fuente porque depende del tiempo, pero podemos usar un operador onBackpressureXXX () para
proxy entre él y el río abajo. Usaremos Flowable.interval () para estos
ejemplos, pero esto puede aplicarse a cualquier Flowable que no tenga contrapresión
implementado.
A veces, Flowable puede configurarse simplemente con BackpressureStrategy.
para que estos operadores onBackpressureXXX () puedan especificar la estrategia más adelante.
 */
public class OnBackPressureBufferExample {

    public static void main(String[] args) {
        /*System.out.println("************ onBackPressureBuffer ************");
        onBackPressureBuffer();

        System.out.println("************ onBackPressureOverflow ************");
        onBackPressureOverflow();

        System.out.println("************ onBackPressureLatest ************");
        onBackPressureLatest();*/

        System.out.println("************ onBackPressureDrop ************");
        onBackPressureDrop();
    }

    /*
     * OnBackPressureBuffer () tomará un Flowable existente que se supone que no tiene
     * contrapresión implementada y luego esencialmente aplica BackpressureStrategy.BUFFER en
     * ese punto a la corriente abajo. Como Flowable.interval () no se puede contrapresión en
     * la fuente, poniendo onBackPressureBuffer () después de que enviará una cola contrapresionada a
     * la corriente abajo:
     */
    private static void onBackPressureBuffer() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer()
                .observeOn(Schedulers.io())
                .subscribe( i -> {
                    ThreadUtil.sleep(5);
                    System.out.println(i);
                });
        ThreadUtil.sleep(5000);
    }

    /*
     * El argumento de capacidad creará un umbral máximo para
     * el búfer en lugar de permitir que sea ilimitado. Una acción onOverflow lambda puede ser
     * especificado para disparar una acción cuando un desbordamiento excede la capacidad. También puede especificar un
     * BackpressureOverflowStrategy enum para indicar cómo manejar un desbordamiento que
     * excede la capacidad. Aquí están:
     *  - ERROR: Simplemente arroja un error en el momento en que se excede la capacidad
     *  - DROP_OLDEST: Elimina el valor más antiguo del búfer para dar paso a uno nuevo
     *  - DROP_LATEST: Elimina el último valor del búfer para priorizar los más antiguos,
                       valores no consumidos
     */
    private static void onBackPressureOverflow() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureBuffer(10,
                        () -> System.out.println("Overflow"),
                        BackpressureOverflowStrategy.DROP_LATEST)
                .observeOn(Schedulers.io())
                .subscribe(i -> {
                    ThreadUtil.sleep(5);
                    System.out.println(i);
                });

        ThreadUtil.sleep(5000);
        /*
         * Tenga en cuenta que en esta parte de mi salida ruidosa, se omitió una gran variedad de números
         * entre 136 y 492. Esto se debe a que estas emisiones se redujeron de la cola debido
         * a BackpressureOverflowStrategy.DROP_LATEST. La cola ya estaba llena de
         * emisiones a la espera de ser consumidas, por lo que se ignoraron las nuevas emisiones.
         */
    }

    /*
     * Una ligera variante de onBackpressureBuffer () es onBackPressureLatest (). Esta voluntad
     * retener el último valor de la fuente mientras que el flujo descendente está ocupado, y una vez que
     * aguas abajo es libre de procesar más, proporcionará el último valor. Cualquier valor anterior
     * emitido durante este período ocupado se perderá:
     */
    private static void onBackPressureLatest() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .observeOn(Schedulers.io())
                .subscribe(i -> {
                    ThreadUtil.sleep(5);
                    System.out.println(i);
                });

        ThreadUtil.sleep(5000);
        /*
         * Si estudia mi salida, notará que hay un salto entre 127 y 494. Esto es
         * porque todos los números intermedios finalmente fueron superados por 494 siendo el último valor, y en
         * En ese momento, el río abajo estaba listo para procesar más emisiones. Comenzó consumiendo
         * el 494 en caché y los demás antes de que se cayera.
         */
    }

    /*
    OnBackpressureDrop () simplemente descartará las emisiones si el flujo descendente está demasiado ocupado
    para procesarlos Esto es útil cuando las emisiones se consideran redundantes si el
    aguas abajo ya está ocupado (como una solicitud "EJECUTAR" que se envía repetidamente, aunque
    el proceso resultante ya se está ejecutando). Opcionalmente, puede proporcionar una lambda onDrop
    argumento que especifica qué hacer con cada elemento descartado, que simplemente imprimiremos, como
    se muestra en el siguiente código:
     */
    private static void onBackPressureDrop() {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .onBackpressureDrop(i -> System.out.println("Dropping " + i))
                .observeOn(Schedulers.io())
                .subscribe(i -> {
                   ThreadUtil.sleep(5);
                    System.out.println(i);
                });

        ThreadUtil.sleep(5000);
    }
}
