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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
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

        //save the image
        MultipartFile image = productDTO.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();


        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }catch (Exception ex) {
            System.out.println("Exception "+ ex.getMessage());
        }

        //create product item with information received from DTO
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);

        repo.save(product);

        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id){

        try{
            Product product = repo.findById(id).get();
            model.addAttribute("product", product);

            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(product.getName());
            productDTO.setBrand(product.getBrand());
            productDTO.setCategory(product.getCategory());
            productDTO.setPrice(product.getPrice());
            productDTO.setDescription(product.getDescription());

            model.addAttribute("productDTO", productDTO);
        }
        catch(Exception ex){
            System.out.println("Exception "+ ex.getMessage());
            return "redirect/products";
        }
        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(Model model, @RequestParam int id, @Valid @ModelAttribute ProductDTO productDTO,
                                BindingResult result){
        try{
            Product product = repo.findById(id).get();
            model.addAttribute("product, product");

            if (result.hasErrors()){
                return "products/EditProduct";
            }

            if (!productDTO.getImageFile().isEmpty()){   //delete old image
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir+product.getImageFileName());

                try{
                    Files.delete(oldImagePath);
                }
                catch(Exception ex){
                    System.out.println("Exception: "+ ex.getMessage());
                }

                //save new image file
                MultipartFile image = productDTO.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImageFileName((storageFileName));
            }

            product.setName(productDTO.getName());
            product.setBrand(productDTO.getBrand());
            product.setCategory(productDTO.getCategory());
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());

            repo.save(product);

        }
        catch (Exception ex){
            System.out.println("Exception "+ex.getMessage());
        }
        return "redirect:/products";
    }
}
