import org.hibernate.cfg.annotations.ResultsetMappingSecondPass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuan on 2017/2/19 0019.
 */
public class JDBCTest {
    public void addStudent(Student student) {
        String sql =
                "insert into examstudent values (" +
                        student.getFlowID() + ", " +
                        student.getType() + ", " +
                        "'" + student.getIdCard() +"'" + ", " +
                        "'" + student.getExamCard() + "'" + ", " +
                        "'" + student.getStudentName() + "'" + ", " +
                        "'" + student.getLocation() + "'" + ", " +
                        student.getGrade() + ")";
        System.out.println(sql);
        JDBCTools.update(sql);
    }

    public void addStudent2(Student student) {
        String sql = "insert into examstudent" +
                "(FlowID, Type, IDCard, ExamCard, StudentName, Location, Grade) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        JDBCTools.update(sql,
                student.getFlowID(),
                student.getType(),
                student.getIdCard(),
                student.getExamCard(),
                student.getStudentName(),
                student.getLocation(),
                student.getGrade());
    }

    public void verifyWrong(Student student) throws SQLException, IOException, ClassNotFoundException {
        String sql = "select * from examstudent where IDCard='" +
                student.getIdCard() + "' and ExamCard='" +
                student.getExamCard() + "'";
        System.out.println(sql);
        Connection conn = JDBCTools.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            System.out.println("成功登陆");
        } else {
            System.out.println("登陆失败");
        }
    }


    public void verifyRight(Student student) throws SQLException, IOException, ClassNotFoundException {
        String sql = "select * from examstudent where IDCard=? and ExamCard=?";
        Connection conn = JDBCTools.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setObject(1, student.getIdCard());
        pstmt.setObject(2, student.getExamCard());
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            System.out.println("成功登陆");
        } else {
            System.out.println("登陆失败");
        }
    }

    public void testSQLInjection() throws SQLException, IOException, ClassNotFoundException {
        Student student = new Student();
        student.setIdCard("a' or ExamCard=");
        student.setExamCard(" or '1'='1");
        verifyWrong(student);
        verifyRight(student);
    }

    public void testResultSetMetaData() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select FlowID flowID, Type type, IDCard idCard" +
                ", ExamCard examCard, StudentName studentName" +
                ", Location location, Grade grade from examstudent";

        try {
            conn = JDBCTools.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
//            for (int i = 0; i < rsmd.getColumnCount(); i++) {
//                String columnLabel = rsmd.getColumnLabel(i+1);
//                System.out.println(columnLabel);
//            }
            Map<String, Object> values =
                    new HashMap<>();
            while (rs.next()) {
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    Object columnValue = rs.getObject(columnLabel);
                    values.put(columnLabel, columnValue);
                }
//                System.out.println(values);
                Student student = new Student();
                System.out.println(student.getFlowID());
                Class clazz = Student.class;
                for (Map.Entry<String, Object> entry: values.entrySet()) {
                    Field field = clazz.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(student, entry.getValue());
                }
                System.out.println(student.getFlowID());
                values.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JDBCTools.release(rs, pstmt, conn);
        }
    }

    public static void main(String[] args) {
        Student student = new Student();
//        student.setExamCard("123987");
//        student.setFlowID(12);
//        student.setGrade(98);
//        student.setIdCard("123777");
//        student.setLocation("广州");
//        student.setStudentName("小红");
//        student.setType(4);
//        student.setFlowID(14);
//
//        new JDBCTest().addStudent(student);
        new JDBCTest().testResultSetMetaData();
    }
}
