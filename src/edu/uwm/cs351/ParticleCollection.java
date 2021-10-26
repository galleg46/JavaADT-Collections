package edu.uwm.cs351;

import java.awt.Color;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

// This is a Homework Assignment for CS 351 at UWM

/**
 * An array implementation of the Java Collection interface
 * We use java.util.AbstractCollection to implement most methods.
 * You should override clear() for efficiency, and add(Particle)
 * for functionality.  You will also be required to override the abstract methods
 * size() and iterator().  All these methods should be declared "@Override".
 * 
 * The data structure is a dynamic sized array.
 * The fields should be:
 * <dl>
 * <dt>_data</dt> The data array.
 * <dt>_count</dt> Number of true elements in the collection.
 * <dt>_version</dt> Version number (used for fail-fast semantics)
 * </dl>
 * The class should define a private _wellFormed() method
 * and perform assertion checks in each method.
 * You should use a version stamp to implement <i>fail-fast</i> semantics
 * for the iterator.
 */
public class ParticleCollection extends AbstractCollection<Particle> implements Collection<Particle>, Iterable<Particle>, Cloneable{

	/** Static Constants */
	private static final int INITIAL_CAPACITY = 1;

	/** Collection Fields */
	private int _count;
	private int _version;
	private Particle[] _data;
	
	private ParticleCollection(boolean ignored) {} // DO NOT CHANGE THIS

	private static boolean doReport = true; // only to be changed in JUnit test code
	
	private boolean _report(String s) {
		if (doReport) System.out.println(s);
		return false;
	}
	
	// The invariant:
	private boolean _wellFormed() {
		// 0. _data is not null
		if(_data == null) return _report("_data is null");
		// 1. _count is a valid index of _data
		if(_count < 0 || _data.length < _count) return _report("count is incorrect");
		return true;
	}
	
	/**
	 * Initialize an empty particle collection with an initial
	 * capacity of INITIAL_CAPACITY. The {@link #add(Particle)} method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @postcondition
	 *   This particle collection is empty, has an initial
	 *   capacity of INITIAL_CAPACITY.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new Particle[initialCapacity].
	 **/   
	public ParticleCollection()
	{
		_data = new Particle[INITIAL_CAPACITY];
		
		assert _wellFormed(): "Invariant failed at the end of constructor";
	}
	
	private void ensureCapacity(int minimumCapacity)
	{
		if (_data.length >= minimumCapacity) return;
		int newCapacity = Math.max(_data.length*2+1, minimumCapacity);
		Particle[] newData = new Particle[newCapacity];
		for (int i=0; i < _count; i++) 
			newData[i] = _data[i];
		
		_data = newData;
	}
	

	/*
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 * NB: We are able to parameterize this method with Particle instead of Object
	 * 	   because we have extended AbstractCollection with type parameter <Particle>.
	 */
	@Override
	public boolean add(Particle n){
		assert _wellFormed(): "Invariant failed at the beginning of add";
		
		ensureCapacity(_count + 1);
		
		_data[_count++] = n;
		++_version;
		
		assert _wellFormed(): "Invariant failed at the end of add";
		
		return false;
	}
	
	/*
	 * @see java.util.AbstractCollection#clear()
	 */
	@Override
	public void clear(){
		assert _wellFormed(): "Invariant failed at the beginning of clear";
		
		_data = new Particle[INITIAL_CAPACITY];
		
		if(_count != 0)
		{
			_count = 0;
			++_version;
		}
		
		assert _wellFormed(): "Invariant failed at the end of clear";
	}
	
