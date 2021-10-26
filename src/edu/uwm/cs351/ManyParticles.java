package edu.uwm.cs351;

import java.awt.Graphics;

import javax.swing.JPanel;

public class ManyParticles extends JPanel {

	/**
	 * Put this in to keep Eclipse happy. 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ParticleCollection all;
	
	public ManyParticles(ParticleCollection ps) {
		all = ps;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(Particle p : all)
		{
			p.draw(g);
		}
	}
	
	public void move() {
		// Apply gravitational force on all particles,
		// and then Move all particles (see Homework #2 for basic idea)
		
		for(Particle p : all)
		{
			Vector force = new Vector();
			
			for(Particle p2 : all)
			{
				if(p != p2)
				{
					force = force.add(p2.gravForceOn(p));
				}
			}
			p.applyForce(force);
		}
		
		for(Particle q : all)
		{
			q.move();
		}
		
		
		repaint();
	}
}
