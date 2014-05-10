package com.ericsson.raso.sef.client.air.request;

import java.util.Date;

public class AccumulatorInformation extends NativeAirRequest {

	private Integer accumulatorID;
	private Integer accumulatorValueRelative;
	private Integer accumulatorValueAbsolute;
	private Date accumulatorStartDate;

	public Integer getAccumulatorID() {
		return accumulatorID;
	}

	public void setAccumulatorID(Integer accumulatorID) {
		this.accumulatorID = accumulatorID;
		addParam("accumulatorID", this.accumulatorID);
	}

	public Integer getAccumulatorValueRelative() {
		return accumulatorValueRelative;
	}

	public void setAccumulatorValueRelative(Integer accumulatorValueRelative) {
		this.accumulatorValueRelative = accumulatorValueRelative;
		addParam("accumulatorValueRelative", this.accumulatorValueRelative);
	}

	public Integer getAccumulatorValueAbsolute() {
		return accumulatorValueAbsolute;
	}

	public void setAccumulatorValueAbsolute(Integer accumulatorValueAbsolute) {
		this.accumulatorValueAbsolute = accumulatorValueAbsolute;
		addParam("accumulatorValueAbsolute", this.accumulatorValueAbsolute);
	}

	public Date getAccumulatorStartDate() {
		return accumulatorStartDate;
	}

	public void setAccumulatorStartDate(Date accumulatorStartDate) {
		this.accumulatorStartDate = accumulatorStartDate;
		addParam("accumulatorStartDate", this.accumulatorStartDate);
	}
}