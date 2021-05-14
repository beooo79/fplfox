package de.beooo79.fplfox.model.bo.flightplan;

public abstract class Segment {

    protected String name;

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(this.getClass()) && obj instanceof Segment
                && ((Segment) obj).name.equals(this.name);
    }
}
