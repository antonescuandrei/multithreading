package counter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a simple counter which can be incremented and decremented properly
 * by multiple threads.
 */
class Counter {
    private final AtomicInteger count; // the count
    
    /**
     * Constructs the atomic count.
     */
    Counter() {
        count = new AtomicInteger();
    }
    
    /**
     * Increments the count and returns its value.
     * @return the value of the count after being incremented
     */
    int increment() {
        return count.incrementAndGet();
    }
    
    /**
     * Decrements the count and returns its value.
     * @return the value of the count after being decremented
     */
    int decrement() {
        return count.decrementAndGet();
    }
    
    /**
     * Gets the value of the count.
     * @return the value of the count
     */
    int getCount() {
        return count.get();
    }
}