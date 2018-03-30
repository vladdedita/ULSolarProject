package main.classes.daos;

import main.classes.models.Measurement;
import org.springframework.data.repository.CrudRepository;

public interface MeasurementDao extends CrudRepository<Measurement,Long> {


    public Measurement findById(Integer id);

//    Query("SELECT * FROM solar WHERE time > ")
//    public Measurement findMeasurementByTime(@Param("time") String time);

}
