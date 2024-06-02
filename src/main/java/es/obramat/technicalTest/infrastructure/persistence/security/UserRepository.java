package es.obramat.technicalTest.infrastructure.persistence.security;

import es.obramat.technicalTest.domain.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
