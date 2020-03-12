package push.service;

import org.glassfish.jersey.message.internal.MediaTypes;
import push.bean.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;

//127.0.0.1/api/account/...
@Path("/account")
public class AccountService  {

    //127.0.0.1/api/account/login
    @GET
    @Path("/login")
    public String get(){
        return "You get the login.";
    }

    //127.0.0.1/api/account/login
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User pist(){
        User user = new User();
        user.setName("Tom");
        user.setSex(2);
        return user;
    }

}
