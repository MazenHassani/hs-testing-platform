package com.harborspace.spacecraft.codingquestion.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
@Entity(name = "coding_question_judge")
public class Judge {
    @Id
    private Integer id;

    private Integer compilerId;

    @Builder.Default
    private Integer typeId = 1;

    @Lob
    @Column(name = "checker", length = 65000, columnDefinition = "BLOB")
    private String source; // checker
}