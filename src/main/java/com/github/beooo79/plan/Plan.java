package com.github.beooo79.plan;

import com.github.beooo79.FoxMain;
import lombok.extern.java.Log;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


@Log
public class Plan implements Serializable {
	public static Color DARK_GREEN = new Color(0, 100, 0);
	public static Color SLATE_GRAY = new Color(112, 128, 144);
	private static final long serialVersionUID = 8966967712210168137L;

	private static String getDegrees(int s, float num) {
		return String.format("%0" + s + "d", (int) Math.abs(num));
	}

	public static String[] toFSFormat(float lat, float lon) {
		String[] gps = new String[2];
		if (lat < 0) {
			gps[0] = "S";
		} else {
			gps[0] = "N";
		}
		if (lon < 0) {
			gps[1] = "W";
		} else {
			gps[1] = "E";
		}

		float lat1 = Math.abs(lat) - (int) Math.abs(lat);
		float minLat = lat1 * 60;
		// FoxMain.getFrame().sysout(lat + "->" + getDegrees(2, lat) + " Rest:"
		// + lat1
		// + " =" + minLat + "'");

		float lon1 = Math.abs(lon) - (int) Math.abs(lon);
		float minLon = lon1 * 60;
		// FoxMain.getFrame().sysout(lon + "->" + getDegrees(3, lon) + " Rest:"
		// + lon1
		// + " =" + minLon + "'");
		if (minLat < 10) {
			gps[0] += String.format("%02d* %05.3g'", (int) Math.abs(lat),
					minLat);
		} else {
			gps[0] += String.format("%02d* %05.4g'", (int) Math.abs(lat),
					minLat);
		}

		if (minLon < 10) {
			gps[1] += String.format("%03d* %05.3g'", (int) Math.abs(lon),
					minLon);
		} else {
			gps[1] += String.format("%03d* %05.4g'", (int) Math.abs(lon),
					minLon);
		}

		return gps;
	}

	public static String toPMDGFormat(float lat, float lon) {
		String[] gps = new String[2];
		if (lat < 0) {
			gps[0] = "S";
		} else {
			gps[0] = "N";
		}
		if (lon < 0) {
			gps[1] = "W";
		} else {
			gps[1] = "E";
		}
		return String.format("1 %S %8.4f %S %8.4f 0", gps[0], Math.abs(lat),
				gps[1], Math.abs(lon));
	}

	private String adep_raw;

	private String ades_raw;
	private String rte_raw;
	private List<String> url = new ArrayList<String>();
	private boolean SHORT = false;
	private StringBuffer fs200xString;
	private Airport adep;

	private Airport ades;
	private ArrayList<Fix> fixes;

	private StringBuffer pmdgString;

	public static String nl = System.getProperty("line.separator");

	public static String nl2 = System.getProperty("line.separator")
			+ System.getProperty("line.separator");

	public Plan(String adep, String ades, String fpl) {
		this.adep_raw = adep.toUpperCase();
		this.ades_raw = ades.toUpperCase();
		this.rte_raw = fpl.toUpperCase();
		this.fixes = new ArrayList<Fix>();
	}

	// for fixes
	private void appendFs200xFixDetails(StringBuffer bf, int i, Fix f,
			String type, Airway awy, Airport apt) {
		if (SHORT) {
			bf.append(f.getId() + " ");
			return;
		}

		String[] gps = null;
		String name = "";
		String aw = "";

		if (apt != null) {
			gps = toFSFormat(apt.getLat(), apt.getLon());
			name = apt.getIcao();
		} else if (f != null) {
			gps = toFSFormat(f.getLat(), f.getLon());
			name = f.getId();
		} else {
			return;
		}

		if (awy != null) {
			aw = awy.name();
		}

		bf.append("waypoint." + i + "=, " + name + ", , " + name + ", " + type
				+ ", " + gps[0] + ", " + gps[1] + ", +000000, " + aw);
		bf.append(nl);

	}

