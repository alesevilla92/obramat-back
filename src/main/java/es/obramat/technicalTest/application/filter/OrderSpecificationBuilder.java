package es.obramat.technicalTest.application.filter;

import es.obramat.technicalTest.domain.model.Order;
import es.obramat.technicalTest.infrastructure.filter.OrderFilter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class OrderSpecificationBuilder {

    private final OrderFilter config;
    private Specification<Order> specification;

    public OrderSpecificationBuilder(OrderFilter config) {
        this.config = config;
    }

    public Specification<Order> buildSpecification() {
        specification = Specification.where(null);

        addDateRangeFilter("creationDate", config.getStartDate(), config.getEndDate());
        addPriceRangeFilter("totalWithTax", config.getMinPrice(), config.getMaxPrice());
        addFilter("state", config.getState());

        return specification;
    }

    private void addDateRangeFilter(String fieldName, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            specification = specification.and((root, query, builder) ->
                    builder.between(root.get(fieldName), startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
        } else if (startDate != null) {
            specification = specification.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get(fieldName), startDate.atStartOfDay()));
        } else if (endDate != null) {
            specification = specification.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get(fieldName), endDate.atTime(23, 59, 59)));
        }
    }

    private void addPriceRangeFilter(String fieldName, Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null) {
            specification = specification.and((root, query, builder) ->
                    builder.between(root.get(fieldName), minPrice, maxPrice));
        } else if (minPrice != null) {
            specification = specification.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get(fieldName), minPrice));
        } else if (maxPrice != null) {
            specification = specification.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get(fieldName), maxPrice));
        }
    }

    private void addFilter(String fieldName, Object value) {
        if (value != null) {
            specification = specification.and((root, query, builder) -> builder.equal(root.get(fieldName), value));
        }
    }
}