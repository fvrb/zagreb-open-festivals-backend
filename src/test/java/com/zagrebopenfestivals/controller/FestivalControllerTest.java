package com.zagrebopenfestivals.controller;

import com.zagrebopenfestivals.dto.response.FestivalSummaryResponse;
import com.zagrebopenfestivals.security.JwtAuthenticationFilter;
import com.zagrebopenfestivals.security.JwtTokenProvider;
import com.zagrebopenfestivals.security.SecurityProblemDetailWriter;
import com.zagrebopenfestivals.service.FestivalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FestivalController.class)
@AutoConfigureMockMvc(addFilters = false)
class FestivalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FestivalService festivalService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private SecurityProblemDetailWriter securityProblemDetailWriter;

    @Test
    void getAllReturnsFestivalSummariesAsJson() throws Exception {
        when(festivalService.getAll()).thenReturn(List.of(
                new FestivalSummaryResponse(
                        1L,
                        "Zagreb Summer",
                        "Zagreb",
                        LocalDate.of(2026, 7, 12),
                        "https://example.com/festival.jpg",
                        "Open air festival"
                )
        ));

        mockMvc.perform(get("/festivals").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "id": 1,
                            "name": "Zagreb Summer",
                            "location": "Zagreb",
                            "date": "2026-07-12",
                            "imageUrl": "https://example.com/festival.jpg",
                            "shortDescription": "Open air festival"
                          }
                        ]
                        """));
    }
}
