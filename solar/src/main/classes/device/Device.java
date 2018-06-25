package main.classes.device;

import javax.persistence.*;

@Entity
@Table(name="device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name ="userId")
    private Integer userId;

    @Column(name="locationId")
    private Integer locationId;


    public Device() {
   }
   public Device(String name, Integer userId)
   {
       this.name=name;
   }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
