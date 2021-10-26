import java.awt.Color;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Particle;
import edu.uwm.cs351.ParticleCollection;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;

public class TestParticleCollection extends LockedTestCase {
	
	private ParticleCollection s1, s2;
	private Iterator<Particle> it, it2;
	
	private Particle n1 = new Particle(new Point(1,1), new Vector(1,1), 1, Color.RED);
	private Particle n2 = new Particle(new Point(2,2), new Vector(2,2), 2, Color.ORANGE);
	private Particle n3 = new Particle(new Point(3,3), new Vector(3,3), 3, Color.YELLOW);
	private Particle n4 = new Particle(new Point(4,4), new Vector(4,4), 4, Color.GREEN);
	private Particle n5 = new Particle(new Point(5,5), new Vector(5,5), 5 , Color.BLUE);
	
	@Override
	protected void setUp() {
		try {
			assert s1.size() == s2.size();
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration",false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		s1 = new ParticleCollection();
		s2 = new ParticleCollection();
	}
	
	// Convert a Particle result to a string
	Particle particles[] = { null, n1, n2, n3, n4, n5 };
	private String getName(Particle b) {
		for (int i=1; i<particles.length;i++)
			if (particles[i] == b)
				return "n" + i;
		return "null";}
	
	protected void testCollection(ParticleCollection col, String name, Particle... parts)
	{
		assertEquals(name + ".size()",parts.length,col.size());
		Iterator<Particle> it = col.iterator();
		int i=0;
		while (it.hasNext() && i < parts.length) {
			assertEquals(name + "[" + i + "]",parts[i],it.next());
			++i;}
		assertFalse(name + " too long",it.hasNext());
		assertFalse(name + " too short",(i < parts.length));
	}
	
	public void test00()
	{
		assertEquals(true, s1.isEmpty());
		it = s1.iterator();
		assertEquals(Tb(1044074673), it.hasNext());
		s1.add(n1);
		it = s1.iterator();
		assertEquals(Tb(50269584), it.hasNext());
		assertEquals("Which particle should be next?", Ts(276645695), getName(it.next()));
		assertEquals(Tb(568761500), it.hasNext());
	
		s1.add(n2);
		testCollection(s1,"{n1,n2}",n1,n2);
		s1.add(n3);
		testCollection(s1,"{n1,n2,n3}",n1,n2,n3);
		s1.add(n4);
		testCollection(s1,"{n1,n2,n3,n4}",n1,n2,n3,n4);
		
		s1.clear();
		testCollection(s1,"after clear");
		it = s1.iterator();
		assertEquals(false, it.hasNext());
	}
	
	public void test01() {
		s1.add(n1);
		it = s1.iterator();
		it.next();
		it.remove();
		assertEquals(Tb(218713852), it.hasNext());
		testCollection(s1,"{n1} after remove(n1)");
	}
	
	public void test02() {
		s1.add(n1);
		s1.add(n2);
		it = s1.iterator();
		it.next();
		it.remove();
		assertEquals(Tb(1048892551), it.hasNext());
		assertEquals("Which particle should be next?",Ts(2124211383), getName(it.next()));
		assertEquals(false, it.hasNext());
		testCollection(s1,"{n1,n2} after remove(n1)",n2);
	}
	
	public void test03() {
		s1.add(n1);
		s1.add(n2);
		it = s1.iterator();
		it.next();
		it.next();
		it.remove();
		assertEquals(false, it.hasNext());
		testCollection(s1,"{n1,n2} after remove(n2)",n1);
	}
	
	public void test04() {
		s1.add(n2);
		s1.add(n1);
		s1.add(n3);
		it = s1.iterator();
		it.next();
		it.remove();
		assertEquals(Tb(2101852861), it.hasNext());//
		assertEquals("Which particle should be next?", Ts(654566560), getName(it.next()));//
		assertEquals(true, it.hasNext());//
		assertEquals("Which particle should be next?", "n3", getName(it.next()));//
		assertEquals(false, it.hasNext());
		testCollection(s1,"{n2,n1,n3} after remove(n2)",n1,n3);
				
	}
	
	public void test05() {
		s1.add(n3);
		s1.add(n1);
		s1.add(n2);
		it = s1.iterator();
		it.next();
		it.next();
		it.remove();
		assertEquals(true, it.hasNext());
		assertEquals("n2", getName(it.next()));
		assertEquals(false, it.hasNext());
		testCollection(s1,"{n3,n1,n2} after remove(n1)",n3,n2);
	}

	public void test06() {
		s1.add(n3);
		s1.add(n2);
		s1.add(n1);
		it = s1.iterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		assertEquals(Tb(273857373), it.hasNext());
		it = s1.iterator();
		assertEquals(Tb(1728770422), it.hasNext());
		assertEquals("Which particle should be next?",Ts(1849851716), getName(it.next()));
		assertEquals(true, it.hasNext());
		assertEquals("Which particle should be next?", "n2", getName(it.next()));
		assertEquals(false, it.hasNext());
		testCollection(s1,"{n3,n2,n1} after remove(n1)",n3,n2);	
	}

	public void test07() {
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		it = s1.iterator();
		it.next(); it.remove();
		it.next(); it.remove();
		assertTrue(it.hasNext());
		assertEquals("n3",getName(it.next()));
		testCollection(s1,"{n1,n2,n3} after remove(n1,n2)",n3);
	}

	public void test08() {
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		it = s1.iterator();
		it.next(); it.remove();
		it.next();
		it.next(); it.remove();
		assertEquals(false, it.hasNext());
		testCollection(s1,"{n1,n2,n3} after remove(n1,n3)",n2);
	}

	public void test09() {
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		it = s1.iterator();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		assertEquals(false, it.hasNext());
		it = s1.iterator();
		assertEquals(false, it.hasNext());
		testCollection(s1,"{n1,n2,n3} after remove(n1,n2,n3)");
	}

	public void test10() 
	{
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		s1.add(n4);
		testCollection(s1,"{n1 n2 n3 n4}",n1,n2,n3,n4);
		
		it = s1.iterator();
		it.next();
		it.next();
		it.remove();
		assertTrue("Two more after n2 removed",it.hasNext());
		assertEquals("Next after n2 removed","n3",getName(it.next()));
		assertTrue("One more and next() after n2 removed",it.hasNext());
		assertEquals("Next after next() after n2 removed","n4",getName(it.next()));
		assertTrue("Only two more after n2 removed",!it.hasNext());		
		testCollection(s1,"{n1,n2,n3,n4} after remove(n2)",n1,n3,n4);
		
		s1.clear();
		testCollection(s1,"after clear");
	}
	
	public void test11() {
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		s1.add(n4);
		
		it = s1.iterator();
		it.next();
		it.remove();
		assertEquals(true, it.hasNext());
		assertEquals("Which particle should be next?","n2",getName(it.next()));
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1)",n2,n3,n4);
		assertEquals(true,it.hasNext());
		assertEquals("Which particle should be next?","n3",getName(it.next()));
		it.remove();
		assertEquals(true, it.hasNext());
		assertEquals("Which particle should be next?","n4",getName(it.next()));
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1,n3)",n2,n4);
		it.remove();
		assertEquals(false,it.hasNext());
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1,n3,n4)",n2);
		
		it = s1.iterator();
		it.next();
		it.remove();
		assertEquals(false, it.hasNext());
		testCollection(s1,"all removed");
		
	}
	
	public void test12() {
		s1.add(n1);
		s1.add(n2);
		s1.add(n3);
		s1.add(n4);
		
		it= s1.iterator();
		it.next();
		it.remove();
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1)",n2,n3,n4);
		it = s1.iterator();
		it.next();
		it.remove();
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1,n2)",n3,n4);
		it = s1.iterator();
		it.next();
		it.next();
		it.remove();
		testCollection(s1,"{n1,n2,n3,n4} after remove(n1,n2,n4)",n3);
		it = s1.iterator();
		it.next();
		it.remove();
		testCollection(s1,"all removed again");
	}
	
	public void test13()
	{
		it = s1.iterator();
		try {
			it.next();
			assertFalse("next() on iterator over empty collection should throw exception",true);
		} catch (Exception ex) {
			assertTrue("empty.next() threw wrong exception ",ex instanceof NoSuchElementException);
		}
		assertEquals(false, it.hasNext());
		testCollection(s1,"still empty");
	}
	
	public void test14()
	{
		it = s1.iterator();
		try {
			it.remove();
			assertFalse("remove() on iterator over empty collection should throw exception",true);
		} catch (Exception ex) {
			assertTrue("empty.remove() threw wrong exception ",ex instanceof IllegalStateException);
		}
		assertEquals(Tb(1927063199), it.hasNext());
		testCollection(s1,"yet still empty");

	}
	
	public void test15()
	{
		it = s1.iterator();
		s1.add(n3);
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		testCollection(s1,"{n3}",n3);
	}
	
	public void test16()
	{
		s1.add(n3);
		it = s1.iterator();
		try {
			it.remove();
			assertTrue("remove() at start of iteration should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("just started remove() threw wrong exception " + ex),(ex instanceof IllegalStateException));
		}
		assertEquals(Tb(1256896379), it.hasNext());
		assertEquals("Which particle should be next?", "n3", getName(it.next()));
		testCollection(s1,"still {n3}",n3);
	}
	
	public void test17()
	{
		s1.add(n4);
		it = s1.iterator();
		it.next();
		it.remove();
		try {
			it.next();
			assertTrue("next() after removed only element should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("after removal of only element, next() threw wrong exception " + ex),(ex instanceof NoSuchElementException));
		}
		assertTrue("after removal of only element, hasNext() should still be false",(!it.hasNext()));
		testCollection(s1,"{n4} after remove (n4)");
	}
	
	public void test18()
	{
		s1.add(n2);
		s1.add(n4);
		it = s1.iterator();
		it.next();
		it.remove();
		try {
			it.remove();
			assertTrue("remove() after remove() should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() after remove() threw wrong exception " + ex),(ex instanceof IllegalStateException));
		}
		assertEquals(Tb(665890557), it.hasNext());
		assertEquals("Which particle should be next?", "n4", getName(it.next()));
		testCollection(s1,"{n2,n4} after remove (n2)",n4);
	}
	
	public void test19()
	{
		s1.add(n3);
		it = s1.iterator();
		it.next();
		it.remove();
		s1.add(n4);
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		testCollection(s1,"{n4}",n4);
	}
	
	public void test20()
	{
		s1.add(n2);
		it = s1.iterator();
		it.next();
		try {
			it.next();
			assertTrue("next() after iterated past only element should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("after iteration past only element, next() threw wrong exception " + ex),(ex instanceof NoSuchElementException));
		}
		assertTrue("after iteration past only element, hasNext() should still be false",(!it.hasNext()));
		testCollection(s1,"{n2}",n2);
	}
	
	public void test21()
	{
		s1.add(n5);
		s1.add(n1);
		it = s1.iterator();
		it2 = s1.iterator();
		it.next();
		it2.next();
		it.remove();
		try {
			it2.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}	
		try {
			it2.remove();
			assertTrue("remove() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}		
		try {
			it.remove();
			assertTrue("remove() after first remove() should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() after first remove() threw wrong exception " + ex),ex instanceof IllegalStateException);
		}
		assertEquals(true, it.hasNext());
		testCollection(s1,"{n5,n1} after remove (n5)",n1);
		
	}
	
	public void test22() {
		s1.add(n1);
		s1.add(n3);
		it = s1.iterator();
		it.next();
		it.remove();
		
		it2 = s1.iterator();
		it.next();
		it.remove();
		
		try {
			it2.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
		try {
			it2.next();
			assertTrue("next() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("next() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}

		try {
			it2.remove();
			assertTrue("remove() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
		try {
			it.remove();
			assertTrue("remove() after second remove() should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() after second remove() threw wrong exception " + ex),ex instanceof IllegalStateException);
		}
		assertTrue("after remove() after second remove(), hasNext() should still be false",(!it.hasNext()));
		testCollection(s1,"{n1,n3} after remove (n1,n3)");
	}
	
	public void test23() {
		s1.add(n1);
		it = s1.iterator();
		s1.clear();
		
		try {
			it.hasNext();
			assertTrue("hasNext() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("hasNext() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
		try {
			it.next();
			assertTrue("next() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("next() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}

		try {
			it.remove();
			assertTrue("remove() on stale iterator should throw exception",false);
		} catch (RuntimeException ex) {
			assertTrue(("remove() on stale iterator threw wrong exception " + ex),(ex instanceof ConcurrentModificationException));
		}
		
	}
	
	public void test24() {
		s1.add(null);
		s1.add(n1);
		it = s1.iterator();
		assertTrue(it.hasNext());
		assertNull(it.next());
		assertTrue(it.hasNext());
		assertEquals(n1,it.next());
		assertFalse(it.hasNext());
	}

}
