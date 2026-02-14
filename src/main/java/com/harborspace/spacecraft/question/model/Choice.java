package com.harborspace.spacecraft.question.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
@Entity(name = "answer_choice")
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String statement;
    private Boolean selected;
    private Integer index;

    @ManyToOne(targetEntity = Answer.class, optional = false)
    @JoinColumn(name = "answer_id", insertable = false, updatable = false)
    private Answer answer;
}