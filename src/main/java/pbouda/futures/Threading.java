package pbouda.futures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Threading {

    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    String name = Thread.currentThread().getName();
                    System.out.println(name);
                    return "1";
//                    sleepMillis(1);
                })
                .thenApply(str -> {
                    String name = Thread.currentThread().getName();
                    System.out.println(name);
                    return str + "2";
                })
                .thenApplyAsync(str -> {
                    String name = Thread.currentThread().getName();
                    System.out.println(name);
                    return str + "3";
                });

        future.join();
    }

    private static void sleepMillis(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
