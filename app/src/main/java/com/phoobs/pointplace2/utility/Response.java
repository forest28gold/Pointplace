package com.phoobs.pointplace2.utility;

import com.phoobs.pointplace2.R;
import com.phoobs.pointplace2.models.Global;

public class Response {

	private int statusCode;
	private String responseText;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
	
	public int getMessageId() {
		int resId = 0;
		switch (statusCode) {
			case Global.CONNECTION_ERROR:
				resId = R.string.err_no_internet;
				break;
			case Global.CONNECT_TIMEOUT:
				resId = R.string.err_timeout;
				break;
			case Global.READ_TIMEOUT:
				resId = R.string.err_timeout;
				break;
			case Global.IO_ERROR:
				resId = R.string.err_io;
				break;
			case Global.CLIENT_PROTOCOL_ERROR:
				resId = R.string.err_protocol;
				break;
			default:
				resId = R.string.err_unknown;
				break;
		}
		return resId;
	}
}
