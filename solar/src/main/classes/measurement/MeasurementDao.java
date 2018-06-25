package main.classes.measurement;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeasurementDao extends CrudRepository<Measurement, Long> {


    public List<Measurement> findByUserId(Integer id);

    public Measurement findById(Integer id);


    @Query(value = "SELECT s.id,s.power,s.time,s.direction,s.user_id FROM solar s WHERE TIMESTAMPDIFF(MINUTE,s.time,NOW()) < :time and s.user_id=:userId ORDER BY s.time ASC", nativeQuery = true)
    public List<Measurement> getMeasurementByCustomTime(@Param("userId") Integer userId, @Param("time") Integer time);
    @Query(value =  "SELECT AVG(s.power) FROM solar s where s.power > 10 and s.user_id=:userId",nativeQuery = true)
    public Double getMeasurementAverage(@Param("userId") Integer userId);
    @Query(value="SELECT * FROM solar s WHERE s.user_id=:userId ORDER BY time DESC LIMIT 1",nativeQuery = true)
    public Measurement getLast(@Param("userId") Integer userId);

    @Query(value="SELECT * FROM solar s WHERE s.time=:time",nativeQuery = true)
    public Measurement findByTime(@Param("time") String time);


}
