/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jtp.vlcfxmediaplayer;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Dub
 */
public class VLCFXMediaPlayer extends Application{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {        
        MainWindow window = new MainWindow(stage);
        stage.initStyle(StageStyle.TRANSPARENT);      
        
        Scene scene = new Scene(window);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
        
    }
    
    
}
