package com.github.beooo79.plan;

import com.github.beooo79.FoxMain;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

@Log
public class Airports implements Serializable {
  @Serial
  private static final long serialVersionUID = -6345887629236323018L;
  private final HashMap<String, Airport> airports;

  public Airports() {
    this.airports = new HashMap<>();
    init();
  }

  @SneakyThrows
  private void init() {
    FileReader fr = new FileReader(FoxMain.NAVDATA_APTS);
    BufferedReader br = new BufferedReader(fr);
    String line;
    int i = 0;
    while ((line = br.readLine()) != null) {
      i++;
      if (!line.isEmpty()) {
        if (!line.startsWith(";")) {
          try {
            var icao = line.substring(0, 4);
            var lat = Float.parseFloat(line.substring(4, 14));
            var lon = Float.parseFloat(line.substring(14, 25));
            airports.put(line.substring(0, 4).trim(), new Airport(icao, icao, lat, lon));
          } catch (NumberFormatException ne) {
            log.warning("unable to parse integer:" + line);
          } catch (Exception e) {
            log.warning("unable to parse:" + line);
          }
        }
      }
    }
    FoxMain.frame.out(airports.size() + " airports hashed, " + i + " lines read.");
  }

  public Airport get(String icao) {
    return airports.get(icao);
  }
}
