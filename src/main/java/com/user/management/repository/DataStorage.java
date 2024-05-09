package com.user.management.repository;

import com.user.management.dao.User;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataStorage {

    public static List<User> users = new ArrayList<>();
}
