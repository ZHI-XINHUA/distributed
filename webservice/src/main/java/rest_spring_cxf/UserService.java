package rest_spring_cxf;

import javax.jws.WebService;
import javax.ws.rs.*;
import java.util.List;

@WebService
@Path(value = "/users/")
public interface UserService {


    @GET
    @Path("/")
    List<User> getUsers();

    @DELETE
    @Path("{id}")
    Response delete(@PathParam("id") int id);

    @GET
    @Path("{id}")
    User getUser(@PathParam("id") int id);

    @POST
    @Path("add")
    Response insert(User user);

    @PUT
    @Path("update")
    Response update(User user);
}
