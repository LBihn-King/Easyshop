package org.yearup.controllers;

import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@RequestMapping("categories")
@CrossOrigin
public class CategoriesController {
    private CategoryDao categoryDao;
    private ProductDao productDao;

    // create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // add the appropriate annotation for a get action
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Category> getAll() {
        // find and return all categories
        List<Category> categories = categoryDao.getAllCategories();
        return categories;
    }

    // add the appropriate annotation for a get action
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id) {
        // get the category by id
        Category category = categoryDao.getById(id);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return category;
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        // get a list of product by categoryId
        List<Product> products = productDao.listByCategoryId(categoryId);
        return products;
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category) {
        // insert the category
        try {
            return categoryDao.create(category);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad1.");
        }
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        // update the category by id
        try {
            categoryDao.update(id, category);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad2.");
        }
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id) {
        // delete the category by id
        try {
            Category category = categoryDao.getById(id);
            if (category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            categoryDao.delete(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad3.");
        }
    }
}