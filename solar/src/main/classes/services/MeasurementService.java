package main.classes.services;

import main.classes.daos.MeasurementDao;
import main.classes.models.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;




@RestController
public class MeasurementService {

    @Autowired
    private MeasurementDao dao;

    public List<Measurement> getAllMeasurements(){

        List<Measurement> measurements=new ArrayList<>();

        for(Measurement m : dao.findAll())
            measurements.add(m);


        return measurements;

    }


}
