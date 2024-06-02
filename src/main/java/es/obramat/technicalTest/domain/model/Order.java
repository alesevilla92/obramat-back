package es.obramat.technicalTest.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders") // We can't set order as table name because is a keyword for H2
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate;

    private double totalWithoutTax;

    @Formula("total_without_tax * 1.21")
    @Setter(AccessLevel.NONE)
    private double totalWithTax;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private Set<OrderProduct> products;

}
