package com.example.companyusersystem_db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    public static Connection getConnection(){
        // JDBCドライバの読み込み
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/companydb", "testuser", "test");
        } catch (Exception e) {
            // 本来は専用の例外クラスを作成したほうがよい
            throw new ProductNotFoundException(e.getMessage());
        }
    }
}
