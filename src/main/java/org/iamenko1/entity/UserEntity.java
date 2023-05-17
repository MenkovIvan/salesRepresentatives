package org.iamenko1.entity;

import lombok.*;

import javax.persistence.*;

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
}
