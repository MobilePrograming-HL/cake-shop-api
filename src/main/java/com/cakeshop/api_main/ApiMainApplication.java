package com.cakeshop.api_main;

import com.cakeshop.api_main.model.Category;
import com.cakeshop.api_main.model.Discount;
import com.cakeshop.api_main.model.Product;
import com.cakeshop.api_main.model.Tag;
import com.cakeshop.api_main.repository.internal.ICategoryRepository;
import com.cakeshop.api_main.repository.internal.IDiscountRepository;
import com.cakeshop.api_main.repository.internal.IProductRepository;
import com.cakeshop.api_main.repository.internal.ITagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableFeignClients
public class ApiMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMainApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner initData(ICategoryRepository categoryRepository, ITagRepository tagRepository, IProductRepository productRepository, IDiscountRepository discountRepository) {
//        return args -> {
//            List<Category> categories = categoryRepository.findAll();
//            List<Tag> tags = tagRepository.findAll();
//            List<Discount> discounts = discountRepository.findAll();
//
//            if (categories.isEmpty() || tags.isEmpty()) {
//                throw new RuntimeException("Cần có dữ liệu Category và Tag trước khi tạo Product.");
//            }
//
//            List<Product> products = Arrays.asList(
//                    Product.builder()
//                            .name("Hương Tình Yêu")
//                            .price(525000.0)
//                            .description("Bánh cưới 'Hương Tình Yêu' mang đến sự ngọt ngào và lãng mạn, với lớp kem mềm mịn cùng thiết kế hoa trang nhã, lý tưởng cho ngày trọng đại.")
//                            .quantity(10L)
//                            .status(1)
//                            .images(Arrays.asList(
//                                    "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744210471/wedding-3_dlteib.webp",
//                                    "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744210338/wedding-1_robivv.webp",
//                                    "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744210509/wedding-2_mljufv.webp"
//                            ))
//                            .category(findCategoryByCode(categories, "WEDDING_CAKE"))
//                            .discount(findDiscountByCode(discounts, "DISCOUNT_10"))
//                            .tags(findTagsByCodes(tags, "VANILLA", "CHEESE"))
//                            .build(),
//
//                    Product.builder()
//                            .name("Socola Ấm Áp")
//                            .price(15000.0)
//                            .description("Cupcake chocolate nhỏ gọn, dễ thương, vị ngọt dịu.")
//                            .quantity(50L)
//                            .status(1)
//                            .images(Arrays.asList(
//                                    "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744208115/product-10_q4cevx.webp",
//                                    "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744213474/cupcake-4_whytdy.webp"))
//                            .category(findCategoryByCode(categories, "CUP_CAKE"))
//                            .discount(findDiscountByCode(discounts, "DISCOUNT_5"))
//                            .tags(findTagsByCodes(tags, "CHOCOLATE"))
//                            .build(),
//
//                    Product.builder()
//                            .name("Kem Mây Bồng Bềnh")
//                            .price(15000.0)
//                            .description("Cupcake nhỏ gọn, dễ thương, vị ngọt dịu.")
//                            .quantity(40L)
//                            .status(1)
//                            .images(Arrays.asList(
//                                    "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744213475/cupcake-3_doaecs.webp",
//                                    "https://res.cloudinary.com/dcxgx3ott/image/upload/v1744213474/cupcake-2_vgtxpx.webp"))
//                            .category(findCategoryByCode(categories, "CUP_CAKE"))
//                            .discount(findDiscountByCode(discounts, "DISCOUNT_5"))
//                            .tags(findTagsByCodes(tags, "CHOCOLATE", "CHEESE", "FRUIT"))
//                            .build(),
//
//                    Product.builder()
//                            .name("Cupcake vani truyền thống")
//                            .price(15000.0)
//                            .description("Cupcake vani truyền thống, mềm mịn và ngọt nhẹ.")
//                            .quantity(25L)
//                            .status(1)
//                            .images(Arrays.asList("https://res.cloudinary.com/dcxgx3ott/image/upload/v1744214185/cupcake-5_jrvzfq.webp"))
//                            .category(findCategoryByCode(categories, "CUP_CAKE"))
//                            .discount(findDiscountByCode(discounts, "DISCOUNT_5"))
//                            .tags(findTagsByCodes(tags, "VANILLA"))
//                            .build(),
//
//                    Product.builder()
//                            .name("Bông Lan Cacao Đậm Vị")
//                            .price(450000.0)
//                            .description("Bông Lan Cacao đậm vị thơm béo.")
//                            .quantity(20L)
//                            .status(1)
//                            .images(Arrays.asList("https://res.cloudinary.com/dcxgx3ott/image/upload/v1744211374/sponge-3_qsse7y.png"))
//                            .category(findCategoryByCode(categories, "SPONGE_CAKE"))
//                            .discount(findDiscountByCode(discounts, "DISCOUNT_20"))
//                            .tags(findTagsByCodes(tags, "CHOCOLATE", "CHEESE", "COFFEE"))
//                            .build(),
//
//                    Product.builder()
//                            .name("Bông Lan Kem Dâu Hồng")
//                            .price(550000.0)
//                            .description("Bông Lan Kem Dâu Hồng đậm vị thơm béo.")
//                            .quantity(20L)
//                            .status(1)
//                            .images(Arrays.asList("https://res.cloudinary.com/dcxgx3ott/image/upload/v1744211374/sponge-3_qsse7y.png"))
//                            .category(findCategoryByCode(categories, "SPONGE_CAKE"))
//                            .discount(findDiscountByCode(discounts, "DISCOUNT_10"))
//                            .tags(findTagsByCodes(tags, "STRAWBERRY", "CHEESE", "FRUIT"))
//                            .build()
//            );
//
//            productRepository.saveAll(products);
//        };
//    }
//    private Category findCategoryByCode(List<Category> categories, String code) {
//        return categories.stream()
//                .filter(category -> category.getCode().equals(code))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy Category với code: " + code));
//    }
//
//    private Discount findDiscountByCode(List<Discount> discounts, String code) {
//        return discounts.stream()
//                .filter(category -> category.getCode().equals(code))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy discount với code: " + code));
//    }
//
//    private List<Tag> findTagsByCodes(List<Tag> tags, String... codes) {
//        return tags.stream()
//                .filter(tag -> Arrays.asList(codes).contains(tag.getCode()))
//                .collect(Collectors.toList());
//    }
}