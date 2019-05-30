package authentication;

import entities.User;
import io.javalin.Context;
import io.javalin.Javalin;
import managers.UserDbManager;

public class UserAuthentication {

    public void authenticateUser(String email, String password){
        User user = new UserDbManager().getUserByEmail(email);
        boolean isAuthenticated = user.getPassword().equals(password);
        if(isAuthenticated){
            System.out.println("ok");
        }
    }

    public User signUpUser(User user){
        return new UserDbManager().saveUser(user);
    }

    public Javalin startListener(){
        return Javalin.create()
                .enableStaticFiles("/public")
                .start(7777);
    }

    public void login(Context context, Javalin app){

        app.post("/login", ctx -> {
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");
            this.authenticateUser(email, password);
            ctx.html("You're logged in");
        });
    }

    public static void main(String [] s){
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.startListener();
    }


}
