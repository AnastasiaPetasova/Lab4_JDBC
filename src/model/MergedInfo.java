package model;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MergedInfo {

    long userId;
    String userName;
    Date userRegDate;

    long orderId;
    String orderInfo;
    Date orderRegDate;

    public MergedInfo(long userId, String userName, Date userRegDate, long orderId, String orderInfo, Date orderRegDate) {
        this.userId = userId;
        this.userName = userName;
        this.userRegDate = userRegDate;
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.orderRegDate = orderRegDate;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserRegDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(userRegDate);
    }

    public long getOrderId() {
        return orderId;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public String getOrderRegDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(orderRegDate);
    }
}
