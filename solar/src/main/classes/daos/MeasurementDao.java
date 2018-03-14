package main.classes.daos;

import main.classes.models.Measurement;
import org.springframework.data.repository.CrudRepository;

public interface MeasurementDao extends CrudRepository<Measurement,Long> {


    public Measurement findById(Integer id);


}
