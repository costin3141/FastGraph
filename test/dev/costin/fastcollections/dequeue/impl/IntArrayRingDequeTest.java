package dev.costin.fastcollections.dequeue.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import dev.costin.fastcollections.dequeue.IntQueue;
import dev.costin.fastcollections.dequeue.IntStack;


public class IntArrayRingDequeTest {

   @Test
   public void testStack() {
      final IntStack stack = new IntArrayRingDeque(3);
      
      stack.push( 1 );
      stack.push( 2 );
      stack.push( 3 );
      
      assertTrue( stack.pop() == 3 );
      assertTrue( stack.pop() == 2 );
      assertTrue( stack.pop() == 1 );
   }

   @Test
   public void testStackGrowth() {
      final IntStack stack = new IntArrayRingDeque(3);
      
      stack.push( 1 );
      stack.push( 2 );
      stack.push( 3 );
      
      assertTrue( stack.pop() == 3 );
      
      stack.push( 3 );
      
      assertTrue( stack.push( 4 ) );
      
      assertTrue( stack.pop() == 4 );
      assertTrue( stack.pop() == 3 );
      assertTrue( stack.pop() == 2 );
      assertTrue( stack.pop() == 1 );
   }

   @Test
   public void testQueue() {
      final IntQueue queue = new IntArrayRingDeque(3);
      
      queue.offer( 1 );
      queue.offer( 2 );
      queue.offer( 3 );
      
      assertTrue( queue.take() == 1 );
      assertTrue( queue.take() == 2 );
      assertTrue( queue.take() == 3 );
   }
   
   @Test
   public void testQueueRingWrapAround() {
      final IntQueue queue = new IntArrayRingDeque(3);
      
      queue.offer( 1 );
      queue.offer( 2 );
      queue.offer( 3 );
      
      assertTrue( queue.take() == 1 );
      assertTrue( queue.take() == 2 );
      
      queue.offer( 4 );
      queue.offer( 5 );
      
      assertTrue( queue.take() == 3 );
      assertTrue( queue.take() == 4 );
      assertTrue( queue.take() == 5 );
   }

   @Test
   public void testQueueGrowthWithWrap() {
      final IntQueue queue = new IntArrayRingDeque(3);
      
      queue.offer( 1 );
      queue.offer( 2 );
      queue.offer( 3 );
      
      assertTrue( queue.take() == 1 );
      assertTrue( queue.take() == 2 );
      
      queue.offer( 4 );
      queue.offer( 5 );
      
      // growing now
      queue.offer( 6 );
      
      assertTrue( queue.take() == 3 );
      assertTrue( queue.take() == 4 );
      assertTrue( queue.take() == 5 );
      assertTrue( queue.take() == 6 );
   }
   
   @Test
   public void testEquals() {
      final IntQueue queue1 = new IntArrayRingDeque(3);
      
      queue1.offer( 1 );
      queue1.offer( 2 );
      queue1.offer( 3 );
      
      final IntQueue queue2 = new IntArrayRingDeque(3);
      
      queue2.offer( 1 );
      queue2.offer( 2 );
      queue2.offer( 3 );
      
      assertEquals( queue1, queue2 );
      
      final IntQueue queue3 = new IntArrayRingDeque(3);
      
      queue3.offer( 1 );
      queue3.offer( 3 );
      queue3.offer( 2 );
      
      assertNotEquals( queue1, queue3 );
      
      final int v2 = queue2.take();
      assertEquals( 1, v2 );
      
      assertNotEquals( queue1, queue2 );
      
      final int v1 = queue1.take();
      assertEquals( 1, v1 );
      
      assertEquals( queue1, queue2 );
   }
}
