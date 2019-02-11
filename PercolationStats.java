package algs15.perc;
import stdlib.*;

public class PercolationStats {
	double[] results;

	// perform T independent computational experiments on an N-by-N grid
	public PercolationStats(int N, int T) {
		if (N<=0 || T<=0) throw new IllegalArgumentException();
		this.results = new double[T];

		for (int t=0; t<T; t++) {
			int opened = 0;
			Percolation perc = new Percolation(N);
			while (!perc.percolates()) {
				int i = StdRandom.uniform(N);
				int j = StdRandom.uniform(N);
				if (!perc.isOpen(i, j)) {
					opened ++;
					perc.open(i, j);
				}
			}
			results[t] = opened / (double) (N*N);
		}
	}
	// sample mean of percolation threshold
	public double mean() {
		return StdStats.mean (results);
	}
	// sample standard deviation of percolation threshold
	public double stddev() {
		return StdStats.stddev (results);
	}
	// low end of 95% confidence interval
	public double confidenceLow() {
		return StdStats.mean(results)
				- ((1.96 * StdStats.stddev(results))
						/ Math.sqrt(results.length));
	}
	// high end of 95% confidence interval
	public double confidenceHigh() {
		return StdStats.mean(results)
				+ ((1.96 * StdStats.stddev(results))
						/ Math.sqrt(results.length));
	}

	public static void main(String[] args) {
		final int MIN = 16;
		final int MAX = 200000;
		final int T = 200;
		double time = 0;
		double prev = 0;
		for (int NSquare=MIN; NSquare<=MAX; NSquare+=NSquare) {
			int N = (int) Math.floor (Math.sqrt (NSquare));
			Stopwatch timer = new Stopwatch();
			PercolationStats stats = new PercolationStats(N,T);
			time = timer.elapsedTime ();
			StdOut.format ("T=%d N=%3d N^2=%6d mean=%5.3f confidence=[%5.3f,%5.3f] time=%5.2f ratio=%3.2f\n",
					T, N, N*N, stats.mean(), stats.confidenceLow(), stats.confidenceHigh(), time, time/prev);
			prev = time;
		}
	}
}
