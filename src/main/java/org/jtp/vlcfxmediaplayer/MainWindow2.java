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
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.ParallelTransitionBuilder;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.jtp.vlcfxmediaplayer.components.ControlPanel;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;

/**
 * FXML Controller class
 *
 * @author Dub
 */
public class MainWindow2 extends AnchorPane implements Initializable {

    private Stage stage;
    private static Logger logger = Logger.getLogger(MainWindow.class);

    public MainWindow2(Stage stage) {
        this.stage = stage;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException ex) {
            logger.fatal(ex);
        }
    }
    @FXML
    private AnchorPane shadowHolder;
    @FXML
    private StackPane imageHolder;
    @FXML
    private ImageView videoImage;
    @FXML
    private StackPane dragBar;
    @FXML
    private HBox frameControls;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button maximizeButton;
    @FXML
    private Button exitButton;
    @FXML
    private Pane seResize;
    
    private ControlPanel controls;
    
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
    private static final double stageMinWidth = 600;
    private static final double stageMinHeight = stageMinWidth / 1.78;
    
    private MouseNotMovedHandler mouseNotMoved;
    private static ParallelTransition fadeOutTransition;
    private static ParallelTransition fadeInTransition;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initButtons();
        initMediaComponents();
        initStageBindings();
        initMouseEvents();
        fadeInTransition = ParallelTransitionBuilder.create()
                        .children(
                        FadeTransitionBuilder.create()
                        .node(dragBar)
                        .toValue(1.0)
                        .duration(Duration.millis(200))
                        .interpolator(Interpolator.EASE_OUT)
                        .build(),
                        FadeTransitionBuilder.create()
                        .node(controls)
                        .toValue(1.0)
                        .duration(Duration.millis(200))
                        .interpolator(Interpolator.EASE_OUT)
                        .build()).build();
        fadeOutTransition = ParallelTransitionBuilder.create()
                        .children(
                        FadeTransitionBuilder.create()
                        .node(dragBar)
                        .toValue(0.0)
                        .duration(Duration.seconds(3))
                        .interpolator(Interpolator.EASE_OUT)
                        .build(),
                        FadeTransitionBuilder.create()
                        .node(controls)
                        .toValue(0.0)
                        .duration(Duration.seconds(3))
                        .interpolator(Interpolator.EASE_OUT)
                        .build()).build();
    }

    private void initMouseEvents() {
        dragBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouX = me.getSceneX();
                mouY = me.getSceneY();
                me.consume();
            }
        });
        dragBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                stage.setX(me.getScreenX() - mouX);
                stage.setY(me.getScreenY() - mouY);
                me.consume();
            }
        });
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (fadeOutTransition != null) {
                    fadeOutTransition.stop();
                }
                fadeInTransition.play();
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (fadeInTransition != null) {
                    fadeInTransition.stop();
                }                
                fadeOutTransition.play();
            }
        });
        setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
                if (player.isPlayable()) {
                    if (!player.isPlaying()) {
                        player.play();
                    } else {
                        player.pause();
                    }
                }
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
                System.out.println(dragOffsetX + " : " + mouX);
            }
        });
        seResize.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                double x = t.getScreenX() + dragOffsetX;
                System.out.println(x - stage.getX());
                double width = (x - stage.getX());
                double height = width / 1.78;
                stage.setWidth(Math.max(stageMinWidth, width));
                stage.setHeight(Math.max(stageMinHeight, height));
                t.consume();
            }
        });
        
    }

    private void initMediaComponents() {
        imgWidth = 580;
        imgHeight = imgWidth / 1.78;

        vidImage = new WritableImage((int) imgWidth, (int) imgHeight);
        pixelWriter = vidImage.getPixelWriter();

        videoImage.setImage(vidImage);
        videoImage.fitWidthProperty().bind(imageHolder.widthProperty());
        videoImage.fitHeightProperty().bind(imageHolder.heightProperty());

        byteBgraInstance = PixelFormat.getByteBgraPreInstance();
        mediaComponent = new DirectMediaPlayerComponent(new BufferFormatCallback() {
            public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
                return new BufferFormat("RV32", (int) imgWidth, (int) imgHeight, new int[]{(int)imgWidth * 4}, new int[]{(int)imgHeight});
            }
        }){
            @Override
            public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {
                super.display(mediaPlayer, nativeBuffers, bufferFormat);

                ByteBuffer byteBuffer = nativeBuffers[0].getByteBuffer(0, nativeBuffers[0].size());
                pixelWriter.setPixels(0, 0, (int) imgWidth, (int) imgHeight, byteBgraInstance, byteBuffer, (int) imgWidth * 4);
            }
        };
        player = mediaComponent.getMediaPlayer();

        controls = new ControlPanel(player);
        StackPane.setAlignment(controls, Pos.BOTTOM_CENTER);
        imageHolder.getChildren().add(controls);
        imageHolder.setPadding(new Insets(0, 0, 5, 0));
    }

    private void initButtons() {
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });
        minimizeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if(stage.isFullScreen()){
                    stage.setFullScreen(false);
                }else{
                    stage.setIconified(true);
                }
            }
        });
        maximizeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                stage.setFullScreen(true);
            }
        });
    }

    private void initStageBindings() {
        mouseNotMoved = new MouseNotMovedHandler();
        stage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    //clear constraints for imageHolder and apply to root
                    AnchorPane.clearConstraints(imageHolder);
                    AnchorPane.setBottomAnchor(imageHolder, 0d);
                    AnchorPane.setTopAnchor(imageHolder, 0d);
                    AnchorPane.setLeftAnchor(imageHolder, 0d);
                    AnchorPane.setRightAnchor(imageHolder, 0d);
                    //remove fullscreenButton
                    frameControls.getChildren().remove(maximizeButton);
                    //hide resize pane
                    seResize.setVisible(false);
                    // init mouse listener
                    setEventHandler(MouseEvent.MOUSE_MOVED, mouseNotMoved);

                } else {
                    //clear constraints imageHolder re-apply shadow
                    AnchorPane.clearConstraints(imageHolder);
                    AnchorPane.setBottomAnchor(imageHolder, 10d);
                    AnchorPane.setTopAnchor(imageHolder, 10d);
                    AnchorPane.setLeftAnchor(imageHolder, 10d);
                    AnchorPane.setRightAnchor(imageHolder, 10d);
                    //add fullscreen button back to top
                    frameControls.getChildren().add(1, maximizeButton);
                    //show resize pane
                    seResize.setVisible(true);
                    // remove mouse listener
                    removeEventHandler(MouseEvent.MOUSE_MOVED, mouseNotMoved);
                }
            }
        });
    }

    private class MouseNotMovedHandler implements EventHandler<MouseEvent> {

        private Timeline timer;
        private KeyFrame frame;

        public MouseNotMovedHandler() {
            this.timer = new Timeline();
        }

        public void handle(MouseEvent t) {
            if(true){       
                setCursor(Cursor.DEFAULT);                
                fadeInTransition.play();
            }
            timer.getKeyFrames().clear();
            frame = new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if (fadeInTransition != null) {
                        fadeInTransition.stop();
                    }
                    fadeOutTransition.play();
                    fadeOutTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent t) {
                            setCursor(Cursor.NONE);
                        }
                    });
                }
            });
            timer.getKeyFrames().add(frame);
            timer.playFromStart();
        }
    }
    
    
}
