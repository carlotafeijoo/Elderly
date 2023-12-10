package BITalino;

public class BITalinoException extends java.lang.Exception {

	private static final long serialVersionUID = 1L;
	/**
	Constructs a new BITalinoException with the specified BITalinoErrorTypes.
	@param errorType the BITalinoErrorTypes enum constant representing the error type
	 */
	public BITalinoException(BITalinoErrorTypes errorType) {
		super(errorType.getName());
		code = errorType.getValue();
	}

	public int code;

}
