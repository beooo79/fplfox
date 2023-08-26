package com.github.beooo79.plan;

import com.github.beooo79.FoxMain;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import lombok.Getter;
import lombok.extern.java.Log;

@Log
@Getter
public class Plan implements Serializable {
  public static final Color DARK_GREEN = new Color(0, 100, 0);
  public static final Color SLATE_GRAY = new Color(112, 128, 144);

  @Serial private static final long serialVersionUID = 8966967712210168137L;

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
    float lon1 = Math.abs(lon) - (int) Math.abs(lon);
    float minLon = lon1 * 60;

    if (minLat < 10) {
      gps[0] += String.format("%02d* %05.3g'", (int) Math.abs(lat), minLat);
    } else {
      gps[0] += String.format("%02d* %05.4g'", (int) Math.abs(lat), minLat);
    }

    if (minLon < 10) {
      gps[1] += String.format("%03d* %05.3g'", (int) Math.abs(lon), minLon);
    } else {
      gps[1] += String.format("%03d* %05.4g'", (int) Math.abs(lon), minLon);
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
    return String.format("1 %S %8.4f %S %8.4f 0", gps[0], Math.abs(lat), gps[1], Math.abs(lon));
  }

  private final String adep_raw;
  private final String ades_raw;
  private final String rte_raw;
  private final List<String> url = new ArrayList<>();
  private final boolean SHORT = false;
  private StringBuffer fs200xString;
  private Airport adep;
  private Airport ades;
  private ArrayList<Fix> fixes;
  private StringBuffer pmdgString;

  public static String nl = System.getProperty("line.separator");
  public static String nl2 =
      System.getProperty("line.separator") + System.getProperty("line.separator");

  public Plan(String adep, String ades, String fpl) {
    this.adep_raw = adep.toUpperCase();
    this.ades_raw = ades.toUpperCase();
    this.rte_raw = fpl.toUpperCase();
    this.fixes = new ArrayList<>();
  }

  // for fixes
  private void appendFs200xFixDetails(
      StringBuffer bf, int i, Fix f, String type, Airway awy, Airport apt) {
    if (SHORT) {
      bf.append(f.getId()).append(" ");
      return;
    }

    String[] gps;
    String name;
    String aw = "";

    if (apt != null) {
      gps = toFSFormat(apt.lat(), apt.lon());
      name = apt.icao();
    } else if (f != null) {
      gps = toFSFormat(f.getLat(), f.getLon());
      name = f.getId();
    } else {
      return;
    }

    if (awy != null) {
      aw = awy.name();
    }

    bf.append("waypoint.")
        .append(i)
        .append("=, ")
        .append(name)
        .append(", , ")
        .append(name)
        .append(", ")
        .append(type)
        .append(", ")
        .append(gps[0])
        .append(", ")
        .append(gps[1])
        .append(", +000000, ")
        .append(aw)
        .append(nl);
  }

  // for fixes
  private void appendPMDGFixDetails(
      StringBuffer stringBuffer, Fix fix, String type, Airway awy, Airport apt) {
    if (apt == null && fix != null) {
      // Intersection,VOR,NDB
      stringBuffer.append(fix.getId()).append(nl);
      stringBuffer.append("5").append(nl);
      if (awy == null) stringBuffer.append("DIRECT").append(nl);
      else stringBuffer.append(awy.name()).append(nl);
      stringBuffer.append(toPMDGFormat(fix.getLat(), fix.getLon())).append(nl);
      stringBuffer.append("0").append(nl).append("0").append(nl).append("0");
    } else {
      // Airport
      assert apt != null;
      stringBuffer.append(apt.icao()).append(nl);
      stringBuffer.append("1").append(nl);
      if (type.equals("DEP")) stringBuffer.append("DIRECT").append(nl);
      else stringBuffer.append("-").append(nl);
      stringBuffer.append(toPMDGFormat(apt.lat(), apt.lon())).append(nl);
      stringBuffer.append("-----").append(nl);
      if (type.equals("DEP")) stringBuffer.append("1").append(nl);
      else stringBuffer.append("0").append(nl);
      stringBuffer
          .append("0")
          .append(nl)
          .append(nl)
          .append("1")
          .append(nl)
          .append("0")
          .append(nl)
          .append("-")
          .append(nl)
          .append("-1000000")
          .append(nl)
          .append("-1000000");
    }
    stringBuffer.append(nl2);
  }

