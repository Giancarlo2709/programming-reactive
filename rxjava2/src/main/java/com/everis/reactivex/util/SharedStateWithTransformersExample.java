package com.everis.reactivex.util;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;

import java.util.concurrent.atomic.AtomicInteger;

/*
Cuando comience a crear sus propios Transformadores y operadores personalizados (cubiertos más adelante), un
Una manera fácil de dispararse en el pie es compartir estados entre más de una suscripción.
Esto puede crear rápidamente efectos secundarios no deseados y aplicaciones con errores y es uno de los
razones por las que debe pisar con cuidado al crear sus propios operadores.
Digamos que desea crear un ObservableTransformer <T, IndexedValue <T>>, que se empareja
cada emisión con su índice consecutivo que comienza en 0. Primero, crea un
IndexedValue <T> para simplemente emparejar cada valor T con un índice int:
 */
public class SharedStateWithTransformersExample {

    public static void main(String[] args) {
        System.out.println("**************** base ****************");
        base();

        System.out.println("**************** defer ****************");
        defer();

        System.out.println("**************** zip ****************");
        zip();
    }

    private static void base() {
        Observable<IndexedValue<String>> indexedStrings = ObservableUtil.GREEK_ALPHABET
                .compose(withIndex());

        indexedStrings.subscribe(v ->
                System.out.println("Subscriber 1: " + v));

        indexedStrings.subscribe(v ->
                System.out.println("Subscriber 2: " + v));

        /*
         * Tenga en cuenta que se compartió una sola instancia de AtomicInteger entre ambas suscripciones,
         * lo que significa que su estado también fue compartido. En la segunda suscripción, en lugar de comenzar
         * en 0, se retoma en el índice dejado por la suscripción anterior y comienza en el índice 5 desde
         * la suscripción anterior finalizó a las 4.
         */
    }

    private static void defer() {
        Observable<IndexedValue<String>> indexedStrings = ObservableUtil.GREEK_ALPHABET
                .compose(withIndexWithoutShared());

        indexedStrings.subscribe(v ->
                System.out.println("Subscriber 1: " + v));

        indexedStrings.subscribe(v ->
                System.out.println("Subscriber 2: " + v));
    }

    /*
     * También puede crear un AtomicInteger dentro de Observable.fromCallable () y
     * use flatMap () en el Observable que lo usa.
     * En este ejemplo en particular, también puede usar Observable.zip () o zipWith ()
     * con Observable.range (). Dado que este es un enfoque puro de Rx también, ningún estado será
     * compartido entre múltiples suscriptores, y esto también resolverá nuestro problema:
     */
    private static void zip() {
        Observable<IndexedValue<String>> indexedStrings = ObservableUtil.GREEK_ALPHABET
                .compose(withIndexWithZip());

        indexedStrings.subscribe(v ->
                System.out.println("Subscriber 1: " + v));

        indexedStrings.subscribe(v ->
                System.out.println("Subscriber 2: " + v));
    }

    private static <T> ObservableTransformer<T, IndexedValue<T>> withIndex() {
        final AtomicInteger indexer = new AtomicInteger(-1);
        return upstream -> upstream.map(v -> new
                IndexedValue<T>(indexer.incrementAndGet(), v));
    }

    /*
     * A menos que tenga algunos comportamientos con estado que está implementando deliberadamente, esto es
     * probablemente un efecto secundario no deseado que puede provocar errores enloquecedores. Las constantes son usualmente
     * bien, pero un estado compartido mutable entre suscripciones a menudo es algo que desea
     * evitar.
     * Una forma rápida y fácil de crear un nuevo recurso (como AtomicInteger) para cada
     * la suscripción es envolver todo en Observable.defer (), incluido el
     * Instancia AtomicInteger. De esta forma, se crea un nuevo AtomicInteger cada vez con el
     * operaciones de indexación devueltas:
     */
    private static <T> ObservableTransformer<T, IndexedValue<T>> withIndexWithoutShared() {
        return upstream -> Observable.defer(() -> {
            AtomicInteger indexer = new AtomicInteger(-1);
            return upstream.map(v -> new
                    IndexedValue<T>(indexer.incrementAndGet(), v));
        });
    }

    static <T> ObservableTransformer<T,IndexedValue<T>> withIndexWithZip() {
        return upstream ->
                Observable.zip(upstream,
                        Observable.range(0,Integer.MAX_VALUE),
                        (v,i) -> new IndexedValue<T>(i, v)
                );
    }

    static final class IndexedValue<T> {
        final int index;
        final T value;

        IndexedValue(int index, T value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public String toString() {
            return index + " - " + value;
        }
    }
}

