package ru.otus.servlet;

import com.google.gson.Gson;
import ru.otus.model.User;
import ru.otus.dao.UserDao;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class UsersApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final UserDao userDao;
    private final Gson gson;

    public UsersApiServlet(UserDao userDao, Gson gson) {
        this.userDao = userDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Long id = extractIdFromRequest(request);

        String jsonResponse = "";

        if (id == null)  {
            List<User> userList = userDao.getUsers();
            jsonResponse = gson.toJson(userList);
        } else {
            User user = userDao.findById(id).orElse(null);
            jsonResponse = gson.toJson(user);
        }

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(jsonResponse);
    }

    private Long extractIdFromRequest(HttpServletRequest request) {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            return null;
        }

        String[] path = pathInfo.split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

}
