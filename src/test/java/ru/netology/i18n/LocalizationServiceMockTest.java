package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;

public class LocalizationServiceMockTest {
    @ParameterizedTest
    @CsvSource(value = {
            // country; message;
            "LOCALHOST,  'Hello world!'",
            "RUSSIA,     'Добро пожаловать'",
            "USA,        'Welcome'"
    })
    public void testLocale(String country, String expectedMessage) {
        LocalizationService localizationService = new LocalizationServiceMock();
        Country countryEnum = Country.valueOf(country);

        String actualMessage = localizationService.locale(countryEnum);
        Assertions.assertEquals(expectedMessage, actualMessage);

        // Optional, just to touch Spy method
        LocalizationService spyLocalizationService = Mockito.spy(LocalizationService.class);
        Mockito.when(spyLocalizationService.locale(Mockito.any())).thenReturn(expectedMessage);
        String spyActualMessage = spyLocalizationService.locale(countryEnum);
        Assertions.assertEquals(expectedMessage, spyActualMessage);
    }
}