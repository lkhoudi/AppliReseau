package authentication;

import entities.User;
import managers.UserDbManager;

public class UserAuthentication {

    public Boolean authenticateUser(String email, String password){
        User user = new UserDbManager().getUserByEmail(email);
        return user.getPassword().equals(password);
    }

    public User signUpUser(User user){
        return new UserDbManager().saveUser(user);
    }
}
