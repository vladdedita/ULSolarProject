package main.classes.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.classes.models.Measurement;
import main.classes.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class MeasurementController {

    @Autowired
    MeasurementService ms;

    @Autowired
    APIController ttn;

//    @RequestMapping(value = "/isauthorized", method = RequestMethod.POST, produces = {"application/json"})
//    @CrossOrigin
//    public Boolean isAuthorized(@RequestBody String str) throws IOException {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode node = objectMapper.readTree(str);
//
//       return ttn.getAuthorized();
//    }


    @RequestMapping(value = "/measurements", method = RequestMethod.GET, produces = {"application/json"})
    @CrossOrigin
    public @ResponseBody
    String getMeasurements() {
        JsonObject obj=new JsonObject();
        obj.addProperty("average",ms.getAverage());
        obj.addProperty("last",ms.getLast().getPower());
        return obj.toString();
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = {"application/json"})
    @CrossOrigin
    public Integer authorize(@RequestBody String str) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(str);

        String name = objectMapper.convertValue(node.get("name"), String.class);
        String accessKey = objectMapper.convertValue(node.get("key"), String.class);


        if (name == null || name.isEmpty()) {
            System.out.println("authorize: empty name");
            return 0;

        }

        if (accessKey == null || accessKey.isEmpty()) {
            System.out.println("authorize: empty key");
            return 0;

        }

        if (ttn.authorize(name, accessKey, "ded-things-uno"))
            return 1;

        return 0;
    }

    /**
     * @return list of registered data
     */

    @RequestMapping(value = "/getdata/{unit}/{time}", method = RequestMethod.GET, produces = {"application/json"})
    @CrossOrigin
    public void getData(@PathVariable String unit, @PathVariable Integer time) {

        List<Measurement> measurementList;
        List<Measurement> toSendBuffer = new ArrayList<Measurement>();

        if(time == 0)
            measurementList = ms.getAllMeasurements();
        else {
            measurementList = ms.getMeasurements(unit,time);
        }
        Gson gson = new Gson();

        for (Measurement m : measurementList) {
            toSendBuffer.add(m);
            if(measurementList.indexOf(m) != measurementList.size()-1) {
                if (toSendBuffer.size() >= 10) {
                    ttn.getPusher().trigger("chart", "chartData", gson.toJson(Collections.singletonMap("data", toSendBuffer)));
                    System.out.println("Pushing: " + gson.toJson(Collections.singletonMap("data", toSendBuffer)));
                    toSendBuffer.clear();
                }
            }
            else
            {
                ttn.getPusher().trigger("chart", "chartData", gson.toJson(Collections.singletonMap("data", toSendBuffer)));
                System.out.println("Pushing: " + gson.toJson(Collections.singletonMap("data", toSendBuffer)));
                toSendBuffer.clear();
            }



        }
    }

    @RequestMapping(value = "/getdata/date/{date}", method = RequestMethod.GET, produces = {"application/json"})
    @CrossOrigin
    public void getDateData(@PathVariable String date) throws ParseException {

        List<Measurement> measurementList;
        List<Measurement> toSendBuffer = new ArrayList<Measurement>();
        System.out.println("Fetching measurements for:"+date);
        measurementList = ms.getMeasurementsByDate(date);
        Gson gson = new Gson();

        for (Measurement m : measurementList) {
            toSendBuffer.add(m);
            if(measurementList.indexOf(m) != measurementList.size()-1) {
                if (toSendBuffer.size() >= 10) {
                    ttn.getPusher().trigger("chart", "chartData", gson.toJson(Collections.singletonMap("data", toSendBuffer)));
                    System.out.println("Pushing: " + gson.toJson(Collections.singletonMap("data", toSendBuffer)));
                    toSendBuffer.clear();

                }
            }
            else
            {
                ttn.getPusher().trigger("chart", "chartData", gson.toJson(Collections.singletonMap("data", toSendBuffer)));
                System.out.println("Pushing: " + gson.toJson(Collections.singletonMap("data", toSendBuffer)));
                toSendBuffer.clear();
            }
        }
    }

    @RequestMapping(value = "/changeTime", method = RequestMethod.POST)
    @CrossOrigin
    public void changeTime(@RequestBody String str) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(str);
            String time = objectMapper.convertValue(node.get("time"), String.class);
            String processId = objectMapper.convertValue(node.get("processId"), String.class);
            System.out.println("Trying to schedule changetime downlink: " + time + " - " + processId);
            System.out.println(ttn.scheduleDownlink(processId, APIController.DOWNLINK_TYPE.INTEROGATION_TIME, time));
            System.out.println("Done");
        } catch (IOException e) {
            System.out.printf(e.toString());
        }
    }

    @RequestMapping(value = "/changePosition", method = RequestMethod.POST)
    @CrossOrigin
    public void changePosition(@RequestBody String str) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(str);
            String position = objectMapper.convertValue(node.get("position"), String.class);
            String processId = objectMapper.convertValue(node.get("processId"), String.class);
            System.out.println("Trying to schedule change position downlink: " + position + " - " + processId);
            System.out.println(ttn.scheduleDownlink(processId, APIController.DOWNLINK_TYPE.POSITION, position));
            System.out.println("Done");
        } catch (IOException e) {
            System.out.printf(e.toString());
        }
    }

    @RequestMapping(value = "/changeState", method = RequestMethod.POST)
    @CrossOrigin
    public void changeState(@RequestBody String str) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(str);
            String state = objectMapper.convertValue(node.get("state"), String.class);
            String processId = objectMapper.convertValue(node.get("processId"), String.class);
            System.out.println("Trying to schedule change state downlink: " + state + " - " + processId);
            System.out.println(ttn.scheduleDownlink(processId, APIController.DOWNLINK_TYPE.STATE, state));
            System.out.println("Done");
        } catch (IOException e) {
            System.out.printf(e.toString());
        }
    }

    @RequestMapping(value = "/getSolarData/{latitude}/{longitude}", method=RequestMethod.GET, produces = {"application/json"})
    @CrossOrigin
    public String getSolarData(@PathVariable Double latitude, @PathVariable Double longitude){

        return ms.getSolarInsulation(latitude,longitude);
    }


}
