package es.obramat.technicalTest.domain.model.security;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users") // We can't set user as table name because is a keyword for H2
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
public class User implements Serializable {

    @Id
    private String username;

    private String password;

}