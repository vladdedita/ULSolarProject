package main.classes.user;



import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "appName")
    private String appName;

    @Column(name = "appKey")
    private String appKey;

    @Column(name = "authorized")
    private Boolean authorized;

    @Column(name="token")
    private String token;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name="born")
    private Date born;

    public void setToken(String token) {
        this.token = token;
        this.born=new Date();


    }

    User() {

    }
    User(String appName, String appKey)
    {
        this.appKey=appKey;
        this.appName=appName;
    }

    public Boolean isAuthorized() {
        return authorized;
    }

    public String getToken() {
        return token;
    }

    public Date getBorn() {
        return born;
    }

    public Integer getId() {
        return id;
    }

    public void setAuthorized(){
            this.authorized = true;
    }

    public void unsetAuthorize() {
        this.authorized = false;
    }

}
