package es.obramat.technicalTest.infrastructure.persistence;

import es.obramat.technicalTest.domain.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    Optional<OrderProduct> findByOrderIdAndProductId(Long orderId, Long productId);

    List<OrderProduct> findAllByOrderId(Long orderId);

}
