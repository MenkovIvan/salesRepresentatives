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
@Table(name = "order_basket")
public class OrderBasketEntity {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBasketEntity that = (OrderBasketEntity) o;
        return Objects.equals(orderBasketKey, that.orderBasketKey) && Objects.equals(numbers, that.numbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderBasketKey, numbers);
    }
}
