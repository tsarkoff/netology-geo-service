package ru.netology.i18n;

import ru.netology.entity.Country;

public class LocalizationServiceMock implements LocalizationService {

    public String locale(Country country) {
        switch (country) {
            case LOCALHOST:
                return "Hello world!";
            case RUSSIA:
                return "Добро пожаловать";
            default:
                return "Welcome";
        }
    }
}
