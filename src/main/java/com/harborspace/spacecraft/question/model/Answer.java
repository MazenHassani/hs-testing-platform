package com.harborspace.spacecraft.question.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Number.class, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "answer_number_id", insertable = false, updatable = false)
    private Number number;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, targetEntity = Choice.class, mappedBy = "answer")
    private List<Choice> choices;


}
