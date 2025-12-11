package hotelapp.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    protected String id;
    protected String username;
    protected String password;
    protected String name;

    public User(String id, String username, String password, String name){
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getId(){ return id; }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public String getName(){ return name; }

    public abstract String getRole();

    @Override
    public String toString(){
        return id + " - " + name + " (" + username + ")";
    }
}
