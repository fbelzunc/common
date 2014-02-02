/**
 * 
 */
package com.cisco.vss.foundation.flowcontext;

import java.io.Serializable;

/**
 * This interface defines data that is to be passed between components while invoked from one to another.
 * The interface should be the first argument in each public method in every class exposed to another component.
 * Examples of use for such a functionality can be considered in a case like Logging when you want to share data cross all log entries.
 * @author Yair Ogen, Yehudit Glass
 *
 */
public interface FlowContext extends Serializable{

	/**
	 * 
	 */
	static final long serialVersionUID = -3934451414870443512L;

	/**
	 * 
	 * @return Unique id to identify the FlowContext
	 */
	String getUniqueId();
}
