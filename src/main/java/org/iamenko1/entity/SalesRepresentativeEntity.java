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
@Table(name = "salesRepresentatives")
public class SalesRepresentativeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "sales_rep_id")
    private Long salesRepId;
    @Column(name = "hours_of_working")
    private String hoursOfWorking;
    private String email;
    @Column(name = "num_of_tasks")
    private Integer numOfTasks;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

    @ManyToMany
    @JoinTable(
            name = "salesRep_companies",
            joinColumns = @JoinColumn(name = "sales_rep_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id"))
    Set<CompanyEntity> companies;

    @OneToOne(mappedBy = "salesRepresentative")
    private TaskEntity task;
}
