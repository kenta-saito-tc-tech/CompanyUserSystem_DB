package com.example.companyusersystem_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private Connection connect;

    /**
     * コンストラクタ
     * @param connect データベースのコネクション
     */
    public UserDao(Connection connect) {
        this.connect = connect;
    }

    public List<AllTable> findAllTable(){
        final var SQL = "SELECT c.name AS 会社名, " +
                "u.id AS 社員番号, " +
                "u.name AS 名前, " +
                "u.score AS スコア " +
                "FROM users u " +
                "JOIN companies c ON u.company_id = c.id " +
                "ORDER BY u.id";
        List<AllTable> list = new ArrayList<AllTable>();
        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AllTable allTable = new AllTable
                        (rs.getString("会社名"),
                                rs.getInt("社員番号"),
                                rs.getString("名前"),
                                rs.getInt("スコア"));
                list.add(allTable);
            }
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
        return list;
    }

    /**
     * ユーザー情報全権取得
     * @return
     */
    public List<User> findAll(){
        final var SQL = "SELECT id, name, company_id, score FROM users ORDER BY id";
        List<User> list = new ArrayList<User>();
        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User
                        (rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("company_id"),
                                rs.getInt("score"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
        return list;
    }

    /**
     * ID検索でユーザー情報を取得
     * @param id
     * @return
     */
    public User findById(int id) {
        final var SQL = "SELECT id, name, company_id, score FROM users WHERE id = ?";

        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User
                        (rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("company_id"),
                                rs.getInt("score"));
                return user;
            }
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
        return null;
    }

    /**
     * nameに対して曖昧検索を行い、ヒットした件数分のリストを返す。
     * @param str
     * @return
     */
    public List<User> findByName(String str) {
        final var SQL = "SELECT id, name, company_id, score FROM users WHERE name LIKE ?";

        var list = new ArrayList<User>();

        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            stmt.setString(1, "%" +str + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                var user = new User
                        (rs.getInt("id"),
                                rs.getString("name"),
                                rs.getInt("company_id"),
                                rs.getInt("score"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
        return list;
    }

    /**
     * 引数で受け取った値でテーブルに対してレコードをインサートする
     * 処理件数を戻り値で返す
     *
     * @param
     * @return
     */
    public int insert(User[] users) {
        int counts = 0;
        String SQL = "INSERT INTO users (name, company_id, score) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            this.connect.setAutoCommit(false);
            for (var user : users) {
                stmt.setString(1, user.name());
                stmt.setInt(2, user.company_id());
                stmt.setInt(3, user.score());
                stmt.executeUpdate();
                counts++;
            }
            this.connect.commit();
            return counts;
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
    }

    /**
     * 引数で受け取ったidをwhere句で指定し、データをupdateする
     * 処理件数を戻り値で返す
     *
     * @param
     * @return
     */
    public int update(User[] users) {
        int counts = 0;
        String SQL = "UPDATE users SET name = ?, company_id = ?, score = ? WHERE id = ?";
        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            this.connect.setAutoCommit(false);
            for (var user : users) {
                stmt.setString(1, user.name());
                stmt.setInt(2, user.company_id());
                stmt.setInt(3, user.score());
                stmt.setInt(4, user.id());
                stmt.executeUpdate();
                counts++;
            }
            this.connect.commit();
            return counts;
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
    }

    /**
     * 引数で受け取ったidのカラムをdeleteする
     * 処理件数を戻り値で返す
     *
     * @param id
     * @return
     */
    public int delete(int id) {
        int counts = 0;
        String SQL = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            this.connect.setAutoCommit(false);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            this.connect.commit();
            counts++;
            return counts;
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
    }

    /**
     * 現在の件数を返すメソッド
     * @return
     */
    public int count(){
        int rowCounts = 0;
        final var SQL = "SELECT COUNT(*) AS row_count FROM users";
        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            this.connect.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                rowCounts = rs.getInt("row_count");
            }
            this.connect.commit();
            return rowCounts;
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
    }
}
