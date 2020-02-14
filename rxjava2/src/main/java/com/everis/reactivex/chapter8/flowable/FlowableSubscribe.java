package com.everis.reactivex.chapter8.flowable;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;

/*
En lugar de un observador, el Flowable usa un suscriptor para consumir emisiones y
eventos al final de una cadena Flowable. Si pasa solo argumentos de eventos lambda (y no
un objeto completo del suscriptor), subscribe () no devuelve un desechable sino más bien
una Suscripción, que puede eliminarse llamando a cancel () en lugar de dispose ().
La suscripción también puede servir para otro propósito; se comunica aguas arriba cuántos
Se buscan elementos utilizando su método request (). La suscripción también se puede aprovechar en el
Método onSubscribe () del suscriptor para solicitar elementos () en el momento en que está listo para
recibir emisiones.
 */
public class FlowableSubscribe {

    public static void main(String[] args) {

        /*System.out.println("**************** flowableSubscribe ****************");
        flowableSubscribe();

        System.out.println("**************** flowableWithSubscriber ****************");
        flowableWithSubscriber();*/

        System.out.println("**************** managementRequestFlowable ****************");
        managementRequestFlowable();

        ThreadUtil.sleep(20000);
    }

    /*
    Al igual que un observador, la forma más rápida de crear un suscriptor es pasar lambda
    argumentos para suscribirse (), como hemos estado haciendo anteriormente (y mostrados nuevamente en el
    siguiente código). Esta implementación predeterminada del suscriptor solicitará un ilimitado
    número de emisiones aguas arriba, pero los operadores que lo preceden seguirán automáticamente
    manejar la contrapresión:
     */
    private static void flowableSubscribe() {
        Flowable.range(1, 1000)
                .doOnNext(s -> System.out.println("Source pushed " + s))
                .observeOn(Schedulers.io())
                .map(i -> ThreadUtil.intenseCalculation(i))
                .subscribe(s -> System.out.println("Subscriber received " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!"));

    }

    private static void flowableWithSubscriber() {
        Flowable.range(1, 1000)
                .doOnNext(s -> System.out.println("Source pushed " + s))
                .observeOn(Schedulers.io())
                .map(i -> ThreadUtil.intenseCalculation(i))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        ThreadUtil.sleep(50);
                        System.out.println("Subscriber received " + integer);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });
    }

    /*
     * Si desea que su suscriptor establezca una relación explícita de contrapresión con el
     * operador que lo precede, necesitará microgestionar las llamadas request (). Decir, para algunos
     * situación extrema, usted decide que desea que el suscriptor solicite 40 emisiones inicialmente
     * y luego 20 emisiones a la vez después de eso. Esto es lo que necesitarías hacer:
     */
    private static void managementRequestFlowable() {
        Flowable.range(1, 1000)
                .doOnNext(s -> System.out.println("Source pushed " + s))
                .observeOn(Schedulers.io())
                .map(i -> ThreadUtil.intenseCalculation(i))
                .subscribe(new Subscriber<Integer>() {

                    Subscription subscription;
                    AtomicInteger count = new AtomicInteger(0);

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        System.out.println("Requesting 40 items!");
                        subscription.request(40);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        ThreadUtil.sleep(50);
                        System.out.println("Subscriber received " + integer);

                        if(count.incrementAndGet() % 20 == 0 && count.get() >= 40)
                            System.out.println("Requesting 20 more!");
                            subscription.request(20);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });

        /*
         * Tenga en cuenta que la fuente todavía emite 128 emisiones inicialmente y luego aún empuja 96
         * emisiones a la vez. Pero nuestro Suscriptor recibió solo 40 emisiones, según lo especificado, y luego
         * constantemente pide 20 más. Las solicitudes () llamadas en nuestro suscriptor solo se comunican
         * al operador inmediato aguas arriba, que es map (). El operador map () probablemente retransmite
         * esa solicitud para observar On (), que está almacenando elementos en caché y solo vaciando 40 y luego 20,
         * según lo solicitado por el suscriptor. Cuando su caché se agota o se borra, solicitará
         * otros 96 del río arriba.
         */
    }
}
