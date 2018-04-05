package main.classes.daos;

import main.classes.models.Measurement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeasurementDao extends CrudRepository<Measurement, Long> {


    public Measurement findById(Integer id);
    @Query(value = "SELECT s.id,s.power,s.time,s.direction FROM solar s WHERE TIMESTAMPDIFF(MINUTE,s.time,NOW()) < :time ORDER BY s.time ASC", nativeQuery = true)
    public List<Measurement> getMeasurementByCustomTime(@Param("time") Integer time);
    @Query(value =  "SELECT AVG(s.power) FROM solar s where s.power > 10",nativeQuery = true)
    public Double getMeasurementAverage();
    @Query(value="SELECT * FROM solar s ORDER BY time DESC LIMIT 1",nativeQuery = true)
    public Measurement getLast();

    @Query(value="SELECT * FROM solar s WHERE s.time=:time",nativeQuery = true)
    public Measurement findByTime(@Param("time") String time);
}
