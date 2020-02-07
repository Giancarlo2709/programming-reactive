package com.everis.reactivex.disposable;


import io.reactivex.rxjava3.core.Observable;

/*
Si su Observable.create () devuelve un Observable de larga duración o infinito, usted
idealmente debería verificar el método isDisposed () de ObservableEmitter regularmente, para ver
si debe seguir enviando emisiones. Esto evita que se trabaje innecesariamente
hecho si la suscripción ya no está activa.
En este caso, debe usar Observable.range (), pero por el bien del ejemplo, vamos
digamos que estamos emitiendo enteros en un bucle for en Observable.create (). Antes de emitir cada
entero, debe asegurarse de que ObservableEmitter no indique que hay una eliminación
fue llamado:
 */
public class DisposableCreateExample {

    public static void main(String[] args) {
        Observable<Integer> source = Observable.create(observableEmitter -> {
            try {
                for (int i = 0; i <= 50; i++){
                    while(!observableEmitter.isDisposed()) {
                        observableEmitter.onNext(i);
                    }
                    if(observableEmitter.isDisposed())
                        return;
                }
                observableEmitter.onComplete();
            } catch(Throwable throwable) {
                observableEmitter.onError(throwable);
            }
        });

        source.subscribe(s-> System.out.println("Received: " + s)).dispose();

    }
}
