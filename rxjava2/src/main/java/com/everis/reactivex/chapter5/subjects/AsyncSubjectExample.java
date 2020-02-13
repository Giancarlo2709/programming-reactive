package com.everis.reactivex.chapter5.subjects;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.AsyncSubject;
import io.reactivex.rxjava3.subjects.Subject;
import io.reactivex.rxjava3.subjects.UnicastSubject;
import javafx.beans.value.ObservableValue;

import java.util.concurrent.TimeUnit;


public class AsyncSubjectExample {

    public static void main(String[] args) {

        System.out.println("************** asyncSubject **************");
        asyncSubject();

        System.out.println("************** uniCastSubject **************");
        //uniCastSubject();

        System.out.println("************** uniCastSubjectPublish **************");
        uniCastSubjectPublish();
    }

    /*
    * AsyncSubject tiene un comportamiento específico finito altamente personalizado: solo empujará el último valor
    * recibido, seguido de un evento onComplete ():
     */
    private static void asyncSubject() {
        Subject<String> subject = AsyncSubject.create();

        subject.subscribe(s->
                System.out.println("Observer 1: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("Observer 1 done!"));

        subject.onNext("Alpha");
        subject.onNext("Beta");
        subject.onNext("Gamma");
        subject.onComplete();

        subject.subscribe(s->
                System.out.println("Observer 2: " + s),
                Throwable::printStackTrace,
                () -> System.out.println("Observer 2 done!"));

    }

    /*
    * Un tipo de Asunto interesante y posiblemente útil es UnicastSubject. UN
    * UnicastSubject, como todos los Sujetos, se utilizará para observar y suscribirse a las fuentes. Pero
    * almacenará todas las emisiones que reciba hasta que un observador se suscriba, y luego lo hará
    * liberar todas estas emisiones al observador y borrar su caché:
     */
    private static void uniCastSubject() {
        Subject<String> subject = UnicastSubject.create();

        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> ((l+1) * 300 ) + " milliseconds")
                .subscribe(subject);

        sleep(2000);

        subject.subscribe(s -> System.out.println("Observer 1: " + s));

        sleep(2000);
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * Si desea apoyar a más de un observador y simplemente dejar que los observadores posteriores reciban
    * las emisiones en vivo sin recibir las emisiones perdidas, puede engañarlo llamando
    * Publique () para crear un único proxy de observador que realice multidifusión en más de uno
    * Observador como se muestra en el siguiente fragmento de código:
     */
    private static void uniCastSubjectPublish() {
        Subject<String> subject = UnicastSubject.create();

        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> ( (l + 1) * 300 ) + " milliseconds")
                .subscribe(subject);

        sleep(2000);

        //multicast to support multiple Observers
        Observable<String> multicast = subject.publish().autoConnect();

        //bring in first Observer
        multicast.subscribe(s -> System.out.println("Observer 1. " + s));

        sleep(2000);

        //bring in second Observer
        multicast.subscribe(s -> System.out.println("Observer 2: " + s));

        sleep(1000);
    }
}
