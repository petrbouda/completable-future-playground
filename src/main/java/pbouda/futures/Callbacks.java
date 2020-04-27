package pbouda.futures;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Callbacks {

    public static void main(String[] args) throws IOException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        try (AsyncHttpClient client = asyncHttpClient()) {
            CompletableFuture<Void> flow = invoke(client)
                    .applyToEither(
                            Timeout.timeout(executor, Duration.ofSeconds(1)),
                            response -> "Response Processed")
                    .thenAccept(System.out::println)
                    .exceptionally(t -> {
                        t.printStackTrace();
                        return null;
                    });
        }
    }

    private static CompletableFuture<Response> invoke(AsyncHttpClient client) {
        CompletableFuture<Response> promise = new CompletableFuture<>();
        client.prepareGet("http://localhost:8080")
                .execute(new AsyncCompletionHandler<Void>() {
                    @Override
                    public Void onCompleted(Response response) {
                        promise.complete(response);
                        return null;
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                        promise.completeExceptionally(t);
                    }
                });
        return promise;
    }

    private static AsyncHttpClient asyncHttpClient() {
        return new DefaultAsyncHttpClient();
    }
}
