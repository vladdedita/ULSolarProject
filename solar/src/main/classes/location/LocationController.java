package main.classes.location;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.classes.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class LocationController {

    @Autowired
    UserService us;

    @Autowired
    LocationService ls;



    @RequestMapping(value = "/setlocation")
    public ResponseEntity setLocation(@RequestHeader(value="Authorization") String token, @RequestBody String str)
    {
        if(!us.isAuthorized(token))
        {
            JsonObject obj=new JsonObject();
            obj.addProperty("error","Token not authorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(obj.toString());
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(str);
            String lon = objectMapper.convertValue(node.get("lon"), String.class);
            String lat = objectMapper.convertValue(node.get("lat"), String.class);
            Boolean isPublic = objectMapper.convertValue(node.get("public"), Boolean.class);
            JsonObject obj = new JsonObject();
            if(lon == null || lon.isEmpty())
            {
                obj.addProperty("error", "empty lon");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
            }
            if(lat == null || lat.isEmpty())
            {
                obj.addProperty("error", "empty lat");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
            }
            System.out.print("Setting location: Lat:" + lat + " Lon:" + lon + "for token:" + token + "...");

            if (ls.addLocation(token, lat, lon, isPublic))
            {


                return new ResponseEntity(HttpStatus.OK);
            }
            else
            {

                obj.addProperty("error", "Could not register location");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj.toString());
            }


        }
        catch(IOException e){
            System.out.println(e.toString());
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getlocations")
    public ResponseEntity setLocation(@RequestHeader(value="Authorization") String token)
    {
        if(!us.isAuthorized(token))
        {
            JsonObject obj=new JsonObject();
            obj.addProperty("error","Token not authorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(obj.toString());
        }
        if(ls.getLocations(token) != null)
        {
            JsonObject obj = new JsonObject();
            return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(ls.getLocations(token)));
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
