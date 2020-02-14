package com.everis.reactivex.chapter8.flowable;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/*
Entonces, ¿cómo mitigamos esto? Podría volverse hacky e intentar usar la concurrencia nativa de Java
herramientas como semáforos. Pero afortunadamente, RxJava tiene una solución simplificada para esto
problema: el fluido. El Flowable es una variante de contrapresión del Observable que
le dice a la fuente que emita a un ritmo especificado por las operaciones posteriores.
En el siguiente código, reemplace Observable.range () con Flowable.range (), y esto
hará que toda esta cadena funcione con Flowables en lugar de Observables. Ejecute el código y
verá un comportamiento muy diferente con la salida:
 */
public class IntroducingFlowableExample {

    public static void main(String[] args) {
        System.out.println("************** backPressure **************");
        backPressure();

        ThreadUtil.sleep(Long.MAX_VALUE);
    }

    /*
    Notará algo muy diferente con la salida cuando use Flowable. He omitido
    partes de la salida anterior usando ... para resaltar algunos eventos clave. 128 emisiones fueron
    inmediatamente empujado desde Flowable.range (), que construyó 128 instancias de MyItem.
    Después de eso, observeOn () empujó 96 de ellos aguas abajo al suscriptor. Después de estos 96
    las emisiones fueron procesadas por el suscriptor, otras 96 fueron expulsadas de la fuente. Luego
    otros 96 fueron pasados ​​al suscriptor.
     */
    private static void backPressure() {
        Flowable.range(1, 999_999_999)
                .map(MyItem::new)
                .observeOn(Schedulers.io())
                .subscribe(myItem -> {
                    ThreadUtil.sleep(50);
                    System.out.println("Received: " + myItem);
                });
    }

    static final class MyItem {
        final int id;

        MyItem(int id) {
            this.id = id;
            System.out.println("Constructing MyItem " + id);
        }

        @Override
        public String toString() {
            return "MyItem " + id ;
        }
    }

    /*
     * ¿Ya ves un patrón? La fuente comenzó presionando 128 emisiones, y después de eso, un
     * La cadena Flowable procesó un flujo constante de 96 emisiones a la vez. Es casi como
     * toda la cadena Flowable se esfuerza por no tener más de 96 emisiones en su tubería en ningún momento
     * tiempo dado. ¡Efectivamente, eso es exactamente lo que está sucediendo! Esto es lo que llamamos contrapresión,
     * e introduce efectivamente una dinámica de atracción en la operación basada en empuje para limitar cómo
     * frecuentemente la fuente emite.
     * Pero, ¿por qué Flowable.range () comenzó con 128 emisiones, y por qué solo observóOn ()
     * enviar 96 aguas abajo antes de solicitar otros 96, dejando 32 emisiones sin procesar? los
     * el lote inicial de emisiones es un poco más grande, por lo que se pone en cola algún trabajo adicional si hay algún inactivo
     * hora. Si (en teoría) nuestra operación Flowable comenzara solicitando 96 emisiones y
     * continuó emitiendo constantemente a 96 emisiones a la vez, habría momentos en los que
     * las operaciones pueden esperar ociosamente los próximos 96. Por lo tanto, un caché adicional de 32 emisiones
     * se mantiene para proporcionar trabajo durante estos momentos de inactividad, lo que puede proporcionar mayor
     * rendimiento Esto es muy parecido a un almacén que tiene un pequeño inventario adicional para suministrar pedidos
     * mientras espera más de la fábrica.
     * Lo bueno de Flowables y sus operadores es que generalmente hacen todo el trabajo para
     * ti. No tiene que especificar ninguna política o parámetro de contrapresión a menos que necesite
     * cree sus propios Flowables desde cero o trate con fuentes (como Observables) que lo hacen
     * No implementar contrapresión. Cubriremos estos casos en el resto del capítulo, y
     * con suerte, no te encontrarás con ellos a menudo.
     * De lo contrario, Flowable es como un Observable con casi todos los operadores que aprendimos.
     * lejos. Puede convertir de un Observable en un Flowable y viceversa, lo que haremos
     * cubrir más tarde. Pero primero, cubramos cuándo debemos usar Flowables en lugar de Observables.
     */
}
