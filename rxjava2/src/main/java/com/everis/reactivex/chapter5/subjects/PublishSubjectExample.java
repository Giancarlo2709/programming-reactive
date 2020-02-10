package com.everis.reactivex.chapter5.subjects;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import io.reactivex.rxjava3.subjects.Subject;

import java.util.concurrent.TimeUnit;

/*
Hay un par de implementaciones de Subject, que es un tipo abstracto que implementa
tanto observables como observadores. Esto significa que puede invocar manualmente onNext (),
onComplete () y onError () en un Subject y, a su vez, pasará esos eventos
río abajo hacia sus observadores.
El tipo de Asunto más simple es el PublishSubject, que, como todos los Subjects, tiene mucho interés
difunde a sus observadores río abajo. Otros tipos de Subjects agregan más comportamientos, pero
PublishSubject es del tipo "vainilla", por así decirlo.
 */
public class PublishSubjectExample {

    public static void main(String[] args) {

        System.out.println("************** subject **************");
        subject();

        System.out.println("************** whenToUseSubject **************");
        whenToUseSubject();

        System.out.println("************** serializingSubject **************");
        serializingSubject();

        System.out.println("************** behaviorSubject **************");
        behaviorSubject();

        System.out.println("************** replaySubject **************");
        replaySubject();

    }

    private static void subject() {
        Subject<String> subject = PublishSubject.create();
        subject.map(String::length)
                .subscribe(System.out::println);

        subject.onNext("Alpha");
        subject.onNext("Beta");
        subject.onNext("Gamma");
        subject.onComplete();
    }

    /*
    * Lo más probable es que use Sujetos para suscribirse ansiosamente a un número desconocido de múltiples
    * fuente Observables y consolidar sus emisiones como un solo Observable. Desde sujetos
    * eres un observador, puedes pasarlos a un método subscribe () fácilmente. Esto puede ser útil.
    * en bases de código modularizadas donde toma el desacoplamiento entre Observables y Observadores
    * colocar y ejecutar Observable.merge () no es tan fácil. Aquí, uso Sujeto para fusionar
    * y multidifusión dos fuentes de intervalo observables:
     */
    private static void whenToUseSubject(){
        Observable<String> source1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(l -> (l+1) + " seconds");

        Observable<String> source2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> ( (l+1) * 300) + " milliseconds");

        Subject<String> subject = PublishSubject.create();

        subject.subscribe(System.out::println);

        source1.subscribe(subject);
        source2.subscribe(subject);

        sleep(3000);
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * Una cuestión crítica que se debe tener en cuenta con los Sujetos es esta: onSubscribe (), onNext (), onError (),
    * y onComplete () ¡las llamadas no son seguras para subprocesos! Si tienes múltiples hilos llamando a estos cuatro
    * métodos, las emisiones podrían comenzar a superponerse y romper el contrato Observable, que
    * exige que las emisiones sucedan secuencialmente. Si esto sucede, una buena práctica para adoptar es
    * llame a toSerialized () en Subject para obtener una implementación de sujeto serializada de forma segura
    * (respaldado por el SerializedSubject privado). Esto secuencializará de forma segura concurrente
    * el evento llama para que no se produzcan accidentes de tren aguas abajo:
     */
    private static void serializingSubject() {
        Subject<String> subject = PublishSubject.<String>create().toSerialized();
        subject.map(String::length)
                .subscribe(System.out::println);

        subject.onNext("Alpha");
        subject.onNext("Beta");
        subject.onNext("Gamma");
        subject.onComplete();
    }

    /*
    * Hay algunos otros sabores de sujetos. Aparte del PublishSubject comúnmente utilizado,
    * También hay BehaviorSubject. Se comporta casi de la misma manera que PublishSubject, pero
    * reproducirá el último elemento emitido a cada nuevo observador aguas abajo. Esto es algo
    * como poner replay(1).autoConnect() después de un PublishSubject, pero se consolida
    * estas operaciones en una sola implementación optimizada del sujeto que se suscribe con entusiasmo a
    * la fuente:
     */
    private static void behaviorSubject() {
        Subject<String> subject = BehaviorSubject.create();

        subject.subscribe(s-> System.out.println("Observer 1: " +s));

        subject.onNext("Alpha");
        subject.onNext("Beta");
        subject.onNext("Gamma");

        subject.subscribe(s -> System.out.println("Observer 2: " + s));
    }

    /*
    * ReplaySubject es similar a PublishSubject seguido de un operador cache (). Eso
    * captura inmediatamente las emisiones independientemente de la presencia de observadores aguas abajo y
    * optimiza el almacenamiento en caché para que ocurra dentro del propio sujeto:
     */
    private static void replaySubject() {
        Subject<String> subject = ReplaySubject.create();

        subject.subscribe(s -> System.out.println("Observer 1: " + s));

        subject.onNext("Alpha");
        subject.onNext("Beta");
        subject.onNext("Gamma");

        subject.onComplete();

        subject.subscribe(s -> System.out.println("Observer 2: " + s));
    }
}
