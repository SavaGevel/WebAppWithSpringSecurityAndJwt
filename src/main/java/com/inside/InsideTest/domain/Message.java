package com.inside.InsideTest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Message entity for db
 */

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;

    private LocalDateTime date;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;

}
