-- Dumping data for table db_cake_shop.tbl_group: ~2 rows (approximately)
INSERT INTO `tbl_group` (`id`, `description`, `is_system_role`, `kind`, `name`, `created_at`, `updated_at`) VALUES
    ('beaaf975-0b71-11f0-93e5-0242ac110002', 'Administrator group', b'1', 1, 'Admin', '2025-03-28 01:12:40', '2025-03-28 01:12:40'),
    ('f5ca64e7-0b71-11f0-93e5-0242ac110002', 'Customer group', b'1', 2, 'Customer', '2025-03-28 01:14:13', '2025-03-28 01:14:13');

-- Dumping data for table db_cake_shop.tbl_account: ~0 rows (approximately)
INSERT INTO `tbl_account` (`id`, `username`, `password`, `is_active`, `email`, `avatar_path`, `group_id`, `created_at`, `updated_at`) VALUES
    ('cc6b725f-0b7f-11f0-93e5-0242ac110002', 'admin', '$2a$10$6Rdxpba/3kSoI8ofL7qbuOsHFGoeBXIGyzjySVxXeuLU4MlgCSJd6', b'1', 'admin@gmail.com', '', 'beaaf975-0b71-11f0-93e5-0242ac110002', '2025-03-28 02:53:16', '2025-03-28 02:53:16');

-- Dumping data for table db_cake_shop.tbl_permission: ~9 rows (approximately)
INSERT INTO `tbl_permission` (`id`, `action`, `code`, `name`, `description`, `created_at`, `updated_at`) VALUES
     ('07d78809-0b80-11f0-93e5-0242ac110002', '/api/v1/category/update', 'CAT_UPD', 'Update category', 'Update category', '2025-03-28 02:54:56', '2025-03-28 02:54:56'),
     ('33410c77-0d79-11f0-88b5-0242ac110002', '/api/v1/nation/create', 'NAT_CRE', 'Create nation', 'Create nation', '2025-03-30 15:11:05', '2025-03-30 15:11:05'),
     ('340bb59f-0b80-11f0-93e5-0242ac110002', '/api/v1/category/delete', 'CAT_DEL', 'Delete category', 'Delete category', '2025-03-28 02:56:10', '2025-03-28 02:56:10'),
     ('3ba7c703-0c53-11f0-83d8-0242ac110002', '/api/v1/tag/delete', 'TAG_DEL', 'Delete tag', 'Delete tag', '2025-03-29 04:06:47', '2025-03-29 04:06:47'),
     ('3e00cba9-0c7d-11f0-83d8-0242ac110002', '/api/v1/product/create', 'PRO_CRE', 'Create product', 'Create product', '2025-03-29 09:07:29', '2025-03-29 09:07:29'),
     ('4898be2d-0c7d-11f0-83d8-0242ac110002', '/api/v1/product/update', 'PRO_UPD', 'Update product', 'Update product', '2025-03-29 09:07:47', '2025-03-29 09:07:47'),
     ('59d13e87-0d79-11f0-88b5-0242ac110002', '/api/v1/nation/update', 'NAT_UPD', 'Update nation', 'Update nation', '2025-03-30 15:12:09', '2025-03-30 15:12:09'),
     ('6b45edd9-0c7d-11f0-83d8-0242ac110002', '/api/v1/product/delete', 'PRO_DEL', 'Delete product', 'Delete product', '2025-03-29 09:08:45', '2025-03-29 09:08:45'),
     ('6cf0f02c-0d79-11f0-88b5-0242ac110002', '/api/v1/nation/delete', 'NAT_DEL', 'Delete nation', 'Delete nation', '2025-03-30 15:12:41', '2025-03-30 15:12:41'),
     ('8d273797-0c52-11f0-83d8-0242ac110002', '/api/v1/tag/create', 'TAG_CRE', 'Create tag', 'Create tag', '2025-03-29 04:01:54', '2025-03-29 04:01:54'),
     ('bbe39956-0c52-11f0-83d8-0242ac110002', '/api/v1/tag/update', 'TAG_UPD', 'Update tag', 'Update tag', '2025-03-29 04:03:12', '2025-03-29 04:03:12'),
     ('dc5e454f-0b7f-11f0-93e5-0242ac110002', '/api/v1/category/create', 'CAT_CRE', 'Create category', 'Create category', '2025-03-28 02:53:43', '2025-03-28 02:53:43');

-- Dumping data for table db_cake_shop.tbl_permission_group: ~9 rows (approximately)
INSERT INTO `tbl_permission_group` (`group_id`, `permission_id`) VALUES
     ('beaaf975-0b71-11f0-93e5-0242ac110002', '340bb59f-0b80-11f0-93e5-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', '07d78809-0b80-11f0-93e5-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', 'dc5e454f-0b7f-11f0-93e5-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', '8d273797-0c52-11f0-83d8-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', 'bbe39956-0c52-11f0-83d8-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', '3ba7c703-0c53-11f0-83d8-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', '3e00cba9-0c7d-11f0-83d8-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', '4898be2d-0c7d-11f0-83d8-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', '6b45edd9-0c7d-11f0-83d8-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', '33410c77-0d79-11f0-88b5-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', '59d13e87-0d79-11f0-88b5-0242ac110002'),
     ('beaaf975-0b71-11f0-93e5-0242ac110002', '6cf0f02c-0d79-11f0-88b5-0242ac110002');

