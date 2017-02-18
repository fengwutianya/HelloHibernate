import com.mysql.jdbc.Driver;

import java.sql.*;

/**
 * Created by xuan on 2017/2/18 0018.
 */
public class HelloJDBC {
    public static void main(String[] args) {
        //要先创建javademo数据库，url连接
        String url = "jdbc:mysql://localhost:3306/javademo?"
                + "user=root&password=12301230s&userUnicode=true&characterEncoding=UTF8";
        Connection conn = null;
        String sql = null;
        //加载驱动到DriverManager
        try {
            Class.forName("com.mysql.jdbc.Driver");
//            System.out.println("成功加载mysql驱动");
            conn = DriverManager.getConnection(url);
//            System.out.println("成功连接数据库");
            Statement stmt = conn.createStatement();
            sql = "create table student(NO char(20),name varchar(20),primary key(NO))";
            int result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
            if (result != -1) {
                System.out.println("创建数据表成功");
                sql = "insert into student(NO,name) values('2012001','陶伟基')";
                result = stmt.executeUpdate(sql);
                sql = "insert into student(NO,name) values('2012002','周小俊')";
                result = stmt.executeUpdate(sql);
                sql = "select * from student";
                ResultSet rs = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
                System.out.println("学号\t姓名");
                while (rs.next()) {
                    System.out.println(rs.getString(1) + "\t" + rs.getString(2));// 入如果返回的是int类型可以用getInt()
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
