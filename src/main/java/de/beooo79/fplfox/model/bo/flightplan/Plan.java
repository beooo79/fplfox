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
    private String aerodromeOfDeparture = "";
    @Builder.Default
    private String aerodromeOfDestination = "";
    @Builder.Default
    private List<Segment> waypoints = new LinkedList<>();

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
        waypoints.remove(segment);
    }

    public boolean contains(Segment segment) {
        return waypoints.contains(segment);
    }

    public boolean hasPrevious(Segment a) {
        var index = waypoints.lastIndexOf(a);
        return index > 0;
    }

    public boolean hasNext(Segment c) {
        var index = waypoints.lastIndexOf(c);
        if (index < 0)
            return false;
        if (index == size() - 1)
            return false; // last entry
        else
            return true;
    }

    public Segment previous(Segment a) {
        if (hasPrevious(a))
            return waypoints.get(waypoints.lastIndexOf(a) - 1);
        else
            return null;
    }

    public Segment next(Segment c) {
        if (hasNext(c))
            return waypoints.get(waypoints.lastIndexOf(c) + 1);
        else
            return null;
    }

    public void setRoute(String sequence) {
        String[] split = sequence.split(" ");
        for (String s : split) {
            waypoints.add(SegmentFactory.fromString(s));
        }
    }

    public void addAfter(Segment fix, Segment fixToAdd) {
        if (waypoints.contains(fix)) {
            for (var i = 0; i < waypoints.size(); i++) {
                if (waypoints.get(i) == fix) {
                    waypoints.add(i + 1, fixToAdd);
                }
            }
        }
    }

}
