/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jtp.vlcfxmediaplayer;

import com.sun.jna.Memory;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jtp.vlcfxmediaplayer.components.MediaSlider;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;

/**
 * FXML Controller class
 *
 * @author Dub
 */
public class MainWindow extends AnchorPane implements Initializable {

    private Stage stage;

    public MainWindow(Stage stage) {
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
    @FXML 
    private StackPane dragBar;
    @FXML
    private Pane seResize;
    @FXML 
    private HBox sliderHolder;
    
    private MediaSlider slider;
    
    private Group hideableComponents = new Group();
    private final Font myFont = Font.loadFont(getClass().getResourceAsStream("/styles/freeagent.ttf"), 12d);
    
    private WritableImage vidImage;
    private PixelWriter pixelWriter;
    private WritablePixelFormat<ByteBuffer> byteBgraInstance;
    private double imgWidth;
    private double imgHeight;
    
    private DirectMediaPlayerComponent mediaComponent;
    private MediaPlayer player;
    private long mediaLength;
    private boolean isPaused;
    
    private double mouX;
    private double mouY;
    private double dragOffsetX;
    private double stageMinWidth;
    private double stageMinHeight;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initWindowComponents();
        initComponents();

    }

    private void initWindowComponents(){
        stageMinWidth = getPrefWidth();
        stageMinHeight = stageMinWidth / 1.78; //16:9
        
        videoImage.fitWidthProperty().bind(imageHolder.widthProperty());
        videoImage.fitHeightProperty().bind(imageHolder.heightProperty());
        
        dragBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouX = me.getSceneX();
                mouY = me.getSceneY();
            }
        });
        dragBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                stage.setX(me.getScreenX() - mouX);
                stage.setY(me.getScreenY() - mouY);
            }
        });
        seResize.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                seResize.setCursor(Cursor.SE_RESIZE);
                t.consume();
            }
        });
        seResize.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                seResize.setCursor(Cursor.DEFAULT);
                t.consume();
            }
        });
        seResize.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                dragOffsetX = (stage.getX() + stage.getWidth() - t.getScreenX());
                t.consume();
            }
        });
        seResize.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                double x = t.getScreenX() + dragOffsetX;                
                double width = (x - stage.getX());
                double height = width / 1.78;
                
                stage.setWidth(Math.max(stageMinWidth, width));
                stage.setHeight(Math.max(stageMinHeight, height));
                t.consume();
            }
        });
    }
    
    private void initComponents() { 
        initSlider();
        initLabels();
        initButtons();
        addComponentsToGroup();
        addGroupActions();
    }

    private void initSlider(){
        
        slider = new MediaSlider(10000); 
        HBox.setHgrow(slider, Priority.ALWAYS);
        slider.setPrefWidth(sliderHolder.getMaxWidth());
        sliderHolder.getChildren().addAll(slider);
        
    }
    private void initLabels() {
        nameLabel.setFont(myFont);
    }

    private void initButtons() {
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

    private void initMediaComponents() {
        this.imgWidth = videoImage.getFitWidth();
        this.imgHeight = videoImage.getFitHeight();

        this.vidImage = new WritableImage((int) imgWidth, (int) imgHeight);
        this.pixelWriter = vidImage.getPixelWriter();
        this.byteBgraInstance = PixelFormat.getByteBgraPreInstance();
        mediaComponent = new DirectMediaPlayerComponent("RV32", (int) imgWidth, (int) imgHeight, (int) imgWidth * 4) {
            @Override
            public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {
                super.display(mediaPlayer, nativeBuffers, bufferFormat);
                ByteBuffer byteBuffer = nativeBuffers[0].getByteBuffer(0, nativeBuffers[0].size());
                pixelWriter.setPixels(0, 0, (int) imgWidth, (int) imgHeight, byteBgraInstance, byteBuffer, (int) imgWidth * 4);
            }
        };
        this.player = mediaComponent.getMediaPlayer();
        player.addMediaPlayerEventListener(new MediaPlayerEventListener() {
            public void mediaChanged(MediaPlayer mp, libvlc_media_t l, String string) {
            }

            public void opening(MediaPlayer mp) {
            }

            public void buffering(MediaPlayer mp, float f) {
            }

            public void playing(MediaPlayer mp) {
            }

            public void paused(MediaPlayer mp) {
            }

            public void stopped(MediaPlayer mp) {
            }

            public void forward(MediaPlayer mp) {
            }

            public void backward(MediaPlayer mp) {
            }

            public void finished(MediaPlayer mp) {
            }

            public void timeChanged(MediaPlayer mp, long l) {
            }

            public void positionChanged(MediaPlayer mp, float f) {
            }

            public void seekableChanged(MediaPlayer mp, int i) {
            }

            public void pausableChanged(MediaPlayer mp, int i) {
            }

            public void titleChanged(MediaPlayer mp, int i) {
            }

            public void snapshotTaken(MediaPlayer mp, String string) {
            }

            public void lengthChanged(MediaPlayer mp, long l) {
            }

            public void videoOutput(MediaPlayer mp, int i) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void error(MediaPlayer mp) {
            }

            public void mediaMetaChanged(MediaPlayer mp, int i) {
            }

            public void mediaSubItemAdded(MediaPlayer mp, libvlc_media_t l) {
            }

            public void mediaDurationChanged(MediaPlayer mp, long l) {
            }

            public void mediaParsedChanged(MediaPlayer mp, int i) {
            }

            public void mediaFreed(MediaPlayer mp) {
            }

            public void mediaStateChanged(MediaPlayer mp, int i) {
            }

            public void newMedia(MediaPlayer mp) {
            }

            public void subItemPlayed(MediaPlayer mp, int i) {
            }

            public void subItemFinished(MediaPlayer mp, int i) {
            }

            public void endOfSubItems(MediaPlayer mp) {
            }
        });
    }
    //==========================================================================
    private class TimeChangeListener implements InvalidationListener {

        @Override
        public void invalidated(Observable o) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (slider.getSlider().isPressed()) {
                        long seek = (long) slider.getValue();
                        player.setTime(seek);
                    } else {
                        //do nothing
                    }
                }
            });
        }
    }
    
    
}
