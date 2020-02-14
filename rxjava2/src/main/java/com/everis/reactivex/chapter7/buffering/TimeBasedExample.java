package com.everis.reactivex.chapter7.buffering;

import com.everis.reactivex.util.ThreadUtil;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
Puede usar buffer () a intervalos de tiempo fijos proporcionando una unidad de tiempo larga y unitaria. A
las emisiones del búfer en una lista a intervalos de 1 segundo, puede ejecutar el siguiente código. Tenga en cuenta que
estamos haciendo que la fuente emita cada 300 milisegundos, y cada lista almacenada resultante
probablemente contenga tres o cuatro emisiones debido a los cortes de intervalo de un segundo:
 */
public class TimeBasedExample {

    public static void main(String[] args) {
        /*System.out.println("************* base *************");
        base();*/

        System.out.println("************* timeBasedSkip *************");
        timeBasedSkip();

        ThreadUtil.sleep(5000);
    }

    private static void base() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 300) // map to elapsed time
                .buffer(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);
    }

    /*
     * Hay una opción para especificar también un argumento de salto temporal, que se basa en el temporizador
     * contraparte para saltar. Controla el momento en que comienza cada búfer.
     * También puede aprovechar un tercer argumento de conteo para proporcionar un tamaño máximo de búfer. Esta voluntad
     * resultar en una emisión de amortiguación en cada intervalo de tiempo o cuando se alcanza el recuento, lo que sea
     * sucede primero Si se alcanza el recuento justo antes de que se cierre la ventana de tiempo, resultará en
     * Se emite un búfer vacío.
     */
    private static void timeBasedSkip() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> (i+1) * 300) // map to elapsed time
                .buffer(1, TimeUnit.SECONDS, 2)
                .subscribe(System.out::println);
    }
}
