package com.github.beooo79;

import lombok.Getter;
import lombok.extern.java.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

@Log
@Getter
public class FoxProperties extends Properties {
	@Serial
	private static final long serialVersionUID = 2604523488977337387L;
	public static String[] flightplans = new String[5];
	private ArrayList<String> lastfpls;
	private static final File confFile = new File("fox.conf");
	private static final File fplFile = new File("fox-fpl.dat");
	private static FoxProperties instance;

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
		lastfpls = new ArrayList<>();
		// read from file, if existent
		if (fplFile.exists() && fplFile.isFile()) {
			try {
				BufferedReader r = new BufferedReader(new FileReader(fplFile));
				String line;
				log.info("Reading last fpls");
				while ((line = r.readLine()) != null) {
					lastfpls.add(line);
				}
				r.close();
			} catch (Exception ex) {
				log.severe("loadFPLs failed "+ ex.getMessage());
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
		} catch (Exception ex) {
			log.severe("storeFPLs failed " + ex.getMessage());
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
