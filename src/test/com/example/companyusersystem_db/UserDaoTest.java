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

class UserDaoTest {
    private Connection connection;
    private UserDao userDao;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DBUtil.getConnection();
        connection.setAutoCommit(false);

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

        userDao = new UserDao(connection);
    }

    @AfterEach
    public void tearDown() throws Exception {
        connection.rollback();
    }

    @Test
    public void findAllTableで全てのテーブル情報を取得できる(){
        List<AllTable> list = userDao.findAllTable();
        assertEquals(2, list.size());

        AllTable a = list.get(0);
        assertEquals("aaa会社", a.companyName());
        assertEquals(Integer.valueOf(1), a.id());
        assertEquals("aaa", a.name());
        assertEquals(Integer.valueOf(100), a.score());

        a = list.get(1);
        assertEquals("bbb会社", a.companyName());
        assertEquals(Integer.valueOf(2), a.id());
        assertEquals("bbb", a.name());
        assertEquals(Integer.valueOf(80), a.score());
    }

    @Test
    public void findAllで全件取得できる() {
        List<User> list = userDao.findAll();
        assertEquals(2, list.size());

        User u = list.get(0);
        assertEquals(Integer.valueOf(1), u.id());
        assertEquals("aaa", u.name());
        assertEquals(Integer.valueOf(1), u.company_id());
        assertEquals(Integer.valueOf(100), u.score());

        u = list.get(1);
        assertEquals(Integer.valueOf(2), u.id());
        assertEquals("bbb", u.name());
        assertEquals(Integer.valueOf(2), u.company_id());
        assertEquals(Integer.valueOf(80), u.score());
    }

    @Test
    public void findAllはデータがないと空のリストを返す() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM users")) {
            stmt.executeUpdate();
        }
        List<User> list = userDao.findAll();
        assertEquals(0, list.size());
    }

    @Test
    public void findByIdで存在するデータが正しく取得できる() {
        User u = userDao.findById(1);
        assertEquals(Integer.valueOf(1), u.id());
        assertEquals("aaa", u.name());
        assertEquals(Integer.valueOf(1), u.company_id());
        assertEquals(Integer.valueOf(100), u.score());
    }

    @Test
    public void findByIdで存在しないデータはnullになる() {
        User user = userDao.findById(10);
        assertNull(user);
    }

    @Test
    public void findByNameで名前検索ができる(){
        List<User> list = userDao.findByName("a");
        assertEquals(1, list.size());
        User u = list.get(0);
        assertEquals(Integer.valueOf(1), u.id());
        assertEquals("aaa", u.name());
        assertEquals(Integer.valueOf(1), u.company_id());
        assertEquals(Integer.valueOf(100), u.score());
    }
    @Test
    public void findByNameで存在しないデータはnullになる(){
        List<User> list = userDao.findByName("www");
        assertEquals(0, list.size());
    }

    @Test
    public void insertでデータを登録できる() {
        User[] newUser = {
                new User(10, "ccc", 3, 30),
                new User(11, "ddd", 4, 70),
        };
        userDao.insert(newUser);

        User getUser = userDao.findById(10);
        assertEquals(newUser[0].id(), getUser.id());
        assertEquals(newUser[0].name(), getUser.name());
        assertEquals(newUser[0].company_id(), getUser.company_id());
        assertEquals(newUser[0].score(), getUser.score());

        User getUser2 = userDao.findById(11);
        assertEquals(newUser[1].id(), getUser2.id());
        assertEquals(newUser[1].name(), getUser2.name());
        assertEquals(newUser[1].company_id(), getUser2.company_id());
        assertEquals(newUser[1].score(), getUser2.score());
    }

    @Test
    public void insertで主キーが重複していると例外発生() {
        User[] newUser = {
                new User(1, "ccc", 3, 30),
                new User(11, "ddd", 4, 70),
        };
        assertThrows(RuntimeException.class, () -> userDao.insert(newUser));
    }

    @Test
    public void updateでデータを更新できる() {
        User u = userDao.findById(1);
        assertEquals(Integer.valueOf(1), u.id());
        assertEquals("aaa", u.name());
        assertEquals(Integer.valueOf(1), u.company_id());
        assertEquals(Integer.valueOf(100), u.score());

        User u2 = userDao.findById(2);
        assertEquals(Integer.valueOf(2), u2.id());
        assertEquals("bbb", u2.name());
        assertEquals(Integer.valueOf(2), u2.company_id());
        assertEquals(Integer.valueOf(80), u2.score());

        User[] updateUser = {
                new User(1, "aaanew", 1, 100),
                new User(2, "bbbnew", 2, 80),
        };

        userDao.update(updateUser);

        u = userDao.findById(1);
        assertEquals(Integer.valueOf(1), u.id());
        assertEquals("aaanew", u.name());
        assertEquals(Integer.valueOf(1), u.company_id());
        assertEquals(Integer.valueOf(100), u.score());

        u2 = userDao.findById(2);
        assertEquals(Integer.valueOf(2), u2.id());
        assertEquals("bbbnew", u2.name());
        assertEquals(Integer.valueOf(2), u2.company_id());
        assertEquals(Integer.valueOf(80), u2.score());
    }
    @Test
    public void countで件数を調べる(){
        int rs = userDao.count();
        assertEquals(2,rs);
    }
    @Test
    public void deleteでデータを削除できる() {
        User user = userDao.findById(1);
        assertNotNull(user);

        userDao.delete(1);

        user = userDao.findById(1);
        assertNull(user);
    }



}