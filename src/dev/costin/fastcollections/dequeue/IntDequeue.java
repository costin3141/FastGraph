package dev.costin.fastcollections.dequeue;

/**
 * Combines {@link IntStack} and {@link IntQueue} into one interface.
 * <p><strong>Note:</strong>
 * The methods {@link #push(int)} and {@link #offer(int)} must insert a new
 * element at the very same position.
 * </p>
 * 
 * @author Stefan C. Ionescu
 *
 */
public interface IntDequeue extends IntQueue, IntStack {

}
