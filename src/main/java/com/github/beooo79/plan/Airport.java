package com.github.beooo79.plan;

import com.github.beooo79.FoxMain;
import lombok.extern.java.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
@Log
public record Airport(String icao, String name, float lat, float lon)
    implements Serializable {

  @Serial
  private static final long serialVersionUID = -7234122596653153304L;

  public Fix toFix() {
    return new Fix(name, lat, lon, Fix.AIRPORT, name, name);
  }

  public String toFS200xString() {
    String[] gps = Plan.toFSFormat(lat, lon);
    return icao() + "," + gps[0] + "," + gps[1] + ", +000000";
    // SUDU,S33* 21.53',W056* 29.95', +000305
  }

  public List<String> getListOfSTAREntryPoints() {
    ArrayList<String> l = new ArrayList<>();
    try {
      FileReader fr = new FileReader(FoxMain.getFileSIDSTAR(icao()));
      BufferedReader br = new BufferedReader(fr);
      String line;
      while ((line = br.readLine()) != null) {
        if (line.startsWith("SID")) {}
      }
    } catch (Exception ex) {
      log.severe("getListOfSTAREntryPoints failed with " + ex.getMessage());
    }
    return l;
  }

  public void getListOfSIDExitPoints() {}
}
