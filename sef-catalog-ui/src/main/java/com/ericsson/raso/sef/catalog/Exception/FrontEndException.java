package com.ericsson.raso.sef.catalog.Exception;

public class FrontEndException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7127685574768938261L;
	private String errCode;
	private String errMsg;
	public FrontEndException(String errorMessage,String errorCode){
		this.errMsg=errorMessage;
		this.errCode=errorCode;
	}
	
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	

}
