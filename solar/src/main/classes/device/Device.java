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

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name ="userId")
    private Integer userId;

   public Device() {
   }
   public Device(String name, Integer userId)
   {
       this.name=name;
   }

    public Device(String name, String latitude, String longitude, Integer userId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }
}
