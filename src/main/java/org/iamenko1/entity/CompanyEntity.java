package org.iamenko1.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "companies")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "company_id")
    private Long companyId;
    private String name;
    private String inn;
    @Column(name = "num_of_orders")
    private Integer numOfOrders;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

    @ManyToMany(mappedBy = "companies")
    private Set<SalesRepresentativeEntity> salesRepresentatives;

    @OneToMany(mappedBy = "company")
    private  Set<ProductEntity> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity that = (CompanyEntity) o;
        return Objects.equals(companyId, that.companyId) && Objects.equals(name, that.name) && Objects.equals(inn, that.inn) && Objects.equals(numOfOrders, that.numOfOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, name, inn, numOfOrders);
    }
}
