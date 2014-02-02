/**
 *
 */
package com.cisco.vss.foundation.application.exception;

import java.util.List;

/**
 * @author Yair Ogen
 */
public interface ApplicationExceptionInterface {

    ErrorCodeInterface getErrorCode();

    List<ErrorCodeInterface> getAllErrorCodes();

    String getMessage();
}
