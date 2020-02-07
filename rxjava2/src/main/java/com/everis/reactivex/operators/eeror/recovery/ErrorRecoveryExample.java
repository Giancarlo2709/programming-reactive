package com.everis.reactivex.operators.eeror.recovery;

import io.reactivex.rxjava3.core.Observable;

/*
Pueden ocurrir excepciones en su cadena de Observable en muchos operadores dependiendo de qué
Tú lo estás haciendo. Ya sabemos sobre el evento onError () que se comunica
la cadena observable al observador. Después de eso, la suscripción termina y no
Se producirán más emisiones. Pero a veces, queremos interceptar excepciones antes de que lleguen
al observador e intente alguna forma de recuperación. No podemos pretender necesariamente que
el error nunca ocurrió y esperamos que se reanuden las emisiones, pero podemos intentar volver a suscribirnos
o cambiar a una fuente alternativa Observable.
 */
public class ErrorRecoveryExample {

    public static void main(String[] args) {

        System.out.println("********************* general *********************");
        general();

        System.out.println("********************* onErrorReturnItem *********************");
        onErrorReturnItem();

        System.out.println("********************* onErrorReturn *********************");
        onErrorReturn();

        System.out.println("********************* onErrorInMap *********************");
        onErrorInMap();

        System.out.println("********************* onErrorResumeNext *********************");
        onErrorResumeNext();

        System.out.println("********************* onErrorResumeNextEmpty *********************");
        onErrorResumeNextEmpty();

        System.out.println("********************* retry *********************");
        retry();
    }

    private static void general() {
        Observable.just(5, 2, 4, 0, 3, 2, 8)
                .map(i -> 10 / i)
                .subscribe(i -> System.out.println("RECEIVED: " + i),
                        e -> System.out.println("RECEIVED ERROR: " + e)
                );
    }

    /*
    * Cuando desee recurrir a un valor predeterminado cuando ocurra una excepción, puede usar
    * onErrorReturnItem (). Si queremos emitir -1 cuando ocurre una excepción, podemos hacerlo como
    * esta:
    */
    private static void onErrorReturnItem() {
        Observable.just(5, 2, 4, 0, 3, 2, 8)
                .map(i -> 10 / i)
                .onErrorReturnItem(-1)
                .subscribe(i -> System.out.println("Received: " + i));
    }

    /*
    * También puede suministrar la función <Throwable, T> para generar dinámicamente el valor utilizando un
    * lambda Esto le da acceso a Throwable, que puede usar para determinar la devolución
    * valor como se muestra en el siguiente fragmento de código:
     */
    private static void onErrorReturn() {
        Observable.just(5, 2, 4, 0, 3, 2, 8)
                .map(i -> 10 / i)
                .onErrorReturn(e -> -1)
                .subscribe(i -> System.out.println("Received: " + i));
    }

    /*
    *  La ubicación de onErrorReturn () es importante. Si lo ponemos antes del operador map (), el
    * el error no se detectará porque ocurrió después de onErrorReturn (). Para interceptar el
    * error emitido, debe estar aguas abajo de donde ocurrió el error.
    * Tenga en cuenta que aunque emitimos -1 para manejar el error, la secuencia aún terminó después de
    * ese. No obtuvimos los 3, 2 u 8 que se suponía que seguirían. Si quieres reanudar
    * emisiones, solo querrá manejar el error dentro del operador map () donde está el error
    * puede ocurrir. Haría esto en lugar de onErrorReturn () o onErrorReturnItem ():
     */
    private static void onErrorInMap(){
        Observable.just(5, 2, 4, 0, 3, 2, 8)
                .map(i -> {
                    try {
                        return 10/ i;
                    } catch (ArithmeticException e) {
                        return -1;
                    }
                })
                .subscribe(s -> System.out.println("Received: " + s));
    }

    /*
    * Similar a onErrorReturn () y onErrorReturnItem (), onErrorResumeNext () es muy
    * similar. La única diferencia es que acepta otro Observable como parámetro para emitir
    * potencialmente múltiples valores, no un solo valor, en caso de una excepción.
    * Esto es algo artificial y probablemente no tiene un caso de uso comercial, pero podemos emitir tres -1
    *  emisiones en caso de error:
     */
    private static void onErrorResumeNext() {
        Observable.just(5, 2, 4, 0, 3, 2, 8)
                .map(i -> 10/ i)
                .onErrorResumeNext(e -> Observable.just(-1).repeat(3))
                .subscribe( i -> System.out.println("Received: " + i),
                        e -> System.out.println("Received Error: " + e));
    }

    /*
    * También podemos pasarlo Observable.empty () para detener silenciosamente las emisiones en caso de que haya
    * es un error y llama con gracia a la función onComplete ():
     */
    private static void onErrorResumeNextEmpty() {
        Observable.just(5, 2, 4, 0, 3, 2, 8)
                .map(i -> 10/ i)
                .onErrorResumeNext( e -> Observable.empty())
                .subscribe( i -> System.out.println("Received: " + i),
                        e -> System.out.println("Received Error: " + e));
    }

    /*
    * Otra forma de intentar la recuperación es usar el operador retry (), que tiene varias
    * sobrecarga de parámetros. Se volverá a suscribir al Observable anterior y, con suerte, no
    * tener el error nuevamente.
    * Si llama a retry () sin argumentos, se volverá a suscribir un número infinito de veces para
    * cada error Debe tener cuidado con retry () ya que puede tener efectos caóticos. Utilizándolo con
    * nuestro ejemplo hará que emita estos enteros infinita y repetidamente:
     */
    private static void retry() {
        Observable.just(5, 2, 4, 0, 3, 2, 8)
                .map(i -> 10/ i)
                .retry(2)//sin argumentos es infinito
                .subscribe(i -> System.out.println("Received: " + i),
                        e -> System.out.println("Received: " + e));
    }
}
