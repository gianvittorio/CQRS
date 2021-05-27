package com.gianvittorio.estore.ProductService.command.rest;

import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.springboot.autoconfig.EventProcessingAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
public class EventsReplayController {

    private final EventProcessingAutoConfiguration eventProcessingAutoConfiguration;

    @PostMapping(path = "/eventProcessor/{processorName}/reset")
    public ResponseEntity<String> replayEvents(@PathVariable String processorName) {
        Optional<TrackingEventProcessor> trackingEventProcessor = eventProcessingAutoConfiguration.eventProcessingModule()
                .eventProcessor(processorName, TrackingEventProcessor.class);

        if (trackingEventProcessor.isPresent()) {
            TrackingEventProcessor eventProcessor = trackingEventProcessor.get();

            eventProcessor.shutDown();
            eventProcessor.resetTokens();
            eventProcessor.start();

            return ResponseEntity.ok()
                    .body(String.format("The event processor with a name [%s] has been reset", processorName));
        }

        return ResponseEntity.badRequest()
                .body(String.format("The event processor with a name [%s] is not a tracking event processor," +
                        " Only Tracking event processor is supported", processorName));
    }
}
