import java.util.HashMap;
import java.util.Map;

import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceMock;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceMock;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderMock;

public class Main {

    //Тестовый пример
    public static void main(String[] args) {
        GeoService geoService = new GeoServiceMock();
        LocalizationService localizationService = new LocalizationServiceMock();
        MessageSender messageSender = new MessageSenderMock(geoService, localizationService);

        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderMock.IP_ADDRESS_HEADER, "172.123.12.19");
        messageSender.send(headers);
    }
}