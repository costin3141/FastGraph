package dev.costin.fastcollections;

import java.util.Objects;

public interface IntPredicate {

   boolean test( int t );

   /**
    * Returns a composed predicate that represents a short-circuiting logical
    * AND of this predicate and another.  When evaluating the composed
    * predicate, if this predicate is {@code false}, then the {@code other}
    * predicate is not evaluated.
    *
    * <p>Any exceptions thrown during evaluation of either predicate are relayed
    * to the caller; if evaluation of this predicate throws an exception, the
    * {@code other} predicate will not be evaluated.
    *
    * @param other a predicate that will be logically-ANDed with this
    *              predicate
    * @return a composed predicate that represents the short-circuiting logical
    * AND of this predicate and the {@code other} predicate
    */
   default IntPredicate and(IntPredicate other) {
       return (t) -> test(t) && other.test(t);
   }

   /**
    * Returns a predicate that represents the logical negation of this
    * predicate.
    *
    * @return a predicate that represents the logical negation of this
    * predicate
    */
   default IntPredicate negate() {
       return (t) -> !test(t);
   }

   /**
    * Returns a composed predicate that represents a short-circuiting logical
    * OR of this predicate and another.  When evaluating the composed
    * predicate, if this predicate is {@code true}, then the {@code other}
    * predicate is not evaluated.
    *
    * <p>Any exceptions thrown during evaluation of either predicate are relayed
    * to the caller; if evaluation of this predicate throws an exception, the
    * {@code other} predicate will not be evaluated.
    *
    * @param other a predicate that will be logically-ORed with this
    *              predicate
    * @return a composed predicate that represents the short-circuiting logical
    * OR of this predicate and the {@code other} predicate
    */
   default IntPredicate or(IntPredicate other) {
       return (t) -> test(t) || other.test(t);
   }

   /**
    * Returns a predicate that tests if two arguments are equal according
    * to {@link Objects#equals(Object, Object)}.
    *
    * @param targetRef the object reference with which to compare for equality,
    *               which may be {@code null}
    * @return a predicate that tests if two arguments are equal according
    * to {@link Objects#equals(Object, Object)}
    */
   static IntPredicate isEqual(Object targetRef) {
       return (null == targetRef)
               ? Objects::isNull
               : object -> targetRef.equals(object);
   }
   
   static IntPredicate isEqual( int other ) {
      return object -> object == other;
   }

   /**
    * Returns a predicate that is the negation of the supplied predicate.
    * This is accomplished by returning result of the calling
    * {@code target.negate()}.
    *
    * @param target  predicate to negate
    *
    * @return a predicate that negates the results of the supplied
    *         predicate
    */
   static IntPredicate not( IntPredicate target ) {
       return target.negate();
   }
   
}
