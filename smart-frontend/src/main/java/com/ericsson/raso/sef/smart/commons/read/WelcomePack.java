package com.ericsson.raso.sef.smart.commons.read;

public class WelcomePack {

	private WelcomePackRead read;
	private WelcomePackVersionRead versionRead;
	private WelcomePackBucketRead bucketRead;

	@Override
	public String toString() {
		return "WelcomePack [read=" + read + ", versionRead=" + versionRead
				+ ", bucketRead=" + bucketRead + "]";
	}

	public WelcomePackRead getRead() {
		return read;
	}

	public void setRead(WelcomePackRead read) {
		this.read = read;
	}

	public WelcomePackVersionRead getVersionRead() {
		return versionRead;
	}

	public void setVersionRead(WelcomePackVersionRead versionRead) {
		this.versionRead = versionRead;
	}

	public WelcomePackBucketRead getBucketRead() {
		return bucketRead;
	}

	public void setBucketRead(WelcomePackBucketRead bucketRead) {
		this.bucketRead = bucketRead;
	}

}
