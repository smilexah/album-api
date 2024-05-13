package sdu.edu.kz.authcontroller.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "album_id", nullable = false)
    private Long id;

    @Column(name = "album_name", length = 100)
    private String albumName;

    @Column(name = "album_description", length = 1000)
    private String albumDescription;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;
}
