package org.pikerobodevils.lib.led;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Notifier;

public class DevilAddressableLED extends AddressableLED {
    private AddressableLEDBuffer buffer;

    private Notifier updateNotifier;

    private LEDPattern pattern;

    private final double animationDT = 0.1;

    public DevilAddressableLED(int port, int length) {
        super(port);
        updateNotifier = new Notifier(this::update);
        buffer = new AddressableLEDBuffer(length);
        setLength(length);
        setData(buffer);
        start();
        setPattern(buffer -> {});
    }

    public void setPattern(LEDPattern pattern) {
        updateNotifier.stop();
        this.pattern = pattern;
        update();
        if(pattern.isAnimated()) {
            updateNotifier.startPeriodic(animationDT);
        }
    }

    public void update() {
        pattern.setLEDs(buffer);
        setData(buffer);
    }


}