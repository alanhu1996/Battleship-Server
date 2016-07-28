package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Orange
 */
public class DBConnect {

    static String host = "jdbc:derby://localhost:1527/Accounts";
    static String uName = "root";
    static String uPass = "root";
    static String sql = "SELECT * FROM ROOT.PLAYER";
    static String newUser;

    public DBConnect(String newUser) {
        this.newUser = newUser;

    }

    public static void add(String content) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(sql);
            rs.moveToInsertRow();
            rs.updateString("USER_NAME", content);
            rs.updateInt("LEVEL", 1);
            rs.updateInt("WINS", 0);
            rs.insertRow();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
        }
    }

    public static boolean checkUserExist(String name) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (name.equals(rs.getString("user_Name"))) {
                    return true;
                }
            }
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
        return false;
    }

    public static void loadInfo(int[] level, int[] wins) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("USER_NAME").equals(newUser)) {
                    level[0] = rs.getInt("LEVEL");
                    wins[0] = rs.getInt("WINS");
                }
            }


        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }

    public static void increaseWins(int wins[]) {
        try {
            Connection con = DriverManager.getConnection(host, uName, uPass);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("USER_NAME").equals(newUser)) {
                    wins[0] = rs.getInt("WINS") + 1;
                    int gamesWon = rs.getInt("WINS") + 1;
                    rs.updateInt("WINS", gamesWon);
                    rs.updateRow();
                }
            }


        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
    }
}
