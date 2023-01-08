package pbouda.futures;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FailFast {

    public static void main(String[] args) {
        CompletableFuture<String> cf1 = new CompletableFuture<>();
        CompletableFuture<String> cf2 = new CompletableFuture<>();

        // cf1 is not completed - it would wait indefinitely without the fail-fast solution
        // cf1.complete("Oleee");
        cf2.completeExceptionally(new RuntimeException("something-wrong"));

        List<CompletableFuture<String>> futures = List.of(cf1, cf2);

        CompletableFuture<List<String>> future = failFast(futures);

        //  CompletableFuture<Void> future = CompletableFuture.allOf(
        //          futures.toArray(new CompletableFuture[0]));

        future.join();
    }

    private static <T> CompletableFuture<List<T>> failFast(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> result = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));

        futures.forEach(f -> {
            f.whenComplete((t, throwable) -> {
                if (throwable != null) {
                    result.completeExceptionally(throwable);
                }
            });
        });

        return result.thenApply(__ -> futures.stream().map(CompletableFuture::join).toList());
    }
}
