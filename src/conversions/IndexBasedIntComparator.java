package conversions;

import utils.ArrayUtils;
import utils.IntComparator;

/**
 * Comparator for sorting ints according to their index position in the provided array. Intended for
 * sorting highest to lowest, ints not present in the array will be at the end
 */
public class IndexBasedIntComparator
        implements IntComparator {

    private int[] indexArray;

    public IndexBasedIntComparator(int[] indexArray) {
        this.indexArray = indexArray;
    }

    @Override
    public int compare(int a, int b) {
        int indexA = ArrayUtils.index(indexArray, a);
        int indexB = ArrayUtils.index(indexArray, b);
        return indexB - indexA;
    }

}
