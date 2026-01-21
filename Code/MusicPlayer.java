import javax.sound.midi.*;
import java.io.File;

public class MusicPlayer {

    private Sequencer sequencer;
    private Synthesizer synthesizer;
    private boolean looping = false;
    private int volume = 80; // 0–127

    public MusicPlayer(String path) {
        try {
            Sequence sequence = MidiSystem.getSequence(new File(path));

            sequencer = MidiSystem.getSequencer(false); // IMPORTANT
            synthesizer = MidiSystem.getSynthesizer();

            synthesizer.open();
            sequencer.open();

            sequencer.setSequence(sequence);

            // Connect sequencer to synth
            Transmitter transmitter = sequencer.getTransmitter();
            Receiver receiver = synthesizer.getReceiver();
            transmitter.setReceiver(receiver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (sequencer == null) return;
        sequencer.setTickPosition(0);
        sequencer.start();
    }

    public void loop() {
        if (sequencer == null) return;

        looping = true;
        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        sequencer.start();
    }

    public void stop() {
        if (sequencer == null) return;
        looping = false;
        sequencer.stop();
        sequencer.setTickPosition(0);
    }

    public void setVolume(int volume) {
        this.volume = Math.max(0, Math.min(127, volume));

        if (synthesizer == null) return;

        MidiChannel[] channels = synthesizer.getChannels();
        for (MidiChannel channel : channels) {
            if (channel != null) {
                channel.controlChange(7, this.volume); // CC 7 = volume
            }
        }
    }

    public void close() {
        stop();
        if (sequencer != null) sequencer.close();
        if (synthesizer != null) synthesizer.close();
    }
}
