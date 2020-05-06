package view;

import controller.JDBCService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.MergedInfo;
import model.Order;
import model.User;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private JDBCService jdbcService = JDBCService.instance();

    @FXML
    public ListView<User> usersListView;

    @FXML
    public ListView<Order> ordersListView;

    @FXML
    public TableView<MergedInfo> fullInfoTableView;

    @FXML
    public TextField newUserNameTextField;

    @FXML
    public Button addNewUserButton;

    @FXML
    public TextField newOrderNameTextField;

    @FXML
    public Button addNewOrderButton;

    @FXML
    public Button removeUserButton;

    @FXML
    public Button removeOrderButton;

    @FXML
    public Button showFullInfoButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initUsers();
        initOrders();
        initFullInfo();
    }

    private void initFullInfo() {
        showFullInfoButton.setOnAction(actionEvent -> {
            List<MergedInfo> rows = jdbcService.getFullInfo();

            fullInfoTableView.setItems(
                    FXCollections.observableArrayList(
                            rows
                    )
            );
        });

        TableColumn<MergedInfo, Long> orderIdColumn = new TableColumn<>("Order id");
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        fullInfoTableView.getColumns().add(orderIdColumn);

        TableColumn<MergedInfo, String> orderInfoColumn = new TableColumn<>("Order name");
        orderInfoColumn.setCellValueFactory(new PropertyValueFactory<>("orderInfo"));
        fullInfoTableView.getColumns().add(orderInfoColumn);

        TableColumn<MergedInfo, String> orderRegDateColumn = new TableColumn<>("Order reg date");
        orderRegDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderRegDate"));
        fullInfoTableView.getColumns().add(orderRegDateColumn);

        TableColumn<MergedInfo, Long> userIdColumn = new TableColumn<>("User id");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        fullInfoTableView.getColumns().add(userIdColumn);

        TableColumn<MergedInfo, String> userNameColumn = new TableColumn<>("User name");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        fullInfoTableView.getColumns().add(userNameColumn);

        TableColumn<MergedInfo, String> userRegDateColumn = new TableColumn<>("User reg date");
        userRegDateColumn.setCellValueFactory(new PropertyValueFactory<>("userRegDate"));
        fullInfoTableView.getColumns().add(userRegDateColumn);

    }

    private User getSelectedUser() {
        return usersListView.getSelectionModel().getSelectedItem();
    }

    private void initUsersListItems() {
        usersListView.setItems(
                FXCollections.observableArrayList(
                        jdbcService.getAllUsers()
                )
        );
    }

    private void initUsers() {
        usersListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldUser, newUser) -> {
                    if (oldUser == newUser) return;
                    if (null == newUser) return;

                    initOrdersListItems(newUser);
                }
        );

        addNewUserButton.setOnAction(actionEvent -> {
            String newUserName = newUserNameTextField.getText();
            if (newUserName.isEmpty()) return;

            jdbcService.addUser(newUserName);

            initUsersListItems();

            usersListView.getSelectionModel().selectLast();
        });

        removeUserButton.setOnAction(actionEvent -> {
            User selectedUser = getSelectedUser();
            if (null == selectedUser) return;

            boolean needRemove = true;

            List<Order> orders = jdbcService.getAllOrdersOf(selectedUser);
            for (Order order : orders) {
                if (!askRemove(order)) {
                    needRemove = false;
                    break;
                }
            }

            if (!needRemove) return;

            int selectedUserIndex = usersListView.getSelectionModel().getSelectedIndex();

            jdbcService.removeUser(selectedUser);

            initUsersListItems();
            if (selectedUserIndex < usersListView.getItems().size()) {
                usersListView.getSelectionModel().select(selectedUserIndex);
            } else if (!usersListView.getItems().isEmpty()) {
                usersListView.getSelectionModel().selectLast();
            }
        });

        initUsersListItems();
    }

    private boolean askRemove(Order order) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удалить заказ?");
        alert.setHeaderText("Вы уверены, что хотите удалить заказ?");

        alert.setContentText(String.format(
                "%s - %s (дата заказа %s)?",
                order.owner.toString(), order.info, order.registrationDate
                )
        );

        Optional<ButtonType> option = alert.showAndWait();
        return option.isPresent() && option.get() == ButtonType.OK;
    }

    private void initOrdersListItems(User user) {
        List<Order> userOrders = jdbcService.getAllOrdersOf(user);
        ordersListView.setItems(
                FXCollections.observableArrayList(
                        userOrders
                )
        );
    }

    private void initOrders() {
        addNewOrderButton.setOnAction(actionEvent -> {
            String newOrderName = newOrderNameTextField.getText();
            if (newOrderName.isEmpty()) return;

            User owner = getSelectedUser();
            if (null == owner) return;

            jdbcService.addOrder(owner, newOrderName);

            initOrdersListItems(owner);

            ordersListView.getSelectionModel().selectLast();
        });

        removeOrderButton.setOnAction(actionEvent -> {
            Order selectedOrder = ordersListView.getSelectionModel().getSelectedItem();
            if (null == selectedOrder) return;

            int selectedOrderIndex = ordersListView.getSelectionModel().getSelectedIndex();

            jdbcService.removeOrder(selectedOrder);

            initOrdersListItems(
                    getSelectedUser()
            );

            if (selectedOrderIndex < ordersListView.getItems().size()) {
                ordersListView.getSelectionModel().select(selectedOrderIndex);
            } else if (!ordersListView.getItems().isEmpty()) {
                ordersListView.getSelectionModel().selectLast();
            }
        });
    }
}
