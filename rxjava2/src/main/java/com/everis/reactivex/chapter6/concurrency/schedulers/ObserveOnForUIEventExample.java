package com.everis.reactivex.chapter6.concurrency.schedulers;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Scanner;

/*
Cuando se trata de crear aplicaciones móviles, aplicaciones de escritorio y otras experiencias de usuario,
los usuarios tienen poca paciencia para las interfaces que cuelgan o se congelan mientras se realiza el trabajo. los
La actualización visual de las interfaces de usuario a menudo se realiza mediante un único subproceso de interfaz de usuario dedicado, y
los cambios en la interfaz de usuario deben realizarse en ese hilo. Los eventos de entrada del usuario son típicamente
disparado en el hilo de la interfaz de usuario también. Si la entrada de un usuario activa el trabajo, y ese trabajo no se mueve a
otro hilo, ese hilo de la IU estará ocupado. Esto es lo que hace que la interfaz de usuario
no responde, y los usuarios de hoy esperan algo mejor que esto. Todavía quieren interactuar con el
aplicación mientras el trabajo se realiza en segundo plano, por lo que la concurrencia es imprescindible.
¡Afortunadamente, RxJava puede venir al rescate! Puede usar observeOn () para mover eventos de IU a
un cálculo o IO Scheduler para hacer el trabajo, y cuando el resultado esté listo, muévalo
al hilo de la interfaz de usuario con otro observeOn (). Este segundo uso de observeOn () pondrá
emisiones en un subproceso de interfaz de usuario utilizando un programador personalizado que se ajusta alrededor del subproceso de interfaz de usuario.
Bibliotecas de extensiones de RxJava como RxAndroid (h t t p s: // g i t h u b. C o m / R e a c t i v e X / R x A n d r o i
d), RxJavaFX (h t t p s: // g i t h u b. c o m / R e a c t i v e X / R x J a v a F X) y RxSwing (h t t p s: // g i t h u b
. c / m / R e a c t i v e X / R x S w i n g) vienen con estas implementaciones personalizadas del Programador.
 */
public class ObserveOnForUIEventExample extends Application {

    /*
    * Por ejemplo, supongamos que tenemos una aplicación JavaFX simple que muestra un ListView <String>
    * de los 50 estados de EE. UU. cada vez que se hace clic en un botón. Podemos crear
    * Observable <ActionEvent> fuera del botón y luego cambie a un IO Scheduler con
    * observeOn () (subscribeOn () no tendrá ningún efecto contra las fuentes de eventos de IU). Podemos cargar
    * los 50 estados de una respuesta web de texto mientras está en el IO Scheduler. Una vez que los estados son
    * devuelto, podemos usar otro observeOn () para volver a colocarlos en JavaFxScheduler, y
    * rellenarlos de forma segura en ListView <String> en el subproceso JavaFX UI:
     */

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();

        ListView<String> listView = new ListView<>();
        Button refreshButton = new Button("REFRESH");

        /*JavaFxObservable.actionEventsOf(refreshButton)
                .observeOn(Schedulers.io())
                .flatMapSingle(a ->
                        Observable.fromArray(getResponse("https://goo.gl/S0xuOi")
                                .split("\\r?\\n")
                        ).toList()
                ).observeOn(JavaFxScheduler.platform())
                .subscribe(list ->
                        listView.getItems().setAll(list));
         */
    }

    private static String getResponse(String path) {
        try {
            return new Scanner(new URL(path).openStream(),
                    "UTF-8").useDelimiter("\\A").next();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
