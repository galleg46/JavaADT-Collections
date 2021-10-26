import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import edu.uwm.cs351.ManyParticles;
import edu.uwm.cs351.Particle;
import edu.uwm.cs351.ParticleCollection;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;

public class Main {
	public static void main(String[] args) {
		Particle p1 = new Particle(new Point(100.0,100.0), new Vector(0.0,0.0), 100.0, Color.YELLOW);
		Particle p2 = new Particle(new Point(100.0,300.0),new Vector(0.5,0.0), 5.0, Color.BLACK);
		Particle p3 = new Particle(new Point(150.0,100.0),new Vector(0.0,-1.0),1, Color.BLUE);
		Particle p4 = new Particle(new Point(10,10), new Vector(-0.3,0.3), 3, Color.RED);

		ParticleCollection ps = new ParticleCollection();
		ps.add(p1);
		ps.add(p2);
		ps.add(p3);
		ps.add(p4);
		
		final ManyParticles animation = new ManyParticles(ps);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame j = new JFrame();
				j.setContentPane(animation);
				j.setSize(300,300);
				j.setVisible(true);
				j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			}
		});

		while (true) {
			try {
				Thread.sleep(20);
				animation.move();
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
