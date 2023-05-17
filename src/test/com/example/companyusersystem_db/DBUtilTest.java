package com.example.companyusersystem_db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBUtilTest {
    @Test
    public void testGetConnection() throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            assertEquals(false, conn.isClosed());
        }
    }

}