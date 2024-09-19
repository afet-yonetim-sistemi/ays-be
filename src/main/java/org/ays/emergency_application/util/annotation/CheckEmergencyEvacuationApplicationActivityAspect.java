package org.ays.emergency_application.util.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.ays.common.util.exception.EmergencyEvacuationApplicationEndpointNotActiveException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
class CheckEmergencyEvacuationApplicationActivityAspect {

    @Value("${ays.emergency-evacuation-application.enabled}")
    private boolean isEmergencyEvacuationApplicationEnabled;

    @Before(value = "@annotation(CheckEmergencyEvacuationApplicationActivity)")
    public void checkEmergencyEvacuationActivity() {

        if (isEmergencyEvacuationApplicationEnabled) {
            return;
        }

        throw new EmergencyEvacuationApplicationEndpointNotActiveException();
    }

}
