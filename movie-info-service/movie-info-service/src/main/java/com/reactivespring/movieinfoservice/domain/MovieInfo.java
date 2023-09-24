package com.reactivespring.movieinfoservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

    @NotBlank(message = "Name must be present")
    private String name;

    @NotNull
    @Positive(message = "Year must be a Positive value")
    private Integer year;

    private List<String> cast;

    private LocalDate release_date;
}
