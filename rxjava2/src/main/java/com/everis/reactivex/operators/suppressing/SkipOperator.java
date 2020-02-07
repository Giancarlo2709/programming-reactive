package com.everis.reactivex.operators.suppressing;

import io.reactivex.rxjava3.core.Observable;

/*
El operador skip () hace lo contrario del operador take (). Ignorará el especificado
número de emisiones y luego emitir las que siguen. Si quisiera saltarme los primeros 90
emisiones de un Observable, podría usar este operador, como se muestra en el siguiente código
retazo:
 */
public class SkipOperator {

    /*
    * Al igual que el operador take (), también hay una sobrecarga que acepta una duración de tiempo. Ahi esta
    * también un operador skipLast (), que omitirá el último número especificado de elementos (o tiempo
    * duración) antes de que se llame al evento onComplete (). Solo tenga en cuenta que skipLast ()
    * El operador hará cola y retrasará las emisiones hasta que confirme las últimas emisiones en ese ámbito.
     */
    public static void main(String[] args) {
        Observable.range(1, 100)
                .skip(90)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
    }
}
