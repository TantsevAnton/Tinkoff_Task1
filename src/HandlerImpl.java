import java.util.concurrent.atomic.AtomicReference;

public class HandlerImpl implements Handler {

    private Client client;

    public HandlerImpl(Client client) {
        this.client = client;
    }


    @Override
    public ApplicationStatusResponse performOperation(String id) {
        AtomicReference<Response> result = new AtomicReference<>();
        Runnable extractorThread1 = () -> { result.set(client.getApplicationStatus1(id)); notify(); };
        Runnable extractorThread2 = () -> { result.set(client.getApplicationStatus2(id)); notify(); };
        new Thread(extractorThread1).start();
        new Thread(extractorThread2).start();
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка получения статуса", e);
        }
        Response response = result.get();

        if (response instanceof Response.Success) {
            return new ApplicationStatusResponse.Success(((Response.Success) response).applicationId(), ((Response.Success) response).applicationStatus());
        }

        return null;

    }
}
