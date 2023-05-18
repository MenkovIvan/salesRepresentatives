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

    @OneToMany(mappedBy = "salesRepresentative")
    private Set<TaskEntity> tasks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesRepresentativeEntity that = (SalesRepresentativeEntity) o;
        return Objects.equals(salesRepId, that.salesRepId) && Objects.equals(hoursOfWorking, that.hoursOfWorking) && Objects.equals(email, that.email) && Objects.equals(numOfTasks, that.numOfTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salesRepId, hoursOfWorking, email, numOfTasks);
    }
}
