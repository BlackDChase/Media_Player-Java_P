/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testplayer;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author Hussain
 */
public class FXMLDocumentController implements Initializable {
    
    private MediaPlayer mediaPlayer;
    private String filePath;
    
    @FXML
    private MediaView mediaView;
        
    @FXML
    private Slider vol_slider;
    
    @FXML
    private Slider seek_slider;
    
    @FXML
    private Slider speed_slider;
    
    private double curr_rate,curr_vol;
    
    
    
    @FXML
    private MaterialDesignIconView mplay,mpause;
    
    private int flag = 0;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select *.mp4 ", "*.mp4");  
            fileChooser.getExtensionFilters().add(filter);
            File file = fileChooser.showOpenDialog(null);
            filePath = file.toURI().toString();
            
            if(filePath != null)
            {
                
                flag++;
                if(flag>1)
                {
                    mediaPlayer.stop();
                }
                
                Media media = new Media(filePath);
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                    DoubleProperty width = mediaView.fitWidthProperty();
                    DoubleProperty height = mediaView.fitHeightProperty();
                    
                    width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                    height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
                   
                    // ------------------ vol -------------------------//
                    vol_slider.setValue(mediaPlayer.getVolume()*100);
                    vol_slider.valueProperty().addListener(new InvalidationListener()
                    {                  
                        @Override
                        public void invalidated(Observable observable)
                        {
                          mediaPlayer.setVolume(vol_slider.getValue()/100);
                          curr_vol = mediaPlayer.getVolume();
                        }
                    
                    });
                    // -------------------- vol --------------------------//
                   
                
                 // -------------   Time test  ---------------------- //
                 
                 Double time = mediaPlayer.getTotalDuration().toSeconds();

                mediaPlayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
                seek_slider.setValue(newValue.toSeconds());
                });
                    seek_slider.setOnMouseClicked((MouseEvent mouseEvent) -> {
                    mediaPlayer.seek(Duration.seconds(seek_slider.getValue()));
                     });
                    
                    seek_slider.maxProperty().bind(Bindings.createDoubleBinding(
                    () -> mediaPlayer.getTotalDuration().toSeconds(),
                    mediaPlayer.totalDurationProperty()));
                 
                 // -------------   Time Test  ---------------------- //
                 
                 
                 // -------------  Speed ----------------------------//
                 
                 //double currentSpeed = 0;
                 speed_slider.valueProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable)
                    {
                        double lastSpeed = 1.0;
                        if (speed_slider.isValueChanging())
                         {
                            double currentSpeed = Math.round( (int) (speed_slider.getValue() * 100) )/100.0; 
                            if(currentSpeed != lastSpeed){
                            lastSpeed = currentSpeed;
                            mediaPlayer.setRate(currentSpeed);
                            curr_rate = currentSpeed;
                        }
                }}});
                 
                 
                 // ----------------speed   -------------------------//
                 
        
        
                mediaPlayer.play();
                
                
            }
        
    }
    
     
    
    @FXML
    private void pauseVideo(ActionEvent event)
    {
        mediaPlayer.pause();        
    }
    
    @FXML
    private void playVideo(ActionEvent event)
    {
        mediaPlayer.play();
        mediaPlayer.setRate(curr_rate);
        mediaPlayer.setVolume(curr_vol);
        
        
    }
    
    @FXML
    private void stopVideo(ActionEvent event)
    {
        curr_rate = 1;
        curr_vol = 50;
        mediaPlayer.stop();
        speed_slider.setValue(1);
        vol_slider.setValue(50);
        
        
    }
    
    @FXML
    private void exitVideo(ActionEvent event)
    {
        System.exit(0);
    }
    
    @FXML
    private void muteVideo(ActionEvent event)
    {
        mediaPlayer.setVolume(0);
    }
    
    
        
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    
    
}
//- 