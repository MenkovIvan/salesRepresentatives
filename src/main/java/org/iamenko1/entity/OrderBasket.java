package org.iamenko1.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_basket")
public class OrderBasket {
    @EmbeddedId
    private OrderBasketKey orderBasketKey;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private OrderEntity order;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("productId")
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private ProductEntity product;

    private Integer numbers;
}
