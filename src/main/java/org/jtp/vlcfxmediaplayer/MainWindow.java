/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jtp.vlcfxmediaplayer;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jtp.vlcfxmediaplayer.components.MediaSlider;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;

/**
 * FXML Controller class
 *
 * @author Dub
 */
public class MainWindow extends AnchorPane implements Initializable {
    private Stage stage;
    public MainWindow(Stage stage){
        this.stage = stage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private StackPane imageHolder;
    @FXML
    private ImageView videoImage;
    @FXML
    private HBox controlBar;
    @FXML
    private Button playButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button backButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button stopButton;
    @FXML
    private HBox frameControls;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button maximizeButton;
    @FXML
    private Button exitButton;
    @FXML 
    private VBox controlBarHolder;
    @FXML
    private Label nameLabel;
    
    private MediaSlider slider;
    private Group hideableComponents = new Group();
    private final Font myFont = Font.loadFont(getClass().getResourceAsStream("/styles/freeagent.ttf"),12d);
    
    private WritableImage vidImage;
    private PixelWriter pixelWriter;
    private WritablePixelFormat<ByteBuffer> byteBgraInstance;
    private double imgWidth;
    private double imgHeight;
    
    private DirectMediaPlayerComponent mediaComponent;
    private static MediaPlayer player;
    private long mediaLength;
    private boolean isPaused;
    
    private double mouX;
    private double mouY;
    private double dragOffsetX;
    private double stageMinWidth = 600;
    private double stageMinHeight = stageMinWidth / 1.78;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initComponents();      
    }    
    private void initComponents(){
        
        HBox sh = new HBox();
        slider = new MediaSlider(100);
        sh.getChildren().add(slider);        
        HBox.setHgrow(slider, Priority.ALWAYS);
        slider.setMax(100000);
        
        controlBarHolder.getChildren().add(0, sh);
        initLabels();
        initButtons();
        addComponentsToGroup();
        addGroupActions();
    }
    
    private void initLabels(){
        nameLabel.setFont(myFont);
    }
    private void initButtons(){
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });
        minimizeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                stage.setIconified(true);
            }
        });
        maximizeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                stage.setFullScreen(true);
            }
        });
    }

    private void addComponentsToGroup() {
        
    }

    private void addGroupActions() {
        
    }
    
    
}
