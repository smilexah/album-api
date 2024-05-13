package sdu.edu.kz.authcontroller.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sdu.edu.kz.authcontroller.entity.Album;
import sdu.edu.kz.authcontroller.repository.AlbumRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    public Album save(Album album) {
        return albumRepository.save(album);
    }

    public List<Album> findByAccount_id(Long id) {
        return albumRepository.findByAccount_id(id);
    }

    public Optional<Album> findById(Long id) {
        return albumRepository.findById(id);
    }
}
