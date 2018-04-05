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

    public List<Measurement> getAllMeasurements() {

        List<Measurement> measurements = new ArrayList<>();

        for (Measurement m : dao.findAll())
            measurements.add(m);


        return measurements;
    }

    public List<Measurement> getMeasurements(String unit,Integer period) {


        switch(unit){
            case("hour"):
                period *=60;
                break;
            case("day"):
                period *=60;
                period *=24;
                break;
            case("month"):
                period*=60;
                period*=24;
                period*=30;
                break;
            case("year"):
                period*=60;
                period*=24;
                period*=30;
                period*=12;
                break;
        }
        return dao.getMeasurementByCustomTime(period);

    }

}


