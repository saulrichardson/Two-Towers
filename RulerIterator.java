import structure5.*;
import java.util.Iterator;
/**
 * An iterator for the first 2^n - 1 values of the (binary) ruler sequence:
 *   0, 1, 0, 2, 0, 1, 0  (n = 3)
 *
 * The iterator returns each successive value in worst-case O(1)-time.
 * These values can be used to create the n-bit binary reflected Gray code:
 *   000, 001, 011, 010, 110, 111, 101, 100  (n = 3)
 *
 * More specifically, the ith value in the sequence gives the index of the bit
 * that changes between the ith and (i+1)st binary strings in the Gray code.
 *
 * Each successive value in the sequence is provided in worst-case O(1)-time.
 * In other words, the next method of the iterator is a loopless algorithm.
 */
public class RulerIterator extends AbstractIterator<Integer>
{
    private Integer   n;
    private Integer   j; // Used for
    private Integer[] f; // The focus pointers help us update index j.
    /**
     * Construct a ruler iterator describing a Gray code over n-bit integers.
     * @pre n must be non-negative
     */ 
    public RulerIterator(Integer n) {
        // Check the pre-condition: n >= 0.
        Assert.pre(n >= 0, "n must be non-negative");
        this.n = n;
        // Allocate the private arrays.
        f = new Integer[n+1];
        // Set the initial values of the private variables.
        reset();
    }
    // Reset the iterator.
    public void reset() {
        j = 0;
        // note that this is an n+1 element array!
        for (int i = 0; i <= n; i++) {
            f[i] = i;
        }
    }
    /**
     * Does the iterator have another value?
     */
    public boolean hasNext() {
        return f[0] < n;
    }
    /**
     * Get the current value, but don’t advance the iterator.
     */
    public Integer get() {
        return f[0];
    }
    /**
     * Return the current value, and advance the iterator to the next value.
     */
    public Integer next() {
        // Set j to the current value in the sequence.
        j = f[0];
        // Reset the first bit’s focus pointer.
        f[0] = 0;
        // Update the focus pointers.
        f[j] = f[j+1];
        f[j+1] = j+1;
        // Return the next bit index that will change.
        return j;
    }
    /**
     * Prints a boolean array as a string of binary digits.
     * The printing is done right-to-left to match the layout of bits in ints.
     */
    public static void printBinary(boolean[] binary) {
        for (int i = binary.length - 1; i >= 0; i--) {
            System.out.print(binary[i] ? 1 : 0);
        }
    }
    /**
     * Print the blocks represented by the boolean array.
     */
    public static void printBlocks(boolean[] binary) {
        System.out.print("Blocks:");
        for (int i = 0;  i < binary.length; i++) {
            if (binary[i]) System.out.print(" "+(i+1));
        }
        System.out.println();
    }
    /**
     * This main method illustrates how the ruler sequence can be used
     * to generate the binary reflected Gray code of binary strings.
     * TODO: Modify this method to solve the Two Towers problem
     * using a "modify and update" approach (rather than "generate and test").
     */
    public static void main(String[] args) {
        // Read in n from the command-line or set it to a default value.
        Integer n = (args.length > 0) ? Integer.parseInt(args[0]) : 4;
        // The heights of the ith box will be given by height[i-1].
        double totalHeight = 0;
        double height[] = new double[n];
        for (int i = 0; i < n; i ++) {
            height[i] = Math.sqrt(i+1);
            totalHeight += height[i];
        }
        // Initialize an n-bit binary array using one boolean per bit.
        // This binary array keeps track of the current subset.
        // DAB: For this array, entry i represents block i+1...
        boolean[] binary = new boolean[n];
        for (int i = 0; i < n; i++) {
            binary[i] = false;
        }
        // Initialize various heights.
        // Note: We don’t explicitly store the height of the current subset.
        // Instead we store the current height difference (i.e., target - current).
        double targetHeight = totalHeight / 2;  // The target is half the total.
        // below: leftHeight is 0, rightHeight is totalHeight
        double currentDiff = targetHeight; // Target height - current height.
        double bestDiff = currentDiff;  // The smallest non-negative difference.
        System.out.println("Target height: " + targetHeight);
        // Create the ruler sequence iterator.
        for (int j : new RulerIterator(n)) {
            // Update the current height difference.
            //  - If binary[j] is true, then we are removing j from the subset,
            //    so we subtract from the current height, and add to difference.
            //  - If binary[j] is false, then we are adding j to the subset,
            //    so we add to the current height, and substract from difference.
            currentDiff += binary[j] ? height[j] : -height[j];
            // Update the associated binary string.
            binary[j] = !binary[j];
            // Check if this is a new best solution.
            // If it is, then update the best values.
            if (currentDiff >= 0 && currentDiff < bestDiff) {
                bestDiff = currentDiff;
                System.out.println("New best: left height=" + (targetHeight-bestDiff) +
 " below target="+bestDiff);
                printBlocks(binary);
            }
        }
    }
}
