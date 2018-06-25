package main.classes.location;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationDao extends CrudRepository<Location, Long> {

    @Query(value="SELECT * FROM location l WHERE l.lon=:lon AND l.lat=:lat",nativeQuery = true)
    Location findByLatAndLon(@Param(value = "lat")String lat,@Param(value = "lon") String lon);
    List<Location> findAllByIsPublic(Boolean isPublic);
}
