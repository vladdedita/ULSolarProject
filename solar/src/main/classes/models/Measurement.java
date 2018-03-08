package main.classes.models;


import javax.persistence.*;

@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="measurement")
    private String value;

    @Column(name="date")
    private String date;

    protected Measurement(){}

    public Measurement(String value, String date) {
        this.value = value;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
