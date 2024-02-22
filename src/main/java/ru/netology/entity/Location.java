package ru.netology.entity;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class Location {
    private final String city;
    private final Country country;
    private final String street;
    private final int building;
}