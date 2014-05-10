package com.ericsson.raso.sef.client.air.response;

import java.util.Map;

public class Community extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public Community(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private Integer communityId;

	public Integer getCommunityId() {
		if (communityId == null) {
			communityId = getParam("communityId", Integer.class);
		}
		return communityId;
	}

}
