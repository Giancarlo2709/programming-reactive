package com.everis.reactivex.observer;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;


/*
* Implementar y suscribirse a un observador:
* Cuando llama al método subscribe () en un Observable, se utiliza un Observador para
* consumir estos tres eventos mediante la implementación de sus métodos. En lugar de especificar lambda
* argumentos como estábamos haciendo antes, podemos implementar un observador y pasar una instancia
* de ello al método subscribe (). No te preocupes por onSubscribe () en el
* momento. Simplemente deje su implementación vacía hasta que lo discutamos al final de este capítulo:
 */
public class SubscribeObserver {

    public static void main(String[] args) {
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observer<Integer> myObserver = new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                System.out.println("RECEIVED: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };

        source.map(String::length).filter(s-> s >= 5)
                .subscribe(myObserver);

        System.out.println("********************* observerWithLambdas *********************");
        observerWithLambdas();

        System.out.println("********************* onCompleteNotRequired *********************");
        onCompleteNotRequired();
    }

    /*
    * Observadores taquigráficos con lambdas.
    * Implementar un observador es un poco detallado y engorroso. Afortunadamente, el
    * El método subscribe () está sobrecargado para aceptar argumentos lambda para nuestros tres eventos. Esta
    * es probable que queramos usar para la mayoría de los casos, y podemos especificar tres lambda
    * parámetros separados por comas: onNext lambda, onError lambda y the
    * onComplete lambda. Para nuestro ejemplo anterior, podemos consolidar nuestros tres métodos
    * implementaciones usando estas tres lambdas:
    * */
    public static void observerWithLambdas() {
        Consumer<Integer> onNext = i -> System.out.println("RECEIVED: " + i);

        Action onComplete = () -> System.out.println("Done!");

        Consumer<Throwable> onError = Throwable::printStackTrace;

        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        source.map(String::length).filter(s-> s >= 5)
                .subscribe(onNext, onError, onComplete);

    }

    /*
    * Tenga en cuenta que hay otras sobrecargas para subscribe (). Puede omitir onComplete () y
    * solo implemente onNext () y onError (). Esto ya no realizará ninguna acción para
    * onComplete (), pero probablemente habrá casos en los que no necesite uno:
     */
    public static void onCompleteNotRequired(){
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        source.map(String::length).filter(s-> s >= 5)
                .subscribe(i -> System.out.println("RECEIVED: " + i), Throwable::printStackTrace);
    }
}
