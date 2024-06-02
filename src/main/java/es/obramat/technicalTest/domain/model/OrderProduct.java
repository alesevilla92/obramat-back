package es.obramat.technicalTest.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    @EqualsAndHashCode.Include
    private Long orderId;

    @Column(name = "product_id")
    @EqualsAndHashCode.Include
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Product product;

    private double quantity;

    private double price;

}