package com.ericsson.raso.sef.smart.commons.read;

public enum Tag {
	
	/**
	 	<entry name="0" value="NOTAG" />
		<entry name="1" value="FORCEDDELETION" />
		<entry name="2" value="BANNED" />
		<entry name="3" value="BARREDIRM" />
		<entry name="4" value="BARREDOTHER" />
		<entry name="5" value="SPECIALFRAUD" />
		<entry name="6" value="ACTIVATIONFLAG" />
		<entry name="7" value="RECYCLE" />
	 */
	
	NOTAG(0,0, true), 
	FORCEDDELETION(7,1, true), 
	BANNED(3,2, true), 
	BARREDIRM(5,3, true),
	BARREDOTHER(4,4, true), 
	SPECIALFRAUD(6,5, true),
	ACTIVATIONFLAG(1,6, false),
	RECYCLE(2,7, false);

	Integer id;
	Integer smartId;
	boolean isSmartTag;

	Tag(Integer id, Integer smartId, boolean isSmartTag) {
		this.id = id;
		this.isSmartTag = isSmartTag;
		this.smartId = smartId;
	}

	public Integer getId() {
		return id;
	}

	public boolean isSmartTag() {
		return isSmartTag;
	}
	
	public Integer getSmartId() {
		return smartId;
	}
	
	public static Tag getTagById(int id) {
		Tag[] tags = Tag.values();
		for (Tag tag : tags) {
			if(tag.getId().intValue() == id) {
				return tag;
			}
		}
		
		return null;
	}
}
