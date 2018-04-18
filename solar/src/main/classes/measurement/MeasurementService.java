package main.classes.measurement;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

//
@Component
public class MeasurementService {

    @Autowired
    private MeasurementDao dao;

    public List<Measurement> getAllMeasurements() {

        List<Measurement> measurements = new ArrayList<Measurement>();

        for (Measurement m : dao.findAll())
            measurements.add(m);


        return measurements;
    }
    public String getSolarInsulation(Double latitude, Double longitude) {


        //https://developer.nrel.gov/api/solar/solar_resource/v1.json?api_key=DEMO_KEY&lat=40&lon=-105
        //api key KuBHzEKFbBS0aVC66QzBc8QQbVuKJC63LkhRTgox
        String URL = "https://developer.nrel.gov/api/solar/solar_resource/v1.json?" +
                "api_key=KuBHzEKFbBS0aVC66QzBc8QQbVuKJC63LkhRTgox&" +
                "lat="+latitude+"&" +
                "lon="+longitude+"&" +
                "format=JSON";


        URL url = null;
        try {
            url = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

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
    public List<Measurement> getMeasurements(String unit,Integer period) {


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
        return dao.getMeasurementByCustomTime(period);

    }
    public List<Measurement> getMeasurementsByDate(String date) throws ParseException {
        List<Measurement> measurements = new ArrayList<Measurement>();


        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime d1 = format.parseDateTime(date);

        for (Measurement m : dao.findAll()){
            if(date.equals(m.getTime().toString().split(" ")[0]))
            {
                measurements.add(m);
            }
        }
        return measurements;
    }
    public Double getAverage(){
        return dao.getMeasurementAverage();
    }


    public Measurement getLast(){
        return dao.getLast();
    }

}


