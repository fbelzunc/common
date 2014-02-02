/**
 * 
 */
package com.cisco.vss.foundation.application.exception;

import java.util.List;

/**
 * @author Yair Ogen
 * 
 */
public enum ErrorCodeUtil {

	INSTANCE;

	public void updateErrorDetails(final Throwable origCause, List<ErrorCodeInterface> errorCodes) {
		Throwable cause = origCause;
		while (cause != null) {
			if (cause instanceof ApplicationExceptionInterface) {
				ApplicationExceptionInterface applicationException = (ApplicationExceptionInterface) cause;
				errorCodes.addAll(applicationException.getAllErrorCodes());
				break;
			}
			cause = cause.getCause();
		}

	}

}
