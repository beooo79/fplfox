package com.github.beooo79.plan;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Formatter;

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
	 * 
	 * @param name
	 * @param lat
	 * @param lon
	 */
	public Fix(String name, String lat, String lon) {
		this.id = name;
		this.lat = Float.parseFloat(lat);
		this.lon = Float.parseFloat(lon);
		this.type = INTERSECTION;
		// FoxMain.getFrame().sysout(name + ":" + lat + "," + lon);
	}

	public Fix(String name, String lat, String lon, int type, String fullName,
			String frequency) {
		this.id = name;
		this.lat = Float.parseFloat(lat);
		this.lon = Float.parseFloat(lon);
		this.type = type;
		this.fullName = fullName;
		this.frequency = frequency;
		// FoxMain.getFrame().sysout(name + ":" + lat + "," + lon);
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

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public String getId() {
		return id;
	}

	public void setId(String name) {
		this.id = name;
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

	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id = in.readUTF();
		lat = in.readFloat();
		lon = in.readFloat();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(id);
		out.writeFloat(lat);
		out.writeFloat(lon);
	}

	public Fix getNextFix() {
		return nextFix;
	}

	public void setNextFix(Fix nextFix) {
		this.nextFix = nextFix;
		if (nextFix != null) {
			distanceToNextFix = getDistance(this, nextFix);
		}
	}

	/**
	 * Get the type of station from a station in string representation
	 * 
	 * @param staType
	 * @return
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
	 * 
	 * @param a
	 *            first fix
	 * @param b
	 *            second fix
	 * @return distance
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

	/**
	 * 
	 * @param l
	 * @return
	 */
	private static double r(float l) {
		return (l / 180 * Math.PI);
	}

	public double getDistanceToNextFix() {
		return distanceToNextFix;
	}

	public void setDistanceToNextFix(double dnm) {
		this.distanceToNextFix = dnm;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
}
