package com.everis.reactivex.chapter9.transformers;

import javafx.application.Application;
import javafx.stage.Stage;

/*
En raras ocasiones, es posible que tenga que pasar un Observable a otra API
eso lo convierte en un tipo propietario. Esto se puede hacer simplemente pasando un Observable
como argumento para una fábrica que hace esta conversión. Sin embargo, esto no siempre se siente
fluido, y aquí es donde entra el operador to ().
 */
public class UsingToExample extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*VBox root = new VBox();
        Label label = new Label("");

        //observable with second timer
        Observable<String> seconds =
                Observable.interval(1, TimeUnit.SECONDS)
                    .map(i -> i.toString())
                .observeOn(JavaFxScheduler.platform());

        //turn observable into binding
        Binding<String> binding = JavaFxObserver.toBinding(seconds);

        //Bind Label to Binding
        label.textProperty().bind(binding);
        root.setMinSize(200, 100);
        root.getChildren().addAll(label);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();*/


        /*VBox root = new VBox();
        Label label = new Label("");
        // Turn Observable into Binding
        Binding<String> binding =
                Observable.interval(1, TimeUnit.SECONDS)
                        .map(i -> i.toString())
                        .observeOn(JavaFxScheduler.platform())
                        .to(JavaFxObserver::toBinding);
        //Bind Label to Binding
        label.textProperty().bind(binding);
        root.setMinSize(200, 100);
        root.getChildren().addAll(label);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
         */
    }
}
