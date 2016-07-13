package controllers;


import models.Product;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.products.list;

import java.util.List;

//Repo test - problems with import views.html.products.list - off comp;

/**
 * Authors: Leroux et al., 2014
 * Re-Created by bpupadhyaya on 7/11/16.
 */
public class Products extends Controller {

    public static Result newProduct() {
        return TODO;
    }

    public static Result list() {
        List<Product> products = Product.findAll();
        return ok(list.render(products));

    }

    public static Result details(String ean) {
        return TODO;
    }

    public static Result save() {
        return TODO;
    }
}
