package com.reactivespring.movieinfoservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

/**
 * Roman Pashkov created on 23.09.2023 inside the package - com.reactivespring.movieinfoservice.domain
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
// this is a representation of Entity in relational DB
@Document
public class MovieInfo {

    @Id
    private String movieInfoId;

    private String name;

    private Integer year;

    private List<String> cast;

    private LocalDate release_date;
}
