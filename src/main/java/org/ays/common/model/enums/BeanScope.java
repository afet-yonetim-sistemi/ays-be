package org.ays.common.model.enums;

import lombok.experimental.UtilityClass;

/**
 * This utility class is used to define the bean scope values in the Spring Framework.
 */
@UtilityClass
public class BeanScope {

    /**
     * Bean instances defined with "prototype" scope will create a new instance for each request.
     */
    public static final String SCOPE_PROTOTYPE = "prototype";

    /**
     * Bean instances defined with "request" scope will create an instance that is available for the duration of an HTTP request.
     */
    public static final String SCOPE_REQUEST = "request";

}
