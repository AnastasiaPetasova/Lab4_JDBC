package model;

import java.sql.Date;

public class Order extends DatabaseModel {

    public User owner;

    public Order(String info, Date registrationDate, User owner) {
        super(info, registrationDate);
        this.owner = owner;
    }

    public Order(String info, Date registrationDate, long id, User owner) {
        super(info, registrationDate, id);
        this.owner = owner;
    }
}
