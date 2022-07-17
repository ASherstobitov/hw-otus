package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.service.api.DBServiceClient;
import ru.otus.crm.model.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;


public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Long id = extractIdFromRequest(request);

        String jsonResponse = "";

        if (id == null)  {
            List<Client> clientList = dbServiceClient.findAll();
            jsonResponse = gson.toJson(clientList);
        } else {
            Client client = dbServiceClient.getClient(id).orElse(null);
            jsonResponse = gson.toJson(client);
        }

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(jsonResponse);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (BufferedReader reader = req.getReader()) {
            Client client = gson.fromJson(reader, Client.class);

            Client tmpClient = dbServiceClient.saveClient(client);
            System.out.println(tmpClient);
            resp.getOutputStream().print(gson.toJson(tmpClient));
        }
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
