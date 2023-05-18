package org.iamenko1.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "client_id")
    private Long clientId;

    @Builder.Default
    private Instant createdAt = Instant.now();
    private String address;
    private Integer numberOfOrders;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

    @OneToOne(mappedBy = "client")
    private OrderEntity order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientEntity that = (ClientEntity) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(createdAt, that.createdAt) && Objects.equals(address, that.address) && Objects.equals(numberOfOrders, that.numberOfOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, createdAt, address, numberOfOrders);
    }
}
