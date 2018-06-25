package main.classes.location;

import javax.persistence.*;

@Entity
@Table(name="location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "lon")
    private String lon;

    @Column(name = "lat")
    private String lat;

    @Column(name="public")
    private Boolean isPublic;

    public Boolean isPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Location(){}
    public Location( String lat,String lon) {
        this.lon = lon;
        this.lat = lat;
    }
}
