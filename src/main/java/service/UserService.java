package service;

import dao.UserDAO;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public User getOrCreateUser(String name) throws SQLException {
        User user;
        if(userDAO.existsByName(name)){
            user = userDAO.findByName(name);
        } else {
            user = new User(name, userDAO.saveUser(name));
        }
        return user;
    }
}
