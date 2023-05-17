package org.iamenko1.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "routes")
public class RouteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "route_id")
    private Long routeId;
    private String name;
    @Column(name = "total_length")
    private String totalLength;
    private Date time;
    @Column(name = "num_of_storages")
    private Integer numOfStorages;

    @OneToOne(mappedBy = "route")
    private TaskEntity task;
}
