package com.example.companyusersystem_db;

public record User(int id, String name, int company_id, int score) {
    public static User[] changeToUser(Person ps){
        CompanyService cs = new CompanyService();
        User[] user = {new User(ps.idProperty().intValue(),
                ps.nameProperty().get(),
                cs.findByName(ps.companyProperty().get()),
                ps.scoreProperty().intValue())};
        return user;
    }

    public static User[] changeToUserNoId(Person ps){
        CompanyService cs = new CompanyService();
        User[] user = {new User(1,
                ps.nameProperty().get(),
                cs.findByName(ps.companyProperty().get()),
                ps.scoreProperty().intValue())};
        return user;
    }

}
