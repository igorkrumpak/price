package si.iitech.price.exception.impl;

public class RestException {

	private String errorMessage;

	public RestException(Exception ex) {
		this.errorMessage = ex.getMessage();
	}
	
	public RestException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
