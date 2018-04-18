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

    @Column(name = "location")
    private String location;

    @Column(name ="userId")
    private Integer userId;

   public Device() {
   }
   public Device(String name, Integer userId)
   {
       this.name=name;
   }

}
