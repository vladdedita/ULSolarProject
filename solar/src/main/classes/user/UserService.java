package main.classes.user;


import main.classes.device.Device;
import main.classes.device.DeviceDao;
import main.classes.token.tokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    DeviceDao devDao;

    /**
     * Method to add User to database
     * @param appID
     * @param accessKey
     * @param devID
     */
    public void add(String appID, String accessKey, String devID){
        if(appID != null && !appID.isEmpty() && accessKey != null && !accessKey.isEmpty())
        {
            User newUser = new User(appID,accessKey);
            userDao.save(newUser);
            devDao.save(new Device(devID,newUser.getId()));
        }
    }
    /**
     * Method to check if user exists in database
     * @param appID
     * @param accessKey
     * @param devID
     * @return true/false
     */
    public boolean exists(String appID, String accessKey, String devID) {
        if(appID != null && !appID.isEmpty() && accessKey != null && !accessKey.isEmpty())
        {
            return (userDao.findByAppKey(accessKey)!=null);
        }
        return false;
    }

    /**
     * Session token generator + timestamp
     * @param appID
     * @param accessKey
     * @param devID
     */
    public void generateToken(String appID, String accessKey, String devID)
    {
        if(accessKey !=null && !accessKey.isEmpty())
        {
            User user = userDao.findByAppKey(accessKey);
            if(user !=null)
            {
                user.setToken(new tokenGenerator().nextString());
                userDao.save(user);
            }
        }
    }
    public String getToken(String appID, String accessKey, String devID)
    {
        if(accessKey !=null && !accessKey.isEmpty())
        {
            User user = userDao.findByAppKey(accessKey);
            if(user !=null)
            {
                return user.getToken();
            }
        }
        return null;
    }


}
