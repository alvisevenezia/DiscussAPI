package fr.alvisevenezia.web.user;

import java.security.Key;

public class UserProfile {

    private String id;
    private Key key;
    private String name;

    public UserProfile(String id, Key key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
