import java.time.Duration;

public interface Handler {
    ApplicationStatusResponse performOperation(String id);
}
