package com.github.beooo79.plan;

import java.io.Serial;
import java.io.Serializable;

public record AirwaySegment(int sequenceNumber, Fix fix) implements Serializable {
	@Serial
	private static final long serialVersionUID = -1135907982387566456L;
}
