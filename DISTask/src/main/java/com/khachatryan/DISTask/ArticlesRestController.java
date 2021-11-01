package com.khachatryan.DISTask;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController

public class ArticlesRestController {

    @GetMapping(value = "/main/articles")
    public @ResponseBody  String ArticlePage(@RequestParam(name="sortType", required = false) String sortType, @RequestParam(name="sort", required = false) String sort) throws ClassNotFoundException, SQLException{
        String res = "<h2>Articles database</h2>";
        res += ArticleSortingList();
        res += "<a href=\"http://localhost:8080/main/articles/create\">Create new</a> &nbsp;";
        res += "<a href=\"http://localhost:8080/main\">Back</a>";
        res += ShowArticlesTable(sortType, sort);
        return res;
    }

    @GetMapping(value = "/main/articles/{id}/edit")
    public @ResponseBody String EditArticle(@PathVariable("id") Long id) throws ClassNotFoundException, SQLException{
        List<Product> products = DB.GetProducts(null, null);
        List<Article> articles =  DB.GetArticles(null, null);
        Article reqArticle = null;
        for(int i=0; i<articles.size(); i++){
            if(articles.get(i).getId() == id){
                reqArticle = articles.get(i);
            }
        }
        String res = String.format("<h2>Edit article with id=%d</h2>", id);
        res +="<form method=\"post\" action=\"http://localhost:8080/main/articles\">";
        res += "<table><tbody>";
        res += "<tr><th></th><th><input name=\"method\" value=\"edit\" type=\"hidden\"></input></th></tr>";
        res += String.format("<tr><th align=\"left\"><b>ID: </b></th><th><input name=\"id\" value=\"%d\" readonly></input></th></tr>", reqArticle.getId());
        res += "<tr><th align=\"left\"><b>Product ID:</b></th>";
        res += "<th><select required name=\"productId\" size=1>";
        for(int i = 0; i < products.size(); i++) {
            if(products.get(i).getId() == reqArticle.getProductId()){

                res += String.format("<option selected value=\"%d\">%d</option>", products.get(i).getId(), products.get(i).getId());

            }else res += String.format("<option value=\"%d\">%d</option>", products.get(i).getId(), products.get(i).getId());
        }
        res += "</select></th></tr>";
        res += String.format("<tr><th align=\"left\"><b>Name: </b></th><th><input name=\"name\" value=\"%s\"></input></th></tr>", reqArticle.getName());
        res += String.format("<tr><th align=\"left\"><b>Content: </b></th><th><input name=\"content\" value=\"%s\"></input></th></tr>", reqArticle.getContent());
        res += String.format("<tr><th align=\"left\"><b>Date: </b></th><th><input type=\"date\" name=\"date\" value=\"%s\"></input></th></tr>", reqArticle.getCreateDate());
        res += "</tbody></table>";
        res += "<input type=\"submit\">";
        res += "</form>";
        res += String.format("<a href=\"http://localhost:8080/main/articles/%d\">Back</a>", id);
        return res;
    }

    @GetMapping(value = "/main/articles/{id}")
    public @ResponseBody String OpenArticle(@PathVariable("id") Long id) throws ClassNotFoundException, SQLException{
        List<Article> articles =  DB.GetArticles(null, null);
        Article reqArticle = null;
        for(int i=0; i<articles.size(); i++){
            if(articles.get(i).getId() == id){
                reqArticle = articles.get(i);
            }
        }
        List<Product> products =  DB.GetProducts(null, null);
        Product reqProduct = null;
        for(int i=0; i<products.size(); i++){
            if(products.get(i).getId() == reqArticle.getProductId()){
                reqProduct = products.get(i);
            }
        }
        String res = String.format("<h2>Article with id=%d</h2>", id);
        res += String.format("<a href=\"http://localhost:8080/main/articles/%d/edit\">Change</a> &nbsp;", id);
        res += String.format("<a href=\"http://localhost:8080/main/articles/%d/delete\">Delete</a> &nbsp;", id);
        res += "<a href=\"http://localhost:8080/main/articles\">Back</a>";
        res += "<table><tbody>";
        res += String.format("<tr><th align=\"left\"><b>ID: </b></th><th><input value=\"%d\" readonly></input></th></tr>", reqArticle.getId());
        res += String.format("<tr><th align=\"left\"><b>Product ID: </b></th><th><input value=\"%d\" readonly></input></th></tr>", reqArticle.getProductId());
        res += String.format("<tr><th align=\"left\"><b>Name: </b></th><th><input value=\"%s\" readonly></input></th></tr>", reqArticle.getName());
        res += String.format("<tr><th align=\"left\"><b>Content: </b></th><th><input value=\"%s\" readonly></input></th></tr>", reqArticle.getContent());
        res += String.format("<tr><th align=\"left\"><b>Date: </b></th><th><input value=\"%s\" readonly></input></th></tr>", reqArticle.getCreateDate());
        res += "</tbody></table>";
        res +="<h2>Product from this article:</h2>";
        res += "<table><tbody>";
        res += String.format("<tr><th align=\"left\">Name:</th><td>%s</td></tr>", reqProduct.getName());
        res += String.format("<tr><th align=\"left\">Description:</th><td>%s</td></tr>", reqProduct.getDescription());
        res += String.format("<tr><th align=\"left\">Integration Cost:</th><td>%d</td></tr>", reqProduct.getIntegrationCost());
        res += "</tbody></table>";
        res += String.format("<a href=\"http://localhost:8080/main/products/%d\">Open product</a>", reqProduct.getId());

        return res;
    }

