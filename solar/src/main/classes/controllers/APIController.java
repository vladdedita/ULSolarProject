package main.classes.controllers;

import com.google.gson.Gson;
import com.pusher.rest.Pusher;
import main.classes.daos.MeasurementDao;
import main.classes.models.Measurement;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;


@Component
public class APIController {

    @Autowired
    MeasurementDao dao;
    private StringBuilder apiUrl;

    private String appName;
    private String devName;
    private String accessKey;
    private String processId;
    private Boolean authorized=false;

    private int updateRate=7; // time period to check for updates
    private TimeUnit timeUnit= TimeUnit.DAYS;
    private Pusher pusher;

    private ScheduledExecutorService executor;

    public enum DOWNLINK_TYPE{
        INTEROGATION_TIME
    }
    Runnable dataUpdate = new Runnable() {
        public void run() {

            getLatestData();

        }
    };

    APIController() {

        executor = Executors.newScheduledThreadPool(1);
        System.out.println("Created thread pool...");
        pusher = new Pusher("500372", "43efaf697390e4298a8f", "c5f63fc8d80b1ae2c138");
        pusher.setCluster("eu");
        pusher.setEncrypted(true);


    }

    boolean authorize (String appName, String accessKey, String devName) {

        this.appName=appName;
        this.accessKey=accessKey;
        this.devName=devName;

        apiUrl=new StringBuilder("https://");
        apiUrl.append(appName);
        apiUrl.append(".data.thethingsnetwork.org/api/v2");

        String deviceUrl = apiUrl.toString() + "/devices";
        URL url=null;
        try{
            url = new URL (deviceUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Authorization","key "+accessKey);
            int status = con.getResponseCode();
            if(status == 200)
            {
                authorized=true;
                getLatestData();
                return true;
            }
            return false;
        }
        catch (MalformedURLException e)
        {
            System.out.println(e.toString());
            return false;

        }
        catch (IOException urlError)
        {
            System.out.println("Could not open connection" + urlError.toString());
            return false;

        }
    }

    private String request(String URL) {

        URL url=null;
        try{
            url = new URL (URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Authorization","key "+this.accessKey);
            int status = con.getResponseCode();
            if(status != 200)
            {
                return null;
            }
            System.out.println("Status Code: " + status);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();

            while((inputLine = in.readLine()) != null){
                content.append(inputLine);
                content.append(System.getProperty("line.separator"));
            }
            return content.toString();
        }
        catch (MalformedURLException e)
        {
            System.out.println(e.toString());
            return null;
        }
        catch (IOException urlError)
        {
            System.out.println("Could not open connection:" + urlError.toString());
            return null;
        }
    }

    public Pusher getPusher() {
        return pusher;
    }

    private void getLatestData() {

        if (!authorized) {
            System.out.println("\tNot authorized!");

        } else {
            String dataUrl = apiUrl.toString() + "/query/" + this.devName +"?last="+updateRate;

            if(timeUnit==TimeUnit.MINUTES)
                dataUrl+="m";
            else
                dataUrl+="d";

            System.out.println("Send request to:"+dataUrl);
            String jsonData = request(dataUrl);
            if(jsonData == null)
            {
                System.out.println("No data to fetch");
                return;
            }
            System.out.println("OK");

            Gson gson = new Gson();
            Measurement[] data = gson.fromJson(jsonData, Measurement[].class);
            Measurement last=null;

            System.out.print("Updating database...");
            for (Measurement m : data)
            {
                if(m.getPower() != null && m.getTime()!=null)
                {
                    try{
                        last=dao.save(m);
                        pusher.trigger("chart", "chartData", new ArrayList<Measurement>().add(dao.findById(m.getId())));
                    }
                    catch(DataIntegrityViolationException e){

                    }
                }
            }
            System.out.println("OK");


            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");
            Date d1 = null;

            try {
                if(last!=null) {
                    System.out.println("Trying to parse date:"+ last.getTime().toString());
                    d1 = format.parse(last.getTime().toString());
                    System.out.println("Parsed:"+d1.toString());
                    DateTime dt1 = new DateTime(d1);
                    DateTime dt2 = new DateTime();
                    if(abs(Minutes.minutesBetween(dt1, dt2).getMinutes()) > 15)
                        updateRate=Minutes.minutesBetween(dt1, dt2).getMinutes();
                    else
                        updateRate = 15- abs(Minutes.minutesBetween(dt1, dt2).getMinutes());
                    System.out.println("Update rate:" + updateRate);
                    timeUnit = TimeUnit.MINUTES;
                    executor.scheduleAtFixedRate(dataUpdate, 0, 1/*(updateRate==15?1:15-updateRate)*/, timeUnit);
                    System.out.print("Scheduled task...");

                }
                else
                    System.out.println("Nothing found");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("OK");


        }
    }

    public Integer scheduleDownlink(String processId, DOWNLINK_TYPE type, String value ) {
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
        authorized=true;
        if(authorized)
        {
            if(!processId.isEmpty())
                this.processId=processId;
            try {
                URL url = new URL("https://integrations.thethingsnetwork.org/ttn-eu/api/v2/down/" + this.appName+ "/" + this.processId+ "?key=" + this.accessKey);
                System.out.println("Sending request to:" + url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String payload = "{" +
                        "\"port\":1," +
                        "\"dev_id\" : \"" + this.devName + "\"," +
                        "\"confirmed\":false," +
                        "\"payload_fields\" : {" +
                        "\"type\": \"" + type.toString() + "\"," +
                        "\"value\": "+value+"}}";

                System.out.println("Payload:"+payload);

                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json; CHARSET=UTF-8");
                con.setRequestProperty("Authorization", "key " + this.accessKey);
                OutputStream os = con.getOutputStream();
                os.write(payload.getBytes("UTF-8"));
                os.close();
                int status = con.getResponseCode();
                return status;
            }
            catch(IOException e){
                System.out.println(e.toString());
            }
        }
        return 500;

    }


}
