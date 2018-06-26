package main.classes.measurement;

import main.classes.user.User;
import main.classes.user.UserDao;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

//
@Component
public class MeasurementService {


    @Autowired
    private MeasurementDao dao;
    @Autowired
    private UserDao userDao;

    public List<Measurement> getAllMeasurements(String token) {

        List<Measurement> measurements = new ArrayList<Measurement>();
        User user = userDao.findByToken(token);
        for (Measurement m : dao.findByDeviceId(user.getCurrentDeviceId()))
            measurements.add(m);
        return measurements;
    }

    public List<Measurement> getMeasurements(String token, String unit,Integer period) {

        switch(unit){
            case("hour"):
                period *=60;
                break;
            case("day"):
                period *=60;
                period *=24;
                break;
            case("month"):
                period*=60;
                period*=24;
                period*=30;
                break;
            case("year"):
                period*=60;
                period*=24;
                period*=30;
                period*=12;
                break;


        }
        User user = userDao.findByToken(token);
        return dao.getMeasurementByCustomTime(user.getId(), period);

    }
    public List<Measurement> getMeasurementsByDate(String token, String date) throws ParseException {
        List<Measurement> measurements = new ArrayList<Measurement>();

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime d1 = format.parseDateTime(date);
        User user = userDao.findByToken(token);
        for (Measurement m : dao.findByDeviceId(user.getCurrentDeviceId())){
            if(date.equals(m.getTime().toString().split(" ")[0]))
            {
                measurements.add(m);
            }
        }
        return measurements;
    }
    public Double getAverage(Integer userId){

        return dao.getMeasurementAverage(userId);
    }


    public Measurement getLast(Integer userId){
        return dao.getLast(userId);
    }




    //    public String getSolarInsulation(Double latitude, Double longitude) {
//
//
//        //https://developer.nrel.gov/api/solar/solar_resource/v1.json?api_key=DEMO_KEY&lat=40&lon=-105
//        //api key KuBHzEKFbBS0aVC66QzBc8QQbVuKJC63LkhRTgox
//        String URL = "https://developer.nrel.gov/api/solar/solar_resource/v1.json?" +
//                "api_key=KuBHzEKFbBS0aVC66QzBc8QQbVuKJC63LkhRTgox&" +
//                "lat="+latitude+"&" +
//                "lon="+longitude+"&" +
//                "format=JSON";
//
//
//        URL url = null;
//        try {
//            url = new URL(URL);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Content-Type", "application/json");
//
//            int status = con.getResponseCode();
//            if (status != 200) {
//                return null;
//            }
//            System.out.println("Status Code: " + status);
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer content = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//                content.append(System.getProperty("line.separator"));
//            }
//            return content.toString();
//        } catch (MalformedURLException e) {
//            System.out.println(e.toString());
//            return null;
//        } catch (IOException urlError) {
//            System.out.println("Could not open connection:" + urlError.toString());
//            return null;
//        }
//
//    }
}


