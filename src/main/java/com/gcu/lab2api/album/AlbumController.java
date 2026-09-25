package com.gcu.lab2api.album;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    /**
     * Returns all albums.
     *
     * @return a list of all albums
     */
    @GetMapping
    public List<AlbumDto> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    /**
     * Returns a single album by ID.
     *
     * @param id the album ID
     * @return the album if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlbumDto> getAlbumById(@PathVariable Integer id) {
        return albumService.getAlbumById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new album.
     *
     * @param dto the album data
     * @return the created album
     */
    @PostMapping
    public ResponseEntity<AlbumDto> createAlbum(@RequestBody AlbumDto dto) {
        AlbumDto createdAlbum = albumService.createAlbum(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdAlbum);
    }

    /**
     * Updates an existing album.
     *
     * @param id the album ID
     * @param dto the updated album data
     * @return the updated album if found, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlbumDto> updateAlbum(
            @PathVariable Integer id,
            @RequestBody AlbumDto dto) {

        return albumService.updateAlbum(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes an album by ID.
     *
     * @param id the album ID
     * @return 204 if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Integer id) {
        if (albumService.deleteAlbum(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}