	// for fixes
	private void appendPMDGFixDetails(StringBuffer bf, int i, Fix f,
			String type, Airway awy, Airport apt) {
		if (apt == null && f != null) {
			// Intersection,VOR,NDB
			bf.append(f.getId() + nl);
			bf.append("5" + nl);
			if (awy == null)
				bf.append("DIRECT" + nl);
			else
				bf.append(awy.name() + nl);
			bf.append(toPMDGFormat(f.getLat(), f.getLon()) + nl);
			bf.append("0" + nl + "0" + nl + "0");
		} else {
			// Airport
			bf.append(apt.getIcao() + nl);
			bf.append("1" + nl);
			if (type.equals("DEP"))
				bf.append("DIRECT" + nl);
			else
				bf.append("-" + nl);
			bf.append(toPMDGFormat(apt.getLat(), apt.getLon()) + nl);
			bf.append("-----" + nl);
			if (type.equals("DEP"))
				bf.append("1" + nl);
			else
				bf.append("0" + nl);
			bf.append("0" + nl);
			bf.append(nl);
			bf.append("1" + nl);
			bf.append("0" + nl);
			bf.append("-" + nl + "-1000000" + nl + "-1000000");
		}
		bf.append(nl2);
	}

	public void browseFallingRain() {
		if (Desktop.isDesktopSupported()) {
			Desktop desk = Desktop.getDesktop();
			try {
				for (String s : url) {
					desk.browse(new URI(s));
				}
			} catch (Exception ex) {
				log.severe("browseFallingRain failed: " + ex.getMessage());
			}
		}
	}

	public void build(Airways awys, Airports apts) {
		this.fixes = new ArrayList<Fix>();

		// header
		StringBuffer bf_fs200x = new StringBuffer("[flightplan]");
		StringBuffer bf_pmdg = new StringBuffer("Flightplan generated by "
				+ FoxMain.VERSION);
		StringBuffer bf_pmdg_tmp = new StringBuffer(500);
		bf_fs200x.append(nl);
		bf_pmdg.append(nl2);
		bf_fs200x.append("AppVersion=9.1.40901");
		bf_fs200x.append(nl);
		bf_fs200x.append("title=" + this.adep_raw + " to " + this.ades_raw);
		bf_fs200x.append(nl);
		bf_fs200x.append("description=" + this.adep_raw + ", " + this.ades_raw);
		bf_fs200x.append(nl);
		bf_fs200x.append("type=IFR");
		bf_fs200x.append(nl);
		bf_fs200x.append("routetype=3");
		bf_fs200x.append(nl);
		bf_fs200x.append("cruising_altitude=34000");
		bf_fs200x.append(nl);

		adep = apts.get(this.adep_raw.trim());
		ades = apts.get(this.ades_raw.trim());

		// System.out.println("" + this.adep_raw + " -> " + this.ades_raw);
		bf_fs200x.append("departure_id=" + adep.toFS200xString());
		bf_fs200x.append(nl);
		bf_fs200x.append("destination_id=" + ades.toFS200xString());
		bf_fs200x.append(nl);
		bf_fs200x.append("departure_name=" + adep.getName());
		bf_fs200x.append(nl);
		bf_fs200x.append("destination_name=" + ades.getName());
		bf_fs200x.append(nl);

		// waypoints
		appendFs200xFixDetails(bf_fs200x, 0, null, "A", null,
				apts.get(this.adep_raw));
		appendPMDGFixDetails(bf_pmdg_tmp, 0, null, "DEP", null,
				apts.get(this.adep_raw));

		StringTokenizer st = new StringTokenizer(rte_raw.trim());
		Fix lastFix = adep.toFix();
		int i = 1;

		while (st.hasMoreTokens()) {
			String p = st.nextToken();
			// identify if airway or fix

			if (p.trim().equals("DCT") || p.trim().equals("SID")
					|| p.trim().equals("STAR")) {
				// disregard DCT/STAR/SID ... continue, next should be fix or
				// airport!
			} else if (isFix(p, awys)) {
				List<Fix> f = awys.getFixes().get(p.trim());
				if (f != null) {
					Fix choice = null;
					if (f.size() == 1)
						choice = f.get(0);
					else {
						// get nearest
						choice = getNearest(lastFix, f);
						// let user decide
						// choice = FoxMain.getFrame().promptFix(lastFix, f);
					}
					fixes.add(choice);
					appendFs200xFixDetails(bf_fs200x, i, choice, "I", null,
							null);
					appendPMDGFixDetails(bf_pmdg_tmp, i, choice, "I", null,
							null);
					i++;
					choice.setNextFix(lastFix);
					lastFix = choice;
					// FoxMain.getFrame().out("Fix: " + choice);
				} else {
					FoxMain.frame.out("Unable to find: " + p);
				}
			} else {
				// resolve the airway
				// from=p, to=nextFix
				String nextFix = st.nextToken();
				Airway a = awys.getAirway(p);
				// let airway resolve itself
				List<AirwaySegment> segments = a.resolve(lastFix, nextFix);
				// FoxMain.getFrame().out( lastFix.getName() + "-->" + nextFix +
				// " in airway:" + a.getName() + " segments:" +
				// segments.size());
				for (AirwaySegment seg : segments) {
					Fix f = seg.fix();
					fixes.add(f);
					appendFs200xFixDetails(bf_fs200x, i, f, "I", a, null);
					appendPMDGFixDetails(bf_pmdg_tmp, i, f, "I", a, null);
					i++;
					lastFix = f;
				}
			}

		}
		appendFs200xFixDetails(bf_fs200x, i, null, "A", null,
				apts.get(this.ades_raw));
		appendPMDGFixDetails(bf_pmdg_tmp, i, null, "DES", null,
				apts.get(this.ades_raw));
		FoxMain.frame.out(fixes.size() + " enroute fixes found   =====>");
		this.fs200xString = bf_fs200x;

		bf_pmdg.append(fixes.size() + 2);
		bf_pmdg.append(nl2);
		bf_pmdg.append(bf_pmdg_tmp);
		this.pmdgString = bf_pmdg;
	}

