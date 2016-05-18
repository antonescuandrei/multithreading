package counter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * Decrements the counter and informs the user.
 */
class Decrementor implements Runnable {
    private final Counter counter; // reference to the counter
    private final BlockingQueue queue; // reference to the blocking queue
    private final CountDownLatch latch; // reference to the countdown latch
    
    /**
     * Constructs the decrementor with the required references.
     * @param counter the counter to decrement
     * @param queue the queue to add the result to as a string
     * @param latch the countdown latch to count down
     */
    Decrementor(Counter counter, BlockingQueue queue, CountDownLatch latch) {
        this.counter = counter;
        this.queue = queue;
        this.latch = latch;
    }
    
    /**
     * Decrements the counter, displays a message to the user and adds the same
     * message to the blocking queue to be printed to a file by the writer thread.
     * Also counts down the countdown latch.
     */
    @Override
    public void run() {
        String result = String.format("I decremented the counter and now it has the value: %d %n", counter.decrement());
        
        try {
            queue.put(result);
        } catch (InterruptedException iex) {
            Logger.getGlobal().severe(iex.toString());
        }
        
        System.out.print(result);
        
        latch.countDown();
    }
}