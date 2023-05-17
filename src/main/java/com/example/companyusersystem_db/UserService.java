package com.example.companyusersystem_db;

import javafx.beans.property.IntegerProperty;

import java.util.List;

public class UserService {
    UserDao ud;

    public UserService() {
        DBUtil db = new DBUtil();
        UserDao ud = new UserDao(db.getConnection());
        this.ud = ud;
    }

    public List<AllTable> findAllTable() {
        return this.ud.findAllTable();
    }

    public List<User> findAll() {
        return this.ud.findAll();
    }

    public User findById(int id) {
        return this.ud.findById(id);
    }

    public List<User> findByName(String str) {
        return this.ud.findByName(str);
    }

    public int insert(User[] ur) {
        int counts = this.ud.insert(ur);
        return counts;
    }

    public int update(User[] ur) {
        int counts = this.ud.update(ur);
        return counts;
    }

    public int count(){
        return ud.count();
    }

    public int delete(IntegerProperty id) {
        int counts = this.ud.delete(id.intValue());
        return counts;
    }
}
