package com.example.companyusersystem_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDao {
    private Connection connect;

    /**
     * コンストラクタ
     * @param connect データベースのコネクション
     */
    public CompanyDao(Connection connect) {
        this.connect = connect;
    }

    /**
     * 会社情報全権取得
     * @return
     */
    public List<Company> findAll(){
        final var SQL = "SELECT id, name FROM companies ORDER BY id";
        List<Company> list = new ArrayList<Company>();
        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Company company = new Company
                        (rs.getInt("id"),
                                rs.getString("name"));
                list.add(company);
            }
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
        return list;
    }

    /**
     * ID検索で該当のデータを取得する
     * @param id
     * @return
     */
    public Company findById(int id) {
        final var SQL = "SELECT id, name FROM companies WHERE id = ?";

        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Company company = new Company
                        (rs.getInt("id"), rs.getString("name"));
                return company;
            }
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
        return null;
    }

    /**
     * 引数の文字列を元にnameに対して曖昧検索を行い、ヒットした件数分のリストを返す。
     * @param str
     * @return
     */
    public int findByName(String str) {
        final var SQL = "SELECT id, name FROM companies WHERE name LIKE ?";

        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            stmt.setString(1, str);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Company company = new Company
                        (rs.getInt("id"),
                                rs.getString("name"));
                return company.id();

            }
        } catch (SQLException e) {
            throw new ProductNotFoundException(e.getMessage());
        }
        return 0;
    }

    /**
     * 引数で受け取った値でテーブルに対してレコードをインサートする
     * 処理件数を戻り値で返す
     *
     * @param
     * @return
     */
    public int insert(Company[] companies) {
        int counts = 0;
        String SQL = "INSERT INTO companies (name) VALUES (?)";
        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            this.connect.setAutoCommit(false);
            for (var company : companies) {
                stmt.setString(1, company.name());
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
     * 引数で受け取ったidをwhere句で指定し、updateする
     * 処理件数を戻り値で返す
     *
     * @param
     * @return
     */
    public int update(Company[] companies) {
        int counts = 0;
        String SQL = "UPDATE companies SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            this.connect.setAutoCommit(false);
            for (var company : companies) {
                stmt.setString(1, company.name());
                stmt.setInt(2, company.id());
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
     * 引数で受け取ったint型の変数をidとして指定し、deleteする
     * 処理件数を戻り値で返す
     *
     * @param id
     * @return
     */
    public int delete(int id) {
        int counts = 0;
        String SQL = "DELETE FROM companies WHERE id = ?";
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
        final var SQL = "SELECT COUNT(*) AS row_count FROM companies";
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

    public int countById(int id){
        int rowCounts = 0;
        final var SQL = "SELECT COUNT(*) AS row_count FROM companies WHERE id = ?";
        try (PreparedStatement stmt = this.connect.prepareStatement(SQL)) {
            this.connect.setAutoCommit(false);
            stmt.setInt(1, id);
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

