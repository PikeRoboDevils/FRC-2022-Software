/* (C) 2022 Pike RoboDevils, FRC Team 1018 */
package org.pikerobodevils.frc2022;

import badlog.lib.BadLog;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logger {
    private BadLog log = null;

    private List<Loggable> loggables;

    private Logger() {
        loggables = new ArrayList<>();
    }

    public void openLog() {
        log = BadLog.init(Filesystem.getOperatingDirectory().getAbsolutePath() + getLogFileName());
    }

    public void addLoggable(Loggable... toAdd) {
        Collections.addAll(this.loggables, toAdd);
    }

    private static String getLogFileName() {
        StringBuilder builder = new StringBuilder();
        builder.append(DriverStation.getEventName())
                .append("-")
                .append(matchTypeToAbbreviatedString(DriverStation.getMatchType()))
                .append(DriverStation.getMatchNumber());
        if (DriverStation.getReplayNumber() > 0) {
            builder.append("r").append(DriverStation.getReplayNumber());
        }
        builder.append(".bag");
        return builder.toString();
    }

    private static String matchTypeToAbbreviatedString(DriverStation.MatchType type) {
        switch (type) {
            case Practice:
                return "p";
            case Elimination:
                return "e";
            case Qualification:
                return "q";
            case None:
                return "n";
        }
        return "";
    }

    public static Logger INSTANCE = new Logger();

    public static Logger getInstance() {
        return INSTANCE;
    }

    public interface Loggable {
        void addLogItems(Logger logger);
    }
}
