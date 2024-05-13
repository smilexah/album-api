package sdu.edu.kz.authcontroller.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sdu.edu.kz.authcontroller.entity.Album;
import sdu.edu.kz.authcontroller.repository.AlbumRepository;

@Service
@AllArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    public Album save(Album album) {
        return albumRepository.save(album);
    }
}
