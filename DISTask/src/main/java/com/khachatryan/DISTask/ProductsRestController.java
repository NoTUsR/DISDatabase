package com.khachatryan.DISTask;


import org.ibex.nestedvm.util.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController

public class ProductsRestController {

    @GetMapping(value = "/main/products")
    public @ResponseBody String ProductsPage(@RequestParam(name="sortType", required = false) String sortType, @RequestParam(name="sort", required = false) String sort) throws ClassNotFoundException, SQLException {
        String res = "<h2>Products database</h2>";
        res += ProductSortingList();
        res += "<a href=\"http://localhost:8080/main/products/create\">Create new</a> &nbsp;";
        res += "<a href=\"http://localhost:8080/main\">Back</a>";
        res += ShowProductTable(sortType, sort);
        return res;
    }

    @GetMapping(value = "/main/products/{id}/edit")
    public @ResponseBody String EditProduct(@PathVariable("id") Long id) throws ClassNotFoundException, SQLException{
        List<Product> products =  DB.GetProducts(null, null);
        Product reqProduct = null;
        for(int i=0; i<products.size(); i++){
            if(products.get(i).getId() == id){
                reqProduct = products.get(i);
            }
        }
        String res = String.format("<h2>Edit product with id=%d</h2>", id);
        res +="<form method=\"post\" action=\"http://localhost:8080/main/products\">";
        res += "<table><tbody>";
        res += "<tr><th></th><th><input name=\"method\" value=\"edit\" type=\"hidden\"></input></th></tr>";
        res += String.format("<tr><th align=\"left\"><b>ID: </b></th><th><input name=\"id\" value=\"%d\" readonly></input></th></tr>", reqProduct.getId());
        res += String.format("<tr><th align=\"left\"><b>Name: </b></th><th><input name=\"name\" value=\"%s\"></input></th></tr>", reqProduct.getName());
        res += String.format("<tr><th align=\"left\"><b>Description: </b></th><th><input name=\"description\" value=\"%s\"></input></th></tr>", reqProduct.getDescription());
        res += String.format("<tr><th align=\"left\"><b>Integration Cost: </b></th><th><input name=\"integrationCost\" value=\"%d\"></input></th></tr>", reqProduct.getIntegrationCost());
        res += "</tbody></table>";
        res += "<input type=\"submit\">";
        res += "</form>";
        res += String.format("<a href=\"http://localhost:8080/main/products/%d\">Back</a>", id);
        return res;
    }

    @GetMapping(value = "/main/products/{id}")
    public @ResponseBody String OpenProduct(@PathVariable("id") Long id) throws ClassNotFoundException, SQLException{
        List<Product> products =  DB.GetProducts(null, null);
        Product reqProduct = null;
        for(int i=0; i<products.size(); i++){
            if(products.get(i).getId() == id){
                reqProduct = products.get(i);
            }
        }
        List<Article> articles =  DB.GetArticles(null, null);
        String res = String.format("<h2>Product with id=%d</h2>", id);
        res += String.format("<a href=\"http://localhost:8080/main/products/%d/edit\">Change</a> &nbsp;", id);
        res += String.format("<a href=\"http://localhost:8080/main/products/%d/delete\">Delete</a> &nbsp;", id);
        res += "<a href=\"http://localhost:8080/main/products\">Back</a>";
        res += "<table><tbody>";
        res += String.format("<tr><th align=\"left\"><b>ID: </b></th><th><input name=\"id\" value=\"%d\" readonly></input></th></tr>", reqProduct.getId());
        res += String.format("<tr><th align=\"left\"><b>Name: </b></th><th><input name=\"name\" value=\"%s\" readonly></input></th></tr>", reqProduct.getName());
        res += String.format("<tr><th align=\"left\"><b>Description: </b></th><th><input name=\"description\" value=\"%s\" readonly></input></th></tr>", reqProduct.getDescription());
        res += String.format("<tr><th align=\"left\"><b>Integration Cost: </b></th><th><input name=\"integrationCost\" value=\"%d\" readonly></input></th></tr>", reqProduct.getIntegrationCost());
        res += "</tbody></table>";
        res += "<h2>Articles for this product:</h2>";

        res += "<table><tbody>";
        res += "<tr><th>ID</th><th>Product ID</th><th>Name</th><th>Content</th><th>Date</th></tr>";
        for(int i=0; i<articles.size();i++){
            if(articles.get(i).getProductId() == id) {
                res += String.format("<tr><td>%d</td><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td><a href=\"http://localhost:8080/main/articles/%d\">Open article</a></td></tr>", articles.get(i).getId(), articles.get(i).getProductId(), articles.get(i).getName(), articles.get(i).getContent(), articles.get(i).getCreateDate(), articles.get(i).getId());
            }
        }
        res += "<table><tbody>";

        return res;
    }

