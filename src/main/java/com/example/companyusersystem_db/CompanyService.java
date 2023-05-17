package com.example.companyusersystem_db;

import java.util.List;

public class CompanyService {
    CompanyDao cd;

    public CompanyService() {
        DBUtil db = new DBUtil();
        CompanyDao cd = new CompanyDao(db.getConnection());
        this.cd = cd;
    }

    public List<Company> findAll() {
        return this.cd.findAll();
    }

    public Company findById(int id) {
        return this.cd.findById(id);
    }

    public int findByName(String str) {
        return this.cd.findByName(str);
    }

    public int insert(Company[] cm) {
        int counts = this.cd.insert(cm);
        return counts;
    }

    public int update(Company[] cm) {
        int counts = this.cd.update(cm);
        return counts;
    }

    public int delete(int id) {
        int counts = this.cd.delete(id);
        return counts;
    }

    public int count(){
        return cd.count();
    }
}
