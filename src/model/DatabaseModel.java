package model;

import java.sql.Date;

public class DatabaseModel {

    public long id;
    public String info;
    public Date registrationDate;

    DatabaseModel(String info, Date registrationDate) {
        this.info = info;
        this.registrationDate = registrationDate;
    }

    DatabaseModel(String info, Date registrationDate, long id) {
        this(info, registrationDate);
        this.id = id;
    }

    @Override
    public String toString() {
        return info;
    }
}
