package main.classes.location;

import main.classes.device.Device;
import main.classes.device.DeviceDao;
import main.classes.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationService {


    final
    LocationDao dao;

    final
    DeviceDao deviceDao;

    final
    UserDao userDao;

    @Autowired
    public LocationService(LocationDao dao, DeviceDao deviceDao, UserDao userDao) {
        this.dao = dao;
        this.deviceDao = deviceDao;
        this.userDao = userDao;
    }

    Boolean addLocation(String token,String lat,String lon, Boolean isPublic){

        //add location
        if(lat!=null && !lat.isEmpty() && lon!=null && !lon.isEmpty())
        {
            Location loc = dao.findByLatAndLon(lat,lon);
            if(loc != null)
            {

                if(isPublic != loc.isPublic())
                {
                    loc.setPublic(isPublic);
                    dao.save(loc);

                }
                return true;
            }

            Location newLocation = new Location(lat, lon);
            if(isPublic) {
                newLocation.setPublic(true);
            }else
                newLocation.setPublic(false);

            newLocation = dao.save(newLocation);
            Device device = deviceDao.findById(userDao.findByToken(token).getCurrentDeviceId());
            if(device != null) {
                device.setLocationId(newLocation.getId());
                deviceDao.save(device);
                return true;
            }
        }
        return false;
        //relate to user
    }
    List<Location> getLocations(String token){
        return dao.findAllByIsPublic(true);
    }
}
