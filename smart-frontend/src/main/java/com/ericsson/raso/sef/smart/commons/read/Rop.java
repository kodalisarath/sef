package com.ericsson.raso.sef.smart.commons.read;

public class Rop {

	private RopRead ropRead;
	@Override
	public String toString() {
		return "Rop [ropRead=" + ropRead + ", ropBucketRead=" + ropBucketRead
				+ ", ropVersionRead=" + ropVersionRead + "]";
	}

	private RopBucketRead ropBucketRead;
	private RopVersionRead ropVersionRead;

	public RopRead getRopRead() {
		return ropRead;
	}

	public void setRopRead(RopRead ropRead) {
		this.ropRead = ropRead;
	}

	public RopBucketRead getRopBucketRead() {
		return ropBucketRead;
	}

	public void setRopBucketRead(RopBucketRead ropBucketRead) {
		this.ropBucketRead = ropBucketRead;
	}

	public RopVersionRead getRopVersionRead() {
		return ropVersionRead;
	}

	public void setRopVersionRead(RopVersionRead ropVersionRead) {
		this.ropVersionRead = ropVersionRead;
	}

}