	/*
	 * @see java.util.AbstractCollection#size()
	 */
	public int size(){
		assert _wellFormed(): "Invariant failed at the beginning of size";
		
		return _count;
		
		// NB: We don't have to check invariant at end of size(). Why?
	}
	
	
	/*
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<Particle> iterator() {
		assert _wellFormed(): "Invariant failed at the beginning of iterator constructor";
		
		return new MyIterator();
		
		// NB: We don't have to check invariant at end of iterator(). Why?
	}
	
	private class MyIterator implements Iterator<Particle> {
		
		int _myVersion, _currentIndex;
		boolean _isCurrent;

		MyIterator(boolean ignored) {} // DO NOT CHANGE THIS
		
		private boolean _wellFormed() {
			
			// Invariant for recommended fields:
			// NB: Don't check 1,2 unless the version matches.

			// 0. The outer invariant holds
			//		NB: To access the parent ParticleCollection of this iterator, use "ParticleCollection.this"
			//			e.g. ParticleCollection.this.getName()
			if(!ParticleCollection.this._wellFormed()) return ParticleCollection.this._report("Outer invariant failed"); 
			if(_version != _myVersion) return true;
			// 1. _currentIndex is between 0 (inclusive) and _count (inclusive)
			if(_currentIndex < -1 || _currentIndex > _count) return ParticleCollection.this._report("_currentIndex is out of range");
			// 2. _currentIndex is equal to _count only if _isCurrent is false
			if(_currentIndex == _count && _isCurrent != false) 
				return ParticleCollection.this._report("_currentIndex cannot be _count and have a current");
			
			return true;
		}	
		
		/**
		 * Instantiates a new MyIterator.
		 */
		public MyIterator() {
			
			_currentIndex = -1;
			_myVersion = _version;
			_isCurrent = false;
			
			assert _wellFormed() : "invariant fails in iterator constructor";
		}

		/**
		 * Returns true if the iteration has more elements. (In other words, returns true
		 * if next() would return an element rather than throwing an exception.) 
		 * 
		 * @return true if the iteration has more elements
		 * 
		 * @throws ConcurrentModificationException if iterator version doesn't match collection version
		 */
		@Override
		public boolean hasNext() {
			assert _wellFormed() : "invariant fails at beginning of iterator hasNext()";
			
			if(_version != _myVersion) throw new java.util.ConcurrentModificationException("version mismatch");
			
			if(_currentIndex + 1 < _count)  return true;
			
			return false;
		}

		/**
		 * Returns the next element in the iteration. 
		 * 
		 * @return the next element in the iteration
		 * 
		 * @throws ConcurrentModificationException if iterator version doesn't match collection version
		 * @throws NoSuchElementException if the iteration has no more elements
		 */
		@Override
		public Particle next() {
			assert _wellFormed() : "invariant fails at beginning of iterator next()";
			
			if(!hasNext()) throw new NoSuchElementException("there is no next");
			
			_isCurrent = true;
			
			assert _wellFormed() : "invariant fails at end of iterator next()";
			
			++_currentIndex;
			return _data[_currentIndex];
		}

