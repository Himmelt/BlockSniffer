package org.soraworld.sniffer.schedule;

/**
 * @author Himmelt
 */
public class ForgeTask {

    private long delay;
    private final long period;
    private final Runnable action;
    private boolean canceled = false;

    public ForgeTask(Runnable action, long delay) {
        this.delay = delay;
        this.action = action;
        period = -1;
    }

    public ForgeTask(Runnable action, long delay, long period) {
        this.delay = delay;
        this.action = action;
        this.period = period;
    }

    public final synchronized boolean isCanceled() {
        return canceled;
    }

    public final synchronized void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public final synchronized void cancel() {
        setCanceled(true);
    }

    public final synchronized boolean tick() {
        delay--;
        return delay < 0;
    }

    public final synchronized boolean run() {
        if (action != null) {
            action.run();
        }
        delay = period;
        return period < 0;
    }
}
