import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by xuan on 2017/2/18 0018.
 */
public class UseProperties {
    private String driverName = null;
    private String url = null;
    private String user = null;
    private String password = null;

    @Test
    public void testProperties() throws Exception{

        InputStream in =
                getClass().getClassLoader().getResourceAsStream("database.properties");
        Properties info = new Properties();
        info.load(in);
        driverName = info.getProperty("driverName");
        url = info.getProperty("url");
        user = info.getProperty("user");
        password = info.getProperty("password");
        Driver driver = (Driver)Class.forName(driverName).newInstance();
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
        conn.close();
    }

    @Test
    public void testDriver() throws SQLException{
        Driver driver = new com.mysql.jdbc.Driver();

        url = "jdbc:mysql://localhost:3306/javademo";
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "12301230s");

        Connection connection = driver.connect(url,properties);
        System.out.println(connection);
        connection.close();
    }

    public void testDriverManager() throws Exception{

        InputStream in =
                getClass().getClassLoader().getResourceAsStream("database.properties");
        Properties info = new Properties();
        info.load(in);
        driverName = info.getProperty("driverName");
        url = info.getProperty("url");
        user = info.getProperty("user");
        password = info.getProperty("password");

        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) throws Exception{
//        new UseProperties().testDriver();
        new UseProperties().testProperties();
    }

}
