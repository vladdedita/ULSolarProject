package main.classes.measurement;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeasurementDao extends CrudRepository<Measurement, Long> {



    public List<Measurement> findByDeviceId(Integer id);
    public Measurement findById(Integer id);


    @Query(value = "SELECT s.id,s.power,s.time,s.direction,s.device_id,s.location_id FROM solar s WHERE TIMESTAMPDIFF(MINUTE,s.time,NOW()) < :time and s.device_id=:deviceId ORDER BY s.time ASC", nativeQuery = true)
    public List<Measurement> getMeasurementByCustomTime(@Param("deviceId") Integer deviceId, @Param("time") Integer time);
    @Query(value =  "SELECT AVG(s.power) FROM solar s where s.power > 10 and s.device_id=:deviceId",nativeQuery = true)
    public Double getMeasurementAverage(@Param("deviceId") Integer deviceId);
    @Query(value="SELECT * FROM solar s WHERE s.device_id=:deviceId ORDER BY time DESC LIMIT 1",nativeQuery = true)
    public Measurement getLast(@Param("deviceId") Integer deviceId);
    @Query(value="SELECT * FROM solar s WHERE s.time=:time",nativeQuery = true)
    public Measurement findByTime(@Param("time") String time);


}