-- Dumping data for table db_cake_shop.tbl_category: ~1 rows (approximately)
INSERT INTO `tbl_category` (`id`, `code`, `name`, `description`, `image`, `created_at`, `updated_at`) VALUES
    ('7751d611-3b80-4cce-b8f6-1b3130d8c992', 'WEDDING_CAKE', 'Bánh Cưới', 'Những chiếc bánh sinh nhật ngọt ngào với nhiều hương vị hấp dẫn.', 'https://th.bing.com/th/id/OIP.J96rcY0GJUH7bg6UpMcmNQHaHH?w=200&h=193&c=7&r=0&o=5&dpr=1.1&pid=1.7', '2025-03-29 10:54:11', '2025-03-29 10:54:11'),
    ('bbee11f7-4420-4bbe-9150-c8790027e580', 'BIRTH_CAKE', 'Bánh Sinh Nhật', 'Những chiếc bánh sinh nhật xinh đẹp.', '', '2025-04-04 12:15:28', '2025-04-04 12:15:28'),
    ('d187988e-5d2e-47e2-8ca2-be23e6de000f', 'CHEESE_CAKE', 'Bánh phô mai', 'Bánh cheesecake đặc biệt.', '', '2025-04-04 12:15:28', '2025-04-04 12:15:28'),
    ('e121da67-bd67-4d7c-ab82-4b84e785651e', 'CUPCAKE', 'Cupcake', 'Các loại bánh cupcake ngon và đa dạng.', '', '2025-04-04 12:15:28', '2025-04-04 12:15:28');;

-- Dumping data for table db_cake_shop.tbl_product: ~6 rows (approximately)
INSERT INTO `tbl_product` (`id`, `name`, `description`, `price`, `quantity`, `status`, `created_at`, `updated_at`, `category_id`, `discount_id`) VALUES
     ('36286e7e-5231-419b-82c0-43f474d1dedb', 'Bánh trà xanh matcha', 'Bánh kem vị trà xanh matcha, thơm mát và bổ dưỡng.', 180000, 12, 1, '2025-04-04 12:43:36.386000', '2025-04-04 12:43:36.386000', 'bbee11f7-4420-4bbe-9150-c8790027e580', NULL),
     ('abd51573-9c84-4175-9059-a197ddfb2899', 'Cheesecake việt quất', 'Bánh cheesecake việt quất thơm béo, ngọt ngào.', 150000, 15, 1, '2025-04-04 12:43:36.384000', '2025-04-04 12:43:36.384000', 'd187988e-5d2e-47e2-8ca2-be23e6de000f', NULL),
     ('c850eb1f-4b4e-4ca8-9c22-0b65240c0428', 'Bánh Cưới', 'Những chiếc bánh sinh nhật ngọt ngào với nhiều hương vị hấp dẫn.', 100000, 100, 1, '2025-03-29 19:10:23.259000', '2025-03-29 19:10:23.259000', '7751d611-3b80-4cce-b8f6-1b3130d8c992', NULL),
     ('d009db00-2e43-4b73-886e-cf0ced297fe6', 'Cupcake vani truyền thống', 'Cupcake vani truyền thống, mềm mịn và ngọt nhẹ.', 45000, 25, 1, '2025-04-04 12:43:36.388000', '2025-04-04 12:43:36.388000', 'e121da67-bd67-4d7c-ab82-4b84e785651e', NULL),
     ('e5307e40-6893-43bc-bec2-74c01b4f989b', 'Bánh kem socola', 'Bánh kem socola thơm ngon, phù hợp cho tiệc sinh nhật.', 200000, 10, 1, '2025-04-04 12:43:36.330000', '2025-04-04 12:43:36.330000', 'bbee11f7-4420-4bbe-9150-c8790027e580', NULL),
     ('e7cb92e8-a81a-4e74-bcb7-27344275006b', 'Cupcake dâu tây', 'Cupcake dâu tây nhỏ gọn, dễ thương, vị ngọt dịu.', 50000, 20, 1, '2025-04-04 12:43:36.382000', '2025-04-04 12:43:36.382000', 'e121da67-bd67-4d7c-ab82-4b84e785651e', NULL);

