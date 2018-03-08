package main.classes.controllers;


import main.classes.models.Measurement;
import main.classes.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MeasurementController {

    @Autowired
    MeasurementService ms;

    @RequestMapping("/measurements")
    @CrossOrigin
    public List<Measurement> getMeasurements(){

        return ms.getAllMeasurements();

    }


}
