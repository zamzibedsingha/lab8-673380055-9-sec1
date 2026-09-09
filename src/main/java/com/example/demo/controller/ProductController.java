package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetail;
import com.example.demo.model.Review;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {

        model.addAttribute(
                "products",
                productService.getAllProducts()
        );

        return "products/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Product product = new Product();
        ProductDetail detail = new ProductDetail();
        Review review = new Review();

        product.setDetail(detail);
        product.getReviews().add(review);
        model.addAttribute("product", product);

        return "products/add";
    }

    @PostMapping("/save")
    public String saveProduct(
            @ModelAttribute Product product) {

        productService.saveProduct(product);

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        Product product =
                productService.getProductById(id);

        model.addAttribute("product", product);

        return "products/edit";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute Product product) {

        product.setId(id);

        productService.saveProduct(product);

        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String showDeleteForm(
            @PathVariable Long id,
            Model model) {

        Product product =
                productService.getProductById(id);

        model.addAttribute("product", product);

        return "products/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return "redirect:/products";
    }
}