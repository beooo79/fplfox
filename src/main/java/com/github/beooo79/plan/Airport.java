package com.github.beooo79.plan;

import com.github.beooo79.FoxMain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Airport implements Serializable {

	private static final long serialVersionUID = -7234122596653153304L;
	private String raw;
	private String icao;
	private String name;
	private float lat;
	private float lon;

	public Airport(String raw) {
		this.raw = raw;
		init();
	}

	private void init() {
		this.icao = this.name = raw.substring(0, 4);
		this.lat = Float.parseFloat(raw.substring(4, 14));
		this.lon = Float.parseFloat(raw.substring(14, 25));

		// StringTokenizer st = new StringTokenizer(raw.trim());
		// if (st.countTokens() >= 4) {
		// this.icao = st.nextToken().trim();
		// this.lat = Float.parseFloat(st.nextToken().trim());
		// this.lon = Float.parseFloat(st.nextToken().trim());
		// this.name = "";
		// while (st.hasMoreTokens()) {
		// this.name += st.nextToken() + " ";
		// }
		// }
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	@Override
	public String toString() {
		return getIcao() + ":" + getLat() + "," + getLon() + ":" + getName();
	}

	public Fix toFix() {
		return new Fix(name, lat, lon, Fix.AIRPORT, name, name);
	}

	public String getIcao() {
		return icao;
	}

	public void setIcao(String icao) {
		this.icao = icao;
	}

	public String toFS200xString() {
		// TODO Auto-generated method stub
		String[] gps = Plan.toFSFormat(lat, lon);
		return getIcao() + "," + gps[0] + "," + gps[1] + ", +000000";
		// SUDU,S33* 21.53',W056* 29.95', +000305
	}

	public List<String> getListOfSTAREntryPoints() {
		ArrayList<String> l = new ArrayList<String>();

		try {
			FileReader fr = new FileReader(FoxMain.getFileSIDSTAR(getIcao()));
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("SID"))
				{
					
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return l;
	}

	public void getListOfSIDExitPoints() {
	}
}
