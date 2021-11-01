package com.khachatryan.DISTask;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    public static Connection con;
    public static Statement stat;
    public static ResultSet resSet;

    public static void Connect() throws ClassNotFoundException, SQLException
    {
        con = null;
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:DISGroup.s3db");
        System.out.println("Connection done!");
    }

    public static void CreateTables() throws ClassNotFoundException, SQLException {
        stat = con.createStatement();

        stat.execute("CREATE TABLE if not exists 'Products' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' TEXT NOT NULL, 'description' TEXT NOT NULL, 'integrationCost' INT);");
        stat.execute("CREATE TABLE if not exists 'Articles' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'productID' INT, 'name' TEXT NOT NULL, 'content' TEXT NOT NULL, 'date' datetime);");//TEXT as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
        System.out.println("Tables Products and Articles is created!");
    }

    public static void InsertToProducts(String name, String description, Long integrationCost) throws SQLException{
        stat.execute(String.format("INSERT into 'Products' ('name', 'description', 'integrationCost') VALUES ('%s', '%s', '%d');", name, description, integrationCost));
    }

    public static void InsertToArticles(Long productId, String name, String content, String date) throws SQLException{
        stat.execute(String.format("INSERT into 'Articles' ('productID', 'name', 'content', 'date') VALUES ('%d', '%s', '%s', '%s');", productId, name, content, date));
    }

    public static void ChangeProduct(Long id, String name, String description, Long integrationCost) throws SQLException{
        stat.execute(String.format("UPDATE Products SET name=\"%s\", description=\"%s\", integrationCost=%d WHERE id=%d;", name, description, integrationCost, id));
    }

    public static void ChangeArticles(Long id, Long productId, String name, String content, String date) throws SQLException{
        stat.execute(String.format("UPDATE Articles SET productID=%d, name=\"%s\", content=\"%s\", date=\"%s\"  WHERE id=%d;", productId, name, content, date, id));
    }

    public static void DeleteProduct(Long id) throws ClassNotFoundException, SQLException{
        stat.execute(String.format("DELETE FROM Products WHERE id=%d", id));
        List<Article> articles =  DB.GetArticles(null, null);
        List<String> exes = new ArrayList<String>();
        for(int i = 0; i < articles.size(); i++){
            if(articles.get(i).getProductId() == id){
                stat.execute(String.format("DELETE FROM Articles WHERE id=%d;",articles.get(i).getId()));
            }
        }

    }

    public static void DeleteArticle(Long id) throws SQLException{
        stat.execute(String.format("DELETE FROM Articles WHERE id=%d", id));
    }

    public static List<Product> GetProducts(String sortType, String sort) throws ClassNotFoundException, SQLException{
        List<Product> res = new ArrayList<Product>();
        resSet = stat.executeQuery("SELECT * FROM 'Products'");
        if((sort != null) && (sortType != null)) {
            resSet = stat.executeQuery(String.format("SELECT * FROM 'Products' ORDER BY %s %s", sort, sortType));
        }
       while(resSet.next()){
            Product tempProduct = new Product(resSet.getLong("id"), resSet.getString("name"), resSet.getString("description"), resSet.getLong("integrationCost"));
            res.add(tempProduct);
        }

        return res;

    }

    public static List<Article> GetArticles(String sortType, String sort) throws ClassNotFoundException, SQLException{
        List<Article> res = new ArrayList<Article>();
        resSet = stat.executeQuery("SELECT * FROM 'Articles'");
        if((sort != null) && (sortType != null)) {
            resSet = stat.executeQuery(String.format("SELECT * FROM 'Articles' ORDER BY %s %s", sort, sortType));
        }
        while(resSet.next()){
            Article tempProduct = new Article(resSet.getLong("id"), resSet.getLong("productID"), resSet.getString("name"), resSet.getString("content"), resSet.getString("date"));
            res.add(tempProduct);
        }

        return res;

    }
}
