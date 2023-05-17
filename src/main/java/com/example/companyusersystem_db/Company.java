package com.example.companyusersystem_db;

public record Company(int id, String name) {
    public static Company[] changeToCompany(WorkPlace ws){
        CompanyService cs = new CompanyService();
        Company[] Companies = {new Company(ws.idProperty().intValue(),
                ws.nameProperty().get())};
        return Companies;
    }

    public static Company[] changeToCompanyNoId(WorkPlace ws){
        CompanyService cs = new CompanyService();
        Company[] Companies = {new Company(1,
                ws.nameProperty().get())};
        return Companies;
    }
}
