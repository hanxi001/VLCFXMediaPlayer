/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jtp.vlcfxmediaplayer.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Dub
 */
public class MediaSlider extends StackPane implements Initializable {
    private long mediaLength = 0;
    
    public MediaSlider(long max){
        this.mediaLength = max;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/controls/MediaSlider.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MediaSlider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Slider slider;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        slider.setMax(mediaLength);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> source, Number old_val, Number new_val) {
                progressBar.setProgress(new_val.doubleValue() / mediaLength);                
            }
        });
    } 

    @Override
    public void setWidth(double d) {
        super.setWidth(d);
        slider.setPrefWidth(d);
        progressBar.setPrefWidth(d);//To change body of generated methods, choose Tools | Templates.
    }
    
    public final void setMax(double d) {
        mediaLength = (long)d;
        slider.setMax(mediaLength);
    }

    public final double getMax() {
        return slider.getMax();
    }

    public final DoubleProperty maxProperty() {
        return slider.maxProperty();
    }

    public final DoubleProperty minProperty() {
        return slider.minProperty();
    }

    public final void setValue(double d) {
        slider.setValue(d);
    }

    public final double getValue() {
        return slider.getValue();
    }

    public final DoubleProperty valueProperty() {
        return slider.valueProperty();
    }

    public final BooleanProperty valueChangingProperty() {
        return slider.valueChangingProperty();
    }

    public final void setProgress(double d) {
        progressBar.setProgress(d);
    }

    public final double getProgress() {
        return progressBar.getProgress();
    }

    public final DoubleProperty progressProperty() {
        return progressBar.progressProperty();
    }
    
}
