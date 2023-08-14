package com.github.beooo79;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class FoxProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2604523488977337387L;
	public static String[] flightplans = new String[5];
	private ArrayList<String> lastfpls;
	private static File confFile = new File("fox.conf");
	private static File fplFile = new File("fox-fpl.dat");
	private static FoxProperties instance;

	public ArrayList<String> getLastfpls() {
		return lastfpls;
	}

	public void setLastfpls(ArrayList<String> lastfpls) {
		this.lastfpls = lastfpls;
	}

	public static FoxProperties getInstance() {
		if (instance == null) {
			instance = new FoxProperties();
		}
		return instance;
	}

	private FoxProperties() {
		loadConf();
		loadFPLs();
	}

	private void loadFPLs() {
		lastfpls = new ArrayList<String>();
		// read from file, if existent
		if (fplFile.exists() && fplFile.isFile()) {
			try {
				BufferedReader r = new BufferedReader(new FileReader(fplFile));
				String line = null;
				System.out.println("Reading last fpls");
				while ((line = r.readLine()) != null) {
					lastfpls.add(line);
				}
				r.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void storeFPLs() {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(fplFile));
			System.out.println("Writing last fpls");
			for (String s : lastfpls) {
				w.write(s);
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addFPL(String fpl) {
		if (lastfpls != null) {
			for (String s : lastfpls) {
				if (s.equals(fpl)) {
					return;
				}
			}
			lastfpls.add(fpl);
		}
		storeFPLs();
	}

	public static String clearFPL(String trim) {
		return trim.replaceAll("\\s+", " ").toUpperCase();
	}

	public void loadConf() {
		try {
			load(new FileReader(confFile));

		} catch (IOException e) {
			setProperty("PMDG_NAVDATA", ".");
			setProperty("PMDG_SIDSTAR", ".");
			setProperty("FS200x", ".");
			setProperty("PMDG", ".");
			e.printStackTrace();
			storeConf();
		}
	}

	public void storeConf() {
		try {
			store(new FileWriter(confFile), "Fox Configuration");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
