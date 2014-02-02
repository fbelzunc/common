/**
 * 
 */
package com.cisco.vss.foundation.application.exception;

import java.io.Serializable;

/**
 *
 * @author Yair Ogen
 */
public interface ErrorCodeInterface extends Serializable {
	
	/**
	 * 
	 */
	static final long serialVersionUID = 8490892442367501758L;

	String getComponentId();
	
	int getCode();		

}
