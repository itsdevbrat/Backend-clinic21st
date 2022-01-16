package com.clinic.website.service;

import com.clinic.website.entities.Vertical;
import com.clinic.website.repository.VerticalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VerticalService {

    private final VerticalRepository verticalRepository;

    public Flux<Vertical> getVerticals() {
        verticalRepository.findAll().doOnNext(System.out::println);
        return verticalRepository.findAll();
    }

    public Mono<Vertical> saveVertical(Vertical vertical) {
        return verticalRepository.findByVerticalName(vertical.getVerticalName())
                .flatMap(user -> Mono.<Vertical>error(new IllegalArgumentException("Vertical already exists with name " + vertical.getVerticalName())))
                .switchIfEmpty(Mono.defer(()-> verticalRepository.insert(vertical)))
                .doOnCancel(() -> new Exception().printStackTrace()).log();
    }

    public Mono<Vertical> getVertical(String vertical) {
        return verticalRepository.findByVerticalName(vertical);
    }
}
