package de.beooo79.fplfox.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.beooo79.fplfox.service.RouteFinderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/fplfox/route-finder")
@RequiredArgsConstructor
public class RouteFinderController {

    private final RouteFinderService routeFinderService;

    @GetMapping
    public ResponseEntity<String> getRouteFinderRoute(@RequestParam String ADEP, @RequestParam String ADES)
            throws IOException {
        return ResponseEntity.ok(routeFinderService.getRoute(ADEP, ADES));
    }
}
