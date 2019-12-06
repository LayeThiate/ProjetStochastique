package view;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class ApplicationPrincipale extends Application{
	
    @Override
    public void start(Stage primaryStage) throws IOException {
		
        try{
        	Parent content = FXMLLoader.load(getClass().getResource("UI.fxml"));
        	
        	primaryStage.setMinHeight(625.0);
        	primaryStage.setMinWidth(990.0);
        	//Add the scene to the stage and show it
            primaryStage.setTitle("Résolution Stochastique");
            primaryStage.setScene(new Scene(content));
            primaryStage.show();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
