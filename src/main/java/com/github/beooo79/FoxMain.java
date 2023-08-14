package com.github.beooo79;

import lombok.Getter;
import lombok.extern.java.Log;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

/**
 * Geplante Features: - Export PMDG etc. (correlate airways in fixes within
 * plans first)
 * <p>
 * <p>
 * <p>
 * - Navaid Database - Darstellung aller AIRAC Infos: -- Airports -- VOR/NDB --
 * Fixes -- Airways - ILS
 * <p>
 * <p>
 * <p>
 * - Autorouting feature
 * <p>
 * <p>
 * - FPL Actions: Save Favorite, Nav Log (JTable customized)
 * <p>
 * - show possible alternate airports along the route (with circle)
 * <p>
 * <p>
 * <p>
 * - Verbindung IVAO/VATSIM?
 * <p>
 * - Verbindung FSUIPC?
 *
 * @author AB
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
    public static final String VERSION = "Flightplan Fox Version 20230815";

    public static void main(String[] args) throws URISyntaxException {
        System.out.println(System.getProperties());

        PATH_APP = new File(System.getProperty("user.dir")).getParent();
        System.out.println("Application path is " + PATH_APP);

        PATH_NAVDATA = FoxProperties.getInstance().getProperty("PMDG_NAVDATA");
        PATH_SIDSTAR = FoxProperties.getInstance().getProperty("PMDG_SIDSTAR");

        NAVDATA_APTS = PATH_NAVDATA + System.getProperty("file.separator")
                + "airports.dat";
        NAVDATA_AWY = PATH_NAVDATA + System.getProperty("file.separator")
                + "wpnavrte.txt";
        NAVDATA_FIX = PATH_NAVDATA + System.getProperty("file.separator")
                + "wpnavfix.txt";
        NAVDATA_NAVAIDS = PATH_NAVDATA + System.getProperty("file.separator")
                + "wpnavaid.txt";
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            log.severe("main failed: " + ex.getMessage());
        }

        Locale.setDefault(Locale.US);
        frame = new FoxFrame();

    }

    public static String getFileSIDSTAR(String airportIcao) {
        return PATH_SIDSTAR + System.getProperty("file.separator")
                + airportIcao.trim().toUpperCase() + ".txt";
    }

}
