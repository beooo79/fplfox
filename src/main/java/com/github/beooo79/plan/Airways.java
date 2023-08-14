package com.github.beooo79.plan;

import com.github.beooo79.FoxMain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


public class Airways implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1715125433088898231L;
	private HashMap<String, Airway> airways;
	private Fixes fixes;

	public Airways() {

	}

	public Airways(Fixes fixDB) {
		try {
			this.airways = new HashMap<String, Airway>();
			this.fixes = fixDB;
			init();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void init() throws IOException {
		FileReader fr = new FileReader(FoxMain.NAVDATA_AWY);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		int i = 0;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.length() > 0) {
				if (!line.startsWith(";")) {
					try {
						i++;
						parseLine(line);

					} catch (NumberFormatException ne) {
						System.err.println("unable to parse integer:" + line);
					} catch (IllegalStateException e) {
						// e.printStackTrace();
						System.err
								.println("illegal state in parsing from airway:"
										+ line);
					} catch (Exception e) {
						// e.printStackTrace();
						System.err.println("unable to parse in airway:" + line);
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

		// if (fix.equals("TGO"))
		// {
		// System.out.println(fix +":"+ lat +"," +lon);
		// }

		/**
		 * This should be different!!!
		 * 
		 * @TODO
		 */
		ArrayList<Fix> l = fixes.get(fix); // list of possible fixes
		Fix awyFix = new Fix(fix, lat, lon); // the airway fix (raw)
		Fix tmpFix = awyFix;

		if (l == null || l.size() == 0) {
			/**
			 * @TODO new fix (this should throw an exception ideally!!!
			 */
			fixes.put(awyFix);
		} else {
			double distance = Double.MAX_VALUE;
			/**
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
		// Benchmark.reset();
		Airway a = getAirway(airwayName);
		// Benchmark.split(airways.size() + " -> getAirway");

		if (a == null) {
			// add airway because it's new and redo
			addAirway(airwayName);
			addSegment(airwayName, s);
			return;
		}
		a.addSegment(s);
	}

	private void addAirway(String airwayName) {
		airways.put(airwayName.trim(), new Airway(airwayName));
	}

	public Airway getAirway(String name) {
		return airways.get(name.trim());
	}

	public Fixes getFixes() {
		return fixes;
	}

}