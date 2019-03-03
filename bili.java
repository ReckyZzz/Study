import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Pilipili {
    public static void main(String[] args) throws Exception{
        //连接数据库
        Connection con = null;
        String sql;
        String url = "jdbc:mysql://localhost:3306/samp_db";
        String driver = "com.mysql.jdbc.Driver";
        String user = "root";
        String password = "ztb4122262";
        Class.forName(driver);
        con = DriverManager.getConnection(url, user, password);
        Statement statement = con.createStatement();

        if (!con.isClosed()) {
            System.out.println("数据库连接成功");
        }
        //创建novel表
        sql = "create table bili" +
                "(title varchar(50) not null," +
                "img varchar(50) not null," +
                "uploader varchar(20) not null," +
                "intro varchar(200) not null," +
                "fenp varchar(20) not null," +
                "playtimes varchar(20) not null," +
                "dm varchar(20) not null," +
                "uptime varchar(50) not null)";
        statement.executeUpdate(sql);
        System.out.println("bili表创建成功");

        //获取视频标题
        Document document = Jsoup.connect("https://www.bilibili.com/video/av40503169/").get();
        String title = document.select("[class=video-title]").text();
        System.out.println(title);

        //获取up个人空间
        String img = document.select("[class=fa]").toString();
        img = img.substring(9,img.length());
        String[] temp = img.split("\" target");
        img = temp[0];
        System.out.println(img);

        //获取up的名字
        String uploader = document.select("[class=name]").text();
        temp = uploader.split("发消息");
        uploader = temp[0];
        System.out.println(uploader);

        //获取视频简介
        String intro = document.select("[class=info open]").text();
        System.out.println(intro);

        //获取分P
        String fenp = document.select("[class=s1]").text();
        System.out.println(fenp);

        //获取播放量
        String playtimes = document.select("[class=view]").text();
        System.out.println(playtimes);

        //获取弹幕
        String dm = document.select("[class=dm]").text();
        System.out.println(dm);

        //获取上传时间
        String uptime = document.select("time").toString();
        uptime = uptime.substring(6,uptime.length()-7);
        System.out.println(uptime);

        sql = "insert into bili(title,img,uploader,intro,fenp,playtimes,dm,uptime) values(?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,title);
        stmt.setString(2,img);
        stmt.setString(3,uploader);
        stmt.setString(4,intro);
        stmt.setString(5,fenp);
        stmt.setString(6,playtimes);
        stmt.setString(7,dm);
        stmt.setString(8,uptime);
        stmt.executeUpdate();
        System.out.println("视频信息插入成功!");
    }
}
