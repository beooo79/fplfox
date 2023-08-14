package com.github.beooo79.plan;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record Airway(String name, List<AirwaySegment> segments) implements Serializable {
    @Serial
    private static final long serialVersionUID = -7428243350526926375L;

    public void addSegment(AirwaySegment s) {
        segments.add(s);
    }

    public List<AirwaySegment> resolve(Fix fromFix, String nextFix) {
        List<AirwaySegment> sl = new ArrayList<AirwaySegment>();
        // sequenceNumber of fromFix
        int noFrom = getSequenceNumberOfFix(fromFix.getId());
        // sequenceNumber of nextFix
        int noNext = getSequenceNumberOfFix(nextFix);
        //FoxMain.getFrame().sysout("Segments: " + noFrom + "-->" + noNext);
        if (noFrom > 0 && noNext > 0) {
            if (noFrom < noNext) {
                for (int i = noFrom + 1; i <= noNext; i++) {
                    sl.add(segments.get(i - 1));
                }
            } else {
                for (int i = noFrom - 1; i >= noNext; i--) {
                    sl.add(segments.get(i - 1));
                }
            }
        }
        return sl;
    }

    private int getSequenceNumberOfFix(String fix) {
        for (AirwaySegment s : segments) {
            if (s.fix().getId().equals(fix)) {
                return s.sequenceNumber();
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (AirwaySegment seg : segments) {
            buf.append(seg.fix().getId()).append("-");
        }
        return name + " : " + buf;
    }

}
