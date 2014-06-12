package com.ericsson.raso.sef.smart.commons.read;

public class Rpp {

	private RppRead rppRead;
	private RppBucketRead rppBucketRead;
	private RppVersionRead rppVersionRead;

	@Override
	public String toString() {
		return "Rpp [rppRead=" + rppRead + ", rppBucketRead=" + rppBucketRead
				+ ", rppVersionRead=" + rppVersionRead + "]";
	}

	public RppRead getRppRead() {
		return rppRead;
	}

	public void setRppRead(RppRead rppRead) {
		this.rppRead = rppRead;
	}

	public RppBucketRead getRppBucketRead() {
		return rppBucketRead;
	}

	public void setRppBucketRead(RppBucketRead rppBucketRead) {
		this.rppBucketRead = rppBucketRead;
	}

	public RppVersionRead getRppVersionRead() {
		return rppVersionRead;
	}

	public void setRppVersionRead(RppVersionRead rppVersionRead) {
		this.rppVersionRead = rppVersionRead;
	}

}
