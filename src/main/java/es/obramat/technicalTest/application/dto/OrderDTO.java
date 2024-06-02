package es.obramat.technicalTest.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String creationTime;
    private Double totalWithoutTax;
    private Double totalWithTax;
    private String state;
    private List<OrderProductDTO> products;
}
