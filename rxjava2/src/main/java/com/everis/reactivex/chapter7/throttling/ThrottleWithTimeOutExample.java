package com.everis.reactivex.chapter7.throttling;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
Si juegas con throttleFirst () y throttleLast (), es posible que no estés satisfecho con
Un aspecto de su comportamiento. Son agnósticos a la variabilidad de la frecuencia de emisión, y
simplemente "se sumergen" a intervalos fijos y extraen la primera o la última emisión que encuentran. Ahi esta
ninguna noción de esperar un "período de silencio" donde las emisiones se detengan por un momento, y eso
podría ser un momento oportuno para impulsar la última emisión que ocurrió hacia adelante.
Piense en las películas de acción de Hollywood donde un protagonista está bajo fuertes disparos. Mientras
las balas están volando, él / ella tiene que ponerse a cubierto y no puede actuar. Pero el momento su
los atacantes se detienen para recargar, hay un período de silencio en el que tienen tiempo para reaccionar. Esto es
esencialmente lo que hace throttleWithTimout (). Si bien las emisiones se disparan rápidamente, lo hará
no emitir nada hasta que haya un "período de silencio", y luego empujará la última emisión
adelante.
 */
public class ThrottleWithTimeOutExample {

    public static void main(String[] args) {

        System.out.println("**************** base ****************");
        base();

        ThreadUtil.sleep(6000);
    }

    /*
     * throttleWithTimout () (también llamado debounce ()) acepta argumentos de intervalo de tiempo que
     * especifique por cuánto tiempo un período de inactividad (lo que significa que no se emiten emisiones
     * fuente) debe ser antes de que se pueda impulsar la última emisión. En nuestro ejemplo anterior, nuestro
     * tres fuentes concatenados Observable.interval () se disparan rápidamente a 100 milisegundos
     * y luego chorros de 300 milisegundos durante aproximadamente 2 segundos. Pero después de eso, los intervalos son lentos
     * abajo a cada 2 segundos. Si quisiéramos emitir solo después de 1 segundo de silencio, no estamos
     * va a emitir cualquier cosa hasta que lleguemos a ese tercer Observable.interval (), emitiendo cada 2
     * segundos, como se muestra aquí:
     */
    private static void base() {
        Observable<String> source1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .map(i -> ( i + 1) * 100) //map to elapsed time
                .map(i -> "Source 1: " + i)
                .take(10);

        Observable<String> source2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 300) //map to elapsed time
                .map(i -> "Source 2: " + i)
                .take(3);

        Observable<String> source3 = Observable.interval(2000, TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 2000) //map to elapsed time
                .map(i -> "Source 3: " + i)
                .take(2);

        Observable.concat(source1, source2, source3)
                .throttleWithTimeout(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);
    }
}
