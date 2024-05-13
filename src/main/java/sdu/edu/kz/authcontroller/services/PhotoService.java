package sdu.edu.kz.authcontroller.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sdu.edu.kz.authcontroller.entity.Photo;
import sdu.edu.kz.authcontroller.repository.PhotoRepository;

@Service
@AllArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }
}
