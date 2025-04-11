package com.cakeshop.api_main;

import com.cakeshop.api_main.model.*;
import com.cakeshop.api_main.repository.internal.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableFeignClients
public class ApiMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMainApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner initData(IProductRepository productRepository,
//                                      IReviewRepository reviewRepository,
//                                      ICustomerRepository customerRepository) {
//        return args -> {
//        };
//    }


//    List<Category> categories = categoryRepository.findAll();
//    Category spongeCategory = findCategoryByCode(categories, "SPONGE_CAKE");
//    Random random = new Random();
//    List<Product> spongeProducts = productRepository.findAll().stream()
//            .filter(p -> p.getCategory().getId().equals(spongeCategory.getId()))
//            .collect(Collectors.toList());
//
//            for (Product product : spongeProducts) {
//        double newPrice = (50 + random.nextInt((150 - 50) / 10 + 1)) * 1000;
//        product.setPrice(newPrice);
//    }
//
//            productRepository.saveAll(spongeProducts);
//    List<Category> categories = categoryRepository.findAll();
//    List<Tag> tags = tagRepository.findAll();
//    List<Discount> discounts = discountRepository.findAll();
//
//            if (categories.isEmpty() || tags.isEmpty()) {
//        throw new RuntimeException("Cần có dữ liệu Category và Tag trước khi tạo Product.");
//    }
//    List<String> images = Arrays.asList(
//            "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744214185/cupcake-5_jrvzfq.webp",
//            "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744213475/cupcake-3_doaecs.webp",
//            "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744213474/cupcake-4_whytdy.webp",
//            "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744213474/cupcake-2_vgtxpx.webp",
//            "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744208115/product-10_q4cevx.webp"
//    );
//    List<Product> products = new ArrayList<>();
//    Random random = new Random();
//            for (int i = 1; i <= 10; i++) {
//        Discount randomDiscount = discounts.get(i % discounts.size());
//        List<Tag> shuffledTags = new ArrayList<>(tags);
//        Collections.shuffle(shuffledTags, random);
//        List<Tag> randomTags = shuffledTags.subList(0, 3);
//        double randomPrice = (15 + random.nextInt((30 - 15) / 10 + 1)) * 1000;
//        long randomQuantity = 20 + random.nextInt(41);
//        List<String> shuffledImages = new ArrayList<>(images);
//        Collections.shuffle(shuffledImages, random);
//        List<String> randomImages = shuffledImages.subList(2, 5);
//        Product product = Product.builder()
//                .name("Cup cake " + i)
//                .price(randomPrice)
//                .description("Mô tả cup cake " + i)
//                .quantity(randomQuantity)
//                .status(1)
//                .images(randomImages)
//                .category(findCategoryByCode(categories, "CUP_CAKE"))
//                .discount(randomDiscount)
//                .tags(randomTags)
//                .build();
//
//        products.add(product);
//    }
//
//            productRepository.saveAll(products);
}