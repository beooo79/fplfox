package com.github.beooo79.plan;
import com.github.beooo79.FoxMain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;


public class Airports implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6345887629236323018L;
	private HashMap<String, Airport> airports;

	public Airports() {
		try {
			this.airports = new HashMap<String, Airport>();
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
		FileReader fr = new FileReader(FoxMain.NAVDATA_APTS);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		int i = 0;
		while ((line = br.readLine()) != null) {
			i++;
			//line = line.trim();
			if (line.length() > 0) {
				if (!line.startsWith(";")) {
					try {
						airports.put(line.substring(0, 4).trim(), new Airport(
								line));
					} catch (NumberFormatException ne) {
						System.err.println("unable to parse integer:" + line);
					} catch (Exception e) {
						// e.printStackTrace();
						System.err.println("unable to parse:" + line);
					}
				}
			}
		}
		FoxMain.frame.out(airports.size() + " airports hashed, " + i
				+ " lines read.");
		//Benchmark.split("airports init");
	}

	public Airport get(String icao) {
		return airports.get(icao);
	}
}
