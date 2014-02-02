/**
 * 
 */
package com.cisco.vss.foundation.application.exception;


/**
 * This class defined the error details of any application error.
 * It holds a component Id and an error code that should be created by the low level component that originates the exception.
 * The same instance should be aggregated to the wrapping exception until it gets to highest level where it is logged to the  client system.
 * The ErrorCode is wrapped under <link {@code ApplicationException}>.
 * @author Yair Ogen
 * @author Joel Gurfinkel
 */
public class ErrorCode implements ErrorCodeInterface {
	

	private static final long serialVersionUID = 5364995786467605380L;
	private final String componentId;
	private final int code;
	
	public ErrorCode(final String componentId, final int code) {
		this.componentId = componentId;
		this.code = code;
	}

	public String getComponentId() {
		return componentId;
	}
	
	public int getCode() {
		return code;
	}
	
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + code;
		result = 31 * result + ((componentId == null) ? 0 : componentId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {// NOPMD

		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		final ErrorCode other = (ErrorCode) obj;
		if (code != other.code) {
			return false;
		}

		if (componentId == null) {
			
			if (other.componentId != null) {
				return false;
			}
			
		} else if (!componentId.equals(other.componentId)) {
			return false;
		}
		
		return true;
	}

		
	@Override
	public String toString() {
		return getComponentId() + '-' + getCode();
	}

}
