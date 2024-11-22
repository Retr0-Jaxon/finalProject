package org.example.finalproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    @Autowired
    private FileUtil fileUtil;

    private final AtomicLong idCounter = new AtomicLong(1);  // ID生成器

    public User register(User user) {
        List<User> users = fileUtil.readUsers();

        // 自动生成唯一ID并设置到用户对象
//        user.setId(idCounter.getAndIncrement());

        users.add(user);
        fileUtil.writeUsers(users);  // 保存用户列表到JSON文件
        return user;
    }

    public boolean validateUser(String username, String password) {
        List<User> users = fileUtil.readUsers();
        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
        return user.isPresent();
    }

    public List<User> getAllUsers() {
        return fileUtil.readUsers();  // 读取并返回所有用户
    }
}
