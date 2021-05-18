package de.beooo79.fplfox.model.bo.flightplan;

public class SegmentFactory {

    public static Segment fromString(String s) {
        if (s.length() == 5)
            return new Fix(s);
        else if (s.length() > 1 && s.length() < 4)
            return new RadioBeacon(s);
        else if (s.startsWith("Y"))
            return new Airway(s);
        else
            return new DefaultSegment(s);
    }

}