    @GetMapping(value = "/main/products/create")
    public @ResponseBody String CreateProduct() throws ClassNotFoundException, SQLException{

        String res = "<h2>Create product</h2>";
        res +="<form method=\"post\" action=\"http://localhost:8080/main/products\">";
        res += "<table><tbody>";
        res += "<tr><th></th><th><input name=\"method\" value=\"create\" type=\"hidden\"></input></th></tr>";
        res += "<tr><th align=\"left\"><b>Name: </b></th><th><input name=\"name\" required></input></th></tr>";
        res += "<tr><th align=\"left\"><b>Description: </b></th><th><input name=\"description\" required></input></th></tr>";
        res += "<tr><th align=\"left\"><b>Integration Cost: </b></th><th><input name=\"integrationCost\" required></input></th></tr>";
        res += "</tbody></table>";
        res += "<input type=\"submit\">";
        res += "</form>";
        res += "<a href=\"http://localhost:8080/main/products\">Back</a>";
        return res;
    }

    @GetMapping("/main/products/{id}/delete")
    public @ResponseBody String DeleteProduct(@PathVariable("id") Long id) throws ClassNotFoundException, SQLException{
        String res = String.format("<h2>Are you sure to delete product with id=%d?</h2>", id);
        res += "<h3>(It will also delete all articles for this product)</h3>";
        res +="<form method=\"post\" action=\"http://localhost:8080/main/products\">";
        res +="<input name=\"method\" value=\"delete\" type=\"hidden\"></input>";
        res +=String.format("<input name=\"id\" value=\"%d\" type=\"hidden\"></input>", id);
        res += "<input type=\"submit\" value=\"Yes\">";
        res += "</form>";
        res += "<a href=\"http://localhost:8080/main/products\"><input type=\"submit\" value=\"No\"></a>";
        return res;
    }

    @PostMapping(value = "/main/products")
    @ResponseBody
    public String SaveProductsChanges(@RequestParam(name = "method")String method, @RequestParam(name = "id", required = false) Long id, @RequestParam(name = "name", required = false) String name, @RequestParam(name = "description", required = false) String description, @RequestParam(name = "integrationCost", required = false) Long integrationCost) throws ClassNotFoundException, SQLException{
        String res = "";
        if(method.equals("edit")){
            DB.ChangeProduct(id, name, description, integrationCost);
            res = "<h2>The product was changed successfully!</h2>";
        }else if(method.equals(("delete"))){
            DB.DeleteProduct(id);
            res = "<h2>The product was deleted successfully!</h2>";
        }else if(method.equals(("create"))){
            DB.InsertToProducts(name, description, integrationCost);
            res = "<h2>The product was created successfully!</h2>";
        }
        res += "<a href=\"http://localhost:8080/main/products\">OK</a>";
        return res;
    }


    private String ShowProductTable(String sortType, String sort) throws ClassNotFoundException, SQLException{
        String res = "";
        List<Product> products = new ArrayList<Product>();
        products = DB.GetProducts(sortType, sort);

        if(products.isEmpty()){
            res = "<br><b>There is no products created yet. Please create a new one</b>";
        }else {
            res = "<table><tbody><tr><th>ID</th><th>Name</th><th>Description</th><th>Integration cost</th></tr>";
            for (int i = 0; i < products.size(); i++) {
                res += String.format("<tr><td>%d</td><td>%s</td><td>%s</td><td>%d</td><td><a href=\"http://localhost:8080/main/products/%d\" >Open</a></td><td><a href=\"http://localhost:8080/main/products/%d/delete\" >Delete</a></td></tr>", products.get(i).getId(), products.get(i).getName(), products.get(i).getDescription(), products.get(i).getIntegrationCost(), products.get(i).getId(), products.get(i).getId());
            }
            res += "</tbody></table>";
        }

        return  res;
    }

    private String ProductSortingList(){
        String res = "<form><b>Sort:</b>";
        res += "<select name=\"sortType\" size=1>";
        res += "<option selected value=\"ASC\">ascending</option>";
        res += "<option value=\"DESC\">descending </option>";
        res += "</select>";
        res += "<b>by:</b>";
        res += "<select name=\"sort\" size=1>";
        res += "<option selected value=\"id\">ID</option>";
        res += "<option value=\"name\">Name</option>";
        res += "<option value=\"description\">Description</option>";
        res += "<option value=\"integrationCost\">Integration Cost</option>";
        res +="</select>";
        res += "<input type=\"submit\">";
        res += "</form>";


        return  res;
    }

}
