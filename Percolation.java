package algs15.perc;

import stdlib.*;
import algs15.*;

// Uncomment the import statements above.
//

//QUESTIONS TO ASK PROFESSOR:
//1. can we use private methods
//2. do we need to account for N < 0, N = 0 or case N = 1
//3. fixing virtual bottom point index - is it even possible in this problem?
//	 What I mean is, once percolates() then anything connected to bottom row
//	 becomes connected to the top, which changes the isFull evaluation. 
//	 Does this matter?

// You can test this using InteractivePercolationVisualizer and PercolationVisualizer
// All methods should make at most a constant number of calls to the UF data structure,
// except percolates(), which may make up to N calls to the UF data structure.
public class Percolation {
	int N;
	private final int VPTI; //virtual point top index
	boolean[] open;
	private final int id[];
	private final int sz[];

	public Percolation(int N) {
		this.N = N;
		this.VPTI = N*N; //ex: N=10 so VPTI is 100
		//this.open does not allocate space for the virtual point
		//ex: N=10 implies there is space for 0-99 or 100 spaces
		this.open = new boolean[N*N]; 
		//has a space for virtual point indexed at N*N
		//ex: N=10 so there is spaces 0-100 or 101 spaces
		this.id = new int[N*N + 1]; 
		this.sz = new int[N*N + 1];
		
		//initializes each point as its own root
		for (int i = 0; i < N*N + 1; i++) {
			id[i] = i;
			sz[i] = 1;
		}
		
		/*
		//connects top(1st) row to virtual point top
		//can represent a 2D array in a 1D array
		//by following convention:
		//rowindex = i
		//colindex = j
		//N = Number of columns
		//This means i*N is the ith row,
		//and + j is which column in the ith row
		//ex: 5th row 5th column of 6x10 array is represented as
		// 4 * 10 + 4
		//remember! arrays index at 0!!
		 */
		//in this case, go through first row (i=0)
		for (int j = 0; j < N; j++) {
			union(0*N + j, VPTI);
		}
	}
	
	private int find(int point) {
		//keeps looping through to find which number is stored at each array index
		//eventually arrives at the number that is stored at its own index
		//this is the root
		while (point != id[point]) {
			//path compression by halving
			//essentially assigns root of parent to root of child
			//speeds future searches up - more direct connections to root
			id[point] = id[id[point]];   
			point = id[point];
		}
		return point;
	}
	
	private boolean connected(int point, int qoint) {
		//returns true if both points share the same root
		//if points share the same root, then they are connected
		return find(point) == find(qoint);
	}
	
	private void union(int point, int qoint) {
		//finds the root of point and qoint
		int pRootID = find(point);
		int qRootID = find(qoint);
		//if already same root, union is complete so exit method
		if (pRootID == qRootID) return;
		
		//decide which root takes precedence depending on the size of each group
		//essentially, whichever is the smaller group's root is changed to the bigger grouo's root
		//this means there are less loops when running find() overall
		//also adds the total size of the group to whichever the dominant root ID is
		if   (sz[pRootID] <= sz[qRootID]) 	{ id[pRootID] = qRootID; sz[qRootID] += sz[pRootID]; }
		else                     			{ id[qRootID] = pRootID; sz[pRootID] += sz[qRootID]; }
	}
	
	// open point (row i, column j) if it is not already
	public void open(int i, int j) {
		open[i*N + j] = true;
		connect4(i,j);
	}
	
	//connect to squares on all 4 sides if possible
	private void connect4(int i, int j) {
		//Reason for nested IF statement - to avoid indexoutofbounds exception
		//maybe there's an easier way to avoid this exception?
		//is try-catch block faster?
		//connect point with open point above
		if (i > 0) {if(isOpen(i-1,j)) {union((i-1)*N + (j), i*N + j);}}
		//connect point with open point below
		if (i < N-1) {if(isOpen(i+1,j)) {union((i+1)*N + (j), i*N + j);}}
		//connect point with open point to left
		if (j > 0) {if(isOpen(i,j-1)) {union((i)*N + (j-1), i*N + j);}}
		//connect point with open point to right
		if (j < N-1) {if(isOpen(i,j+1)) {union((i)*N + (j+1), i*N + j);}}
	}
	
	
	// is site (row i, column j) open?
	public boolean isOpen(int i, int j) {
		return open[i*N + j];
	}
	
	// is site (row i, column j) full?
	public boolean isFull(int i, int j) {
		//to be full, needs to both be open and connected with the top row
		//here connection with top row represented by the virtual point
		return open[i*N + j] && connected(i*N + j, VPTI);
	}
	
	// does the system percolate?
	public boolean percolates() {
		//runs through each of the last row's columns to see if
		//any of these points are connected with the top row
		//if so, percolates. If not, then does not percolate
		//unfortunately, without implementing a virtual bottom point
		//this runtime is N. Significantly slows program
		for (int j = 0; j < N; j++) {
			if (connected((N-1)*N + j, VPTI)) {return true;}
		}
		return false;
	}
}