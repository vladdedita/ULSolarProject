package main.classes.measurement;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity //defines this as a table entity
@Table(name = "solar") //named solar
public class Measurement {

    @Id //specifices that this column represents an ID and enables autoincrementi
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "power")
    private String power;

    @Column(name = "time")
    private @DateTimeFormat(pattern = "2018-03-27T13:52:39.057164Z yyyy-mm-ddThh:mm:ss")
    Date time;

    @Column(name = "direction")
    private Integer direction;

    @Column(name = "deviceId")
    private Integer deviceId;

    @Column(name = "locationId")
    private Integer locationId;

    protected Measurement() {

    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getDirection() {
        return direction;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public Measurement(String power, Date time) {
        this.power = power;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", power='" + power + '\'' +
                ", time=" + time +
                ", direction=" + direction +
                ", deviceId=" + deviceId +
                ", locationId=" + locationId +
                '}';
    }
}
