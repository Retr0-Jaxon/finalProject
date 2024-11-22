package org.example.finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtil {
    private static final String FILE_PATH = "users.json";  // JSON文件路径
    private ObjectMapper objectMapper = new ObjectMapper();

    public List<User> readUsers() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();  // 如果文件不存在，返回空列表
            }
            return objectMapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();  // 读取失败返回空列表
        }
    }

    public void writeUsers(List<User> users) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), users);  // 将用户数据写入文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}