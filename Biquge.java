import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.sql.*;

public class Test {
    public static void main(String[] args) throws Exception{
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
        sql = "create table novel" +
                "(title varchar(50) not null," +
                "author varchar(50) not null," +
                "intro varchar(300) not null," +
                "img varchar(100) not null," +
                "chapter varchar(10000) not null)";
        statement.executeUpdate(sql);
        System.out.println("novel表创建成功");

        //获取小说标题和作者
        Document document = Jsoup.connect("http://www.xbiquge.la/0/52/").get();
        String info = document.select("[id=info]").text();
        String[] Rinfo= info.split(" 动");
        info = Rinfo[0];
        Rinfo = info.split(" 作 者：");
        String title = Rinfo[0];
        String author = Rinfo[1];

        //获取小说描述
        String intro = document.select("[id=intro]").text();
        Rinfo = intro.split("免费阅读 ");
        intro = Rinfo[1];

        //获取小说头图
        Elements jpgs = document.select("img[src$=.jpg]");
        String pic = jpgs.toString();
        Rinfo = pic.split("alt=");
        pic = Rinfo[1];
        Rinfo = pic.split("\" width");
        pic = Rinfo[0];
        Rinfo = pic.split("src=\"");
        pic = Rinfo[1];
        System.out.println(pic);

        //获取小说每一章节标题和内容
        document = Jsoup.connect("http://www.xbiquge.la/0/52/42119.html").get();
        String para = document.select("[id = content]").text();
        sql = "insert into novel(title,author,intro,img,chapter) values(?,?,?,?,?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,title);
        stmt.setString(2,author);
        stmt.setString(3,intro);
        stmt.setString(4,pic);
        stmt.setString(5,para);
        stmt.executeUpdate();
        System.out.println("第1章插入成功!");

        Elements nextdiv = document.select(".bottem2");
        String next = nextdiv.toString();
        String[] nexturl = next.split("下一章");
        next = nexturl[0];
        nexturl = next.split("\u2192");
        next = nexturl[1];
        next = next.substring(12, next.length() - 2);
        String newurl = "http://www.xbiquge.la" + next;
        for(int i = 2;i < 7627; i++){
            document = Jsoup.connect(newurl).get();
            para = document.select("[id = content]").text();
            System.out.print(para + '\n');
            nextdiv = document.select(".bottem2");
            next = nextdiv.toString();
            nexturl = next.split("下一章");
            next = nexturl[0];
            nexturl = next.split("\u2192");
            next = nexturl[1];
            next = next.substring(12, next.length() - 2);
            newurl = "http://www.xbiquge.la" + next;
            sql = "insert into novel(title,author,intro,img,chapter) values(?,?,?,?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1,title);
            stmt.setString(2,author);
            stmt.setString(3,intro);
            stmt.setString(4,pic);
            stmt.setString(5,para);
            stmt.executeUpdate();
            System.out.println("第" + i +"章插入成功!");
        }
    }
}
