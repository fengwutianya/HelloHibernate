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

    public static void main(String[] args) {
        Student student = new Student();
        student.setExamCard("123987");
        student.setFlowID(12);
        student.setGrade(98);
        student.setIdCard("123777");
        student.setLocation("广州");
        student.setStudentName("小红");
        student.setType(4);

        new JDBCTest().addStudent(student);

    }
}
