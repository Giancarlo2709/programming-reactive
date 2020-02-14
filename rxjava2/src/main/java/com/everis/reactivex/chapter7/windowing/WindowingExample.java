package com.everis.reactivex.chapter7.windowing;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
Los operadores window () son casi idénticos a buffer (), excepto que se almacenan en buffer
otros observables en lugar de colecciones. Esto resulta en un
Observable <Observable <T>> que emite Observables. Cada emisión observable
emisiones de caché para cada alcance y luego enjuagarlas una vez suscritas (al igual que
el GroupedObservable de groupBy (), con el que trabajamos en el Capítulo 4,
Combinando Observables). Esto permite trabajar las emisiones de inmediato, ya que
esté disponible en lugar de esperar a que se finalice y emita cada lista o colección.
También es conveniente trabajar con el operador window () si desea utilizar operadores para
Transforma cada lote.
 */
public class WindowingExample {

    public static void main(String[] args) {
        System.out.println("******************** base ********************");
        base();

        System.out.println("******************** fixedSizeWindow ********************");
        fixedSizeWindow();

        System.out.println("******************** windowSkip ********************");
        windowSkip();

        System.out.println("******************** windowTimeBased ********************");
        windowTimeBased();

        System.out.println("******************** windowBoundaryBased ********************");
        windowBoundaryBased();
    }

    private static void base () {
        Observable.range(1, 10)
                .window(3)
                .subscribe(System.out::println);
    }

    /*
     * Modifiquemos nuestro ejemplo anterior, donde almacenamos 50 enteros en listas de tamaño 8, pero nosotros
     * utilizará window () para almacenarlos como Observables en su lugar. Podemos transformar cada uno reactivamente
     * agruparse en otra cosa además de una colección, como concatenar emisiones en cadenas
     * con tubo "|" separadores:
     */
    private static void fixedSizeWindow() {
        Observable.range(1, 50)
                .window(8)
                .flatMapSingle(obs -> obs.reduce("", (total, next) -> total +
                        (total.equals("") ? "" : "|") + next))
                .subscribe(System.out::println);
    }

    /*
     * Al igual que buffer (), también puede proporcionar un argumento skip. Esta es la cantidad de emisiones
     * debe omitirse antes de comenzar una nueva ventana. Aquí, nuestro tamaño de ventana es 2, pero omitimos
     * Tres artículos. Luego tomamos cada Observable con ventana y lo reducimos a una Cadena
     * concatenación:
     */
    private static void windowSkip() {
        Observable.range(1, 50)
                .window(2, 3)
                .flatMapSingle(obs -> obs.reduce("", (total, next) -> total +
                        (total.equals("") ? "" : "|") + next))
                .subscribe(System.out::println);
    }

    /*
     * Como puede adivinar, puede cortar Observables en ventana a intervalos de tiempo solo
     * como buffer (). Aquí, tenemos un Observable que emite cada 300 milisegundos como antes,
     * y lo dividimos en Observables separados cada 1 segundo. Entonces lo haremos
     * utilice flatMapSingle () en cada concatenación de las emisiones Observable a String:
     */
    private static void windowTimeBased() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> (i +1) * 300) //map to elapsed time
                .window(1, TimeUnit.SECONDS)
                .flatMapSingle(obs -> obs.reduce("", (total, next) -> total +
                        (total.equals("") ? "" : "|") + next))
                .subscribe(System.out::println);

        ThreadUtil.sleep(5000);
    }

    private static void windowBoundaryBased() {
        Observable<Long> cutOffs =
                Observable.interval(1, TimeUnit.SECONDS);
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 300) // map to elapsed time
                .window(cutOffs)
                .flatMapSingle(obs -> obs.reduce("", (total, next) ->
                        total
                                + (total.equals("") ? "" : "|") + next))
                .subscribe(System.out::println);

        ThreadUtil.sleep(5000);
    }
}
