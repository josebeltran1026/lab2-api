package com.gcu.lab2api.album;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "album")
class Album {

    @Id
    @Column(name = "album_id")
    private Integer albumId;

    @Column(name = "title", nullable = false, length = 160)
    private String title;

    @Column(name = "artist_id", nullable = false)
    private Integer artistId;

    Album() {
    }

    Integer getAlbumId() {
        return albumId;
    }

    void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    Integer getArtistId() {
        return artistId;
    }

    void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }
}