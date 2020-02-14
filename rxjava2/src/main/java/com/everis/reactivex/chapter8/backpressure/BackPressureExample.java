package com.everis.reactivex.chapter8.backpressure;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
A lo largo de este libro, enfaticé la naturaleza "basada en empuje" de los Observables. Empujando artículos
sincrónicamente y uno a la vez desde la fuente hasta el observador es, de hecho, cómo
Las cadenas observables funcionan por defecto sin concurrencia.
 */
public class BackPressureExample {

    public static void main(String[] args) {
        /*System.out.println("************ understandingBackPressure ************");
        understandingBackPressure();*/

        System.out.println("************ needBackPressure ************");
        needBackPressure();
    }

    /*
     * Por ejemplo, el siguiente es un Observable que emitirá los números del 1 al
     * 999,999,999. Asignará cada entero a una instancia de MyItem, que simplemente lo mantiene como
     * propiedad. Pero ralenticemos el procesamiento de cada emisión en 50 milisegundos en el
     * Observador. Esto muestra que incluso si el flujo descendente procesa lentamente cada emisión, el
     * Upstream sincrónicamente mantiene el ritmo. Esto se debe a que un hilo está haciendo todo el
     * trabajo:
     */
    private static void understandingBackPressure() {
        Observable.range(1, 999_999_999)
                .map(MyItem::new)
                .subscribe(myItem -> {
                    ThreadUtil.sleep(50);
                    System.out.println("Received: " + myItem);
                });
    }

    /*
    Cuando agrega operaciones de concurrencia a una cadena Observable (particularmente
    observeOn (), paralelización y operadores como delay ()), la operación se convierte en
    asincrónico. Esto significa que se pueden procesar múltiples partes de la cadena Observable
    emisiones en un momento dado, y los productores pueden superar a los consumidores, ya que ahora están operando
    en diferentes hilos. Una emisión ya no se transmite estrictamente aguas abajo de una en una
    tiempo desde la fuente hasta el Observador antes de comenzar el siguiente. Esto es
    porque una vez que una emisión alcanza un programador diferente a través de observeOn () (u otro
    operadores concurrentes), la fuente ya no está a cargo de impulsar esa emisión al
    Observador. Por lo tanto, la fuente comenzará a impulsar la próxima emisión aunque el
    Es posible que la emisión anterior aún no haya llegado al Observador.
    Si tomamos nuestro ejemplo anterior y agregamos observeOn (Shedulers.io ()) a la derecha
    antes de suscribirse () (como se muestra en el siguiente código), notará algo muy
    evidente:
     */
    private static void needBackPressure() {
        Observable.range(1, 999_999_999)
                .map(MyItem::new)
                .observeOn(Schedulers.io())
                .subscribe(myItem -> {
                    ThreadUtil.sleep(50);
                    System.out.println("Received: " + myItem);
                });
        /*
         * Esta es solo una sección de la salida de mi consola. Tenga en cuenta que cuando se crea MyItem 1001902, el
         * Observer todavía solo está procesando MyItem 38. Las emisiones se están impulsando mucho más rápido
         * de lo que el Observador puede procesarlos, y debido a que las emisiones acumuladas se ponen en cola por
         * observeOn () de forma ilimitada, esto podría conducir a muchos problemas, incluidos
         * Excepciones OutOfMemoryError.
         */
    }

    static final class MyItem {
        final int id;

        MyItem(int id) {
            this.id = id;
            System.out.println("Constructing MyItem " + id);
        }

        @Override
        public String toString() {
            return "MyItem " + id ;
        }
    }
}
