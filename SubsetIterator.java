SubsetIterator.java Wed Nov 03 08:14:28 2021 1
import structure5.*;
public class SubsetIterator<E> extends AbstractIterator<Vector<E>>
{
    private int n;          // number of elements in universe
    private long limit;     // 2^n
    private long counter;   // represents the subset, as n bits
    private Vector<E> uni;  // the universe of elements from which to draw subsets
    public SubsetIterator(Vector<E> v)
    {
        uni = v;
        n = uni.size(); // number of elements in universe
        counter = 0L;     // all values, here, are long.  L is mostly optional, but nec
 in bit ops
        limit = 1L<<n;  // how we compute 2^n
    }
    public void reset()
    {
        counter = 0L;
    }
    public boolean hasNext()
    {
        return counter < limit;  // counter should range 0 to 2^n-1
    }
    public Vector<E> get()
    {
        Vector<E> subset = new Vector<>();  // PLEASE NOTE: The "diamond operator" <> i
s expected. No <E>.
        for (int loc = 0; loc < n; loc++) {
            if ((counter & (1L << loc)) != 0L) { // add element
                subset.add(uni.get(loc));
            }
        }
        return subset;
    }
    public Vector<E> next()
    {
        Vector<E> result = get();
        counter += 1L;
        return result;
    }
    // no reset method
    public static void main(String[] args)
    {
        int n = (args.length>0) ? Integer.parseInt(args[0]) : 15;
        Vector<Double> blocks = new Vector<>();  // could also be a vector of block num
bers
        double height = 0.0;
        for (int i = 0; i < n; i++) {
            double r = Math.sqrt(i+1);
            height += r;
            blocks.add(r); // entry i contains sqrt(i+1)
        }
        double halfHeight = height/2;
        double maxLeftHeight = 0;
        Vector<Integer> tower = null;
        // THIS IS IMPORTANT: use iterated for
SubsetIterator.java Wed Nov 03 08:14:28 2021 2
        for (Vector<Double> subset : new SubsetIterator<Double>(blocks)) {
            double leftHeight = 0.0;
            for (double d : subset) {
                leftHeight += d;
            }
            if (leftHeight < halfHeight && leftHeight > maxLeftHeight) {
                tower = new Vector<>();  // diamond operator; see us if you have questi
ons
                for (double blockHeight : subset) {
                    tower.add((int)Math.round(blockHeight*blockHeight)); // a bit ugly
                }
                maxLeftHeight = leftHeight;
            }
        }
        if (tower != null) {
            System.out.println("Best solution:");
            System.out.println("Target height: "+halfHeight);
            System.out.println("Left height: "+maxLeftHeight);
            System.out.println("Tower height difference: "+(2*(halfHeight-maxLeftHeight
)));
            System.out.print("Blocks:");
            for (int block : tower) {
                System.out.print(" "+block);
            }
            System.out.println();
        }
    }
}
