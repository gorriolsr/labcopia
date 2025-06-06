package com.tecnocampus.outlaws.api;

import com.tecnocampus.outlaws.application.OutlawService;
import com.tecnocampus.outlaws.application.dto.OutlawDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/outlaws")
public class OutlawRestController {

    private final OutlawService outlawService;

    public OutlawRestController(OutlawService outlawService) {
        this.outlawService = outlawService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutlawDTO createOutlaw(@RequestBody OutlawDTO outlaw) {
        return outlawService.createOutlaw(outlaw);
    }

    @GetMapping
    public List<OutlawDTO> getAllOutlaws() {
        return outlawService.getAllOutlaws();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OutlawDTO getOutlawById(@PathVariable String id) {
        return outlawService.getOutlawById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OutlawDTO updateOutlaw(@PathVariable String id, @RequestBody OutlawDTO updatedOutlaw) {
        return outlawService.updateOutlaw(id, updatedOutlaw);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OutlawDTO updateBounty(@PathVariable String id, @RequestBody Map<String, String> updates) {
        return outlawService.updateBounty(id, updates);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOutlaw(@PathVariable String id) {
        outlawService.deleteOutlaw(id);
    }

    @GetMapping("/most-wanted")
    public OutlawDTO getMostWantedOutlaw() {
        return outlawService.getMostWantedOutlaw();
    }
}
