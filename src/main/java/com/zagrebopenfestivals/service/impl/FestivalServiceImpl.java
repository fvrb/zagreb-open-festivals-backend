package com.zagrebopenfestivals.service.impl;

import com.zagrebopenfestivals.dto.request.FestivalRequest;
import com.zagrebopenfestivals.dto.response.FestivalDetailResponse;
import com.zagrebopenfestivals.dto.response.FestivalSummaryResponse;
import com.zagrebopenfestivals.entity.Festival;
import com.zagrebopenfestivals.exception.ResourceNotFoundException;
import com.zagrebopenfestivals.mapper.FestivalMapper;
import com.zagrebopenfestivals.repository.FestivalRepository;
import com.zagrebopenfestivals.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService {

    private final FestivalRepository festivalRepository;
    private final FestivalMapper festivalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FestivalSummaryResponse> getAll(String search) {
        List<Festival> festivals = StringUtils.hasText(search)
                ? festivalRepository.findByNameContainingIgnoreCase(search.trim())
                : festivalRepository.findAll();

        return festivals.stream()
                .map(festivalMapper::toSummaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FestivalDetailResponse getById(Long id) {
        Festival festival = festivalRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Festival", id));
        return festivalMapper.toDetailResponse(festival);
    }

    @Override
    @Transactional
    public FestivalDetailResponse create(FestivalRequest request) {
        Festival festival = Festival.builder()
                .name(request.name())
                .description(request.description())
                .location(request.location())
                .date(request.date())
                .imageUrl(request.imageUrl())
                .build();

        Festival saved = festivalRepository.save(festival);
        return festivalMapper.toDetailResponse(saved);
    }

    @Override
    @Transactional
    public FestivalDetailResponse update(Long id, FestivalRequest request) {
        Festival festival = festivalRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Festival", id));

        festival.setName(request.name());
        festival.setDescription(request.description());
        festival.setLocation(request.location());
        festival.setDate(request.date());
        festival.setImageUrl(request.imageUrl());

        return festivalMapper.toDetailResponse(festival);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!festivalRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Festival", id);
        }
        festivalRepository.deleteById(id);
    }
}
