package org.soraworld.sniffer.schedule;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author Himmelt
 */
public final class Scheduler {

    private static long THREAD_ID = 0;
    private static final int CORE_NUM = Runtime.getRuntime().availableProcessors() / 2;
    private static final List<ForgeTask> SYNC_TASKS = new CopyOnWriteArrayList<>();
    private static final List<ForgeTask> ASYNC_TASKS = new CopyOnWriteArrayList<>();
    private static final ScheduledExecutorService SCHEDULER = new ScheduledThreadPoolExecutor(CORE_NUM <= 0 ? 1 : CORE_NUM, task -> {
        Thread thread = Executors.defaultThreadFactory().newThread(task);
        thread.setName("MediaScheduler#" + THREAD_ID++);
        return thread;
    });

    private Scheduler() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (ForgeTask task : SYNC_TASKS) {
                if (task.isCanceled()) {
                    SYNC_TASKS.remove(task);
                    continue;
                }
                if (task.tick()) {
                    if (task.run()) {
                        SYNC_TASKS.remove(task);
                    }
                }
            }
            for (ForgeTask task : ASYNC_TASKS) {
                if (task.isCanceled()) {
                    ASYNC_TASKS.remove(task);
                    continue;
                }
                if (task.tick()) {
                    SCHEDULER.submit(task::run);
                    ASYNC_TASKS.remove(task);
                }
            }
        }
    }

    public static ForgeTask runTask(Runnable action) {
        ForgeTask task = new ForgeTask(action, 0);
        SYNC_TASKS.add(task);
        return task;
    }

    public static ForgeTask runTask(Runnable action, long delay) {
        ForgeTask task = new ForgeTask(action, delay);
        SYNC_TASKS.add(task);
        return task;
    }

    public static ForgeTask runTask(Runnable action, long delay, long period) {
        ForgeTask task = new ForgeTask(action, delay, period);
        SYNC_TASKS.add(task);
        return task;
    }

    public static ForgeTask runAsyncTask(Runnable action, long delay) {
        ForgeTask task = new ForgeTask(action, delay);
        ASYNC_TASKS.add(task);
        return task;
    }

    public static void runAsyncTask(Runnable action) {
        SCHEDULER.submit(action);
    }

    public static void runAsyncTask(Runnable action, long delay, long period, TimeUnit unit) {
        SCHEDULER.scheduleAtFixedRate(action, delay, period, unit);
    }
}
