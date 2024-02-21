package ru.netology.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceMock;
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

    @ParameterizedTest
    @CsvSource(value = {
            // ip; city; country; street; building;
            "127.0.0.1,     '{\"city\":null,\"country\":\"LOCALHOST\",\"street\":null,\"building\":0}'",
            "172.0.32.11,   '{\"city\":\"Moscow\",\"country\":\"RUSSIA\",\"street\":\"Lenina\",\"building\":15}'",
            "172.0.0.1,     '{\"city\":\"Moscow\",\"country\":\"RUSSIA\",\"street\":null,\"building\":0}'",
            "96.44.183.149, '{\"city\":\"New York\",\"country\":\"USA\",\"street\":\"10th Avenue\",\"building\":32}'",
            "96.0.0.1,      '{\"city\":\"New York\",\"country\":\"USA\",\"street\":null,\"building\":0}'"
    })
    public void testGetLocationByIp(String ip, String location) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Location expectedLocation = mapper.readValue(location, Location.class);
        Mockito.when(geoService.byIp(Mockito.anyString())).thenReturn(expectedLocation);

        Location actualLocation = geoService.byIp(ip);
        Assertions.assertEquals(expectedLocation, actualLocation);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(geoService).byIp(argumentCaptor.capture());
        Assertions.assertEquals(ip, argumentCaptor.getValue());
    }

    @Test
    public void testGetLocationByCoordinates() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> new GeoServiceMock().byCoordinates(0F, 0F)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            // country; message;
            "LOCALHOST,  'Hello world!'",
            "RUSSIA,     'Добро пожаловать'",
            "USA,        'Welcome'"
    })
    public void testMessageLocalization(String country, String expectedMessage) {
        Country countryEnum = Country.valueOf(country);
        Mockito.when(localizationService.locale(Mockito.any())).thenReturn(expectedMessage);

        String actualMessage = localizationService.locale(countryEnum);
        Assertions.assertEquals(expectedMessage, actualMessage);

        ArgumentCaptor<Country> argumentCaptor = ArgumentCaptor.forClass(Country.class);
        Mockito.verify(localizationService).locale(argumentCaptor.capture());
        Assertions.assertEquals(countryEnum, argumentCaptor.getValue());

        LocalizationService spyLocalizationService = Mockito.spy(LocalizationService.class);
        Mockito.when(spyLocalizationService.locale(Mockito.any())).thenReturn(expectedMessage);
        String spyActualMessage = spyLocalizationService.locale(countryEnum);
        Assertions.assertEquals(expectedMessage, spyActualMessage);
    }
}