package rest_spring_cxf;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xh.zhi on 2018-8-29.
 */
@Service
public class UserServiceImpl implements UserService{
    @Override
    public List<User> getUsers() {
        return Storage.users;
    }

    @Override
    public Response delete(int id) {
        Response response=new Response();
        response.setCode("00");
        response.setMsg("succes");
        return response;
    }

    @Override
    public User getUser(int id) {
        return Storage.users.get(id);
    }

    @Override
    public Response insert(User user) {
        return null;
    }

    @Override
    public Response update(User user) {
        return null;
    }
}
