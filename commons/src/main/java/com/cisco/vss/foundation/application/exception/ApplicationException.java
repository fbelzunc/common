package com.cisco.vss.foundation.application.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a base exception to be used by all the CAB components that need to
 * create custom exceptions. This exception wraps an <link {@code ErrorCode}>
 * Object and knows to display it when the getMessage function is used and in
 * exception stack traces. Note: If you override the getMessage method in your
 * code, you must call the super so the error code can be updated accordingly.
 * 
 * @author Yair Ogen
 * @author Joel Gurfinkel
 */
public class ApplicationException extends Exception implements ApplicationExceptionInterface {

	private final List<ErrorCodeInterface> errorCodes = new ArrayList<ErrorCodeInterface>(5);

	private static final long serialVersionUID = -4043082316087703586L;

	public ApplicationException(final String message, final Throwable cause) {
		super(message, cause);
		ErrorCodeUtil.INSTANCE.updateErrorDetails(cause, errorCodes);

	}

	public ApplicationException(final String message, final ErrorCodeInterface errorCode) {
		super(message);
		errorCodes.add(errorCode);
	}

	public ApplicationException(final String message, final Throwable cause, final ErrorCodeInterface errorCode) {
		super(message, cause);
		ErrorCodeUtil.INSTANCE.updateErrorDetails(cause, errorCodes);
		errorCodes.add(errorCode);
	}

	@Override
	public final ErrorCodeInterface getErrorCode() {
		return (errorCodes == null || errorCodes.isEmpty()) ? null : errorCodes.get(0);
	}

	@Override
	public final List<ErrorCodeInterface> getAllErrorCodes() {
		return errorCodes;
	}

}
