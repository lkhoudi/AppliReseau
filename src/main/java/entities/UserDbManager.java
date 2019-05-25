package entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDbManager {
    private static final String SQL_SERIALIZE_OBJECT = "INSERT INTO user_table(email, firstname, lastname, psw, active, avatar) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DESERIALIZE_OBJECT = "select id, email, firstname, lastname, psw, active, avatar from user_table WHERE email = ?";

    public User saveUser(User u){
        try(Connection connection = new DbConnectionManager().connectToDb()) {
            PreparedStatement pst = connection.prepareStatement(SQL_SERIALIZE_OBJECT);
                pst.setString(1, u.getEmail());
                pst.setString(2, u.getFirstname());
                pst.setString(3, u.getLastname());
                pst.setString(4, u.getPassword());
                pst.setBoolean(5, u.isActive());
                pst.setString(6, u.getAvatar());
                pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    public User getUserByEmail(String email){
        User user = null;
        try(Connection connection = new DbConnectionManager().connectToDb();
             PreparedStatement pst = connection.prepareStatement(SQL_DESERIALIZE_OBJECT)) {

            pst.setString(1, email);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()){
                String eml = resultSet.getString("email");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String psw = resultSet.getString("psw");
                String avatar = resultSet.getString("avatar");
                Boolean active = resultSet.getBoolean("active");
                user = new User(eml, firstname, lastname, psw, active, avatar);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(UserDbManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return user;
    }
}
