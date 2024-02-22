package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class MessageSenderMockTest {
    @Mock
    public GeoService geoService;
    @Mock
    public LocalizationService localizationService;
    @InjectMocks
    public MessageSenderMock messageSender;

    @ParameterizedTest
    @CsvSource(value = {
            "127.0.0.1,     LOCALHOST,  'Hello world!'",
            "172.0.32.11б,  RUSSIA,     'Добро пожаловать'",
            "172.0.0.1,     RUSSIA,     'Добро пожаловать'",
            "96.44.183.149, USA,        'Welcome'",
            "96.0.0.1,      USA,        'Welcome'"
    })
    public void testSend(String ip, String country, String excepted) {
        Country countryEnum = Country.valueOf(country);
        Mockito.when(geoService.byIp(ip)).thenReturn(new Location(null, countryEnum, null, 0));
        Mockito.when(localizationService.locale(countryEnum)).thenReturn(excepted);

        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderMock.IP_ADDRESS_HEADER, ip);
        String actual = messageSender.send(headers);
        Assertions.assertEquals(excepted, actual);
    }
}