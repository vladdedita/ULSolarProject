package main.classes.measurement;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.classes.api.APIService;
import main.classes.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller for Measurements
 */
@RestController
public class MeasurementController {

    /**
     * Instantiating the service responsible for Measurements
     */
    @Autowired
     private MeasurementService ms;
    @Autowired
    private UserService us;
    @Autowired
    private APIService ttn;

//    @RequestMapping(value = "/isauthorized", method = RequestMethod.POST, produces = {"application/json"})
//    @CrossOrigin
//    public Boolean isAuthorized(@RequestBody String str) throws IOException {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode node = objectMapper.readTree(str);
//
//       return ttn.getAuthorized();
//    }


    /**
     *
     * @return JSON encoded object with the average of all entries in the database and the last one
     */
    @RequestMapping(value = "/measurements", method = RequestMethod.POST, produces = {"application/json"})
    @CrossOrigin
    public @ResponseBody
    ResponseEntity getMeasurements(@RequestHeader(value="Authorization") String token, @RequestBody String body) throws IOException{
        JsonObject obj=new JsonObject();

        if(!us.isAuthorized(token))
        {
            obj.addProperty("error","Token not authorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(obj.toString());
        }

        obj.addProperty("average",ms.getAverage());
        obj.addProperty("last",ms.getLast().getPower());
        return ResponseEntity.status(HttpStatus.OK).body(obj.toString());
    }

    /**
     *
     * @param str POST Request Body containing the app id, access key and device name
     * @return  Authorization Yes/No (0/1) - return Integer because JavaScript has a hard time decoding booleans
     * @throws IOException
     */

    /**
     * @return list of registered data
     */



    @RequestMapping(value = "/getdata/{unit}/{time}", method = RequestMethod.POST, produces = {"application/json"})
    @CrossOrigin
    public ResponseEntity getData(@RequestHeader(value = "Authorization") String token, @PathVariable String unit, @PathVariable Integer time) {
        JsonObject obj=new JsonObject();
        if(!us.isAuthorized(token))
        {

            obj.addProperty("error","Token not authorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(obj.toString());
        }


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
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/getdata/date/{date}", method = RequestMethod.POST, produces = {"application/json"})
    @CrossOrigin
    public ResponseEntity getDateData(@RequestHeader(value="Authorization") String token, @PathVariable String date) throws ParseException {
        JsonObject obj=new JsonObject();
        if(!us.isAuthorized(token))
        {
            obj.addProperty("error","Token not authorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(obj.toString());
        }
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
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/changeTime", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity changeTime(@RequestHeader(value="Authorization") String token, @RequestBody String str) {
        if(!us.isAuthorized(token))
        {
            JsonObject obj=new JsonObject();
            obj.addProperty("error","Token not authorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(obj.toString());
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(str);
            String time = objectMapper.convertValue(node.get("time"), String.class);
            String processId = objectMapper.convertValue(node.get("processId"), String.class);
            System.out.println("Trying to schedule changetime downlink: " + time + " - " + processId);
            System.out.println(ttn.scheduleDownlink(processId, APIService.DOWNLINK_TYPE.INTEROGATION_TIME, time));
            System.out.println("Done");
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            System.out.printf(e.toString());
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/changePosition", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity changePosition(@RequestHeader(value="Authorization") String token,@RequestBody String str) {
        if(!us.isAuthorized(token))
        {
            JsonObject obj=new JsonObject();
            obj.addProperty("error","Token not authorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(obj.toString());
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(str);
            String position = objectMapper.convertValue(node.get("position"), String.class);
            String processId = objectMapper.convertValue(node.get("processId"), String.class);
            System.out.println("Trying to schedule change position downlink: " + position + " - " + processId);
            System.out.println(ttn.scheduleDownlink(processId, APIService.DOWNLINK_TYPE.POSITION, position));
            System.out.println("Done");
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            System.out.printf(e.toString());
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/changeState", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity changeState(@RequestHeader(value="Authorization") String token,@RequestBody String str) {
        if(!us.isAuthorized(token))
        {
            JsonObject obj=new JsonObject();
            obj.addProperty("error","Token not authorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(obj.toString());
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(str);
            String state = objectMapper.convertValue(node.get("state"), String.class);
            String processId = objectMapper.convertValue(node.get("processId"), String.class);
            System.out.println("Trying to schedule change state downlink: " + state + " - " + processId);
            System.out.println(ttn.scheduleDownlink(processId, APIService.DOWNLINK_TYPE.STATE, state));
            System.out.println("Done");
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            System.out.printf(e.toString());
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

//    @RequestMapping(value = "/getSolarData/{latitude}/{longitude}", method=RequestMethod.GET, produces = {"application/json"})
//    @CrossOrigin
//    public ResponseEntity getSolarData(@RequestHeader(value="Authorization") String token, @PathVariable Double latitude, @PathVariable Double longitude){
//        JsonObject obj=new JsonObject();
//        if(!us.isAuthorized(token))
//        {
//
//            obj.addProperty("error","Token not authorized");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(obj.toString());
//        }
//
//        ms.getSolarInsulation(latitude,longitude);
//
//    }


}
