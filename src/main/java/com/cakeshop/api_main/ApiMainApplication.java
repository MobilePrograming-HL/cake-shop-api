package com.cakeshop.api_main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMainApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner initData(ICategoryRepository categoryRepository, ITagRepository tagRepository, IProductRepository productRepository) {
//        return args -> {
//            List<Category> categories = categoryRepository.findAll();
//            List<Tag> tags = tagRepository.findAll();
//
//            if (categories.isEmpty() || tags.isEmpty()) {
//                throw new RuntimeException("Cần có dữ liệu Category và Tag trước khi tạo Product.");
//            }
//
//            List<Product> products = Arrays.asList(
//                    Product.builder()
//                            .name("Bánh kem socola")
//                            .price(200000.0)
//                            .description("Bánh kem socola thơm ngon, phù hợp cho tiệc sinh nhật.")
//                            .quantity(10L)
//                            .status(1)
//                            .images(Arrays.asList("chocolate_cake1.jpg", "chocolate_cake2.jpg"))
//                            .category(findCategoryByCode(categories, "BIRTH_CAKE"))
//                            .tags(findTagsByCodes(tags, "CHOCO"))
//                            .build(),
//
//                    Product.builder()
//                            .name("Cupcake dâu tây")
//                            .price(50000.0)
//                            .description("Cupcake dâu tây nhỏ gọn, dễ thương, vị ngọt dịu.")
//                            .quantity(20L)
//                            .status(1)
//                            .images(Arrays.asList("strawberry_cupcake1.jpg", "strawberry_cupcake2.jpg"))
//                            .category(findCategoryByCode(categories, "CUPCAKE"))
//                            .tags(findTagsByCodes(tags, "CUPCAKE", "STRAWBERRY"))
//                            .build(),
//
//                    Product.builder()
//                            .name("Cheesecake việt quất")
//                            .price(150000.0)
//                            .description("Bánh cheesecake việt quất thơm béo, ngọt ngào.")
//                            .quantity(15L)
//                            .status(1)
//                            .images(Arrays.asList("blueberry_cheesecake1.jpg", "blueberry_cheesecake2.jpg"))
//                            .category(findCategoryByCode(categories, "CHEESE_CAKE"))
//                            .tags(findTagsByCodes(tags, "CHEESE", "BLUEBERRY"))
//                            .build(),
//
//                    Product.builder()
//                            .name("Bánh trà xanh matcha")
//                            .price(180000.0)
//                            .description("Bánh kem vị trà xanh matcha, thơm mát và bổ dưỡng.")
//                            .quantity(12L)
//                            .status(1)
//                            .images(Arrays.asList("matcha_cake1.jpg", "matcha_cake2.jpg"))
//                            .category(findCategoryByCode(categories, "BIRTH_CAKE"))
//                            .tags(findTagsByCodes(tags, "MATCHA"))
//                            .build(),
//
//                    Product.builder()
//                            .name("Cupcake vani truyền thống")
//                            .price(45000.0)
//                            .description("Cupcake vani truyền thống, mềm mịn và ngọt nhẹ.")
//                            .quantity(25L)
//                            .status(1)
//                            .images(Arrays.asList("vanilla_cupcake1.jpg", "vanilla_cupcake2.jpg"))
//                            .category(findCategoryByCode(categories, "CUPCAKE"))
//                            .tags(findTagsByCodes(tags, "CUPCAKE", "VANILLA"))
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
//    private List<Tag> findTagsByCodes(List<Tag> tags, String... codes) {
//        return tags.stream()
//                .filter(tag -> Arrays.asList(codes).contains(tag.getCode()))
//                .collect(Collectors.toList());
//    }
}