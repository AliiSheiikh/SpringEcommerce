package com.Ali.EcommerceApp.controller;

import com.Ali.EcommerceApp.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    
    @Autowired
    private ProductRepo repo;
}
