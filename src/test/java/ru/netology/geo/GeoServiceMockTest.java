package ru.netology.geo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.entity.Location;

public class GeoServiceMockTest {
    @ParameterizedTest
    @CsvSource(value = {
            // ip; city; country; street; building;
            "127.0.0.1,     '{\"city\":null,\"country\":\"LOCALHOST\",\"street\":null,\"building\":0}'",
            "172.0.32.11,   '{\"city\":\"Moscow\",\"country\":\"RUSSIA\",\"street\":\"Lenina\",\"building\":15}'",
            "172.0.0.1,     '{\"city\":\"Moscow\",\"country\":\"RUSSIA\",\"street\":null,\"building\":0}'",
            "96.44.183.149, '{\"city\":\"New York\",\"country\":\"USA\",\"street\":\"10th Avenue\",\"building\":32}'",
            "96.0.0.1,      '{\"city\":\"New York\",\"country\":\"USA\",\"street\":null,\"building\":0}'"
    })
    public void testByIp(String ip, String location) throws JsonProcessingException {
        GeoService geoService = new GeoServiceMock();
        ObjectMapper mapper = new ObjectMapper();
        Location expectedLocation = mapper.readValue(location, Location.class);

        Location actualLocation = geoService.byIp(ip);
        Assertions.assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testByCoordinates() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> new GeoServiceMock().byCoordinates(0F, 0F)
        );
    }
}