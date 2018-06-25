package main.classes.controllers;

import com.google.gson.Gson;
import com.pusher.rest.Pusher;
import main.classes.device.DeviceDao;
import main.classes.measurement.Measurement;
import main.classes.measurement.MeasurementDao;
import main.classes.user.User;
import main.classes.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class APIController {


    private static final int DEFAULT_UPDATERATE = 1;
    private static final TimeUnit DEFAULT_UPDATERATE_TIMEUNIT = TimeUnit.MINUTES;

    @Autowired
    MeasurementDao dao;

    @Autowired
    UserDao userDao;

    @Autowired
    DeviceDao deviceDao;


    private StringBuilder apiUrl;

    private String processId;
    private Boolean authorized = false;

    private Integer updateRate = 15; // time period to check for updates
    private TimeUnit timeUnit = TimeUnit.MINUTES;
    private Pusher pusher;

    Runnable dataUpdate = new Runnable() {
        public void run() {

            getLatestData();

        }
    };

    public Boolean getAuthorized() {
        return authorized;
    }

    APIController() {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        System.out.println("Created thread pool...");


        pusher = new Pusher("500372", "43efaf697390e4298a8f", "c5f63fc8d80b1ae2c138");
        pusher.setCluster("eu");
        pusher.setEncrypted(true);

        executor.scheduleAtFixedRate(dataUpdate, 0, DEFAULT_UPDATERATE/*(updateRate==15?1:15-updateRate)*/, DEFAULT_UPDATERATE_TIMEUNIT);

        System.out.println("Scheduled task at: " + DEFAULT_UPDATERATE + " " + DEFAULT_UPDATERATE_TIMEUNIT);


    }

    boolean authorize(String appName, String accessKey, String devName) {


        apiUrl = new StringBuilder("https://");
        apiUrl.append(appName);
        apiUrl.append(".data.thethingsnetwork.org/api/v2");

        String deviceUrl = apiUrl.toString() + "/devices";
        URL url = null;
        try {
            url = new URL(deviceUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "key " + accessKey);
            int status = con.getResponseCode();
            if (status == 200) {
                authorized = true;
                getAllData();
                getLatestData();
                return true;
            }
            return false;
        } catch (MalformedURLException e) {
            System.out.println(e.toString());
            return false;

        } catch (IOException urlError) {
            System.out.println("Could not open connection" + urlError.toString());
            return false;

        }
    }

    private String request(String URL, String appKey) {

        URL url = null;
        try {
            url = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "key " + appKey);
            int status = con.getResponseCode();
            if (status != 200) {
                return null;
            }
            System.out.println("Status Code: " + status);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append(System.getProperty("line.separator"));
            }
            return content.toString();
        } catch (MalformedURLException e) {
            System.out.println(e.toString());
            return null;
        } catch (IOException urlError) {
            System.out.println("Could not open connection:" + urlError.toString());
            return null;
        }
    }

    public Pusher getPusher() {
        return pusher;
    }

    private void getAllData() {
        if (!authorized) {
            System.out.println("Not authorized");
            return;
        }

    }

    private void getLatestData() {

        if (updateRate == null)
            updateRate = DEFAULT_UPDATERATE;
        if (timeUnit == null)
            timeUnit = DEFAULT_UPDATERATE_TIMEUNIT;


        //foreach authorized user
        for (User u : userDao.findAll()) {

            if (u.isAuthorized()) {

                String dataUrl = apiUrl.toString() + "/query/" + deviceDao.findByUserId(u.getId()).getName() + "?last=" + updateRate;

                if (timeUnit == TimeUnit.MINUTES)
                    dataUrl += "m";
                else
                    dataUrl += "d";

                System.out.println("Send request to:" + dataUrl);
                String jsonData = request(dataUrl, u.getAppKey());

                if (jsonData == null) {
                    System.out.println("No data to fetch");
                }

                System.out.println("OK");

                Gson gson = new Gson();
                Measurement[] data = null;
                if (jsonData != null) {
                    data = gson.fromJson(jsonData, Measurement[].class);
                    System.out.print("Updating database...");

                    for (Measurement m : data) {
                        if (m.getPower() != null && m.getTime() != null) {
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");
                                Date d1 = null;
                                d1 = format.parse(m.getTime().toString());
                                String d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d1);
//                            System.out.println("DB: "+dao.findByTime(d2));
//                            System.out.println("Response: " + d2);


                                //check if data is already in db
                                if (dao.findByTime(d2) == null) {

                                    //if not insert
                                    m.setDeviceId(u.getId());
                                    dao.save(m);
                                    pusher.trigger("chart" + u.getToken(), "chartData", new ArrayList<Measurement>().add(dao.findById(m.getId())));
                                }
                            } catch (DataIntegrityViolationException ee) {

                            } catch (ParseException parseEx) {
                                System.out.println(parseEx.toString());
                            }
                        }
                    }
                }


            }
        }


    }


    public Integer scheduleDownlink(String token, String processId, DOWNLINK_TYPE type, String value) {
//        {
//            "dev_id": "my-dev-id", // The device ID
//                "port": 1,             // LoRaWAN FPort
//                "confirmed": false,    // Whether the downlink should be confirmed by the device
//                "payload_fields": {    // The JSON object to be encoded by your encoder payload function
//            "on": true,
//                    "color": "blue"
//        }
//        }

        //https://integrations.thethingsnetwork.org/ttn-eu/api/v2/down/my-app-id/my-process-id?key=ttn-account-v2.secret
        //authorized=true;

            if (!processId.isEmpty())
                this.processId = processId;
            try {
                User user = userDao.findByToken(token);
                if(user != null) {
                    URL url = new URL("https://integrations.thethingsnetwork.org/ttn-eu/api/v2/down/" + user.getAppName() + "/" + this.processId + "?key=" +user.getAppKey());
                    System.out.println("Sending request to:" + url.toString());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    String payload = "{" +
                            "\"port\":" + type.getValue() + "," +
                            "\"dev_id\" : \"" + deviceDao.findByUserId(user.getId()).getName() + "\"," +
                            "\"confirmed\":false," +
                            "\"payload_fields\" : {" +
                            "\"value\": " + (type.getValue() == 1 ? value : "\"" + value + "\"") + "}}";

                    System.out.println("Payload:" + payload);
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setRequestProperty("Content-Type", "application/json; CHARSET=UTF-8");
                    con.setRequestProperty("Authorization", "key " + user.getAppKey()
                    );
                    OutputStream os = con.getOutputStream();
                    os.write(payload.getBytes("UTF-8"));
                    os.close();
                    int status = con.getResponseCode();
                    return status;
                }
            } catch (IOException e) {
                System.out.println(e.toString());
            }

        return 500;

    }

    public enum DOWNLINK_TYPE {
        INTEROGATION_TIME(2),
        POSITION(3),
        STATE(4);
        private int value;

        DOWNLINK_TYPE(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }
    }


}
