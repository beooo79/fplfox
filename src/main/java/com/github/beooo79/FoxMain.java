package com.github.beooo79;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;
import javax.swing.*;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * Geplante Features: - Export PMDG etc. (correlate airways in fixes within plans first) - Navaid
 * Database - Darstellung aller AIRAC Infos: -- Airports -- VOR/NDB -- Fixes -- Airways - ILS -
 * Autorouting feature - FPL Actions: Save Favorite, Nav Log (JTable customized) - show possible
 * alternate airports along the route (with circle) - Verbindung IVAO/VATSIM? - Verbindung FSUIPC?
 *
 * @author beooo79
 */
@Getter
@Log
public class FoxMain {

  public static String PATH_APP = "";
  public static String PATH_NAVDATA = "";
  public static String PATH_SIDSTAR = "";
  public static String NAVDATA_APTS = "";
  public static String NAVDATA_AWY = "";
  public static String NAVDATA_FIX = "";
  public static String NAVDATA_NAVAIDS = "";

  public static FoxFrame frame;
  public static final String VERSION = "Flightplan Fox Version 20230826";

  public static void main(String[] args) throws URISyntaxException {
    System.getProperties().forEach((o, o2) -> log.info(o + "=" + o2));

    PATH_APP = new File(System.getProperty("user.dir")).getParent();
    log.info("Application path is " + PATH_APP);

    PATH_NAVDATA = FoxProperties.getInstance().getProperty("PMDG_NAVDATA");
    PATH_SIDSTAR = FoxProperties.getInstance().getProperty("PMDG_SIDSTAR");

    NAVDATA_APTS = PATH_NAVDATA + System.getProperty("file.separator") + "airports.dat";
    NAVDATA_AWY = PATH_NAVDATA + System.getProperty("file.separator") + "wpnavrte.txt";
    NAVDATA_FIX = PATH_NAVDATA + System.getProperty("file.separator") + "wpnavfix.txt";
    NAVDATA_NAVAIDS = PATH_NAVDATA + System.getProperty("file.separator") + "wpnavaid.txt";
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ex) {
      log.severe("main failed: " + ex.getMessage());
    }

    Locale.setDefault(Locale.US);
    frame = new FoxFrame();
  }

}
