package com.everis.reactivex.observable;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

/*
Un observable funciona pasando 3 tipos de eventos:
- onNext (): Esto pasa cada elemento uno a la vez desde la fuente Observable all
el camino hasta el observador.
- onComplete (): Esto comunica un evento de finalización hasta el final
Observador, que indica que no se realizarán más llamadas onNext ().
- onError (): esto comunica un error en la cadena al observador, donde
el observador generalmente define cómo manejarlo. A menos que un operador retry () sea
usado para interceptar el error, la cadena Observable generalmente termina, y no
Se producirán más emisiones.
 */
public class ObservableExample {

    public static void main( String[] args )
    {
        /*
        * También podemos usar varios operadores entre Observable y Observer para transformar cada
        * empujó elemento o manipularlos de alguna manera. Cada operador devuelve un nuevo Observable
        * derivado del anterior pero refleja esa transformación. Por ejemplo, podemos usar
        * map () para convertir cada emisión de cadena en su longitud (), y cada entero de longitud será
        * empujado a Observer, como se muestra en el siguiente fragmento de código:
        */
        Observable<String> myString = Observable.just("Alpha", "Beta", "Gamma", "Delta",
                "Epsilon");

        myString.map(s-> s.length())
                .subscribe(System.out::println);
    }
}
