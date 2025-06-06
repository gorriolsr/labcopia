package com.tecnocampus.outlaws.api;

import com.tecnocampus.outlaws.application.SheriffService;
import com.tecnocampus.outlaws.application.dto.OutlawDTO;
import com.tecnocampus.outlaws.application.dto.SheriffDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sheriffs")
public class SheriffRestController {

    private final SheriffService sheriffService;

    public SheriffRestController(SheriffService sheriffService) {
        this.sheriffService = sheriffService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SheriffDTO createSheriff(@RequestBody SheriffDTO sheriffDTO) {
        return sheriffService.createSheriff(sheriffDTO);
    }

    @GetMapping
    public List<SheriffDTO> getAllSheriffs() {
        return sheriffService.getAllSheriffs();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SheriffDTO getSheriffById(@PathVariable String id) {
        return sheriffService.getSheriffById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SheriffDTO updateSheriff(@PathVariable String id, @RequestBody SheriffDTO sheriffToUpdate) {
        return sheriffService.updateSheriff(id, sheriffToUpdate);
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SheriffDTO updateSheriffField(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        return sheriffService.updateSheriffField(id, updates);
    }

    @PostMapping("/{sheriffId}/capture/{outlawId}")
    @ResponseStatus(HttpStatus.OK)
    public OutlawDTO captureOutlaw(@PathVariable String sheriffId, @PathVariable String outlawId) {
        return sheriffService.captureOutlaw(sheriffId, outlawId);
    }

    @PostMapping("/{sheriffId}/eliminate/{outlawId}")
    @ResponseStatus(HttpStatus.OK)
    public OutlawDTO eliminateOutlaw(@PathVariable String sheriffId, @PathVariable String outlawId) {
        return sheriffService.eliminateOutlaw(sheriffId, outlawId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSheriff(@PathVariable String id) {
        sheriffService.deleteSheriff(id);
    }
}