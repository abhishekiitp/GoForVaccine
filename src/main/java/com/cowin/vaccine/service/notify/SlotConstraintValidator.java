package com.cowin.vaccine.service.notify;

import com.cowin.vaccine.model.Center;
import com.cowin.vaccine.model.Session;
import com.cowin.vaccine.model.UserRegistrationConstraints;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SlotConstraintValidator {

    public List<Session> getValidCenter(Center center, UserRegistrationConstraints userRegistrationConstraints) {
        List<Session> centers = new ArrayList<>();
        if (center != null && center.getSessions() != null) {
            for (Session session : center.getSessions()) {
                if (session.getMin_age_limit() <= userRegistrationConstraints.getAge() && session.available_capacity_dose1 > 20) {
                    centers.add(session);
                }
            }
        }
        return centers;
    }
}
