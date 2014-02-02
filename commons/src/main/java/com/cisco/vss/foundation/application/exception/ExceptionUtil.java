package com.cisco.vss.foundation.application.exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * This is a tool to find the lowest exception or the lowest NDS exception This
 * tool is also used to get a string of that represent the all "caused by" to an
 * exception.
 *
 * @author Yehudit Glass
 *
 */
public final class ExceptionUtil {

	private ExceptionUtil() {
		// prevent instantiation
	}

	/**
	 * Get a string that represent the "caused by" of the exception
	 *
	 * @param exception
	 *            - (nested) exception to be dig
	 * @return A string of the "caused by" of the exception
	 */
	public static String getExceptionCause(final Exception exception) {
		if (exception == null) {
			return "";
		}

		final StringBuilder cause = new StringBuilder();
		Throwable nestedException = (Throwable) exception;
		while (nestedException != null) {
			cause.append("Caused by: ").append(nestedException.toString()).append(" ");
			nestedException = getInnerException(nestedException);
		}

		return cause.toString();
	}

	/**
	 * Get the most inner exception
	 *
	 * @param exception
	 *            - (nested) exception to be dig
	 * @return
	 */
	public static Throwable getLowestException(final Exception exception) {

		return (Throwable) getLowestException(exception, "");
	}

	/**
	 * Get the most NDS inner exception.
	 *
	 * @param exception
	 * @param ndsExceptionPrefix
	 * @return
	 */
	public static Exception getNDSLowestException(final Exception exception) {
		final String ndsExceptionPrefix = "com.nds";
		return getLowestException(exception, ndsExceptionPrefix);
	}

	/**
	 * Get the most inner exception that is exception from "exceptionPrefix"
	 * type. If the exception does not include inner exception of type
	 * "exceptionPrefix", returns null.
	 *
	 * @param exception
	 * @param exceptionPrefix
	 * @return
	 */
	public static Exception getLowestException(final Exception exception, final String exceptionPrefix) {

		if (exception == null) {
			return null;
		}

		Throwable nestedException = (Throwable) exception;
		Exception lastLowException = null;
		while (nestedException != null) {
			if (nestedException.getClass().toString().startsWith("class " + exceptionPrefix)) {
				lastLowException = (Exception) nestedException;
			}

			nestedException = getInnerException(nestedException);
		}

		return lastLowException;
	}

	/**
	 * Get a one level inner exception
	 *
	 * @param exception
	 * @return
	 */
	private static Throwable getInnerException(Throwable exception) {// NOPMD

		final Throwable tmpException = exception;

		exception = getExceptionCauseUsingWellKnownTypes(tmpException);
		if (exception == null) {
			for (CauseMethodName methodName : CauseMethodName.values()) {
				exception = getExceptionCauseUsingMethodName(tmpException, methodName);
				if (exception != null) {
					break;
				}
			}
		}

		return exception;
	}

	private static Throwable getExceptionCauseUsingWellKnownTypes(final Throwable exception) {

		if (exception instanceof SQLException) {
			return ((SQLException) exception).getNextException();
		} else if (exception instanceof InvocationTargetException) {
			return ((InvocationTargetException) exception).getTargetException();
		} else {
			return null;
		}
	}

	/**
	 * To discover inner exception, this method try to use reflection of known
	 * methods that could give the nested exception. So, it ignore case of
	 * NoSuchMethodException, SecurityException, IllegalAccessException,
	 * IllegalArgumentException and InvocationTargetException.
	 *
	 * @param throwable
	 * @param methodName
	 * @return
	 */
	private static Throwable getExceptionCauseUsingMethodName(final Throwable throwable, final CauseMethodName methodName) {

		Method method = null;
		try {
			method = throwable.getClass().getMethod(methodName.toString(), (Class[]) null);
		} catch (NoSuchMethodException exception) {// NOPMD
			// Ignore (see Java Doc)
		} catch (SecurityException exception) {// NOPMD
			// Ignore (see Java Doc)
		}

		if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
			try {
				return (Throwable) method.invoke(throwable, new Object[] {});
			} catch (IllegalAccessException exception) {// NOPMD
				// Ignore (see Java Doc)
			} catch (IllegalArgumentException exception) {// NOPMD
				// Ignore (see Java Doc)
			} catch (InvocationTargetException exception) {// NOPMD
				// Ignore (see Java Doc)
			}
		}
		return null;
	}

	// /**
	// * To discover inner exception, this method try to use reflection
	// "details" field
	// * to get a nested exception.
	// * So, it ignore case of NoSuchMethodException, SecurityException,
	// IllegalAccessException
	// * and IllegalArgumentException
	// * @param ex
	// * @param fieldName
	// * @return
	// */
	// private static Throwable getExceptionCauseUsingField(Throwable ex, String
	// fieldName) {
	// Field field = null;
	// try{
	// field = ex.getClass().getField(fieldName);
	// }
	// catch(NoSuchFieldException exception){
	// // Ignore (see Java Doc)
	// }
	// catch(SecurityException exception){
	// // Ignore (see Java Doc)
	// }
	//
	// if (field != null && Throwable.class.isAssignableFrom(field.getType())){
	// try {
	// return (Throwable)field.get(ex);
	// }
	// catch (IllegalArgumentException exception) {
	// // Ignore (see Java Doc)
	// }
	// catch (IllegalAccessException exception) {
	// // Ignore (see Java Doc)
	// }
	// }
	// return null;
	// }
}
