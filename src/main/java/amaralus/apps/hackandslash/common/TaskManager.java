package amaralus.apps.hackandslash.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Service
public class TaskManager {

    private static final Logger log = LoggerFactory.getLogger(TaskManager.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    public void addTask(Runnable runnable) {
        executorService.submit(runnable);
    }

    public <T> List<Future<T>> addTasks(List<? extends Callable<T>> tasks) {
        try {
            return executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new UnexpectedInterruptedException(e);
        }
    }

    public <E> CompletableFuture<E> supplyAsync(Supplier<E> supplier) {
        return CompletableFuture.supplyAsync(supplier, executorService);
    }

    public <E> CompletableFuture<E> applyAsync(CompletableFuture<E> future, UnaryOperator<E> unaryOperator) {
        return future.thenApplyAsync(unaryOperator, executorService);
    }

    public <E> CompletableFuture<Void> acceptAsync(CompletableFuture<E> future, Consumer<E> consumer) {
        return future.thenAcceptAsync(consumer, executorService);
    }

    public <E> CompletableFuture<List<E>> executeTasks(List<E> entities, UnaryOperator<E> action) {
        var futures = entities.stream()
                .map(entity -> CompletableFuture.supplyAsync(() -> action.apply(entity), executorService))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    public Executor getExecutor() {
        return Executors.unconfigurableExecutorService(executorService);
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdownNow();
        log.info("Работа менеджера задач завершена");
    }
}
