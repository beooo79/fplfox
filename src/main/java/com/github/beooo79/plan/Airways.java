package com.github.beooo79.plan;

import com.github.beooo79.FoxMain;
import lombok.Getter;
import lombok.extern.java.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


@Log
public class Airways implements Serializable {
    @Serial
    private static final long serialVersionUID = 1715125433088898231L;
    private HashMap<String, Airway> airways;
    @Getter
    private Fixes fixes;

    public Airways(Fixes fixDB) {
        try {
            this.airways = new HashMap<>();
            this.fixes = fixDB;
            init();
        } catch (Exception ex) {
            log.severe("Airways creation failed");
        }
    }

    private void init() throws IOException {
        FileReader fr = new FileReader(FoxMain.NAVDATA_AWY);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                if (!line.startsWith(";")) {
                    try {
                        i++;
                        parseLine(line);

                    } catch (NumberFormatException ne) {
                        log.warning("unable to parse integer:" + line);
                    } catch (IllegalStateException e) {
                        log.warning("illegal state in parsing from airway:"
                                        + line);
                    } catch (Exception e) {
                        log.warning("unable to parse in airway:" + line);
                    }
                }
            }
        }
        FoxMain.frame.out(
                airways.size() + " airways hashed, " + i + " lines read.");
    }

    private void parseLine(String line) {
        StringTokenizer st = new StringTokenizer(line.trim());
        String aw = st.nextToken();
        int seqno = Integer.parseInt(st.nextToken());
        String fix = st.nextToken().trim().toUpperCase();
        String lat = st.nextToken().trim();
        String lon = st.nextToken().trim();

        ArrayList<Fix> l = fixes.get(fix); // list of possible fixes
        Fix awyFix = new Fix(fix, lat, lon); // the airway fix (raw)
        Fix tmpFix = awyFix;

        if (l == null || l.isEmpty()) {
            fixes.put(awyFix);
        } else {
            double distance = Double.MAX_VALUE;
            /*
             * find the correct fix (nearest to lat/lon)
             */
            for (Fix t : l) {
                double d = Fix.getDistance(t, awyFix);
                if (d < distance) {
                    distance = d;
                    tmpFix = t;
                }
            }

        }

        AirwaySegment seg = new AirwaySegment(seqno, tmpFix);
        addSegment(aw, seg);
    }

    private void addSegment(String airwayName, AirwaySegment s) {
        Airway a = getAirway(airwayName);
        if (a == null) {
            // add airway because it's new and redo
            addAirway(airwayName);
            addSegment(airwayName, s);
            return;
        }
        a.addSegment(s);
    }

    private void addAirway(String airwayName) {
        airways.put(airwayName.trim(), new Airway(airwayName, new ArrayList<>()));
    }

    public Airway getAirway(String name) {
        return airways.get(name.trim());
    }

}