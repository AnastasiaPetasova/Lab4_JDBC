package model;

import java.sql.Date;

public class User extends DatabaseModel {

    public User(String info, Date registrationDate) {
        super(info, registrationDate);
    }

    public User(String info, Date registrationDate, long id) {
        super(info, registrationDate, id);
    }
}
