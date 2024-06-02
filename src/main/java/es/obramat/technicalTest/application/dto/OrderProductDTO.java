package es.obramat.technicalTest.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderProductDTO {
    private Long productId;
    private Double quantity;
    private Double price;
    private String name;
}
