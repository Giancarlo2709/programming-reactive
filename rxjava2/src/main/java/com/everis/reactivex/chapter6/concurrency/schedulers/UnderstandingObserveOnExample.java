package com.everis.reactivex.chapter6.concurrency.schedulers;

import com.everis.reactivex.util.ObservableUtil;
import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/*
El operador observeOn (), sin embargo,
interceptará las emisiones en ese punto de la cadena Observable y las cambiará a
Programador diferente en el futuro.
A diferencia de subscribeOn (), la ubicación de observeOn () es importante. Saldrá de todas las operaciones.
upstream en el programador predeterminado o subscribeOn () - definido, pero cambiará a un
Programador diferente aguas abajo. Aquí, puedo hacer que un Observable emita una serie de cadenas
que son valores separados por / y los dividen en un IO Scheduler. Pero después de eso, puedo
cambie a un Programador de cálculo para filtrar solo números y calcular su suma, como
se muestra en el siguiente fragmento de código:
 */
//***********************************************
/*
Por supuesto, este ejemplo no es computacionalmente intenso, y en la vida real, debe hacerse
en un solo hilo. La sobrecarga de concurrencia que presentamos no está garantizada, pero
Supongamos que es un proceso de larga duración.
Nuevamente, use observeOn () para interceptar cada emisión y empujarlas hacia adelante en una dirección diferente.
Programador En el ejemplo anterior, los operadores antes de observeOn () se ejecutan en
Scheduler.io (), pero los posteriores son ejecutados por Schedulers.computation ().
Los operadores aguas arriba antes de observeOn () no se ven afectados, pero los aguas abajo sí.
Puede usar observeOn () para una situación como la emulada anteriormente. Si quieres
lea una o más fuentes de datos y espere a que vuelva la respuesta, querrá hacer
esa parte en Schedulers.io () y probablemente aprovechará subscribeOn () ya que esa es la
operación inicial. Pero una vez que tenga esos datos, es posible que desee realizar cálculos intensivos
con él, y Scheduler.io () ya no puede ser apropiado. Querrás restringir
estas operaciones a algunos subprocesos que utilizarán completamente la CPU. Por lo tanto, usas
observeOn () para redirigir datos a Schedulers.computation ().
 */
public class UnderstandingObserveOnExample {

    public static void main(String[] args) {

        /*System.out.println("***************** observeOnBasic *******************");
        observeOnBasic();*/

        System.out.println("***************** observeOnComplex *******************");
        observeOnComplex();

        ThreadUtil.sleep(1000);
    }

    private static void observeOnBasic() {
        Observable.just("WHISKEY/27653/TANGO", "6555/BRAVO",
                "232352/5675675/FOXTROT")
                .subscribeOn(Schedulers.io())
                .flatMap(s -> Observable.fromArray(s.split("/")))

                //Happens on IO Scheduler
                .observeOn(Schedulers.computation())
                .filter(s -> s.matches("[0-9]+"))
                .map(Integer::valueOf)
                .reduce((total, next) -> total + next)
                .subscribe(i -> System.out.println("Received " + i + " on thread " +
                        Thread.currentThread().getName()));

    }

    /*
    * En realidad, puede usar múltiples operadores observeOn () para cambiar los Programadores más de
    * una vez. Continuando con nuestro ejemplo anterior, digamos que queremos escribir nuestra suma calculada en
    * un disco y escribirlo en un archivo. Supongamos que se trata de una gran cantidad de datos en lugar de un solo número
    * y queremos sacar esta operación de escritura en disco del Programador de cálculo y ponerlo
    * de vuelta en el IO Scheduler. Podemos lograr esto introduciendo un segundo observeOn ().
    * Agreguemos también algunos operadores doOnNext () y doOnSuccess () (debido a Quizás) a
    * eche un vistazo al hilo que usa cada operación:
     */
    private static void observeOnComplex() {
        Observable.just("WHISKEY/27653/TANGO", "6555/BRAVO",
                "232352/5675675/FOXTROT")
                .subscribeOn(Schedulers.io())
                .flatMap(s -> Observable.fromArray(s.split("/")))
                .doOnNext(s -> System.out.println("Split out " + s + " on thread " +
                        Thread.currentThread().getName()))

                //Happens on Computation Scheduler
                .observeOn(Schedulers.computation())
                .filter(s -> s.matches("[0-9]+"))
                .map(Integer::valueOf)
                .reduce((total, next) -> total + next)
                .doOnSuccess(i -> System.out.println("Calculated sum " + i + " on thread " +
                        Thread.currentThread().getName()))

                //Switch back to IO Scheduler
                .observeOn(Schedulers.io())
                .map(i -> i.toString())
                .doOnSuccess(s -> System.out.println("Writing " + s + " on thread " +
                        Thread.currentThread().getName()))
                .subscribe(s-> write(s, "C:\\Instaladores\\output.txt"));
    }

    private static void write(String text, String path) {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            File file = new File(path);
            writer = new BufferedWriter(new FileWriter(file));
            writer.append(text);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{
                writer.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
