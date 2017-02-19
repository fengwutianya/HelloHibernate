import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xuan on 2017/2/19 0019.
 */
public class JDBCTools {
    /**
     * 获取数据库连接的方法
     * @return Connection from database.properties
     * @throws IOException, ClassNotFoundException, SQLException
     */
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        InputStream in =
                JDBCTools.class.getClassLoader().getResourceAsStream("database.properties");
        Properties info = new Properties();
        info.load(in);
        String driverName = info.getProperty("driverName");
        String url = info.getProperty("url");
        String user = info.getProperty("user");
        String password = info.getProperty("password");

        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public static void release(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void update(String sql) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            release(null, stmt, conn);
        }
    }

    public static void update(String sql, Object ...args) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i+1, args[i]);
            }
            pstmt.executeUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> T get(Class clazz, String sql, Object ... args) {
        T entity = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i+1, args[i]);
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                Map<String, Object> values =
                        new HashMap<>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    values.put(rsmd.getColumnLabel(i+1),
                            rs.getObject(i));
                }
                entity = (T)clazz.newInstance();
                for (Map.Entry<String, Object> entry: values.entrySet()) {
                    Field field = clazz.getDeclaredField(entry.getKey());
                    field.set(entity, entry.getValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            release(rs, pstmt, conn);
        }

        return entity;
    }

}
