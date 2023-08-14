package com.github.beooo79.plan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Airway implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7428243350526926375L;
	private String name;
	private List<AirwaySegment> segments;

	public Airway(String name) {
		this.name = name;
		this.segments = new ArrayList<AirwaySegment>();
	}

	public void addSegment(AirwaySegment s) {
		segments.add(s);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AirwaySegment> getSegments() {
		return segments;
	}

	public void setSegments(List<AirwaySegment> segments) {
		this.segments = segments;
	}

	public List<AirwaySegment> resolve(Fix fromFix, String nextFix) {
		List<AirwaySegment> sl = new ArrayList<AirwaySegment>();
		// seqno of fromFix
		int noFrom = getSeqNo(fromFix.getId());
		// seqno of nextFix
		int noNext = getSeqNo(nextFix);
		//FoxMain.getFrame().sysout("Segments: " + noFrom + "-->" + noNext);
		if (noFrom > 0 && noNext > 0) {
			if (noFrom < noNext) {
				for (int i = noFrom+1; i <= noNext; i++) {
					sl.add(segments.get(i - 1));
				}
			} else {
				for (int i = noFrom-1; i >= noNext; i--) {
					sl.add(segments.get(i - 1));
				}
			}
		}
		return sl;
	}

	private int getSeqNo(String fix) {
		for (AirwaySegment s : segments) {
			if (s.getFix().getId().equals(fix)) {
				return s.getSeqNo();
			}
		}
		return 0;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (AirwaySegment seg : segments) {
			buf.append(seg.getFix().getId() + "-");			
		}
		return name + " : " + buf.toString();
	}

}
