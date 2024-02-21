package ru.netology.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Location {
    private final String city;
    private final Country country;
    private final String street;
    private final int building;
}