	public Airport getAdep() {
		return adep;
	}

	public String getAdep_raw() {
		return adep_raw;
	}

	public Airport getAdes() {
		return ades;
	}

	public String getAdes_raw() {
		return ades_raw;
	}

	public ArrayList<Fix> getFixes() {
		return fixes;
	}

	public StringBuffer getFs200xString() {
		return fs200xString;
	}

	private Fix getNearest(Fix a, List<Fix> list) {
		Fix n = null;
		double nd = Double.MAX_VALUE;

		for (Fix f : list) {
			double d = Fix.getDistance(f, a);
			if (d < nd) {
				n = f;
				nd = d;
			}
		}
		return n;
	}

	public StringBuffer getPMDGRteString() {
		return pmdgString;
	}

	public String getRte_raw() {
		return rte_raw;
	}

	private Object getUrlPoint(String name, float lat, float lon) {
		return "&lat=" + lat + "&long=" + lon + "&name=" + name + "&c=1";
	}

	private boolean isFix(String p, Airways awys) {
		Airway a = awys.getAirway(p);
		return a == null;
	}

	public void out(String s) {
		FoxMain.frame.out(s);
	}

	public void setAdep_raw(String adep_raw) {
		this.adep_raw = adep_raw;
	}

	public void setAdes_raw(String ades_raw) {
		this.ades_raw = ades_raw;
	}

	public void setRte_raw(String rte_raw) {
		this.rte_raw = rte_raw;
	}

	public void walk() {
		Fix lastFix = adep.toFix();

		out(lastFix.toString());

		double distance = 0;
		for (Fix f : fixes) {
			f.setNextFix(lastFix);
			distance += f.getDistanceToNextFix();
			out(f.toString());
			lastFix = f;
		}
		Fix fades = ades.toFix();
		fades.setNextFix(lastFix);
		distance += fades.getDistanceToNextFix();
		out(fades.toString());
		NumberFormat nf = new DecimalFormat("0.00");

		
		out(String.format("%6s     %07.2f | %07.2f       %5fnm       %6s   %s", adep_raw,0f,0f,distance,ades_raw,""));
	}

}
