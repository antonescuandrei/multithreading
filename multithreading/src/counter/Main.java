package counter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

/**
 * This class demonstrates the usage of various constructs in the java.util.concurrent
 * package.
 * A simple counter is incremented and decremented by multiple threads. These
 * threads also put the result of their work inside a blocking queue.
 * Another thread, called the writer, takes those results and writes them in a
 * file. 
 * A countdown latch is used to determine when all of the work done by the
 * incrementor and decrementor threads is finished.
 */
class Main {
    // the name of the file where results are stored
    static final String FILE_NAME = "Counter.txt";

    private static final int QUEUE_SIZE = 10; // the size of the blocking queue
    /* the number of times to modify the counter; this determines the number of
    threads that will increment/decrement the counter */
    private static final int TIMES_TO_MODIFY_COUNTER = 50; 
    private static final int SIZE_OF_POOL = 10; // the size of the thread pool
    
    /**
     * Starts program execution.
     * @param args command-line parameters (not used)
     */
    public static void main(String[] args) {
        Counter counter = new Counter(); // create the counter
        /* create a blocking queue for the strings which will be inserted by the
        incrementor and decrementor threads and written to a file by the writer thread */
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        /* a countdown latch is used to wait for all the incrementor and decrementor threads
        to finish */
        CountDownLatch latch = new CountDownLatch(TIMES_TO_MODIFY_COUNTER * 2);
        // create the thread pool with a fixed size
        ExecutorService pool = Executors.newFixedThreadPool(SIZE_OF_POOL);
        
        System.out.println("START\n"); // show a simple informative message
        
        // create the incrementor and decrementor threads and submit them to the pool
        for (int i = 0; i < TIMES_TO_MODIFY_COUNTER; i++) {
            pool.submit(new Incrementor(counter, queue, latch));
            pool.submit(new Decrementor(counter, queue, latch));
        }
        
        pool.shutdown(); // disallow new tasks
        
        new Thread(new Writer(queue)).start(); // create and start the writer thread
        
        try {
            latch.await(); // wait for the incrementor and decrementor threads to finish

            // inform the user that the program has finished
            System.out.println("\nSTOP\n\nThe final value of the counter is: " + counter.getCount());
            
            // make the writer thread stop
            queue.put("STOP");
        } catch (InterruptedException iex) {
            Logger.getGlobal().severe(iex.toString()); // log exceptions
        }
    }
}