package si.iitech.price.exception.impl;

public class PriceException extends Exception {

	private static final long serialVersionUID = 1L;

	public PriceException(Throwable throwable) {
		super(throwable);
	}
	
	public PriceException(String message) {
		super(message);
	}
}