-- Dumping data for table db_cake_shop.tbl_product_images: ~1 rows (approximately)
INSERT INTO `tbl_product_images` (`product_id`, `image_url`) VALUES
     ('c850eb1f-4b4e-4ca8-9c22-0b65240c0428', 'https://th.bing.com/th/id/OIP.J96rcY0GJUH7bg6UpMcmNQHaHH?w=200&h=193&c=7&r=0&o=5&dpr=1.1&pid=1.7'),
     ('36286e7e-5231-419b-82c0-43f474d1dedb', 'matcha_cake1.jpg'),
     ('36286e7e-5231-419b-82c0-43f474d1dedb', 'matcha_cake2.jpg'),
     ('abd51573-9c84-4175-9059-a197ddfb2899', 'blueberry_cheesecake1.jpg'),
     ('abd51573-9c84-4175-9059-a197ddfb2899', 'blueberry_cheesecake2.jpg'),
     ('d009db00-2e43-4b73-886e-cf0ced297fe6', 'vanilla_cupcake1.jpg'),
     ('d009db00-2e43-4b73-886e-cf0ced297fe6', 'vanilla_cupcake2.jpg'),
     ('e5307e40-6893-43bc-bec2-74c01b4f989b', 'chocolate_cake1.jpg'),
     ('e5307e40-6893-43bc-bec2-74c01b4f989b', 'chocolate_cake2.jpg'),
     ('e7cb92e8-a81a-4e74-bcb7-27344275006b', 'strawberry_cupcake1.jpg'),
     ('e7cb92e8-a81a-4e74-bcb7-27344275006b', 'strawberry_cupcake2.jpg');

-- Dumping data for table db_cake_shop.tbl_tag: ~1 rows (approximately)
INSERT INTO `tbl_tag` (`id`, `code`, `name`, `created_at`, `updated_at`) VALUES
     ('375fd18f-6ee3-4325-8d1a-757974531f7e', 'COFFEE', 'Coffee', '2025-04-04 12:16:00.657000', '2025-04-04 12:16:00.657000'),
     ('4464e134-7974-4b7f-8d4d-41fb14eee46a', 'VANILLA', 'Vanilla', '2025-04-04 12:16:00.652000', '2025-04-04 12:16:00.652000'),
     ('5c79934c-0fc6-4e0c-9928-21d8b1ae56d7', 'CHEESE', 'Cheesecake', '2025-04-04 12:16:00.651000', '2025-04-04 12:16:00.651000'),
     ('606ef0e9-5dd5-4165-b499-b64577f8f324', 'FRUIT', 'Fruit', '2025-04-04 12:16:00.647000', '2025-04-04 12:16:00.647000'),
     ('8f1f608f-be88-467e-9a7a-12e6285ab72a', 'CHOCOLATE', 'Socola', '2025-03-29 11:07:29.341000', '2025-03-29 11:07:29.341000'),
     ('ab115cc9-ccea-4ddd-a4fb-33b813e633b9', 'CUPCAKE', 'Cupcake', '2025-04-04 12:16:00.649000', '2025-04-04 12:16:00.649000'),
     ('bd093f52-429a-416d-aa34-e62a7fdc0747', 'BLUEBERRY', 'Blueberry', '2025-04-04 12:16:00.655000', '2025-04-04 12:16:00.655000'),
     ('bd7cdc57-ec6b-4f89-b407-665539ac97a6', 'MATCHA', 'Matcha', '2025-04-04 12:16:00.658000', '2025-04-04 12:16:00.658000'),
     ('e5c796ad-0893-4dd5-a29b-23d49a3b3647', 'STRAWBERRY', 'Strawberry', '2025-04-04 12:16:00.653000', '2025-04-04 12:16:00.653000'); '2025-03-29 11:07:29.341000');

-- Dumping data for table db_cake_shop.tbl_product_tag: ~1 rows (approximately)
INSERT INTO `tbl_product_tag` (`product_id`, `tag_id`) VALUES
   ('d009db00-2e43-4b73-886e-cf0ced297fe6', '4464e134-7974-4b7f-8d4d-41fb14eee46a'),
   ('abd51573-9c84-4175-9059-a197ddfb2899', '5c79934c-0fc6-4e0c-9928-21d8b1ae56d7'),
   ('c850eb1f-4b4e-4ca8-9c22-0b65240c0428', '8f1f608f-be88-467e-9a7a-12e6285ab72a'),
   ('d009db00-2e43-4b73-886e-cf0ced297fe6', 'ab115cc9-ccea-4ddd-a4fb-33b813e633b9'),
   ('e7cb92e8-a81a-4e74-bcb7-27344275006b', 'ab115cc9-ccea-4ddd-a4fb-33b813e633b9'),
   ('abd51573-9c84-4175-9059-a197ddfb2899', 'bd093f52-429a-416d-aa34-e62a7fdc0747'),
   ('36286e7e-5231-419b-82c0-43f474d1dedb', 'bd7cdc57-ec6b-4f89-b407-665539ac97a6'),
   ('e7cb92e8-a81a-4e74-bcb7-27344275006b', 'e5c796ad-0893-4dd5-a29b-23d49a3b3647');