package com.everis.reactivex.chapter6.concurrency.schedulers;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;

/*
 * Un último operador de concurrencia que debemos cubrir es unsubscribeOn (). Cuando usted
 * disponer de un observable, a veces, que puede ser una operación costosa dependiendo del
 * naturaleza de la fuente. Por ejemplo, si su Observable está emitiendo los resultados de una base de datos
 * consulta usando RxJava-JDBC, (h t t p s: // g i t h u b. c o m / d a v i d m o t e n / r x j a v a - j d b c) puede ser
 * es costoso detener y deshacerse de ese Observable porque necesita cerrar el JDBC
 * recursos que está utilizando.
 * Esto puede hacer que el hilo que llama a dispose () se vuelva ocupado, ya que estará haciendo todo el
 * trabaje deteniendo una suscripción Observable y eliminándola. Si este es un subproceso de interfaz de usuario en JavaFX
 * o Android (por ejemplo, porque se hizo clic en el botón CANCELAR PROCESAMIENTO), esto puede
 * causar una congelación de la interfaz de usuario no deseada porque el subproceso de la interfaz de usuario está funcionando para detener y desechar
 * Operación observable.
 * Aquí hay un Observable simple que se emite cada segundo. Paramos el hilo principal
 * durante tres segundos, y luego llamará a dispose () para cerrar la operación. Vamos
 * use doOnDispose () (que será ejecutado por el subproceso de eliminación) para ver que el principal
 * thread de hecho está eliminando la operación:
 */
public class UnSubscribeOnExample {

    public static void main(String[] args) {
        /*System.out.println("**************** base ****************");
        base();*/

        System.out.println("**************** unSubscribeOn ****************");
        unSubscribeOn();
    }

    /*
     * Aquí hay un Observable simple que se emite cada segundo. Paramos el hilo principal
     * durante tres segundos, y luego llamará a dispose () para cerrar la operación. Vamos
     * use doOnDispose () (que será ejecutado por el subproceso de eliminación) para ver que el principal
     * thread de hecho está eliminando la operación:
     */
    private static void base() {
        Disposable d = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("Disposing on thread " +
                        Thread.currentThread().getName()))
                .subscribe(i -> System.out.println("Received " + i));

        ThreadUtil.sleep(3000);
        d.dispose();
        ThreadUtil.sleep(3000);

    }

    /*
     * Agreguemos unsubscribeOn () y especifiquemos cancelar la suscripción en Schedulers.io (). Debieras
     * ponga unsubscribeOn () donde quiera que se vean afectadas todas las operaciones aguas arriba:
     *  Ahora verá que la eliminación está siendo realizada por el IO Scheduler, cuyo hilo es
     * identificado por el nombre RxCachedThreadScheduler-1. Esto permite que el hilo principal
     * inicie la eliminación y continúe sin esperar a que se complete.
     * Al igual que cualquier operador de concurrencia, realmente no debería necesitar usar unsubscribeOn () para
     * operaciones ligeras como este ejemplo, ya que agrega una sobrecarga innecesaria. Pero si tu
     * tener operaciones observables que son pesadas con recursos que son lentos para desechar,
     * unsubscribeOn () puede ser una herramienta crucial si los hilos que llaman a dispose () son sensibles a
     * cargas de trabajo
     **/
    private static void unSubscribeOn() {
        Disposable d = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("Disposing on thread " +
                        Thread.currentThread().getName()))
                .unsubscribeOn(Schedulers.io())
                .subscribe(i -> System.out.println("Received " + i));

        ThreadUtil.sleep(3000);
        d.dispose();
        ThreadUtil.sleep(3000);

    }
}
