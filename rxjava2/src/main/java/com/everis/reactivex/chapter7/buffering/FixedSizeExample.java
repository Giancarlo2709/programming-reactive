package com.everis.reactivex.chapter7.buffering;

import io.reactivex.rxjava3.core.Observable;

import java.util.HashSet;

/*
El operador buffer () reunirá las emisiones dentro de un cierto alcance y emitirá cada lote como
una lista u otro tipo de colección. El alcance se puede definir mediante un tamaño de búfer fijo o un
ventana de tiempo que se corta a intervalos o incluso cortes por las emisiones de otro
Observable.
 */
public class FixedSizeExample {

    public static void main(String[] args) {
        System.out.println("******************* base *******************");
        base();

        System.out.println("******************* fixedSizeWithHashSet *******************");
        fixedSizeWithHashSet();

        System.out.println("******************* fixedSizeSkip *******************");
        fixedSizeSkip();

        System.out.println("******************* fixedSizeSkipLessThanCount *******************");
        fixedSizeSkipLessThanCount();

        System.out.println("******************* fixedSizeSkipLessThanCountFilter *******************");
        fixedSizeSkipLessThanCountFilter();
    }

    /*
     * La sobrecarga más simple para buffer () acepta un argumento de recuento que agrupa las emisiones en
     * Ese tamaño fijo. Si quisiéramos agrupar las emisiones en listas de ocho elementos, podemos hacer
     * que de la siguiente manera:
     */
    private static void base() {
        Observable.range(1, 50)
                .buffer(8)
                .subscribe(System.out::println);
    }

    private static void fixedSizeWithHashSet() {
        Observable.range(1, 50)
                .buffer(8, HashSet::new)
                .subscribe(System.out::println);
    }

    /*
     * Para hacer las cosas más interesantes, también puede proporcionar un argumento de omisión que especifique cómo
     * se deben omitir muchos elementos antes de comenzar un nuevo búfer. Si saltar es igual a contar, el
     * saltar no tiene ningún efecto. Pero si son diferentes, puede obtener algunos comportamientos interesantes. por
     * Por ejemplo, puede amortiguar 2 emisiones pero omitir 3 antes de que comience el próximo amortiguador, como se muestra aquí.
     * Esto esencialmente causará que cada tercer elemento no sea almacenado:
     */
    private static void fixedSizeSkip() {
        Observable.range(1, 10)
                .buffer(2, 3)
                .subscribe(System.out::println);
    }

    /*
     * Si hace saltos menos que count, puede obtener algunos amortiguadores rodantes interesantes. Si tu
     * almacenará elementos en un tamaño de 3 pero tendrá un salto de 1, obtendrá búferes rodantes. En el siguiente
     * código, por ejemplo, emitimos los números del 1 al 10 pero creamos buffers [1, 2, 3], luego
     * [2, 3, 4], luego [3, 4, 5], y así sucesivamente:
     */
    private static void fixedSizeSkipLessThanCount() {
        Observable.range(1,10)
                .buffer(3, 2)
                .subscribe(System.out::println);
    }

    /*
     * Definitivamente juegue con el argumento de omisión para buffer (), y puede encontrar un uso sorprendente
     * casos para ello. Por ejemplo, a veces uso el buffer (2,1) para emitir la emisión "anterior"
     * y la próxima emisión juntos, como se muestra aquí. También uso filter () para omitir la última lista,
     * que solo contiene 10:
     */
    private static void fixedSizeSkipLessThanCountFilter(){
        Observable.range(1,10)
                .buffer(2, 1)
                .filter(c -> c.size() == 2)
                .subscribe(System.out::println);
    }
}
