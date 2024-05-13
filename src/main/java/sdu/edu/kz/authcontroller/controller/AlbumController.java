package sdu.edu.kz.authcontroller.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sdu.edu.kz.authcontroller.entity.Account;
import sdu.edu.kz.authcontroller.entity.Album;
import sdu.edu.kz.authcontroller.payload.auth.AccountDTO;
import sdu.edu.kz.authcontroller.payload.auth.album.AlbumPayloadDTO;
import sdu.edu.kz.authcontroller.payload.auth.album.AlbumViewDTO;
import sdu.edu.kz.authcontroller.services.AccountService;
import sdu.edu.kz.authcontroller.services.AlbumService;
import sdu.edu.kz.authcontroller.util.constants.AlbumError;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/album")
@Tag(name = "Album Controller", description = "Controller for Album and photo management")
@AllArgsConstructor
public class AlbumController {
    private final AlbumService albumService;
    private final AccountService accountService;

    @PostMapping(value = "/add", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "Album added successfully")
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

            return new ResponseEntity<>(new AlbumViewDTO(album.getAlbumId(), album.getAlbumName(), album.getAlbumDescription()), HttpStatus.OK);
        } catch (Exception e) {
            log.debug(AlbumError.ALBUM_ADD_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
