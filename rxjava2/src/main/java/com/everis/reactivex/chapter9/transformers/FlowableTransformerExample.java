package com.everis.reactivex.chapter9.transformers;

import com.everis.reactivex.util.FlowableUtil;
import com.google.common.collect.ImmutableList;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;

/*
Cuando implemente su propio ObservableTransformer, es posible que desee crear un
Contraparte de FlowableTransformer también. De esta manera, puede usar su operador en ambos
Observables y Flowables.
FlowableTransformer no es muy diferente de ObservableTransformer. De
Por supuesto, soportará la contrapresión ya que está compuesta con Flowables. De lo contrario, es
más o menos lo mismo en su uso, excepto que obviamente lo pasa a compose () en
Fluido, no observable.
Aquí, tomamos nuestro método toImmutableList () regresando
un ObservableTransformer e implementarlo como FlowableTransformer en su lugar:
 */
public class FlowableTransformerExample {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println("************* base *************");
        base();
        long end = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (end - start) + " milliseconds");
    }

    private static void base() {
        FlowableUtil.GREEK_ALPHABET
                .compose(toImmutableList())
                .subscribe(System.out::println);

        Flowable.range(1, 10)
                .compose(toImmutableList())
                .subscribe(System.out::println);
    }

    private static <T> FlowableTransformer<T, ImmutableList<T>> toImmutableList() {
        return upstream -> upstream.collect(ImmutableList::<T>builder,
                ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .toFlowable();// Debe convertir Single en Flowable
    }
}
