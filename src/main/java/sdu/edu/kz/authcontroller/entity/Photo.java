package sdu.edu.kz.authcontroller.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    private String photoName;

    private String photoDescription;

    private String originalFileName;

    private String fileName;

    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id",nullable = false)
    private Album album;
}
