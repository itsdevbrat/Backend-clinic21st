package com.clinic.website.controller;

import com.clinic.website.entities.Vertical;
import com.clinic.website.service.VerticalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vertical")
public class VerticalController {

    private final VerticalService verticalService;

    @GetMapping
    public Flux<Vertical> getVerticals(){
        return verticalService.getVerticals();
    }

    @PostMapping
    public Mono<Vertical> saveVertical(@RequestBody Vertical vertical){
        return verticalService.saveVertical(vertical);
    }

}
