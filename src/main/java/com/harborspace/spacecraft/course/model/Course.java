package com.harborspace.spacecraft.course.model;

import com.harborspace.spacecraft.invitation.model.Invitation;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Entity(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String courseCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, targetEntity = Invitation.class, mappedBy = "course")
    private List<Invitation> invited;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, targetEntity = Test.class, mappedBy = "course")
    private List<Test> tests;

    @CreatedBy
    private String creator;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
}
