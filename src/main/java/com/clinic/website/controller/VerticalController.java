package com.clinic.website.controller;

import com.clinic.website.entities.Vertical;
import com.clinic.website.service.VerticalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vertical")
public class VerticalController {

    private final VerticalService verticalService;

    @GetMapping
    public Mono<List<String>> getVerticals(){
        return verticalService.getVerticals().map(Vertical::getVerticalName).collectList();
    }

    @PostMapping
    public Mono<Vertical> saveVertical(@RequestBody Vertical vertical){
        return verticalService.saveVertical(vertical);
    }

}
