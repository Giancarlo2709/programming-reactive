package com.everis.reactivex.operators.collections;

import com.google.common.collect.ImmutableList;
import io.reactivex.rxjava3.core.Observable;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionExample {

    public static void main(String[] args) {

        System.out.println("****************** toList ******************");
        toList();

        System.out.println("****************** toListWithCapacityHint ******************");
        toListWithCapacityHint();

        System.out.println("****************** toListWithLambda ******************");
        toListWithLambda();

        System.out.println("****************** toSortedList ******************");
        toSortedList();

        System.out.println("****************** toMap ******************");
        toMap();

        System.out.println("****************** toMapKeyPair ******************");
        toMapKeyPair();

        System.out.println("****************** toMapNoteReplace ******************");
        toMapNoteReplace();

        System.out.println("****************** toMultiMap ******************");
        toMultiMap();

        System.out.println("****************** collect ******************");
        collect();

        System.out.println("****************** collectInmutableList ******************");
        collectInmutableList();
    }

    /*
    * Un operador de colección común es toList (). Para un Observable <T> dado, recopilará
    * emisiones entrantes en una Lista <T> y luego empuje toda esa Lista <T> como una sola emisión
    * (a través de Single <List <T>>). En el siguiente fragmento de código, recopilamos emisiones de cadenas
    * en una Lista <String>. Después de las señales observables anteriores enComplete (), esa lista es
    * empujado hacia el observador para ser impreso:
     */
    private static void toList() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .toList()
                .subscribe(s -> System.out.println("Received: " + s));
    }

    /*
    * Por defecto, toList () usará una implementación estándar de ArrayList. Opcionalmente puedes
    * especifique un argumento entero para servir como la capacidad Sugerencia, y eso optimizará el
    * inicialización de ArrayList para esperar aproximadamente ese número de elementos:
     */
    private static void toListWithCapacityHint() {
        Observable.range(1, 1000)
                .toList(1000)
                .subscribe(s -> System.out.println("Received: " + s));
    }

    /*
    * Si desea especificar una implementación de lista diferente además de ArrayList, puede proporcionar un
    * Llamada lambda como argumento para construir uno. En el siguiente fragmento de código, proporciono
    * una instancia de CopyOnWriteArrayList para servir como mi lista:
     */
    private static void toListWithLambda() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .toList(CopyOnWriteArrayList::new)
                .subscribe(s -> System.out.println("Received: " + s));
    }

    /*
    * Un sabor diferente de toList () es toSortedList (). Esto recogerá las emisiones en un
    * lista que ordena los elementos de forma natural en función de la implementación de su Comparador. Entonces, lo hará
    *  emitir esa Lista ordenada <T> hacia el Observador:
     */
    private static void toSortedList() {
        Observable.just(6, 2, 5, 7, 1, 4, 9, 8, 3)
                .toSortedList()
                .subscribe(s -> System.out.println("Received: " + s));
    }

    /*
    * Para un Observable <T> dado, el operador toMap () recogerá las emisiones en el Map <K, T>,
    * donde K es el tipo de clave derivado de una función lambda <T, K> argumento que produce la clave
    * para cada emisión
     */
    private static void toMap() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .toMap(s -> s.charAt(0))
                .subscribe(s -> System.out.println("Received: " + s));
    }

    /*
    * El argumento lambda s -> s.charAt (0) toma cada cadena y deriva la clave para emparejarla
    * con. En este caso, estamos haciendo que el primer carácter de esa cadena sea la clave.
    * Si quisiéramos obtener un valor diferente al de la emisión para asociarlo con la clave,
    * puede proporcionar un segundo argumento lambda que asigna cada emisión a un valor diferente. Nosotros
    * puede, por ejemplo, mapear cada tecla de la primera letra con la longitud de esa cadena:
     */
    private static void toMapKeyPair() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .toMap(s -> s.charAt(0), String::length)
                .subscribe(s -> System.out.println("Received: " + s));
    }

    /*
    * Tenga en cuenta que si tengo una clave que se asigna a múltiples emisiones, la última emisión para esa clave es
    * va a reemplazar a los posteriores. Si hago que la longitud de la cuerda sea la clave para cada emisión,
    * Alpha será reemplazado por Gamma, que será reemplazado por Delta:
     */
    private static void toMapNoteReplace() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .toMap(String::length)
                .subscribe(s -> System.out.println("Received: " + s));
    }

    /*
    * Si desea que una clave determinada se asigne a múltiples emisiones, puede usar toMultiMap () en su lugar,
    * que mantendrá una lista de valores correspondientes para cada clave. Alfa, gamma y
    * Delta se colocará en una lista que está separada de la longitud cinco:
     */
    private static void toMultiMap() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .toMultimap(String::length)
                .subscribe(s -> System.out.println("Received: " + s));
    }

    /*
    * Cuando ninguno de los operadores de colección tiene lo que necesita, siempre puede usar el
    * operador collect() para especificar un tipo diferente en el que recopilar elementos. Por ejemplo, no hay
    * El operador toSet() para recopilar emisiones en un Set <T>, pero puede usar rápidamente collect ()
    * para hacer esto de manera efectiva. Deberá especificar dos argumentos que se crean con lambda
    * expresiones: initialValueSupplier, que proporcionará un nuevo HashSet para un nuevo
    * Observador y colector, que especifica cómo se agrega cada emisión a ese HashSet:
     */
    private static void collect() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .collect(HashSet::new, HashSet::add)
                .subscribe(s -> System.out.println("Received: " + s));
    }

    /*
    * Digamos que agregó Google Guava como una dependencia (h t t p s: // g i t h u b. C o m / g o o g l e / g u a v a) y
    * Desea recolectar emisiones en una Lista Inmutable. Para crear una Lista Inmutable, usted
    * tiene que llamar a su fábrica de constructores () para obtener una ImmutableList.Builder <T>. Entonces llamas
    * su método add () para colocar elementos en el generador, seguido de una llamada a build (), que devuelve un
    * sellado, final ImmutableList <T> que no se puede modificar.
    * Para recolectar emisiones en ImmutableList, puede suministrar
    * un ImmutableList.Builder <T> para su primer argumento lambda y luego agregue cada
    * elemento a través de su método add () en el segundo argumento. Esto emitirá
    * ImmutableList.Builder <T> una vez que está completamente poblado, y puede asignarlo () a su
    * llamada a build () para emitir una ImmutableList <T>:
     */
    private static void collectInmutableList() {
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .collect(ImmutableList::builder, ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .subscribe(s -> System.out.println("Received: " + s));
    }

}
