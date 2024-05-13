package sdu.edu.kz.authcontroller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sdu.edu.kz.authcontroller.entity.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

}
