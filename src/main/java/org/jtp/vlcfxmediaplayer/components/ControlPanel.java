/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jtp.vlcfxmediaplayer.components;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.jtp.vlcfxmediaplayer.util.SystemPrefs;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

/**
 * FXML Controller class
 *
 * @author Dub
 */
public class ControlPanel extends AnchorPane implements Initializable {
    private MediaPlayer player;
    private long mediaLength;
    private int volume;
    
    public ControlPanel(MediaPlayer player){
        this.player = player;
        try {            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/controls/ControlPanel.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Slider timeSlider;
    @FXML
    private Button backButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button playButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button loadButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private Label totalTimeLabel;
    @FXML
    private ProgressBar volProgressBar;
    @FXML
    private Slider volSlider;
    
    private SimpleStringProperty currentTimeProperty;
    private SimpleStringProperty totalTimeProperty;

    private final Font myFont = Font.loadFont(getClass().getResourceAsStream("/styles/freeagent.ttf"), 10d);
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(player != null){
            volume = player.getVolume();
        }
        
        initSliders();
        initButtons();
        initLabels();
        initPlayer();
    }    
    
    private void initSliders(){
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> source, Number old_val, Number new_val) {
                progressBar.setProgress(new_val.doubleValue() / mediaLength);                
            }
        });
        timeSlider.valueProperty().addListener(new TimeChangeListener());
        volSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> source, Number old_val, Number new_val) {
                volProgressBar.setProgress(new_val.doubleValue() / volume);                
            }
        });
        volSlider.valueProperty().addListener(new VolumeChangeListener());
    }
    private void initButtons(){
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if(player.isPlaying()){
                    player.stop();
                }
            }
        });
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if(!player.isPlaying()){
                    player.play();
                }
            }
        });
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if(player.isPlaying()){
                    player.pause();
                }
            }
        });
        forwardButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            }
        });
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                loadMedia();
            }
        });
    }
    private void initLabels(){
        totalTimeProperty = new SimpleStringProperty();        
        //totalTimeLabel.setFont(myFont);
        totalTimeLabel.setTextFill(Color.WHITE);
        totalTimeLabel.textProperty().bind(totalTimeProperty);
        
        currentTimeProperty = new SimpleStringProperty();
        //currentTimeLabel.setFont(myFont);
        currentTimeLabel.setTextFill(Color.WHITE);
        currentTimeLabel.textProperty().bind(currentTimeProperty);
        
        titleLabel.setFont(myFont);
        titleLabel.setTextFill(Color.WHITE);
    }

    private void initPlayer() {
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

            public void timeChanged(MediaPlayer mp, final long l) {
                // update controlPanel slider Position
                Platform.runLater(new Runnable() {
                    public void run() {
                        timeSlider.setValue(l);
                        currentTimeProperty.set(timeFormat(l));
                    }
                });
                
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
    
     private void loadMedia(){
        if(player.isPlaying()){
            player.stop();
        }
        FileChooser fc = new FileChooser();
        if(SystemPrefs.getInstance().getLastUsedDirectory() != null){
            fc.setInitialDirectory(new File(SystemPrefs.getInstance().getLastUsedDirectory()));
        }
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video","*.mp4", "*.avi", "*.mkv"));
        File f = fc.showOpenDialog(null);
        if(f != null){
            SystemPrefs.getInstance().setLastUsedDirectory(f.getParent());
            player.startMedia(f.getAbsolutePath());
            player.pause();
            
            checkVideoSettings();
            
            mediaLength = player.getLength();            
            timeSlider.setMax(mediaLength);              
            titleLabel.setText(player.getMediaMeta().getTitle()); 
            totalTimeProperty.set(timeFormat(mediaLength));
            volSlider.setValue(player.getVolume());
            
            player.play();
        }
     }

    private void checkVideoSettings() {
        //would like to check for aspect-ratio but always comes up null
        
        System.out.println();
    }

    private String timeFormat(long time){
        long t = time;//mediaLength - time;
        if(t >= 3600000){
        return String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(t),
            TimeUnit.MILLISECONDS.toMinutes(t) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(t)),
            TimeUnit.MILLISECONDS.toSeconds(t) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(t)));
        }else {
            return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(t) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(t)),
            TimeUnit.MILLISECONDS.toSeconds(t) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(t)));
        }
    }
    
    private class TimeChangeListener implements InvalidationListener {

        @Override
        public void invalidated(Observable o) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (timeSlider.isPressed()) {
                        long seek = (long) timeSlider.getValue();
                        player.setTime(seek);
                    } else {
                        //do nothing
                    }
                }
            });
        }
    }
    
    private class VolumeChangeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
            player.setVolume(t1.intValue());
        }
    }
     
}
