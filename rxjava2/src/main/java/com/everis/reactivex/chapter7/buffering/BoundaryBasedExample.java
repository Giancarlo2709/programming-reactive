package com.everis.reactivex.chapter7.buffering;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
La variación más poderosa de buffer () es aceptar otro Observable como límite
argumento. No importa de qué tipo emita este otro Observable. Todo lo que importa es
cada vez que emite algo, usará el tiempo de esa emisión como el límite del búfer. En
en otras palabras, la aparición arbitraria de emisiones de otro observable determinará
cuándo "cortar" cada búfer.
 */
public class BoundaryBasedExample {

    public static void main(String[] args) {

        System.out.println("************** base **************");
        base();
        ThreadUtil.sleep(9000);
    }

    /*
     * Por ejemplo, podemos realizar nuestro ejemplo anterior con emisiones de 300 milisegundos
     * amortiguado cada 1 segundo utilizando esta técnica. Podemos tener Observable.interval () de 1
     * segundo servir como límite para nuestro Observable.interval () que emite cada 300
     * milisegundos:
     */
    private static void base() {
        Observable<Long> cutOffs =
                Observable.interval(3, TimeUnit.SECONDS);
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 300) //map to elapsed time
                .buffer(cutOffs)
                .subscribe(System.out::println);


    }
}
