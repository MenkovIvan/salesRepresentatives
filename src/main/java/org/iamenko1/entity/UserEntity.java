package org.iamenko1.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long userId;
    private String login;
    private String password;
    private String type;
    private String fio;
    private String telephoneNumber;

    @OneToOne(mappedBy = "userEntity")
    private ClientEntity clientEntity;

    @OneToOne(mappedBy = "userEntity")
    private CompanyEntity companyEntity;

    @OneToOne(mappedBy = "userEntity")
    private SalesRepresentativeEntity salesRepresentativeEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(login, that.login) && Objects.equals(password, that.password) && Objects.equals(type, that.type) && Objects.equals(fio, that.fio) && Objects.equals(telephoneNumber, that.telephoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, login, password, type, fio, telephoneNumber);
    }
}
