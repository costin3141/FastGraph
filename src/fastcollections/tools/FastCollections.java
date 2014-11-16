package fastcollections.tools;

import fastcollections.sets.impl.IntRangeSet;

/**
 * 
 * Utility methods for creating various collections.
 * 
 * @author Stefan C. Ionescu
 *
 */
public class FastCollections {

	public static IntRangeSet newIntRangeSetWithElements( int...elements ) {
		int min = elements[0];
		int max = min;
		for( int i=1; i<elements.length; i++ ) {
			if( elements[i] < min ) {
				min = elements[i];
			}
			else if( elements[i] > max ) {
				max = elements[i];
			}
		}
		
		final IntRangeSet set = new IntRangeSet(min, max, elements.length+1);
		set.addAll( elements );
		
		return set;
	}

//	public static IntSet newIntRangedSetOfElements( int...elements ) {
//		
//	}
}
