package com.everis.reactivex.observable;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;

/*
* Antes de analizar el método subscribe () un poco más, tenga en cuenta que es probable que no necesite
* use Observable.create () a menudo. Puede ser útil para conectarse a ciertas fuentes que son
* no reactivo, y veremos esto en un par de lugares más adelante en este capítulo. Pero típicamente, nosotros
* use fábricas simplificadas para crear Observables para fuentes comunes.
*/
public class ObservableJust {

    /*
    * Invocará la llamada onNext () para cada uno y luego invocará onComplete () cuando
    * todos han sido empujados:
     */
    public static void main( String[] args ){
        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");
        source.map(String::length).filter(s-> s >= 5)
                .subscribe(s -> System.out.println("RECEIVED: " + s));

        System.out.println("************ fromIterable ************");
        fromIterable();
    }

    /*
    * También podemos usar Observable.fromIterable () para emitir los elementos de cualquier tipo Iterable,
    * como una lista. También llamará a onNext () para cada elemento y luego llamará a onComplete ()
    * después de que se complete la iteración. Probablemente usará esta fábrica con frecuencia ya que Iterables en
    * Java es común y puede hacerse fácilmente reactivo:
     */
    public static void fromIterable() {
        List<String> items = Arrays.asList("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observable<String> source = Observable.fromIterable(items);
        source.map(String::length).filter(s-> s >= 5)
                .subscribe(s-> System.out.println("RECEIVED: " + s));
    }
}
