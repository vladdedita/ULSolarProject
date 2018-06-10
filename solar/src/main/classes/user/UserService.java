package main.classes.user;


import com.google.gson.JsonObject;
import main.classes.api.APIService;
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

    @Autowired
    APIService ttn;

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
        JsonObject jsonError = new JsonObject();

        if (appID == null || appID.isEmpty()) {
            System.out.println("authorize: empty appID");
            jsonError.addProperty("error", "empty appId");
            return jsonError.toString();
        }
        if (accessKey == null || accessKey.isEmpty()) {
            System.out.println("authorize: empty key");
            jsonError.addProperty("error", "empty key");
            return jsonError.toString();
        }
        if (devID == null || devID.isEmpty()) {
            System.out.println("authorize: empty devID");
            jsonError.addProperty("error", "empty devId");
            return jsonError.toString();
        }

        //Calling authorize method for provided credentials
        if (ttn.authorize(appID, accessKey, devID)) {
            //if user never accessed the service
            if (!exists(appID, accessKey, devID)) {
                //adding user to db for future accessing
                add(appID, accessKey, devID);
            }
            //generating auth token
            generateToken(appID, accessKey, devID);

            //returning token
            JsonObject jsonToken = new JsonObject();
            //jsonToken.add("key",  us.getToken(appID, accessKey, devID));
            jsonToken.addProperty("key", getToken(appID, accessKey, devID));
            return jsonToken.toString();
        }
        return null;
    }

    public Boolean isAuthorized(String token) {
        if(token != null && !token.isEmpty()) {
            User u = userDao.findByToken(token);
            if (u != null) {
                return u.isAuthorized();
            }
        }
        return false;
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
                user.setToken(new tokenGenerator().nextString());
                user.setAuthorized();
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
