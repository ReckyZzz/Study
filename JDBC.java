import java.sql.*;
import java.util.Scanner;

public class JDBC {
    public static void main(String[] args) throws Exception {
        Connection con = null;
        String sql;
        String url = "jdbc:mysql://localhost:3306/samp_db";
        String driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String password = "ztb4122262";
        try {
            Class.forName(driver);

            con = DriverManager.getConnection(url, user, password);
            Statement statement = con.createStatement();
            if (!con.isClosed()) {
                System.out.println("数据库连接成功");
            }
            //创建user表
            sql = "create table user" +
                         "(id int not null," +
                         "username varchar(16) not null," +
                         "password varchar(16) not null," +
                         "name varchar(8) not null," +
                         "gender varchar(4) not null," +
                         "phone varchar(16) not null)";
            statement.executeUpdate(sql);
            System.out.println("user表创建成功");
            //向user表中插入数据
            sql = "insert into user values(1,'Recky','ztb4122262','张庭博','男性','15912364094')";
            statement.executeUpdate(sql);
            System.out.println("数据插入成功");
            //用户登录系统查看数据
            System.out.print("请输入你的用户名：");
            Scanner sc = new Scanner(System.in);
            String uname = sc.nextLine();
            sql = "select username from user where username ='" + uname + "'";
            ResultSet rs1 = statement.executeQuery(sql);
            String unameDB = rs1.getString("unameDB");
            if(uname.equals(unameDB)){
                System.out.print("请输入你的密码：");
                sc = new Scanner(System.in);
                String pw = sc.nextLine();
                sql = "select password from user where username ='" + pw + "'";
                ResultSet rs2 = statement.executeQuery(sql);
                String pwDB = rs2.getString("pwDB");
                if(pw.equals(pwDB)){
                    System.out.println("您已成功登录系统！");
                    sql = "select id,username,password from user";
                    ResultSet rs = statement.executeQuery(sql);
                    System.out.println("数据库中数据如下");
                    while(rs.next()){
                        int id = rs.getInt("id");
                        String PW = rs.getString("PW");
                        String name = rs.getString("name");

                        System.out.print(" ID: " + id);
                        System.out.print(" 密码: " + PW);
                        System.out.print(" 名字: " + name);
                        System.out.print("\n");
                    }
                }
                else
                    System.out.println("输入的密码错误！");
            }
            else
                System.out.println("输入的用户名不存在！");

            con.close();
            statement.close();
            rs1.close();
        } catch (SQLException e) {
            System.out.println("数据库连接失败");
        }
    }
}

