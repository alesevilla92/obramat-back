package es.obramat.technicalTest.infrastructure.filter;

import es.obramat.technicalTest.domain.model.OrderState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Getter
@Setter
public class OrderFilter {

    private LocalDate startDate;
    private LocalDate endDate;
    private OrderState state;
    private Double minPrice;
    private Double maxPrice;

    protected String sortBy;
    protected String sortDirection;
    protected Integer pageIndex;
    protected Integer pageSize;
    protected Sort sort;
}
