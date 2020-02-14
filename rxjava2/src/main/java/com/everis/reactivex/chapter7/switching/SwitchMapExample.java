package com.everis.reactivex.chapter7.switching;

import com.everis.reactivex.util.ObservableUtil;
import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class SwitchMapExample {

    public static void main(String[] args) {

        /*System.out.println("***************** base *****************");
        base();*/

        System.out.println("***************** switchMap *****************");
        switchMap();

        //keep application alive for 20 seconds
        ThreadUtil.sleep(20000);
    }

    private static void base() {
        Observable<String> items = ObservableUtil.GREEK_ALPHABET
                .mergeWith(Observable.just("Zeta", "Eta", "Theta", "Iota"));

        Observable<String> processStrings = items.concatMap(s -> Observable.just(s))
                .delay(randomSleepTime(), TimeUnit.MILLISECONDS);

        processStrings.subscribe(System.out::println);
    }

    public static int randomSleepTime() {
    //returns random sleep time between 0 to 2000 milliseconds
        return ThreadLocalRandom.current().nextInt(2000);
    }

    /*
     * Como puede ver, cada emisión tarda entre 0 y 2 segundos en emitirse, y procesa todo
     * Las cuerdas pueden tomar hasta 20 segundos.
     * Supongamos que queremos ejecutar este proceso cada 5 segundos, pero queremos cancelar (o más
     * técnicamente, elimine ()) instancias anteriores del proceso y solo ejecute la última. Esta
     * es fácil de hacer con switchMap (). Aquí, creamos otro Observable.interval (),
     * emitiendo cada 5 segundos y luego usamos switchMap () en el Observable que queremos
     * para procesar (que en este caso es processStrings). Cada 5 segundos, la emisión va
     * en switchMap () eliminará rápidamente el Observable que se está procesando actualmente (si existe
     * son cualesquiera) y luego emiten desde el nuevo Observable al que se asigna. Para demostrar que dispose () es
     * siendo llamado, pondremos doOnDispose () en el Observable dentro de switchMap () para
     * mostrar un mensaje:
     */
    private static void switchMap() {
        Observable<String> items = ObservableUtil.GREEK_ALPHABET
                .mergeWith(Observable.just("Zeta", "Eta", "Theta", "Iota"));

        //delay each String to emulate an intense calculation
        Observable<String> processStrings = items.concatMap(s ->
                Observable.just(s)
                        .delay(randomSleepTime(),
                                TimeUnit.MILLISECONDS)
        );

        //run processStrings every 5 seconds, and kill each previous instance to start next
        Observable.interval(5, TimeUnit.SECONDS)
                .switchMap(i ->
                        processStrings.doOnDispose(() ->
                                        System.out.println("Disposing! Starting next"))
                ).subscribe(System.out::println);
    }
}
