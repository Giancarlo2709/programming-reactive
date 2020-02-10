package com.everis.reactivex.chapter4.combining.merging;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
Uno de los operadores más poderosos y críticos en RxJava es flatMap (). Si usted tiene que
Invertir tiempo en comprender cualquier operador de RxJava, este es el indicado. Es un operador que
realiza un Observable.merge () dinámico al tomar cada emisión y asignarla a un
Observable. Luego, fusiona las emisiones de los Observables resultantes en un solo
corriente.
 */
public class FlatMapExample {

    public static void main(String[] args) {

        System.out.println("**************** flatMap ****************");
        //flatMap();

        System.out.println("**************** flatMapInteger ****************");
        //flatMapInteger();

        System.out.println("**************** flatMapInterval ****************");
        //flatMapInterval();

        System.out.println("**************** flatMapWithConditions ****************");
        //flatMapWithConditions();

        System.out.println("**************** flatMapVariants ****************");
        flatMapVariants();

    }

    /*
    * La aplicación más simple de flatMap () es asignar una emisión a muchas emisiones. Decir nosotros
    * desea emitir los caracteres de cada cadena proveniente de Observable <String>. Podemos
    * use flatMap () para especificar una función <T, Observable <R>> lambda que asigna cada cadena
    * a un <String> observable, que emitirá las letras. Tenga en cuenta que el mapeado
    * El <R> observable puede emitir cualquier tipo R, diferente de las emisiones de la fuente T. En esto
    *  ejemplo, simplemente resultó ser String, como la fuente:
     */
    private static void flatMap() {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gama", "Delta", "Epsilon");

        source.flatMap(s-> Observable.fromArray(s.split("")))
                .subscribe(System.out::println);

        /*
        * Hemos tomado esas cinco emisiones de cadena y las hemos mapeado (a través de flatMap ()) para emitir
        * Las letras de cada uno. Hicimos esto llamando al método split () de cada cadena, y nosotros
        *le pasó un argumento de cadena vacío "", que se separará en cada carácter. Esta
        * devuelve una cadena de matriz [] que contiene todos los caracteres, que pasamos a
        * Observable.fromArray () para emitir cada uno. FlatMap () espera que cada emisión
        * producirá un Observable, y fusionará todos los Observables resultantes y emitirá sus valores
        * en una sola secuencia
         */
    }

    /*
    * Aquí hay otro ejemplo: tomemos una secuencia de valores de cadena (cada una una serie concatenada
    * de valores separados por "/"), use flatMap () en ellos y filtre solo para valores numéricos
    * antes de convertirlos en emisiones enteras:
     */
    private static void flatMapInteger() {
        Observable<String> source = Observable.just("521934/2342/FOXTROT", "21962/12112/78886/TANGO",
                        "283242/4542/WHISKEY/2348562");

        source.flatMap(s -> Observable.fromArray(s.split("/")))
                .filter(s-> s.matches("[0-9]+")) //use regex to filter integers
                .map(Integer::valueOf)
                .subscribe(System.out::println);
    }

    /*
    * Al igual que Observable.merge (), también puede asignar emisiones a infinitos Observables y
    * fusionarlos. Por ejemplo, podemos emitir valores enteros simples de
    * Observable <Integer> pero use flatMap () en ellos para conducir un
    * Observable.interval (), donde cada uno sirve como argumento de período. En el siguiente
    * fragmento de código, emitimos los valores 2, 3, 10 y 7, que producirán intervalos Observables que
    * emitir a los 2 segundos, 3 segundos, 10 segundos y 7 segundos, respectivamente. Estos cuatro
    * Los observables se fusionarán en una sola secuencia:
     */
    private static void flatMapInterval(){
        Observable<Integer> intervalArguments = Observable.just(2, 3, 10, 7);

        intervalArguments.flatMap(i ->
                Observable.interval(i, TimeUnit.SECONDS)
                        .map(i2 -> i + "s interval: " + ((i + 1) * i) + " seconds " +
                                "elapsed")
                        ).subscribe(System.out::println);

        sleep(12000);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    * El operador Observable.merge () aceptará un número fijo de fuentes Observables.
    * Pero flatMap () seguirá agregando dinámicamente nuevas fuentes Observables para cada emisión
    * eso entra. Esto significa que puede seguir fusionando nuevos Observables entrantes con el tiempo.
    * Otra nota rápida sobre flatMap () es que se puede usar de muchas maneras inteligentes. Hasta el día de hoy, yo
    * sigue encontrando nuevas formas de usarlo. Pero otra forma de ser creativo es evaluar cada
    * emisión dentro de flatMap () y descubra qué tipo de Observable desea devolver.
    * Por ejemplo, si mi ejemplo anterior emitió una emisión de 0 a flatMap (), esto
    * rompa el Observable.interval () resultante. Pero puedo usar una declaración if para verificar
    * si es 0 y devuelve Observable.empty () en su lugar, como se usa en el siguiente código
     */
    private static void flatMapWithConditions() {
        Observable<Integer> secondIntervals =
                Observable.just(2, 0, 3, 10, 7);

        secondIntervals.flatMap(i -> {
            if(i == 0)
                return Observable.empty();
            else
                return Observable.interval(i, TimeUnit.SECONDS)
                    .map(l -> i + "s interval: " + ((l + 1) * i) + " seconds elapsed");
        }).subscribe(System.out::println);

        sleep(12000);

        /*
        * Por supuesto, esto es probablemente demasiado inteligente, ya que puede poner filter () antes de flatMap () y
        * filtra las emisiones que son iguales a 0. Pero el punto es que puedes evaluar una emisión en
        * flatMap () y determine qué tipo de Observable desea devolver.
         */
    }

    /*
    * Tenga en cuenta que hay muchos sabores y variantes de flatMap (), aceptando una serie de
    * sobrecargas en las que no entraremos profundamente por razones de brevedad. Podemos pasar un segundo
    * argumento combinador, que es una lambda BiFunction <T, U, R>, para asociar el original
    * emitió un valor T con cada valor U de asignación plana y los convirtió en un valor R. En nuestro anterior
    * ejemplo de emisión de letras de cada cadena, podemos asociar cada letra con el original
    * emisión de cadena de la que se mapeó:
     */
    private static void flatMapVariants() {
        Observable<String> source =
                Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        source.flatMap(s -> Observable.fromArray(s.split("")),
                (s, r) -> s + " - " + r)
                .subscribe(System.out::println);
    }
}
