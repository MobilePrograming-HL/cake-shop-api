package com.cakeshop.api_main.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.ExistsRequest;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElasticsearchIndexInitializer {
    ElasticsearchClient client;
    @PostConstruct
    public void createIndexIfNotExists() throws IOException {
        String indexName = "products";

        boolean exists = client.indices().exists(e -> e.index(indexName)).value();
        if (exists) return;

        client.indices().create(c -> c
                .index(indexName)
                .settings(s -> s.analysis(a -> a
                        .analyzer("vi_fuzzy_analyzer", an -> an
                                .custom(ca -> ca
                                        .tokenizer("standard")
                                        .filter("lowercase", "asciifolding")
                                )
                        )
                ))
                .mappings(m -> m
                        .properties("name", p -> p.text(t -> t.analyzer("vi_fuzzy_analyzer")))
                        .properties("price", p -> p.double_(d -> d))
                        .properties("categoryId", p -> p.keyword(k -> k))
                        .properties("createdAt", p -> p.date(d -> d))
                        .properties("totalSold", p -> p.long_(l -> l))
                )
        );

        System.out.println("✅ Index 'products' created successfully.");
    }
}
