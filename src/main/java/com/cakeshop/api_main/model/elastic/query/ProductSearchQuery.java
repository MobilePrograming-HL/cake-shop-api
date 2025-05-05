package com.cakeshop.api_main.model.elastic.query;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.cakeshop.api_main.model.criteria.ProductCriteria;
import com.cakeshop.api_main.model.index.ProductIndex;
import com.cakeshop.api_main.utils.ConvertUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductSearchQuery {
    ElasticsearchClient client;

    public SearchResponse<ProductIndex> search(ProductCriteria criteria, Pageable pageable) throws IOException {
        int from = pageable.getPageNumber() * pageable.getPageSize();
        int size = pageable.getPageSize();

        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        if (StringUtils.hasText(criteria.getName())) {
            boolQuery.must(m -> m.multiMatch(mm -> mm
                    .fields("name^2")
                    .query(criteria.getName())
                    .fuzziness("AUTO")
                    .operator(Operator.And)
            ));
        }

        if (StringUtils.hasText(criteria.getCategoryId())) {
            boolQuery.filter(f -> f.term(t -> t
                    .field("categoryId")
                    .value(criteria.getCategoryId())
            ));
        }

        if (criteria.getFromPrice() != null || criteria.getToPrice() != null) {
            RangeQuery.Builder priceRange = new RangeQuery.Builder().field("price");
            if (criteria.getFromPrice() != null) {
                priceRange.gte(JsonData.of(criteria.getFromPrice()));
            }
            if (criteria.getToPrice() != null) {
                priceRange.lte(JsonData.of(criteria.getToPrice()));
            }
            boolQuery.filter(f -> f.range(priceRange.build()));
        }

        SearchRequest request = new SearchRequest.Builder()
                .index("products")
                .query(q -> q.bool(boolQuery.build()))
                .from(from)
                .size(size)
                .build();

        return client.search(request, ProductIndex.class);
    }
}
