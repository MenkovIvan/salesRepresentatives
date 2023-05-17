package org.iamenko1.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "storages")
public class StorageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "storage_id")
    private Long storageId;
    private String name;
    private String address;
    @OneToMany(mappedBy = "storage")
    private Set<ProductEntity> products;
}
