package com.ericsson.raso.sef.cg.engine.web;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/API")
public interface ReloadArtifactService {

	@POST
	@Path("processCGRequest")
	public void reload() throws Exception;

}
