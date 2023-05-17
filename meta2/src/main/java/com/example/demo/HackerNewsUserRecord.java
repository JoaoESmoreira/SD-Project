package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // This indicates that any properties not bound in this type should be
                                            // ignored.
public record HackerNewsUserRecord(
        Long created,
        String id,
        Integer karma,
        List submitted

) {
}
