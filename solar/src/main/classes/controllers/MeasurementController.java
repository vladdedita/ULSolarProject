package main.classes.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import main.classes.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
public class MeasurementController {

    @Autowired
    MeasurementService ms;

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


        Integer authorized = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(str);

        String name = objectMapper.convertValue(node.get("name"),String.class);
        String accessKey = objectMapper.convertValue(node.get("key"),String.class);


        if(name.isEmpty()){
            System.out.println("authorize: empty name");
            return authorized;

        }

        if(accessKey.isEmpty()) {
            System.out.println("authorize: empty key");
            return authorized;

        }

        StringBuilder sb=new StringBuilder("https://");
        sb.append(name);
        sb.append(".data.thethingsnetwork.org/api/v2/devices");
        URL url=null;
        try{
            url = new URL (sb.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Authorization","key "+accessKey);
            int status = con.getResponseCode();
            if(status == 200) {
                authorized = 1;
                return authorized;
            }
            else return authorized;


        }
        catch (MalformedURLException e)
        {
            System.out.println(e.toString());
            return authorized;

        }
        catch (IOException urlError)
        {
            System.out.println("Could not open connection" + urlError.toString());
            return authorized;

        }


    }


}
