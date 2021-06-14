package amaralus.apps.hackandslash.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    @PreDestroy
    public void shutdown() {
        executorService.shutdownNow();
        log.info("Работа менеджера задач завершена");
    }
}
