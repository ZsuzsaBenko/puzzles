package com.codecool.zsuzsi.puzzlesbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("test")
class UploadControllerTest {

    private static final String UPLOAD_URL = "/upload";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UploadController uploadController;


    @Test
    @WithMockUser
    public void testSaveImage() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "image.jpeg",
                MediaType.APPLICATION_OCTET_STREAM_VALUE, "test-image".getBytes());

        mockMvc.perform(
                    MockMvcRequestBuilders.multipart(UPLOAD_URL).file(file)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }


    @Test
    public void getImageTest() throws Exception {
        String fileName = "image.jpeg";
        MockMultipartFile file = new MockMultipartFile("file", fileName,
                MediaType.APPLICATION_OCTET_STREAM_VALUE, "test-image".getBytes());
        uploadController.saveImage(file);

        MvcResult mvcResult = mockMvc
                .perform(
                        get("/image-resource/{filename}", fileName)
                )
            .andExpect(status().isOk())
            .andReturn();
        String responseBodyContentType = mvcResult.getResponse().getContentType();

        assertEquals(MediaType.IMAGE_JPEG.toString(), responseBodyContentType);
    }

    @AfterEach
    public void deleteFile() {
        String path = "/home/zsuzsi/Dokumentumok/Codecool/JobHunt/pet-projects/puzzles/images/image.jpeg";
        File file = new File(path);

        if(file.delete()) {
            log.info("File deleted successfully");
        } else {
            log.info("Failed to delete the file");
        }
    }
}