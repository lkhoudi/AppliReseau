import entities.User;
import entities.UserDbManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class UserDbTest {

    @Test
    public void saveUserToDb(){
        User u = new User("user@gmail.com", "lydia", "khoudi","xxx",true,"lk.jpg");
        User savedUser = new UserDbManager().saveUser(u);
        Assertions.assertThat(savedUser.getEmail().equals(u.getEmail())).isTrue();
        Assertions.assertThat(savedUser.getFirstname().equals(u.getFirstname())).isTrue();
        Assertions.assertThat(savedUser.getLastname().equals(u.getLastname())).isTrue();
    }

    @Test
    public void getUserToDb(){
        User u = new User("user@gmail.com", "lydia", "khoudi","xxx",true, "lk.jpg");
        User savedUser = new UserDbManager().getUserByEmail(u.getEmail());
        Assertions.assertThat(savedUser.getEmail().equals(u.getEmail())).isTrue();
        Assertions.assertThat(savedUser.getFirstname().equals(u.getFirstname())).isTrue();
        Assertions.assertThat(savedUser.getLastname().equals(u.getLastname())).isTrue();
    }
}
