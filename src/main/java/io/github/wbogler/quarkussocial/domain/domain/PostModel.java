package io.github.wbogler.quarkussocial.domain.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "posts")
@Entity
public class PostModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_text")
    private String post;

    @Column(name = "datatime")
    private LocalDateTime postTime;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserModel userModel;
}
