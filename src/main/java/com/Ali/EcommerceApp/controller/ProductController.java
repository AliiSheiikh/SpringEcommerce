package com.Ali.EcommerceApp.controller;

import com.Ali.EcommerceApp.models.Product;
import com.Ali.EcommerceApp.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {


    @Autowired
    private ProductRepo repo;

    @GetMapping({"", "/"})
    public String showProducts(Model model){
        List<Product> products = repo.findAll();
        model.addAttribute("products", products);
        return "products/index";
    }
}
