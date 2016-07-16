package dev.costin.fastcollections.bridging.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import dev.costin.fastcollections.IntIterator;
import dev.costin.fastcollections.bridging.IndexedObject;
import dev.costin.fastcollections.bridging.IndexedObjectBridge;
import dev.costin.fastcollections.maps.IntObjectMap;
import dev.costin.fastcollections.maps.impl.IntObjectGrowingMap;

/**
 * Class to manage {@link IndexedObject}s.
 * 
 * <p>
 * For objects managed by an instance of this class this provides mapping methods to translate a
 * given index to the object or from an object to its index. The latter has the very same result as
 * using {@link IndexedObject#getIndex()}.
 * </p>
 * <p>
 * <strong>NOTE:</strong> Indices provided by the objects <strong>must never</strong> change as long
 * as they are managed by this.
 * </p>
 * 
 * @author Stefan C. Ionescu
 *
 * @param <T>
 *           Type of the objects to manage, must be a subtype of {@link IndexedObject}.
 */
public class IndexedObjectManager<T extends IndexedObject> implements IndexedObjectBridge<T> {

   private final IntObjectMap<T> _toObject;

   private int _minIndex;

   private int _maxIndex;

   private boolean _minMaxValid;

   public IndexedObjectManager() {
      _minIndex = Integer.MAX_VALUE;
      _maxIndex = Integer.MIN_VALUE;
      _toObject = new IntObjectGrowingMap<T>();
      _minMaxValid = false;
   }

   public IndexedObjectManager( final Collection<T> objects ) {
      if( objects.isEmpty() ) {
         _minIndex = Integer.MAX_VALUE;
         _maxIndex = Integer.MIN_VALUE;
         _toObject = new IntObjectGrowingMap<T>();

         _minMaxValid = false;
      }
      else {
         final Iterator<T> iter = objects.iterator();
         int min = iter.next().getIndex();
         int max = min;

         for( ; iter.hasNext(); ) {
            final int idx = iter.next().getIndex();

            if( idx < min ) {
               min = idx;
            }
            else if( idx > max ) {
               max = idx;
            }
         }

         _minIndex = min;
         _maxIndex = max;

         _toObject = new IntObjectGrowingMap<T>( _minIndex, _maxIndex, objects.size() );
         for( final T obj : objects ) {
            _toObject.put( obj.getIndex(), obj );
         }

         _minMaxValid = true;
      }
   }

   @Override
   public int getMinIndex() {
      if( !_minMaxValid ) {
         updateMinMaxIndex();
      }
      return _minIndex;
   }

   @Override
   public int getMaxIndex() {
      if( !_minMaxValid ) {
         updateMinMaxIndex();
      }
      return _maxIndex;
   }

   @Override
   public T getObject( int index ) {
      final T obj = _toObject.get( index );
      if( obj == null ) {
         throw new NoSuchElementException();
      }

      return obj;
   }

   @Override
   public int getIndex( T obj ) {
      return obj.getIndex();
   }

   /**
    * Adds an indexed object {@code obj} to the managed list objects.
    * 
    * @param obj
    *           The object to add.
    * @throws IllegalStateException
    *            If an object with the very same index exists already.
    */
   public void add( final T obj ) {
      final int index = obj.getIndex();
      if( _toObject.containsKey( index ) ) {
         throw new IllegalStateException( "Failed adding with " + obj + ": An object with index " + index + " already exists " + _toObject.get( index ) );
      }
      
      _toObject.put( index, obj );

      if( _minMaxValid && index > _maxIndex ) {
         _maxIndex = index;
      }
   }

   /**
    * Same as {@link #add(IndexedObject)} but adds a {@link Collection} of objects at once.
    * 
    * @see #add(IndexedObject)
    */
   public void addAll( final Collection<T> objects ) {
      for( final T obj : objects ) {
         add( obj );
      }
   }

   /**
    * Substitutes an existing object with the very same index as {@code obj}.
    * 
    * @param obj
    *           The new object to substitute an already existing one.
    * @return The previously stored object at the index of {@code obj}, the object that gets
    *         substituted by {@code obj}.
    * @throws IllegalStateException
    *            If there is no object with the same index as {@code obj}.
    */
   public T substitute( final T obj ) {
      final int index = obj.getIndex();
      final T oldObj = _toObject.get( index );
      if( oldObj == null ) {
         throw new IllegalStateException( "Failed substituting with " + obj + ": No object with index " + index + " to substitute." );
      }
      _toObject.put( index, obj );
      return oldObj;
   }

   /**
    * Same as {@link #substitute(IndexedObject)} but substitutes a {@link Collection} of objects at
    * once.
    * 
    * @see #add(IndexedObject)
    */
   public void substituteAll( final Collection<T> objects ) {
      for( final T obj : objects ) {
         substitute( obj );
      }
   }

   /**
    * Removes the given {@code obj} from the managed list of indexed objects.
    * 
    * @param obj
    *           The object to remove.
    * @return {@code True} if the object existed, {@code false} otherwise.
    */
   public boolean remove( final T obj ) {
      final int index = obj.getIndex();
      final boolean removed = _toObject.containsKey( index );

      _toObject.remove( index );

      if( index == _maxIndex || index == _minIndex ) {
         _minMaxValid = false;
      }

      return removed;
   }

   private void updateMinMaxIndex() {
      _minIndex = Integer.MAX_VALUE;
      _maxIndex = Integer.MIN_VALUE;
      
      if( !_toObject.isEmpty() ) {
         final IntIterator i = _toObject.keyIterator();
         int min = i.nextInt();
         int max = min;
         
         for( ; i.hasNext(); ) {
            final int index = i.nextInt();
            if( index < min ) {
               min = index;
            }
            else if( index > max ) {
               max = index;
            }
         }
   
         _minIndex = min;
         _maxIndex = max;
         _minMaxValid = true;
      }
   }
}
