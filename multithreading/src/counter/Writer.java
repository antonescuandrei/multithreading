package counter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

/**
 * Writes the strings from a blocking queue to a file.
 */
class Writer implements Runnable {
    private final BlockingQueue<String> queue; // a reference to the blocking queue
    
    /**
     * Constructs the writer with a reference to the blocking queue from which tp
     * take strings.
     * @param queue the blocking queue from which to take strings
     */
    Writer(BlockingQueue queue) {
        this.queue = queue;
    }
    
    /**
     * Writes the strings from the blocking queue to a file. If the string STOP
     * is encountered, it stops working.
     */
    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Main.FILE_NAME))) {
            boolean done = false;
            
            while (!done) {
                String string = queue.take();
                if (!string.equals("STOP"))
                    writer.append(string);
                else
                    done = true;
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getGlobal().severe(ex.toString());
        }
    }
}