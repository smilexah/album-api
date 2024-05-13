package sdu.edu.kz.authcontroller.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.Path;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sdu.edu.kz.authcontroller.entity.Account;
import sdu.edu.kz.authcontroller.entity.Album;
import sdu.edu.kz.authcontroller.entity.Photo;
import sdu.edu.kz.authcontroller.payload.auth.album.AlbumPayloadDTO;
import sdu.edu.kz.authcontroller.payload.auth.album.AlbumViewDTO;
import sdu.edu.kz.authcontroller.services.AccountService;
import sdu.edu.kz.authcontroller.services.AlbumService;
import sdu.edu.kz.authcontroller.services.PhotoService;
import sdu.edu.kz.authcontroller.util.AppUtils.AppUtil;
import sdu.edu.kz.authcontroller.util.constants.AlbumError;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Album Controller", description = "Controller for Album and photo management")
@AllArgsConstructor
public class AlbumController {
    private final AlbumService albumService;
    private final AccountService accountService;
    private final PhotoService photoService;

    @PostMapping(value = "/albums", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "200", description = "Album added successfully")
    @ApiResponse(responseCode = "400", description = "Character limit exceeded")
    @ApiResponse(responseCode = "401", description = "Please add valid name and description")
    @Operation(summary = "Add a new Album")
    @SecurityRequirement(name = "sduedu-demo-api")
    public ResponseEntity<AlbumViewDTO> addAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO, Authentication authentication) {
        try {
            Album album = new Album();
            album.setAlbumName(albumPayloadDTO.getName());
            album.setAlbumDescription(albumPayloadDTO.getDescription());

            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);

            Account account = optionalAccount.get();
            album.setAccount(account);
            album = albumService.save(album);

            return new ResponseEntity<>(new AlbumViewDTO(album.getId(), album.getAlbumName(), album.getAlbumDescription()), HttpStatus.OK);
        } catch (Exception e) {
            log.debug(AlbumError.ALBUM_ADD_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping(value = "/albums", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Albums retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Get all Albums")
    @SecurityRequirement(name = "sduedu-demo-api")
    public List<AlbumViewDTO> getAlbums(Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);

        Account account = optionalAccount.get();

        List<AlbumViewDTO> albums = new ArrayList<>();

        for (Album album : albumService.findByAccount_id(account.getId())) {
            albums.add(new AlbumViewDTO(album.getId(), album.getAlbumName(), album.getAlbumDescription()));
        }

        return albums;
    }

    @PostMapping(value = "/albums/{album_id}/upload-photos", consumes = {"multipart/form-data"})
    @ApiResponse(responseCode = "200", description = "Photo added successfully")
    @ApiResponse(responseCode = "400", description = "Check the payload or token")
    @ApiResponse(responseCode = "403", description = "This album does not exist")
//    @ApiResponse(responseCode = "401", description = "Please add valid name and description")
    @Operation(summary = "Add a new Photo")
    @SecurityRequirement(name = "sduedu-demo-api")
    public ResponseEntity<List<String>> addPhoto(@RequestPart MultipartFile[] files, @PathVariable Long album_id, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);

        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);

        Album album;

        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();

            if (!album.getAccount().getId().equals(account.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<String> fileNamesWithSuccess = new ArrayList<>();
        List<String> fileNamesWithError = new ArrayList<>();

        Arrays.asList(files).stream().forEach(file -> {
            String contentType = file.getContentType();

            if (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg")) {
                fileNamesWithSuccess.add(file.getOriginalFilename());

                int length = 10;
                boolean useLetters = true;
                boolean useNumbers = true;

                try {
                    String fileName = file.getOriginalFilename();
                    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
                    String finalPhotoName = generatedString + fileName;
                    String absoluteFileLocation = AppUtil.getPhotoUploadPath(finalPhotoName, album_id);
                    Path path = Paths.get(absoluteFileLocation);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    Photo photo = new Photo();
                    photo.setPhotoName(fileName);
                    photo.setFileName(finalPhotoName);
                    photo.setOriginalFileName(fileName);
                    photo.setAlbum(album);

                    photoService.save(photo);
                } catch (Exception e) {
                    log.debug(AlbumError.ALBUM_PHOTO_ADD_ERROR.toString() + ": " + e.getMessage());
                }
            } else {
                fileNamesWithError.add(file.getOriginalFilename());
            }
        });

        return fileNamesWithSuccess.isEmpty()? ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) : ResponseEntity.ok(fileNamesWithSuccess);
    }
}
