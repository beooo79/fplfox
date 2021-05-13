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
    private String ades = "";
    @Builder.Default
    private String adep = "";
    @Builder.Default
    private List<Object> waypoints = new LinkedList<Object>();;

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
        waypoints.add(new Object());
    }

    public void remove(Segment segment) {
        waypoints.remove(0);
    }

}
