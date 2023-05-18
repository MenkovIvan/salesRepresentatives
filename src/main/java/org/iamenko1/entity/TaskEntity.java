package org.iamenko1.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "task_id")
    private Long taskId;
    private String status;
    @Builder.Default
    private Instant updatedBy = Instant.now();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_id", referencedColumnName = "route_id")
    private RouteEntity route;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sales_rep_id", referencedColumnName = "sales_rep_id")
    private SalesRepresentativeEntity salesRepresentative;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private OrderEntity order;
}
