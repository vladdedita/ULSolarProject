package main.classes.device;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import main.classes.controllers.APIController;
import main.classes.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class DeviceController {

    APIController ttn;

    @Autowired
    UserService us;
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
            JsonObject obj = new JsonObject();
            if(time == null || time.isEmpty())
            {
                obj.addProperty("error", "empty time");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
            }
            if(processId == null || processId.isEmpty())
            {
                obj.addProperty("error", "empty processId");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
            }

            System.out.println("Trying to schedule changetime downlink: " + time + " - " + processId);
            System.out.println(ttn.scheduleDownlink(token, processId, APIController.DOWNLINK_TYPE.INTEROGATION_TIME, time));
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
            JsonObject obj = new JsonObject();
            if(position == null || position.isEmpty())
            {
                obj.addProperty("error", "empty position");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
            }
            if(processId == null || processId.isEmpty())
            {
                obj.addProperty("error", "empty processId");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
            }
            System.out.println("Trying to schedule change position downlink: " + position + " - " + processId);
            System.out.println(ttn.scheduleDownlink(token, processId, APIController.DOWNLINK_TYPE.POSITION, position));
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
            JsonObject obj = new JsonObject();
            if(state == null || state.isEmpty())
            {
                obj.addProperty("error", "empty state");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
            }
            if(processId == null || processId.isEmpty())
            {
                obj.addProperty("error", "empty processId");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
            }
            System.out.println("Trying to schedule change state downlink: " + state + " - " + processId);
            System.out.println(ttn.scheduleDownlink(token, processId, APIController.DOWNLINK_TYPE.STATE, state));
            System.out.println("Done");
            return new ResponseEntity(HttpStatus.OK);
        } catch (IOException e) {
            System.out.printf(e.toString());
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
