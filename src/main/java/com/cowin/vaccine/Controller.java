package com.cowin.vaccine;

import com.cowin.vaccine.model.Center;
import com.cowin.vaccine.service.notify.Fetch;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/go")
public class Controller {

    @Autowired
    Fetch service;

    @GetMapping(value = { "/fetch/{district}", "/fetch/{district}/{twoWeek}" })
    public List<Center> fetch(
        @PathVariable(name = "district") String district,
        @PathVariable(name = "twoWeek", required = false) Boolean twoWeek
    ) {
        if (twoWeek == null) twoWeek = Boolean.FALSE;
        return service.get(district, twoWeek);
    }
}
