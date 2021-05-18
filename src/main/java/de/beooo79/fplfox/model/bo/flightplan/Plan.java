package de.beooo79.fplfox.model.bo.flightplan;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Builder.Default
    private String title = "IFPL";
    @Builder.Default
    private String ADEP = "";
    @Builder.Default
    private String ADES = "";
    @Builder.Default
    private List<Segment> waypoints = new LinkedList<Segment>();;

    public Integer size() {
        return waypoints.size();
    }

    public AdexpPlan getAdexp() {
        return new AdexpPlan();
    }

    public IcaoPlan getIcao() {
        return new IcaoPlan();
    }

    public void add(Segment segment) {
        waypoints.add(segment);
    }

    public void remove(Segment segment) {
        waypoints.remove(0);
    }

    public boolean contains(Segment segment) {
        return waypoints.contains(segment);
    }

    public boolean hasPrevious(Fix a) {
        int index = waypoints.lastIndexOf(a);
        if (index <= 0)
            return false;
        else
            return true;
    }

    public boolean hasNext(Fix a) {
        int index = waypoints.lastIndexOf(a);
        if (index < 0)
            return false;
        if (index == size() - 1)
            return false; // last entry
        else
            return true;
    }

    public Segment previous(Fix a) {
        if (hasPrevious(a))
            return waypoints.get(waypoints.lastIndexOf(a) - 1);
        else
            return null;
    }

    public Segment next(Fix a) {
        if (hasNext(a))
            return waypoints.get(waypoints.lastIndexOf(a) + 1);
        else
            return null;
    }

    public void setRoute(String sequence) {
        String[] split = sequence.split(" ");
        for (String s : split) {
            waypoints.add(SegmentFactory.fromString(s));
        }
    }

}
