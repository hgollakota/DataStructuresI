package algs15.perc;
import stdlib.*;
import java.awt.Font;

/* **************************************************************************
 *  Compilation:  javac PercolationVisualizer.java
 *  Execution:    java PercolationVisualizer input.txt
 *  Dependencies: Percolation.java StdDraw.java In.java
 *
 *  This program takes the name of a file as a command-line argument.
 *  From that file, it
 *
 *    - Reads the grid size N of the percolation system.
 *    - Creates an N-by-N grid of sites (intially all blocked)
 *    - Reads in a sequence of sites (row i, column j) to open.
 *
 *  After each site is opened, it draws full sites in light blue,
 *  open sites (that aren't full) in white, and blocked sites in black,
 *  with with site (0, 0) in the upper left-hand corner.
 *
 ****************************************************************************/
public class PercolationVisualizer {
	// delay in miliseconds (controls animation speed)
	private static int delay = 100;

	// draw N-by-N percolation system
	public static void draw(Percolation perc, int N) {
		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setXscale(0, N);
		StdDraw.setYscale(0, N);
		StdDraw.filledSquare(N/2.0, N/2.0, N/2.0);

		// draw N-by-N grid
		int opened = 0;
		for (int row = 0; row < N; row++) {
			for (int col = 0; col < N; col++) {
				if (perc.isFull(row, col)) {
					StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
					opened++;
				}
				else if (perc.isOpen(row, col)) {
					StdDraw.setPenColor(StdDraw.WHITE);
					opened++;
				}
				else
					StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledSquare(col + 0.5, N - row - 0.5, 0.45);
			}
		}

		// write status text
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 12));
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(.25*N, -N*.025, opened + " open sites");
		if (perc.percolates()) StdDraw.text(.75*N, -N*.025, "percolates");
		else                   StdDraw.text(.75*N, -N*.025, "does not percolate");

	}

	// file input
	private static void simulateFromFile(String filename) {
		In in = new In(filename);
		int N = in.readInt();
		Percolation perc = new Percolation(N);

		// turn on animation mode
		StdDraw.show(0);

		// repeatedly read in sites to open and draw resulting system
		PercolationVisualizer.draw(perc, N);
		StdDraw.show(delay);
		while (!in.isEmpty()) {
			int i = in.readInt();
			int j = in.readInt();
			perc.open(i, j);
			PercolationVisualizer.draw(perc, N);
			StdDraw.show(delay);
			//if (i==0 && !perc.isFull (i, j)) StdOut.format("yikes: %d %d\n", i, j);
		}
		PercolationVisualizer.draw(perc, N);
		StdDraw.show(delay);
	}

	// random input
	private static void simulateFromRandom(int N) {
		// repeatedly generate sites at random and draw resulting system
		Percolation perc = new Percolation(N);

		// turn on animation mode
		StdDraw.show(0);

		// repeatedly open sites until system percolates and draw resulting system
		PercolationVisualizer.draw(perc, N);
		StdDraw.show(delay);
		while (!perc.percolates()) {
			int i = StdRandom.uniform(N);
			int j = StdRandom.uniform(N);
			if (!perc.isOpen(i, j)) {
				perc.open(i, j);
				PercolationVisualizer.draw(perc, N);
				StdDraw.show(delay);
				
				//if (i==0 && !perc.isFull (i, j)) StdOut.format("yikes: %d %d\n", i, j);
			}
		}
	}

	public static void main(String[] args) {
		//        delay = 500; args = new String[] { "src/algs15/perc/perc-input1.txt" };
		//        delay = 500; args = new String[] { "src/algs15/perc/perc-input1-no.txt" };
		//        delay = 500; args = new String[] { "src/algs15/perc/perc-input2.txt" };
		//        delay = 500; args = new String[] { "src/algs15/perc/perc-input2-no.txt" };
		//        delay = 500; args = new String[] { "src/algs15/perc/perc-input3.txt" };
		//        delay = 500; args = new String[] { "src/algs15/perc/perc-input4.txt" };
		//        delay = 200; args = new String[] { "src/algs15/perc/perc-input5.txt" };
		//        delay = 100; args = new String[] { "src/algs15/perc/perc-input6.txt" };
		//        delay = 100; args = new String[] { "src/algs15/perc/perc-input7.txt" };
		//        delay = 100; args = new String[] { "src/algs15/perc/perc-input8.txt" };
		//        delay = 100; args = new String[] { "src/algs15/perc/perc-input8-no.txt" };
		//        delay = 100; args = new String[] { "src/algs15/perc/perc-input10.txt" };
		//        delay = 100; args = new String[] { "src/algs15/perc/perc-input10-no.txt" };
		//        delay = 100; args = new String[] { "src/algs15/perc/perc-input20.txt" };
		//        delay = 0;   args = new String[] { "src/algs15/perc/perc-input50.txt" };
		//        delay = 0;   args = new String[] { "src/algs15/perc/perc-greeting57.txt" };
		//        delay = 100; args = new String[] { "src/algs15/perc/perc-snake13.txt" };
		//        delay = 0;   args = new String[] { "src/algs15/perc/perc-snake101.txt" };
		//        delay = 10;  args = new String[] { "src/algs15/perc/perc-heart25.txt" };
		        delay = 0; args = new String[] { "100" };

		if (args.length >= 2) throw new RuntimeException("Command line argument should be a filename or integer");
		else if (args.length == 0) simulateFromRandom(10);
		else {
			try {
				int N = Integer.parseInt(args[0]);
				simulateFromRandom(N);
			}
			catch (NumberFormatException e) {
				String filename = args[0];
				simulateFromFile(filename);
			}
		}
	}
}
