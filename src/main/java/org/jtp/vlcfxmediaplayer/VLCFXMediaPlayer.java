/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jtp.vlcfxmediaplayer;


import com.sun.jna.NativeLibrary;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

/**
 *
 * @author Dub
 */
public class VLCFXMediaPlayer extends Application{
    private boolean libsOK;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        NativeDiscovery nd = new NativeDiscovery();
        if(nd.discover()){
            System.out.println("Libraries found: SUCCESS.");
            libsOK = true; 
        }else{
            //do nothing
        }
    }

    @Override
    public void start(Stage stage) throws Exception { 
        if(libsOK){
            MainWindow2 window = new MainWindow2(stage);
            
            Scene scene = new Scene(window);
            scene.setFill(Color.TRANSPARENT);
            
            stage.initStyle(StageStyle.TRANSPARENT);            
            stage.setScene(scene);
            stage.show();      
        }else{
            System.exit(0);
        }
    }
    
    
}
