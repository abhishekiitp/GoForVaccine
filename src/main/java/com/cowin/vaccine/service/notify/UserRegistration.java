package com.cowin.vaccine.service.notify;

import com.cowin.vaccine.model.UserRegistrationConstraints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UserRegistration {

    Map<String, List<UserRegistrationConstraints>> userRegistrationConstraints;

    UserRegistration() {
        userRegistrationConstraints = new HashMap<>();
        userRegistrationConstraints.put("188", district144());
    }

    List<UserRegistrationConstraints> district144() {
        List<UserRegistrationConstraints> district144 = new ArrayList<>();
        district144.add(new UserRegistrationConstraints(32, "COVAXIN", "abhishek0agarwal@gmail.com"));

        return district144;
    }

    public Map<String, List<UserRegistrationConstraints>> getUserRegistrationConstraints() {
        return userRegistrationConstraints;
    }

    public void setUserRegistrationConstraints(Map<String, List<UserRegistrationConstraints>> userRegistrationConstraints) {
        this.userRegistrationConstraints = userRegistrationConstraints;
    }
}
