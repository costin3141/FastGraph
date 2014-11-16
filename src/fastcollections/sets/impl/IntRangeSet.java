package fastcollections.sets.impl;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import fastcollections.IntCursor;
import fastcollections.sets.IntSet;

public class IntRangeSet implements IntSet {
	
	public static final int DEFAULT_LIST_CAPACITY = 8;
	
	private final int[] _set;
	private int[] _list;
	private int _size;
	private final int _offset;
	
	protected int _modCounter = 0;
	
	protected static class IntCursorIterator implements Iterator<IntCursor>, IntCursor {
		
		private final IntRangeSet _set;
		private final int[] _list;
		private int _next;
		private int _value;
		
		private int _modCounter;
		
		IntCursorIterator( final IntRangeSet set ) {
			_set = set;
			_list = _set._list;
			_next = 0;
			_value = 0;
			_modCounter = _set._modCounter;
		}

		@Override
		public boolean hasNext() {
			return _next < _set.size();
		}

		@Override
		public IntCursor next() {
			if( _modCounter != _set._modCounter ) {
				throw new ConcurrentModificationException();
			}
			
			_value = _list[_next++];
			return this;
		}

		@Override
		public void remove() {
			assert _set.contains(_value) : "Element has already removed  outside of this iterator!";
			
			_set.remove(_value);
			++_modCounter;
			if( _modCounter != _set._modCounter ) {
				throw new ConcurrentModificationException();
			}
		}

		@Override
		public int value() {
			return _value;
		}

	}
	
	protected static class IntIterator implements fastcollections.IntIterator {
		
		private final IntRangeSet _set;
		
		private final int[] _list;

		private int _next;
		
		private int _lastValue;
		
		private int _modCounter;
		
		IntIterator( final IntRangeSet set ) {
			_set = set;
			_list = _set._list;
			_next = 0;
			_modCounter = _set._modCounter;
		}

		@Override
		public int nextInt() {
			if( _modCounter != _set._modCounter ) {
				throw new ConcurrentModificationException();
			}

			return _list[ _next++ ];
		}

		@Override
		public boolean hasNext() {
			return _next < _set.size();
		}

		@Override
		public void remove() {
			assert _set.contains(_lastValue) : "Element has already removed  outside of this iterator!";
			
			_set.remove(_lastValue);
			++_modCounter;
			if( _modCounter != _set._modCounter ) {
				throw new ConcurrentModificationException();
			}
		}
		
	}
	
	public IntRangeSet( final int from, final int to ) {
		this( from, to, Math.min(to-from+1, DEFAULT_LIST_CAPACITY) );
	}
	
	public IntRangeSet( final int from, final int to, final int listCapacity ) {
		_offset = from;
		_set = new int[to-from+1];
		_list = new int[listCapacity];
		_size = 0;
	}

	@Override
	public Iterator<IntCursor> iterator() {
		return new IntCursorIterator(this);
	}
	
	@Override
	public int size() {
		return _size;
	}
	
	@Override
	public boolean contains( final int value ) {
		return _set[ value -_offset ] > 0;
	}

	@Override
	public boolean add( int value ) {
		final int v = value - _offset;

		if( _set[v] > 0 ) {
			return false;
		}
		_set[ v ] = addToList( value );
		++_modCounter;
		
		return true;
	}

	private int addToList(int value) {
		if( _size == _list.length ) {
			_list = Arrays.copyOf( _list, _size + (_size>>1) + 1 );
		}
		_list[ _size++ ] = value;
		return _size;
	}

	@Override
	public boolean remove(int value) {
		final int v = value - _offset;
		final int ref = _set[v];
		if( ref == 0 ) {
			return false;
		}
		_list[ ref-1 ] = _list[ --_size ];
		_set[ v ] = 0;
		++_modCounter;
		
		return true;
	}

	@Override
	public IntIterator intIterator() {
		return new IntIterator(this);
	}

	@Override
	public void clear() {
		for( int i=0; i<_size; i++ ) {
			final int val = _list[i];
			_set[val-_offset] = 0;
		}
		_size = 0;
		++_modCounter;
	}

	public int get(int i) {
//		if( i >= _size ) {
//			throw new IndexOutOfBoundsException();
//		}
		return _list[i];
	}

}
