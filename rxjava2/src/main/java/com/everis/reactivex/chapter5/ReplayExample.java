package com.everis.reactivex.chapter5;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
El operador replay () es una forma poderosa de mantener las emisiones anteriores dentro de un determinado
alcance y reemitirlos cuando llegue un nuevo observador. Se devolverá un
ConnectableObservable que emitirá tanto emisiones de multidifusión como emisiones anteriores
emisiones definidas en un alcance. Las emisiones anteriores que almacena en caché se dispararán inmediatamente a una nueva
Observador para que quede atrapado, y luego disparará las emisiones de corriente desde ese punto en adelante.
 */
public class ReplayExample {

    public static void main(String[] args) {

        //System.out.println("**************** replay ****************");
        //replay();

        System.out.println("**************** replayString ****************");
        replayString();

        System.out.println("**************** replayStringWihRefCount ****************");
        replayStringWihRefCount();

        System.out.println("**************** replayWithInterval ****************");
        //replayWithInterval();

        System.out.println("**************** replayWithInterval2 ****************");
        replayWithInterval2();

    }

    /*
    * Comencemos con una repetición () sin argumentos. Esto reproducirá todas las emisiones anteriores a
    * Observadores tardíos, y luego emiten emisiones de corriente tan pronto como el Observador tardío es atrapado
    * arriba. Si usamos Observable.interval () para emitir cada segundo, podemos llamar a replay () en él
    * para multidifusión y reproducir emisiones enteras anteriores. Como vuelve a reproducir ()
    * ConnectableObservable, usemos autoConnect () para que comience a disparar en el primer
    * suscripción. Después de 3 segundos, traeremos un segundo observador. Mira de cerca qué
    * sucede:
     */
    private static void replay() {
        Observable<Long> seconds =
                Observable.interval(1, TimeUnit.SECONDS)
                    .replay(1)
                    .autoConnect();

        //Observer 1
        seconds.subscribe(l -> System.out.println("Observer 1: " + l));

        sleep(3000);

        //Observer 2
        seconds.subscribe(l -> System.out.println("Observer 2: " + l));

        sleep(3000);

        /*
        * ¿Viste eso? Después de 3 segundos, Observer 2 entró e inmediatamente recibió el primer
        * tres emisiones que perdió: 0, 1 y 2. Después de eso, recibe las mismas emisiones que Observer
        * 1 en adelante. Solo tenga en cuenta que esto puede ser costoso con la memoria, ya que replay () mantendrá
        * almacenamiento en caché de todas las emisiones que recibe. Si la fuente es infinita o solo te importa la última
        * emisiones anteriores, es posible que desee especificar un argumento bufferSize para limitar solo
        * reproduciendo un cierto número de últimas emisiones. Si llamamos replay (2) en nuestro segundo
        * Observador para almacenar en caché las dos últimas emisiones, no obtendrá 0, pero recibirá 1 y 2. El 0
        * se cayó por esa ventana y se liberó del caché tan pronto como entraron 2.
         */
    }

    /*
    * Tenga en cuenta que si siempre desea conservar los valores almacenados en caché en su repetición () incluso si hay
    * sin suscripciones, úsela junto con autoConnect (), no refCount (). Si emitimos
    * nuestras cadenas Alpha a Epsilon y use replay (1) .autoConnect () para mantener el
    * último valor, nuestro segundo observador solo recibirá el último valor, como se esperaba:
     */
    private static void replayString() {
        Observable<String> source =
                Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .replay(2)
                .autoConnect();

        //Observer 1
        source.subscribe(l -> System.out.println("Observer 1: " + l));

        //Observer 2
        source.subscribe(l -> System.out.println("Observer 2: " +l));
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * Lo que sucedió aquí es que refCount () hace que el caché (y toda la cadena) se deshaga
    * de y restablecer el momento en que se realiza el Observador 1, ya que no hay más Observadores. Cuando
    * Observer 2 entró, comienza de nuevo y emite todo como si fuera el primer observador,
    * y se construye otro caché. Esto puede no ser deseable, por lo que puede considerar usar
    * autoConnect () para mantener el estado de repetición () y no eliminarlo cuando no
    * Los observadores están presentes.
     */
    private static void replayStringWihRefCount() {
        Observable<String> source =
                Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                        .replay(2)
                        .refCount();

        //Observer 1
        source.subscribe(l -> System.out.println("Observer 1: " + l));

        //Observer 2
        source.subscribe(l -> System.out.println("Observer 2: " +l));
    }

    /*
    * Hay otras sobrecargas para replay (), particularmente una ventana basada en el tiempo que puede especificar.
    * Aquí, construimos un Observable.interval () que emite cada 300 milisegundos y
    * suscríbete También mapeamos cada entero consecutivo emitido en los milisegundos transcurridos.
    *  Reproduciremos solo el último 1 segundo de emisiones para cada nuevo observador, lo que haremos
    *  traer después de 2 segundos:
     */
    private static  void replayWithInterval() {
        Observable<Long> seconds = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 300)// map to elapsed milliseconds
                .replay(1, TimeUnit.SECONDS)
                .autoConnect();

        //Observer 1
        seconds.subscribe(l -> System.out.println("Observer 1: " + l));

        sleep(2000);

        //Observer 1
        seconds.subscribe(l -> System.out.println("Observer 2: " + l));

        sleep(1000);
    }

    /*
    * También puede especificar un argumento bufferSize encima de un intervalo de tiempo, por lo que solo un cierto
    * El número de las últimas emisiones se almacenan en ese período. Si modificamos nuestro ejemplo a
    * solo reproduce una emisión que ocurrió en el último segundo, solo debe reproducir 1800 para
    * Observador 2:
     */
    private static  void replayWithInterval2() {
        Observable<Long> seconds = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 300)// map to elapsed milliseconds
                .replay(1,1, TimeUnit.SECONDS)
                .autoConnect();

        //Observer 1
        seconds.subscribe(l -> System.out.println("Observer 1: " + l));

        sleep(2000);

        //Observer 1
        seconds.subscribe(l -> System.out.println("Observer 2: " + l));

        sleep(1000);
    }
}
