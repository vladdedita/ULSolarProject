package main.classes.user;


import main.classes.controllers.APIController;
import main.classes.device.Device;
import main.classes.device.DeviceDao;
import main.classes.token.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserService {

    final
    UserDao userDao;

    final
    DeviceDao devDao;

    final
    APIController ttn;

    @Autowired
    public UserService(UserDao userDao, DeviceDao devDao, APIController ttn) {
        this.userDao = userDao;
        this.devDao = devDao;
        this.ttn = ttn;
    }

    /**
     * Method to add User to database
     *
     * @param appID
     * @param accessKey
     * @param devID
     */
    public void add(String appID, String accessKey, String devID) {
        if (appID != null && !appID.isEmpty() && accessKey != null && !accessKey.isEmpty()) {
            User newUser = new User(appID, accessKey);
            userDao.save(newUser);
            devDao.save(new Device(devID, newUser.getId()));
        }
    }

    /***
     *
     * @param appID
     * @param accessKey
     * @param devID
     * @return generated access/auth token
     */
    public String authorize(String appID, String accessKey, String devID) {


        //Calling authorize method for provided credentials
        if (ttn.authorize(appID, accessKey, devID)) {
            //if user never accessed the service
            if (!exists(appID, accessKey, devID)) {
                //adding user to db for future accessing
                add(appID, accessKey, devID);
            }
            //generating auth token
            generateToken(appID, accessKey, devID);
//            //returning token
//            JsonObject jsonToken = new JsonObject();
//            //jsonToken.add("key",  us.getToken(appID, accessKey, devID));
//            jsonToken.addProperty("key", );
            return getToken(appID, accessKey, devID);
        }
        return null;
    }

    public Boolean isAuthorized(String token) {
        if(token != null && !token.isEmpty()) {
            User u = userDao.findByToken(token);
            //System.out.println(new Date().getTime()+ "             " +u.getBorn().getTime() + 30000);
            if (u != null && new Date().getTime() < u.getBorn().getTime() + 3000000) { //30 mins
                return u.isAuthorized();
            }
        }
        return false;
    }

    public User currentUser(String token){
        return userDao.findByToken(token);
    }


    /**
     * Method to check if user exists in database
     *
     * @param appID
     * @param accessKey
     * @param devID
     * @return true/false
     */
    public boolean exists(String appID, String accessKey, String devID) {
        if (appID != null && !appID.isEmpty() && accessKey != null && !accessKey.isEmpty()) {
            return (userDao.findByAppKey(accessKey) != null);
        }
        return false;
    }

    /**
     * Session token generator + timestamp
     *
     * @param appID
     * @param accessKey
     * @param devID
     */
    private void generateToken(String appID, String accessKey, String devID) {
        if (accessKey != null && !accessKey.isEmpty()) {
            User user = userDao.findByAppKey(accessKey);
            if (user != null) {
                user.setToken(new TokenGenerator().nextString());
                user.setAuthorized();
                user.setCurrentDeviceId(devDao.findByNameAndUserId(devID,user.getId()).getId());
                userDao.save(user);
            }
        }
    }

    private String getToken(String appID, String accessKey, String devID) {
        if (accessKey != null && !accessKey.isEmpty()) {
            User user = userDao.findByAppKey(accessKey);
            if (user != null) {
                return user.getToken();
            }
        }
        return null;
    }


}
