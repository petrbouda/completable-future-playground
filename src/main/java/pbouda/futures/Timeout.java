package pbouda.futures;

import java.time.Duration;
import java.util.concurrent.*;

public class Timeout {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        CompletableFuture<String> flow =  complicatedLogic()
                .applyToEither(timeout(executor, Duration.ofSeconds(1)), string -> {
                    /* Execute this block of code if a timeout didn't occur  */
                    return "We made it! Without Timeout!";
                });
    }

    private static CompletableFuture<String> complicatedLogic() {
        return CompletableFuture.completedFuture("ComplicatedLogic");
    }

    public static <T> CompletableFuture<T> timeout(
            ScheduledExecutorService executor, Duration duration) {
        CompletableFuture<T> promise = new CompletableFuture<>();
        executor.schedule(() -> promise.completeExceptionally(
                new TimeoutException()), duration.toMillis(), TimeUnit.MILLISECONDS);
        return promise;
    }
}
