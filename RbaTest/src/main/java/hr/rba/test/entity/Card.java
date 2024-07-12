package hr.rba.test.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Card")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="firstName", length = 50, nullable = false)
    private String firstName;

    @Column(name="lastName", length = 50, nullable = false)
    private String lastName;

    @Column(name="oib", length = 11, nullable = false, unique = true)
    private String oib;

    @Column(name="status", length = 100)
    private String status;
}
