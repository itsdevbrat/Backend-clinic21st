package com.clinic.website.controller;

import com.clinic.website.entities.Vertical;
import com.clinic.website.service.VerticalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vertical")
public class VerticalController {

    private final VerticalService verticalService;

    @GetMapping
    public Mono<List<String>> getVerticals() {
        return verticalService.getVerticals().map(Vertical::getVerticalName).collectList();
    }

    @PostMapping
    public Mono<ResponseEntity<String>> saveVertical(@RequestBody Vertical vertical) {
        return verticalService.saveVertical(vertical)
                .map(vertical1 -> ResponseEntity
                        .created(URI.create(vertical1.getVerticalName())).body("Vertical saved successfully"))
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(error.getMessage())));
    }

    @GetMapping("/id")
    public Mono<Vertical> getVerticals(@RequestParam("vertical") String vertical) {
        return verticalService.getVertical(vertical);
    }
}
