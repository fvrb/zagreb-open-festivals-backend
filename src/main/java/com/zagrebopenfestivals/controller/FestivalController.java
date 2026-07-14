package com.zagrebopenfestivals.controller;

import com.zagrebopenfestivals.dto.request.FestivalRequest;
import com.zagrebopenfestivals.dto.response.FestivalDetailResponse;
import com.zagrebopenfestivals.dto.response.FestivalSummaryResponse;
import com.zagrebopenfestivals.service.FestivalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/festivals")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;

    @GetMapping
    public List<FestivalSummaryResponse> getAll(@RequestParam(required = false) String search) {
        return festivalService.getAll(search);
    }

    @GetMapping("/{id}")
    public FestivalDetailResponse getById(@PathVariable Long id) {
        return festivalService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FestivalDetailResponse create(@Valid @RequestBody FestivalRequest request) {
        return festivalService.create(request);
    }

    @PutMapping("/{id}")
    public FestivalDetailResponse update(@PathVariable Long id, @Valid @RequestBody FestivalRequest request) {
        return festivalService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        festivalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
