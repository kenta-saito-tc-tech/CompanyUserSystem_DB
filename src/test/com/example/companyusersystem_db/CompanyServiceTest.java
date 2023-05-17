package com.example.companyusersystem_db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompanyServiceTest {
    private Connection connection;
    private CompanyDao companyDao;

    private CompanyService cs;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DBUtil.getConnection();
        connection.setAutoCommit(false);

        this.cs = new CompanyService();

        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM users")) {
            stmt.executeUpdate();
        }
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM companies")) {
            stmt.executeUpdate();
        }

        try (Statement stmt = connection.createStatement()) {
            stmt.addBatch("INSERT INTO users VALUES (1, 'aaa', 1, 100)");
            stmt.addBatch("INSERT INTO users VALUES (2, 'bbb', 2, 80)");
            stmt.executeBatch();
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.addBatch("INSERT INTO companies VALUES (1, 'aaa会社')");
            stmt.addBatch("INSERT INTO companies VALUES (2, 'bbb会社')");
            stmt.executeBatch();
        }

        companyDao = new CompanyDao(connection);
    }

    @AfterEach
    public void tearDown() throws Exception {
        connection.rollback();
    }

    @Test
    public void findAllで全件取得できる() {
        List<Company> list = this.cs.findAll();
        assertEquals(2, list.size());

        Company c = list.get(0);
        assertEquals(Integer.valueOf(1), c.id());
        assertEquals("aaa会社", c.name());

        c = list.get(1);
        assertEquals(Integer.valueOf(2), c.id());
        assertEquals("bbb会社", c.name());
    }

    @Test
    public void findAllはデータがないと空のリストを返す() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM companies")) {
            stmt.executeUpdate();
        }
        List<Company> list = cs.findAll();
        assertEquals(0, list.size());
    }

    @Test
    public void findByIdで存在するデータが正しく取得できる() {
        Company c = cs.findById(1);
        assertEquals(Integer.valueOf(1), c.id());
        assertEquals("aaa会社", c.name());
    }

    @Test
    public void findByIdで存在しないデータはnullになる() {
        Company company = cs.findById(10);
        assertNull(company);
    }

    @Test
    public void findByNameで名前検索ができる(){

        int num = cs.findByName("aaa会社");
        assertEquals(1, companyDao.countById(num));
        assertEquals(Integer.valueOf(1), num);
        assertEquals("aaa会社", companyDao.countById(num));
    }
    @Test
    public void findByNameで存在しないデータはnullになる(){
       int num = cs.findByName("www");
        assertEquals(0, num);
    }

    @Test
    public void insertでデータを登録できる() {
        Company[] newCompany = {
                new Company(10, "ccc会社"),
                new Company(11, "ddd会社"),
        };
        cs.insert(newCompany);

        Company getCompany = cs.findById(10);
        assertEquals(newCompany[0].id(), getCompany.id());
        assertEquals(newCompany[0].name(), getCompany.name());

        Company getCompany2 = cs.findById(11);
        assertEquals(newCompany[1].id(), getCompany2.id());
        assertEquals(newCompany[1].name(), getCompany2.name());
    }

    @Test
    public void insertで主キーが重複していると例外発生() {
        Company[] newCompany = {
                new Company(1, "ccc会社"),
                new Company(11, "ddd会社"),
        };
        assertThrows(RuntimeException.class, () -> cs.insert(newCompany));
    }

    @Test
    public void updateでデータを更新できる() {
        Company c = cs.findById(1);
        assertEquals(Integer.valueOf(1), c.id());
        assertEquals("aaa会社", c.name());

        Company c2 = cs.findById(2);
        assertEquals(Integer.valueOf(2), c2.id());
        assertEquals("bbb会社", c2.name());

        Company[] updateCompany = {
                new Company(1, "newaaa会社"),
                new Company(2, "newbbb会社"),
        };

        cs.update(updateCompany);

        c = cs.findById(1);
        assertEquals(Integer.valueOf(1), c.id());
        assertEquals("newaaa会社", c.name());

        c2 = cs.findById(2);
        assertEquals(Integer.valueOf(2), c2.id());
        assertEquals("newbbb会社", c2.name());
    }

    @Test
    public void deleteでデータを削除できる() {
        Company company = cs.findById(1);
        assertNotNull(company);

        cs.delete(1);

        company = cs.findById(1);
        assertNull(company);
    }
}