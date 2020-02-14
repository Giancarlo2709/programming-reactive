package com.everis.reactivex.chapter8.flowable;


import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateFlowableExample {

    public static void main(String[] args) {
        /*System.out.println("************* createObservable *************");
        createObservable();*/

        System.out.println("************* createFlowableBackPressureStrategy *************");
        createFlowableBackPressureStrategy();
    }

    /*
     * Anteriormente en este libro, usamos Observable.create () un puñado de veces para crear nuestro propio
     * Observable desde cero, que describe cómo emitir elementos cuando está suscrito, como
     * se muestra en el siguiente fragmento de código:
     */
    private static void createObservable() {
        Observable<Integer> source = Observable.create(emitter -> {
            for(int i= 0; i <= 1000; i++){
                if(emitter.isDisposed())
                    return;
                emitter.onNext(i);
            }
            emitter.onComplete();
        });
        source.observeOn(Schedulers.io())
                .subscribe(System.out::println);
        ThreadUtil.sleep(1000);
    }


    /*
     * Aprovechar Flowable.create () para crear un Flowable se parece mucho a
     * Observable.create (), pero hay una diferencia crítica; debes especificar un
     * BackpressureStrategy como segundo argumento. Este tipo enumerable no lo hace
     * significa proporcionar implementaciones mágicas de soporte de contrapresión. De hecho, esto
     * simplemente admite la contrapresión al almacenar en caché o reducir las emisiones o no implementar
     * contrapresión en absoluto.
     * Aquí, usamos Flowable.create () para crear un Flowable, pero proporcionamos un segundo
     * BackpressureStrategy.BUFFER argumento para amortiguar las emisiones antes de que sean
     * contrapresión:
     */
    private static void createFlowableBackPressureStrategy() {
        Flowable<Integer> source = Flowable.create(emitter -> {
            for(int i= 0; i<= 1000; i++) {
                if(emitter.isCancelled())
                    return;
                emitter.onNext(i);
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
        source.observeOn(Schedulers.io())
                .subscribe(System.out::println);

        ThreadUtil.sleep(1000);
    }

}
