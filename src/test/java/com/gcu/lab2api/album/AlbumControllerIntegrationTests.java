package com.gcu.lab2api.album;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AlbumControllerIntegrationTests {

    private static final Integer TEST_ALBUM_ID = 9999;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlbumRepository albumRepository;

    @BeforeEach
    void setUp() {
        albumRepository.deleteById(TEST_ALBUM_ID);
    }

    @AfterEach
    void cleanUp() {
        albumRepository.deleteById(TEST_ALBUM_ID);
    }

    @Test
    void getAlbumByIdReturnsAlbum() throws Exception {
        mockMvc.perform(get("/api/albums/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.albumId").value(1));
    }

    @Test
    void getAllAlbumsReturnsAlbums() throws Exception {
        mockMvc.perform(get("/api/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createAlbumReturnsCreatedAlbum() throws Exception {
        String requestBody = """
                {
                    "albumId": 9999,
                    "title": "Integration Test Album",
                    "artistId": 1
                }
                """;

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.albumId").value(TEST_ALBUM_ID))
                .andExpect(jsonPath("$.title").value("Integration Test Album"))
                .andExpect(jsonPath("$.artistId").value(1));
    }

    @Test
    void updateAlbumReturnsUpdatedAlbum() throws Exception {
        Album album = new Album();
        album.setAlbumId(TEST_ALBUM_ID);
        album.setTitle("Original Test Album");
        album.setArtistId(1);
        albumRepository.save(album);

        String requestBody = """
                {
                    "albumId": 9999,
                    "title": "Updated Test Album",
                    "artistId": 1
                }
                """;

        mockMvc.perform(put("/api/albums/{id}", TEST_ALBUM_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.albumId").value(TEST_ALBUM_ID))
                .andExpect(jsonPath("$.title").value("Updated Test Album"));
    }

    @Test
    void deleteAlbumReturnsNoContent() throws Exception {
        Album album = new Album();
        album.setAlbumId(TEST_ALBUM_ID);
        album.setTitle("Album To Delete");
        album.setArtistId(1);
        albumRepository.save(album);

        mockMvc.perform(delete("/api/albums/{id}", TEST_ALBUM_ID))
                .andExpect(status().isNoContent());
    }
}