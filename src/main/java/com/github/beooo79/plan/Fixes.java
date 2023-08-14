package com.github.beooo79.plan;

import com.github.beooo79.FoxMain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Fixes implements Serializable {

	private static final long serialVersionUID = 6445588824547266518L;

	/**
	 * 1 (identifier) -> n (fixes) #TGO -> {TGO_1, TGO_2, ..., TGO_n}
	 */
	private HashMap<String, ArrayList<Fix>> fixes;

	public Fixes() {
		try {
			this.fixes = new HashMap<String, ArrayList<Fix>>();
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

		/**
		 * First read the FIX file (intersections)
		 */
		FileReader fr = new FileReader(FoxMain.NAVDATA_FIX);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		int i = 0;
		while ((line = br.readLine()) != null) {
			i++;
			// line = line.trim();
			if (line.length() > 0) {
				if (!line.startsWith(";")) {
					try {
						// System.out.println(line.length());
						String fixName = line.substring(0, 5);
						String lat = line.substring(29, 39);
						String lon = line.substring(39, line.length());
						// System.out.println(lat + "," +lon);
						put(new Fix(fixName, lat, lon));
					} catch (NumberFormatException ne) {
						ne.printStackTrace();
						System.err.println("unable to parse integer:" + line);
					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("unable to parse line with length:"
								+ line.length());
						System.err.println("line:" + line);
					}
				}
			}
		}

		/**
		 * Now read the NAVAID file (VOR, NDB, No! ILS)
		 */
		fr = new FileReader(FoxMain.NAVDATA_NAVAIDS);
		br = new BufferedReader(fr);
		line = null;
		while ((line = br.readLine()) != null) {
			i++;
			// line = line.trim();
			if (line.length() > 0) {
				if (!line.startsWith(";")) {
					try {
						// System.out.println(line.length());
						String fullName = line.substring(0, 24).trim();
						String id = line.substring(24, 28).trim();
						String type = line.substring(29, 32);
						String lat = line.substring(33, 43);
						String lon = line.substring(43, 54);
						String fx = line.substring(54, 60);
						// System.out.println(lat + "," +lon);
						Fix navFix = new Fix(id, lat, lon,
								Fix.getTypeForString(type), fullName, fx);
						// if (navFix.getId().equalsIgnoreCase("SLT"))
						// System.out.println(navFix);
						put(navFix);
					} catch (NumberFormatException ne) {
						ne.printStackTrace();
						System.err.println("unable to parse integer:" + line);
					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("unable to parse line with length:"
								+ line.length());
						System.err.println("line:" + line);
					}
				}

			}
		}

		// Benchmark.split("fixes init");
	}

	public ArrayList<Fix> get(String fixName) {
		return fixes.get(fixName);
	}

	/**
	 * Put (add) a fix to the database, if not already in the database
	 * 
	 * @param f
	 */
	public void put(Fix f) {
		ArrayList<Fix> l = fixes.get(f.getId());
		if (l == null) {
			// new fix id, add new list
			l = new ArrayList<Fix>();
			l.add(f);
			// System.out.println("f");
			fixes.put(f.getId(), l);
		} else if (l.size() > 0) {
			if (!exists(f)) {
				// make sure it really does not exists, then add
				l.add(f);
				// System.out.println("f");
			}

		} else {
			// emtpy list, just add
			l.add(f);
			// System.out.println("f");
		}
	}

	public boolean exists(Fix f) {
		ArrayList<Fix> l = fixes.get(f.getId());
		if (l == null)
			return false;
		for (Fix a : l) {
			if (Math.abs(a.getLat() - f.getLat()) < 0.001
					&& Math.abs(a.getLon() - f.getLon()) < 0.001) {
				return true;
			}
		}
		return false;
	}

	public int size() {
		return fixes.size();
	}
}
