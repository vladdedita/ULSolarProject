package main.classes.user;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import main.classes.controllers.APIController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    APIController ttn;
    @Autowired
    UserService us;

    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = {"application/json"})
    @CrossOrigin
    public ResponseEntity authorize(@RequestBody String str) throws IOException {

        //Decoding JSON Request Body
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(str);       //


        String appID = objectMapper.convertValue(node.get("appID"), String.class);
        String devID = objectMapper.convertValue(node.get("devID"), String.class);
        String accessKey = objectMapper.convertValue(node.get("appKey"), String.class);

        JsonObject jsonError = new JsonObject();
        if (appID == null || appID.isEmpty()) {
            System.out.println("authorize: empty appID");
            jsonError.addProperty("error", "empty appId");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonError.toString());
        }
        if (accessKey == null || accessKey.isEmpty()) {
            System.out.println("authorize: empty key");
            jsonError.addProperty("error", "empty key");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonError.toString());
        }
        if (devID == null || devID.isEmpty()) {
            System.out.println("authorize: empty devID");
            jsonError.addProperty("error", "empty devId");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonError.toString());
        }

        String token = us.authorize(appID,accessKey,devID);
        JsonObject obj=new JsonObject();
        if(token != null) {

            obj.addProperty("key", token);
            return ResponseEntity.status(HttpStatus.OK).body(obj.toString());
        }
           else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

}
