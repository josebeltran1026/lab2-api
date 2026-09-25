package com.gcu.lab2api.album;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public List<AlbumDto> getAllAlbums() {
        return albumRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<AlbumDto> getAlbumById(Integer id) {
        return albumRepository.findById(id)
                .map(this::toDto);
    }

    public AlbumDto createAlbum(AlbumDto dto) {
        Album album = new Album();
        album.setAlbumId(dto.albumId());
        album.setTitle(dto.title());
        album.setArtistId(dto.artistId());

        return toDto(albumRepository.save(album));
    }

    public Optional<AlbumDto> updateAlbum(Integer id, AlbumDto dto) {
        return albumRepository.findById(id)
                .map(existingAlbum -> {
                    existingAlbum.setTitle(dto.title());
                    existingAlbum.setArtistId(dto.artistId());

                    return toDto(albumRepository.save(existingAlbum));
                });
    }

    public boolean deleteAlbum(Integer id) {
        if (!albumRepository.existsById(id)) {
            return false;
        }

        albumRepository.deleteById(id);
        return true;
    }

    private AlbumDto toDto(Album album) {
        return new AlbumDto(
                album.getAlbumId(),
                album.getTitle(),
                album.getArtistId()
        );
    }
}