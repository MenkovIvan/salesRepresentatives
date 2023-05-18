package org.iamenko1.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteEntity that = (RouteEntity) o;
        return Objects.equals(routeId, that.routeId) && Objects.equals(name, that.name) && Objects.equals(totalLength, that.totalLength) && Objects.equals(time, that.time) && Objects.equals(numOfStorages, that.numOfStorages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId, name, totalLength, time, numOfStorages);
    }
}