    @GetMapping(value = "/main/articles/create")
    public @ResponseBody String CreateArticle() throws ClassNotFoundException, SQLException{
        List<Product> products = DB.GetProducts(null, null);
        String res = "";
        if(products.isEmpty()){
            res = "<h2>You have not any products to create articles for them</h2>";
            res += "<p>Please, create one in &nbsp; <a href=\"http://localhost:8080/main/products\">products database</a></p>";
        }else {
            res = "<h2>Create article</h2>";
            res += "<form method=\"post\" action=\"http://localhost:8080/main/articles\">";
            res += "<table><tbody>";
            res += "<tr><th></th><th><input name=\"method\" value=\"create\" type=\"hidden\"></input></th></tr>";
            res += "<tr><th align=\"left\"><b>Product ID:</b></th>";
            res += "<th><select required name=\"productId\" size=1>";
            for(int i = 0; i < products.size(); i++) {
                res += String.format("<option value=\"%d\">%d</option>", products.get(i).getId(), products.get(i).getId());
            }
            res += "</select></th></tr>";
            res += "<tr><th align=\"left\"><b>Name: </b></th><th><input name=\"name\" required></input></th></tr>";
            res += "<tr><th align=\"left\"><b>Content: </b></th><th><input name=\"content\" required></input></th></tr>";
            res += "<tr><th align=\"left\"><b>Date: </b></th><th><input type=\"date\" name=\"date\" required></input></th></tr>";
            res += "</tbody></table>";
            res += "<input type=\"submit\">";
            res += "</form>";
            res += "<a href=\"http://localhost:8080/main/articles\">Back</a>";
        }
        return res;
    }


    @GetMapping("/main/articles/{id}/delete")
    public @ResponseBody String DeleteArticle(@PathVariable("id") Long id) throws ClassNotFoundException, SQLException{
        String res = String.format("<h2>Are you sure to delete article with id=%d?</h2>", id);
        res +="<form method=\"post\" action=\"http://localhost:8080/main/articles\">";
        res +="<input name=\"method\" value=\"delete\" type=\"hidden\"></input>";
        res +=String.format("<input name=\"id\" value=\"%d\" type=\"hidden\"></input>", id);
        res += "<input type=\"submit\" value=\"Yes\">";
        res += "</form>";
        res += "<a href=\"http://localhost:8080/main/articles\"><input type=\"submit\" value=\"No\"></a>";
        return res;
    }

    @PostMapping(value = "/main/articles")
    @ResponseBody
    public String SaveArticlesChanges(@RequestParam(name = "method")String method, @RequestParam(name = "id", required = false) Long id, @RequestParam(name = "productId", required = false) Long productId, @RequestParam(name = "name", required = false) String name, @RequestParam(name = "content", required = false) String content, @RequestParam(name = "date", required = false) String date) throws ClassNotFoundException, SQLException{
        String res = "";
        if(method.equals("edit")){
            DB.ChangeArticles(id, productId, name, content, date);
            res = "<h2>The product was changed successfully!</h2>";
        }else if(method.equals(("delete"))){
            DB.DeleteArticle(id);
            res = "<h2>The product was deleted successfully!</h2>";

        }else if(method.equals(("create"))){
            DB.InsertToArticles(productId, name, content, date);
            res = "<h2>The product was created successfully!</h2>";
        }
        res += "<a href=\"http://localhost:8080/main/articles\">OK</a>";
        return res;
    }

    private String ShowArticlesTable(String sortType, String sort) throws ClassNotFoundException, SQLException {
        String res = "";
        List<Article> articles = new ArrayList<Article>();
        articles = DB.GetArticles(sortType, sort);

        if(articles.isEmpty()){
            res = "<br><b>There is no articles created yet. Please create a new one</b>";
        }else {
            res = "<table><tbody><tr><th>ID</th><th>Product ID</th><th>Name</th><th>Content</th><th>Date</th></tr>";
            for (int i = 0; i < articles.size(); i++) {
                res += String.format("<tr><td>%d</td><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td><a href=\"http://localhost:8080/main/articles/%d\" >Open</a></td><td><a href=\"http://localhost:8080/main/articles/%d/delete\" >Delete</a></td></tr>", articles.get(i).getId(), articles.get(i).getProductId(), articles.get(i).getName(), articles.get(i).getContent(), articles.get(i).getCreateDate(), articles.get(i).getId(), articles.get(i).getId() );
            }
            res += "</tbody></table>";
        }

        return  res;
    }

    private String ArticleSortingList(){
        String res = "<form><b>Sort:</b>";
        res += "<select name=\"sortType\" size=1>";
        res += "<option selected value=\"ASC\">ascending</option>";
        res += "<option value=\"DESC\">descending </option>";
        res += "</select>";
        res += "<b>by:</b>";
        res += "<select name=\"sort\" size=1>";
        res += "<option selected value=\"id\">ID</option>";
        res += "<option value=\"productID\">Product ID</option>";
        res += "<option value=\"name\">Name</option>";
        res += "<option value=\"content\">Content</option>";
        res += "<option value=\"date\">Date</option>";
        res +="</select>";
        res += "<input type=\"submit\">";
        res += "</form>";


        return  res;
    }
}
