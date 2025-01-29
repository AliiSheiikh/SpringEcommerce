package com.Ali.EcommerceApp.controller;

import com.Ali.EcommerceApp.models.Product;
import com.Ali.EcommerceApp.models.ProductDTO;
import com.Ali.EcommerceApp.repo.ProductRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {


    @Autowired
    private ProductRepo repo;

    @GetMapping({"", "/"})
    public String showProducts(Model model){
        List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showcreateProductPage(Model model){
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("productDTO", productDTO);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDTO productDTO, BindingResult result){

        if (productDTO.getImageFile().isEmpty()){
            result.addError(new FieldError("productDTO","imageFile","The image file is required"));
        }

        if (result.hasErrors()){
            return "products/CreateProduct";
        }
        return "redirect:/products";
    }
}
