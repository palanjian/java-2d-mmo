package audio;

import main.GamePanel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

public class AudioHandler {

    Clip clip;
    public AudioHandler(){
    }


    public void setFile(String soundName){
        try{
            URL url = getClass().getResource("/sounds/" + soundName + ".wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }
        catch(Exception e){
            System.out.println("Cannot play sound");
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

}
