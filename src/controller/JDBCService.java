package controller;

import model.MergedInfo;
import model.Order;
import model.User;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JDBCService {

    private static final JDBCService instance;

    public static JDBCService instance() {
        return instance;
    }

    private static final String dbURL = "jdbc:postgresql://127.0.0.1:5432/shop";
    private static final String userName = "postgres";
    private static final String password = "postgres";

    static {
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection(dbURL, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        instance = new JDBCService(connection);
    }

    private Connection connection;

    private PreparedStatement removeUserStatement, removeOrderStatement;

    private JDBCService(Connection connection) {
        this.connection = connection;

        try {
            this.removeUserStatement = connection.prepareStatement(
                    "DELETE FROM public.user WHERE id = ?"
            );

            this.removeOrderStatement = connection.prepareStatement(
                    "DELETE FROM public.order WHERE id = ?"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ResultSet execute(String sql) {
        try {
            Statement statement = connection.createStatement();

            boolean resultReturned = statement.execute(sql);

            if (resultReturned) return statement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT id, name, reg_date FROM public.user ORDER BY id ASC";

        ResultSet resultSet = execute(sql);
        if (null != resultSet) {
            try {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    Date regDate = resultSet.getDate("reg_date");

                    User user = new User(name, regDate, id);
                    users.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return users;
    }

    public List<Order> getAllOrdersOf(User owner) {
        List<Order> orders = new ArrayList<>();

        String sql = String.format(
                "SELECT id, info, reg_date, owner_id FROM public.order WHERE owner_id = %d ORDER BY id ASC",
                owner.id
        );

        ResultSet resultSet = execute(sql);
        if (null != resultSet) {
            try {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String info = resultSet.getString("info");
                    Date regDate = resultSet.getDate("reg_date");

                    Order order = new Order(info, regDate, id, owner);
                    orders.add(order);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return orders;
    }

    public List<MergedInfo> getFullInfo() {
        List<MergedInfo> rows = new ArrayList<>();

        String sql = "SELECT public.user.id, public.user.name, public.user.reg_date, public.order.id, public.order.info, public.order.reg_date FROM public.order INNER JOIN public.user ON public.order.owner_id = public.user.id";

        ResultSet resultSet = execute(sql);
        if (null != resultSet) {
            try {
                while (resultSet.next()) {
                    long userId = resultSet.getLong(1);
                    String userName = resultSet.getString(2);
                    Date userRegDate = resultSet.getDate(3);

                    long orderId = resultSet.getLong(4);
                    String orderInfo = resultSet.getString(5);
                    Date orderRegDate = resultSet.getDate(6);

                    MergedInfo row = new MergedInfo(userId, userName, userRegDate, orderId, orderInfo, orderRegDate);
                    rows.add(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rows;
    }

    private Date currentDate() {
        return new Date(new java.util.Date().getTime());
    }

    public void addUser(String name) {
        User user = new User(name, currentDate());

        String sql = String.format(
            "INSERT INTO public.user(name, reg_date) VALUES ('%s', '%s')",
                user.info, new SimpleDateFormat("dd-MM-yyyy").format(user.registrationDate)
        );

        execute(sql);
    }

    public void addOrder(User owner, String info) {
        Order order = new Order(info, currentDate(), owner);

        String sql = String.format(
                "INSERT INTO public.order(info, reg_date, owner_id) VALUES ('%s', '%s', %d)",
                order.info, new SimpleDateFormat("dd-MM-yyyy").format(order.registrationDate), owner.id
        );

        execute(sql);
    }

    public void removeUser(User user) {
        try {
            removeUserStatement.setLong(1, user.id);
            removeUserStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeOrder(Order order) {
        try {
            removeOrderStatement.setLong(1, order.id);
            removeOrderStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
