package com.everis.reactivex.operators.action;

import io.reactivex.rxjava3.core.Observable;

/*
Para cerrar este capítulo, cubriremos algunos operadores útiles que pueden ayudar en la depuración como
así como obtener visibilidad en una cadena Observable. Estos son los operadores action o doOn.
 */
public class ActionOperatorsExample {

    public static void main(String[] args) {

        System.out.println("*************** doOnNext ***************");
        doOnNext();

        System.out.println("*************** doOnComplete ***************");
        doOnComplete();

        System.out.println("*************** doOnError ***************");
        doOnError();

        System.out.println("*************** doOnSubscribe ***************");
        doOnSubscribe();

        System.out.println("*************** doOnSuccess ***************");
        doOnSuccess();
    }

    /*
    * El operador doOnNext () le permite echar un vistazo a cada emisión que sale de un operador
    * y entrando en el siguiente. Este operador no afecta la operación ni transforma la
    * emisiones de cualquier manera. Simplemente creamos un efecto secundario para cada evento que ocurre en ese punto en
    * La cadena. Por ejemplo, podemos realizar una acción con cada cadena antes de que se asigne a su
    * longitud. En este caso, solo los imprimiremos proporcionando una lambda <T> para el consumidor:
     */
    private static void doOnNext() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .doOnNext(s-> System.out.println("Processing: " + s))
                .map(String::length)
                .subscribe(i -> System.out.println("Received: " +i));
    }

    /*
    * El operador onComplete () le permite activar una acción cuando se llama a onComplete ()
    * en el punto de la cadena Observable. Esto puede ser útil para ver qué puntos de la
    * La cadena observable se ha completado, como se muestra en el siguiente fragmento de código:
     */
    private static void doOnComplete() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .doOnComplete(() -> System.out.println("Source is done emitting!"))
                .map(String::length)
                .subscribe(i -> System.out.println("Received: " +i));
    }

    /*
    * Y, por supuesto, onError () observará el error que se emite en la cadena, y usted puede
    * realiza una acción con ella. Esto puede ser útil para poner entre operadores para ver cuál es
    * culpar por un error:
     */
    private static void doOnError() {
        Observable.just(5, 2, 4, 0, 3, 2, 8)
                .doOnError(e -> System.out.println("Source Failed!"))
                .map(i -> 10/i)
                .doOnError(e-> System.out.println("Division Failed!"))
                .subscribe(e-> System.out.println("Received: "+e),
                        e -> System.out.println("Received Error: " + e));
    }

    /*
    * Otros dos operadores de acción útiles son doOnSubscribe () y doOnDispose ().
    * DoOnSubscribe () dispara un consumidor específico <Disposable> en el momento de la suscripción
    * ocurre en ese punto en la cadena Observable. Proporciona acceso a lo desechable en caso de que
    * desea llamar a dispose () en esa acción. El operador doOnDispose () realizará un
    *  acción específica cuando la eliminación se ejecuta en ese punto de la cadena Observable.
    * Utilizamos ambos operadores para imprimir cuando se produce la suscripción y la eliminación, como se muestra en el
    * siguiente fragmento de código. Como puede predecir, vemos que el evento de suscripción se dispara primero. Luego,
    * las emisiones pasan, y luego la eliminación finalmente se dispara:
     */
    private static void doOnSubscribe() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .doOnSubscribe(d -> System.out.println("Subscribing!"))
                .doOnDispose(() -> System.out.println("Disposing!"))
                .subscribe(i -> System.out.println("Received: " + i));
    }

    /*
    * Recuerde que los tipos Maybe y Single no tienen un evento onNext () sino más bien un
    * El operador onSuccess () pasa una sola emisión. Por lo tanto, no hay doOnNext ()
    * operador en cualquiera de estos tipos, como se observa en el siguiente fragmento de código, pero más bien un
    * Operador doOnSuccess (). Su uso debería sentirse efectivamente como doOnNext ():
     */
    private static void doOnSuccess() {
        Observable.just(5, 3, 7, 10, 2, 14)
                .reduce((total, next) -> total + next)
                .doOnSuccess(i -> System.out.println("Emitting: " + i))
                .subscribe(i -> System.out.println("Received: " + i));
    }
}