		/**
		 * Removes from the underlying collection the last element returned by this iterator.
		 * This method can be called only once per call to next().
		 * 
		 * @throws ConcurrentModificationException if iterator version doesn't match collection version
		 * @throws IllegalStateException if the next() method has not yet been called, or the remove() 
		 * 			method has already been called after the last call to the next() method
		 */
		@Override
		public void remove() {
			assert _wellFormed() : "invariant fails at beginning of iterator remove()";
			
			if(_version != _myVersion) throw new java.util.ConcurrentModificationException("version mismatch"); 
			if(!_isCurrent) throw new IllegalStateException("nothing to remove");
			
			for(int i = _currentIndex + 1; i < _count; ++i)
			{
				_data[i - 1] = _data[i];
			}
			--_count;
			--_currentIndex;
			_isCurrent = false;
			_myVersion = ++_version;
			
			assert _wellFormed() : "invariant fails at end of iterator remove()";
		}
	}
	
	/**
	 * Generate a copy of this particle collection.
	 * @param - none
	 * @return
	 *   The return value is a copy of this particle collection. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public ParticleCollection clone( ) { 
		assert _wellFormed() : "invariant failed at start of clone";
		ParticleCollection result;

		try
		{
			result = (ParticleCollection) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{
			// This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		// all that is needed is to clone the data array.
		// (Exercise: Why is this needed?)
		result._data = _data.clone( );

		assert _wellFormed() : "invariant failed at end of clone";
		assert result._wellFormed() : "invariant on result failed at end of clone";
		return result;
	}
	
	public static class TestInvariant extends TestCase {
		
		protected ParticleCollection self;
		protected ParticleCollection.MyIterator iterator;
		
		private Particle n1 = new Particle(new Point(1,1),new Vector(1,1), 1, Color.BLUE);
		private Particle n2 = new Particle(new Point(2,2),new Vector(2,2), 2, Color.RED);;
		private Particle n3 = new Particle(new Point(3,3),new Vector(3,3), 3, Color.CYAN);
		private Particle n4 = new Particle(new Point(4,4),new Vector(4,4), 4, Color.GREEN);
		
		@Override
		protected void setUp() {
			self = new ParticleCollection(false);
			iterator = self.new MyIterator(false);
			doReport = false;
		}
		
		// outer invariant 0 - null data
		public void test01() {
			assertFalse("null data", self._wellFormed());
		}
		
		// outer invariant 2 - null element in count
		public void test02() {
			self._data = new Particle[2];
			assertTrue(self._wellFormed());
			self._count = -1;
			assertFalse(self._wellFormed());
			self._count = 2;
			self._data[0] = null;
			self._data[1] = n1;
			assertTrue("null element OK",self._wellFormed());
			self._count = 0;
			assertTrue("good empty collection of length 2",self._wellFormed());
		}
		
		// outer invariants 1, 2 - count off
		public void test03() {
			self._data = new Particle[4];
			self._count = 1;
			assertTrue("count is OK",self._wellFormed());
			self._count = 0;
			self._data[0] = n1;
			self._data[1] = n2;
			assertTrue("good empty collection",self._wellFormed());
			self._count = 1;
			assertTrue("good one element collection",self._wellFormed());
			self._count = 3;
			self._data[3] = n3;
			++self._count;
			self._data[2] = n4;
			assertTrue("good four element collection",self._wellFormed());
			++self._count;
			assertFalse("_count of 5 in _data array of length 4",self._wellFormed());
		}
		
		// inner invariant 0 - outer invariant broken
		public void test04() {
			self._data = new Particle[2];
			self._count = -1;
			assertFalse("outer invariant should fail",iterator._wellFormed());
		}
		
		// iterator invariant 1, 2, invariant only enforced if versions match
		public void test05() {
			self._data = new Particle[2];
			iterator._currentIndex = -10;
			assertFalse("_currentIndex too small",iterator._wellFormed());
			iterator._currentIndex = 1;
			assertFalse("_currentIndex too big",iterator._wellFormed());
			++self._version;
			assertTrue("versions don't match",iterator._wellFormed());
			iterator._currentIndex = 0;
			++iterator._myVersion;
			assertTrue("current OK",iterator._wellFormed());
			iterator._isCurrent = true;
			assertFalse("cannot have current when after end",iterator._wellFormed());
		}
		
		// iterator invariant 1, 2, invariant only enforced if versions match
		public void test06() {
			self._data = new Particle[10];
			self._version += 456;
			self._data[0] = n1;
			self._data[1] = n2;
			self._count = 2;
			assertTrue(self._wellFormed());
			assertTrue(iterator._wellFormed());
			iterator._myVersion = 456;
			iterator._currentIndex = 0;
			assertTrue(iterator._wellFormed());
			iterator._currentIndex = 1;
			assertTrue(iterator._wellFormed());
			iterator._isCurrent = true;
			assertTrue(iterator._wellFormed());
			iterator._currentIndex = 2;
			assertFalse("currentIndex out of bounds",iterator._wellFormed());
			++iterator._myVersion;
			assertTrue(iterator._wellFormed());
		}
	}
}