  public void build(Airways awys, Airports apts) {
    this.fixes = new ArrayList<>();

    // header
    StringBuffer bf_fs200x = new StringBuffer("[flightplan]");
    StringBuffer bf_pmdg = new StringBuffer("Flightplan generated by " + FoxMain.VERSION);
    StringBuffer bf_pmdg_tmp = new StringBuffer(500);
    bf_fs200x.append(nl);
    bf_pmdg.append(nl2);
    bf_fs200x.append("AppVersion=9.1.40901");
    bf_fs200x.append(nl);
    bf_fs200x.append("title=").append(this.adep_raw).append(" to ").append(this.ades_raw);
    bf_fs200x.append(nl);
    bf_fs200x.append("description=").append(this.adep_raw).append(", ").append(this.ades_raw);
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
    bf_fs200x
        .append("departure_id=")
        .append(adep.toFS200xString())
        .append(nl)
        .append("destination_id=")
        .append(ades.toFS200xString())
        .append(nl)
        .append("departure_name=")
        .append(adep.name())
        .append(nl)
        .append("destination_name=")
        .append(ades.name())
        .append(nl);

    // waypoints
    appendFs200xFixDetails(bf_fs200x, 0, null, "A", null, apts.get(this.adep_raw));
    appendPMDGFixDetails(bf_pmdg_tmp, null, "DEP", null, apts.get(this.adep_raw));

    StringTokenizer st = new StringTokenizer(rte_raw.trim());
    Fix lastFix = adep.toFix();
    int i = 1;

    while (st.hasMoreTokens()) {
      String p = st.nextToken();
      // identify if airway or fix

      if (isFix(p, awys)) {
        List<Fix> f = awys.getFixes().get(p.trim());
        if (f != null) {
          Fix choice;
          if (f.size() == 1) choice = f.get(0);
          else {
            // get nearest
            choice = getNearest(lastFix, f);
            // let user decide
            // choice = FoxMain.getFrame().promptFix(lastFix, f);
          }
          fixes.add(choice);
          appendFs200xFixDetails(bf_fs200x, i, choice, "I", null, null);
          appendPMDGFixDetails(bf_pmdg_tmp, choice, "I", null, null);
          i++;
          choice.setNextFix(lastFix);
          lastFix = choice;
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
        for (AirwaySegment seg : segments) {
          Fix f = seg.fix();
          fixes.add(f);
          appendFs200xFixDetails(bf_fs200x, i, f, "I", a, null);
          appendPMDGFixDetails(bf_pmdg_tmp, f, "I", a, null);
          i++;
          lastFix = f;
        }
      }
    }
    appendFs200xFixDetails(bf_fs200x, i, null, "A", null, apts.get(this.ades_raw));
    appendPMDGFixDetails(bf_pmdg_tmp, null, "DES", null, apts.get(this.ades_raw));
    FoxMain.frame.out(fixes.size() + " enroute fixes found   =====>");
    this.fs200xString = bf_fs200x;

    bf_pmdg.append(fixes.size() + 2);
    bf_pmdg.append(nl2);
    bf_pmdg.append(bf_pmdg_tmp);
    this.pmdgString = bf_pmdg;
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

  private boolean isFix(String p, Airways awys) {
    Airway a = awys.getAirway(p);
    return a == null;
  }

  public void out(String s) {
    FoxMain.frame.out(s);
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
    out(
        String.format(
            "%6s     %07.2f | %07.2f       %5fnm       %6s   %s",
            adep_raw, 0f, 0f, distance, ades_raw, ""));
  }
}
