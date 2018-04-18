package main.classes.measurement;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "solar")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "power")
    private String power;

    @Column(name = "time")
    private @DateTimeFormat(pattern = "2018-03-27T13:52:39.057164Z yyyy-mm-ddThh:mm:ss")
    Date time;

    @Column(name = "direction")
    private Integer direction;

    protected Measurement() {

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
}
