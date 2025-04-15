package com.ecomusic.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.ecomusic.model.User;
import com.ecomusic.model.dao.UserDAO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO(); // make sure this DAO works first
    }

    // GET: fetch list or user by id
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            User user = userDAO.getUserById(id);
            if (user != null) {
                out.println("<h3>User:</h3>");
                out.println("ID: " + user.getUserId() + "<br>");
                out.println("Name: " + user.getName() + "<br>");
                out.println("Email: " + user.getEmail() + "<br>");
                out.println("User Type: " + user.getUserType());
            } else {
                out.println("User not found.");
            }
        } else {
            List<User> userList = userDAO.getAllUsers();
            out.println("<h3>All Users:</h3>");
            for (User u : userList) {
                out.println("ID: " + u.getUserId() + ", Name: " + u.getName() + ", Email: " + u.getEmail() + "<br>");
            }
        }
    }

    // POST: create new user
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("user_type");

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserType(userType);

        boolean success = userDAO.insertUser(user);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (success) {
            out.println("User added successfully!");
        } else {
            out.println("Failed to add user.");
        }
    }
}
