package com.everis.reactivex.chapter8.flowable;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.ThreadLocalRandom;

/*
Gran parte del contenido que cubrimos hasta ahora en este capítulo no mostró los enfoques óptimos
contrapresión de una fuente. Sí, usando un Flowable y la mayoría de las fábricas estándar y
los operadores manejarán automáticamente la contrapresión por usted. Sin embargo, si estás creando
sus propias fuentes personalizadas, Flowable.create () o los operadores onBackPressureXXX ()
están algo comprometidos en cómo manejan las solicitudes de contrapresión. Mientras rápido y
eficaz para algunos casos, las emisiones en caché o simplemente su eliminación no siempre es
deseable. Sería mejor hacer que la fuente se contrapresionara en primer lugar.
Afortunadamente, Flowable.generate () existe para ayudar a crear contrapresión, respetando las fuentes
a un nivel muy bien abstraído. Aceptará un consumidor <Emisor <T>> muy parecido
Flowable.create (), pero usará un lambda para especificar qué onNext (), onComplete (),
y los eventos onError () que se pasan cada vez que se solicita un elemento desde la parte superior.
Antes de usar Flowable.generate (), considere hacer su fuente Iterable <T>
en su lugar y pasarlo a Flowable.fromIterable (). los
Flowable.fromIterable () respetará la contrapresión y podría ser más fácil de usar para muchos
casos. De lo contrario, Flowable.generate () es su próxima mejor opción si necesita algo
mas especifico.
 */
public class FlowableGenerateExample {

    public static void main(String[] args) {
        System.out.println("************** base **************");
        base();
    }

    /*
     * La sobrecarga más simple para Flowable.generate () acepta solo
     * Consumidor <Emisor <T>> y supone que no se mantiene un estado entre las emisiones.
     * Esto puede ser útil para crear un generador de enteros aleatorio que tenga en cuenta la contrapresión, como
     * se muestra aquí. Tenga en cuenta que 128 emisiones se emiten inmediatamente, pero después de eso, 96 son
     * empujado aguas abajo antes de que se envíen otros 96 desde la fuente:
     */
    private static void base() {
        randomGenerator(1, 10000)
                .subscribeOn(Schedulers.computation())
                .doOnNext(i -> System.out.println("Emitting " + i))
                .observeOn(Schedulers.io())
                .subscribe(i -> {
                    ThreadUtil.sleep(50);
                    System.out.println("Received " + i);
                });

        ThreadUtil.sleep(10000);
    }

    private static Flowable<Integer> randomGenerator(int min, int max) {
        return Flowable.generate(emitter ->
          emitter.onNext(ThreadLocalRandom.current().nextInt(min, max))
        );
    }
}
