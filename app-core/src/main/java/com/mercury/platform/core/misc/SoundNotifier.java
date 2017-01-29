package com.mercury.platform.core.misc;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.MercuryTradeSettings;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.MercuryEventHandler;
import com.mercury.platform.shared.events.custom.DndModeEvent;
import com.mercury.platform.shared.events.custom.UpdateReadyEvent;
import com.mercury.platform.shared.events.custom.WhisperNotificationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.Arrays;

/**
 * Created by Константин on 08.12.2016.
 */
public class SoundNotifier {
    private final Logger logger = LogManager.getLogger(SoundNotifier.class);
    private boolean dnd = false;
    public SoundNotifier() {
        EventRouter.INSTANCE.registerHandler(WhisperNotificationEvent.class,new MercuryEventHandler<WhisperNotificationEvent>(){
            public void handle(WhisperNotificationEvent event) {
                if (MercuryTradeSettings.WHISPER_NOTIFIER_STATUS == WhisperNotifierStatus.ALWAYS ||
                        ((MercuryTradeSettings.WHISPER_NOTIFIER_STATUS == WhisperNotifierStatus.ALTAB) &&
                                (MercuryTradeSettings.APP_STATUS == FrameStates.HIDE))) {
                    if(!dnd) {
                        ClassLoader classLoader = getClass().getClassLoader();
                        try (AudioInputStream stream = AudioSystem.getAudioInputStream(classLoader.getResource("app/icq-message.wav"))) {
                            Clip clip = AudioSystem.getClip();
                            clip.open(stream);
                            double gain = .5D;
                            float db = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                            gainControl.setValue(db);
                            clip.start();
                        } catch (Exception e) {
                            logger.debug("Cannot start playing music: " + Arrays.toString(e.getStackTrace()));
                        }
                    }
                }
            }
        });
        EventRouter.INSTANCE.registerHandler(UpdateReadyEvent.class, event -> {
            if(!dnd) {
                ClassLoader classLoader = getClass().getClassLoader();
                try (AudioInputStream stream = AudioSystem.getAudioInputStream(classLoader.getResource("app/patch_tone.wav"))) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(stream);
                    double gain = .5D;
                    float db = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(db);
                    clip.start();
                } catch (Exception e) {
                    logger.debug("Cannot start playing music: " + Arrays.toString(e.getStackTrace()));
                }
            }
        });
        EventRouter.INSTANCE.registerHandler(DndModeEvent.class, event -> {
            this.dnd = ((DndModeEvent)event).isDnd();
        });
    }
}