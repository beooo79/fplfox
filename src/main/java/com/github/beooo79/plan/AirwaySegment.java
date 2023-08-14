package com.github.beooo79.plan;

import java.io.Serializable;

public class AirwaySegment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1135907982387566456L;
	/**
	 * 
	 */

	private int seqNo;
	private Fix fix;

	public AirwaySegment(int seqno, Fix fix)
	{
		this.seqNo = seqno;
		this.fix = fix;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public Fix getFix() {
		return fix;
	}

	public void setFix(Fix fix) {
		this.fix = fix;
	}
}
