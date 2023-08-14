package com.github.beooo79.plan;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Getter
@NoArgsConstructor
public class Fix implements Externalizable {

    public static final int INTERSECTION = 55550;
    public static final int VOR = INTERSECTION + 1;
    public static final int NDB = VOR + 1;
    public static final int ILS = NDB + 1;
    public static final int AIRPORT = ILS + 1;

    private float lat;
    private float lon;
    private int type;
    private String id;
    private String fullName;
    private String frequency;

    private Fix nextFix = null; // nextFix for distance measurement
    private double distanceToNextFix = 0; // distance to fix - distance in
    // nautical miles

    /**
     * Constructs an intersection
     */
    public Fix(String name, String lat, String lon) {
        this.id = name;
        this.lat = Float.parseFloat(lat);
        this.lon = Float.parseFloat(lon);
        this.type = INTERSECTION;
    }

    public Fix(String name, String lat, String lon, int type, String fullName,
               String frequency) {
        this.id = name;
        this.lat = Float.parseFloat(lat);
        this.lon = Float.parseFloat(lon);
        this.type = type;
        this.fullName = fullName;
        this.frequency = frequency;
    }

    public Fix(String name, float lat, float lon, int type, String fullName,
               String frequency) {
        this.id = name;
        this.lat = lat;
        this.lon = lon;
        this.type = type;
        this.fullName = fullName;
        this.frequency = frequency;
    }



    @Override
    public String toString() {
        if (type == INTERSECTION)
            return String.format("%6s     %07.2f | %07.2f       %5snm", id,
                    lat, lon, Math.round(distanceToNextFix));
        else
            return String.format(
                    "%6s     %07.2f | %07.2f       %5snm       %6s   %s", id,
                    lat, lon, Math.round(distanceToNextFix), frequency,
                    fullName);

    }

    public void readExternal(ObjectInput in) throws IOException{
        id = in.readUTF();
        lat = in.readFloat();
        lon = in.readFloat();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(id);
        out.writeFloat(lat);
        out.writeFloat(lon);
    }

    public void setNextFix(Fix nextFix) {
        this.nextFix = nextFix;
        if (nextFix != null) {
            distanceToNextFix = getDistance(this, nextFix);
        }
    }

    /**
     * Get the type of station from a station in string representation
     */
    public static int getTypeForString(String staType) {
        if (staType.startsWith("ILS"))
            return ILS;
        if (staType.startsWith("VOR"))
            return VOR;
        if (staType.startsWith("NDB"))
            return NDB;
        else
            return INTERSECTION;
    }

    /**
     * Measures the distance between two fixes
     */
    public static double getDistance(Fix a, Fix b) {
        double rLat1 = r(a.getLat());
        double rLat2 = r(b.getLat());
        double rLon1 = r(a.getLon());
        double rLon2 = r(b.getLon());
        if (rLat1 == rLat2 && rLon1 == rLon2)
            return 0;
        else
            return Math.acos((Math.sin(rLat1) * Math.sin(rLat2))
                    + (Math.cos(rLat1) * Math.cos(rLat2) * Math.cos(rLon1
                    - rLon2))) * 3440.06479;
    }

    private static double r(float l) {
        return (l / 180 * Math.PI);
    }

}
