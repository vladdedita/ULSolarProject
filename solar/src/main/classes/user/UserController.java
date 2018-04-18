package main.classes.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.classes.api.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    APIService ttn;
    @Autowired
    UserService us;

    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = {"application/json"})
    @CrossOrigin
    public String authorize(@RequestBody String str) throws IOException {

        //Decoding JSON Request Body
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(str);
        //
        String appID = objectMapper.convertValue(node.get("appID"), String.class);
        String devID = objectMapper.convertValue(node.get("devID"), String.class);
        String accessKey = objectMapper.convertValue(node.get("appKey"), String.class);

        if (appID == null || appID.isEmpty()) {
            System.out.println("authorize: empty appID");
            return null;
        }
        if (accessKey == null || accessKey.isEmpty()) {
            System.out.println("authorize: empty key");
            return null;
        }
        if (devID == null || devID.isEmpty()) {
            System.out.println("authorize: empty devID");
            return null;
        }
        //Calling authorize method for provided credentials
        if (ttn.authorize(appID, accessKey, devID))
            {
               if(!us.exists(appID, accessKey, devID))
               {
                   us.add(appID, accessKey, devID);
               }
                us.generateToken(appID, accessKey, devID);
                return us.getToken(appID, accessKey, devID);
            }
        return null;
    }

}
