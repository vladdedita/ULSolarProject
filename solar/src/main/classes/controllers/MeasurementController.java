package main.classes.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import main.classes.models.Measurement;
import main.classes.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class MeasurementController {

    @Autowired
    MeasurementService ms;

    @Autowired
    APIController ttn;



    @RequestMapping(value ="/measurements", method= RequestMethod.GET, produces={"application/json"} )
    @CrossOrigin
    public @ResponseBody String getMeasurements(){

        Gson gson = new Gson();
        String jsonString = gson.toJson(ms.getAllMeasurements());
        System.out.println(jsonString);

        return jsonString;

    }

    @RequestMapping(value="/auth", method=RequestMethod.POST,produces={"application/json"})
    @CrossOrigin
    public Integer authorize(@RequestBody String str) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(str);

        String name = objectMapper.convertValue(node.get("name"),String.class);
        String accessKey = objectMapper.convertValue(node.get("key"),String.class);


        if(name.isEmpty()){
            System.out.println("authorize: empty name");
            return 0;

        }

        if(accessKey.isEmpty()) {
            System.out.println("authorize: empty key");
            return 0;

        }

        if(ttn.authorize(name,accessKey,"ded-things-uno"))
            return 1;

        return 0;
    }


    /**
     *
     *
     * @return list of registered data
     */
    @RequestMapping(value="/getdata", method=RequestMethod.GET,produces={"application/json"})
    @CrossOrigin
    public void getData() {

//        if(period == 0)
//            return ms.getAllMeasurements();
//        else {
//            return ms.getMeasurements(period);
//        }
        Gson gson = new Gson();
          List<Measurement> measurementList = ms.getAllMeasurements();
        List<Measurement> toSendBuffer = new ArrayList<>();
        for (Measurement m: measurementList) {
            toSendBuffer.add(m);
            if(toSendBuffer.size() == 10)
            {
                ttn.getPusher().trigger("chart", "chartData", gson.toJson(Collections.singletonMap("data",toSendBuffer)));
                System.out.println("Pushing" + gson.toJson(Collections.singletonMap("data",toSendBuffer)));

                toSendBuffer.clear();
            }

        }


       // return ms.getAllMeasurements();

    }

    @RequestMapping(value = "/changeTime",method = RequestMethod.POST)
    @CrossOrigin
    public void changeTime(@RequestBody String str)  {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(str);
            String time = objectMapper.convertValue(node.get("time"), String.class);
            String processId = objectMapper.convertValue(node.get("processId"), String.class);
            System.out.println("Trying to schedule changetime downlink: " + time + " - " + processId);
            System.out.println(ttn.scheduleDownlink(processId, APIController.DOWNLINK_TYPE.INTEROGATION_TIME, time.toString()));
            System.out.println("Done");
        }
        catch(IOException e){
            System.out.printf(e.toString());
        }
    }



}
