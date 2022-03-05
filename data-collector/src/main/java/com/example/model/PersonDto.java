package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonDto {

    @NotNull(message = "fistName is null")
    @NotBlank(message = "fistName is empty")
    @JsonProperty("first_name")
    private String firstName;

    @NotNull(message = "lastName is null")
    @NotBlank(message = "lastName is empty")
    @JsonProperty("last_name")
    private String lastName;

    @NotNull(message = "age is null")
    private int age;
}
