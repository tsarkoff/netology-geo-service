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

        // ЧТО НУЖНО СДЕЛАТЬ (из задания https://github.com/netology-code/jd-homeworks/blob/master/mocks/task1/README.md)

        // 1 - Проверка определения локации по ip (класс GeoServiceImpl, метод public Location byIp(String ip))
        Mockito.when(geoService.byIp(ip)).thenReturn(new Location(null, countryEnum, null, 0));

        // 2 - Проверка возвращаемого текста (класс LocalizationServiceImpl, метод public String locale(Country country))
        Mockito.when(localizationService.locale(countryEnum)).thenReturn(excepted);

        // 3 - Поверка, что MessageSenderImpl всегда отправляет только русский текст, если ip относится к российскому сегменту адресов.
        // 4 - Поверка, что MessageSenderImpl всегда отправляет только английский текст, если ip относится к американскому сегменту адресов.
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderMock.IP_ADDRESS_HEADER, ip);
        String actual = messageSender.send(headers);
        Assertions.assertEquals(excepted, actual);
    }
}