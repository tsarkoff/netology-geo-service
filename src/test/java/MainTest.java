import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    public void testMain() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Main.main(null)
        );
    }
}