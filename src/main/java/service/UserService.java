package service;


import dao.UserDao;
import model.User;
import util.PasswordUtil;

public class UserService {

    private final UserDao userDao = new UserDao();

    public void register(User user) throws Exception {

        String hashedPassword = PasswordUtil.hash(user.getPassword());
        user.setPassword(hashedPassword);
        userDao.register(user);
    }

    public boolean login(String username, String password) throws Exception {

        String dbPassword = userDao.getPassword(username);
        return dbPassword != null && PasswordUtil.match(password, dbPassword);
    }

	}
