package org.iamenko1.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id")
    private Long orderId;
    private String status;

    @Builder.Default
    private Instant UpdatedAt = Instant.now();

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private ClientEntity client;

    @OneToOne(mappedBy = "order")
    private TaskEntity task;

    @OneToMany(mappedBy = "order")
    private Set<OrderBasket> productsInBasket;
}
