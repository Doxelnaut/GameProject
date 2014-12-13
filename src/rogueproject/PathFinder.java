package rogueproject;


import java.io.*;

public class PathFinder 
{
	/*the below variables are the "INPUT" variables*/
	static int numrows=100;		static 	int numcols=100;
	static int startrow;
	static int startcol;
    static int endrow ;
    static int endcol;
	static boolean foundPath;
	static 	int[][] myMaze;

	/*these two variables keep track of the shortest path found so far*/
	static 	int shortestpath[]=new int[numrows*numcols];
	static 	int shortestlength;
	int pathsofar[]=new int[this.numrows*this.numcols];  /*for max size*/

	public PathFinder(int[][] map,Actor enemy, Player player) {
		this.myMaze = map;
		foundPath = false;
		startrow=(int) (player.wPosition.getY()/RogueGame.TILE_SIZE);
		startcol=(int) (player.wPosition.getX()/RogueGame.TILE_SIZE);
	    endrow= (int) (enemy.wPosition.getY() /RogueGame.TILE_SIZE);
	    endcol=(int)(enemy.wPosition.getX()/RogueGame.TILE_SIZE);
	    if((endrow - startrow) + (endcol-startcol) > 5) return;
		int r,c,x;				/*various counters*/
		int lengthsofar;		/*length to get started with*/
		for (x=0;x<this.numrows*this.numcols;x++){
			this.shortestpath[x]=-1;  /* initializing the path arrays*/
			this.pathsofar[x]=-1;
		}

		/*initial lengths*/
		this.shortestlength=this.numrows*this.numcols;
		lengthsofar=0;

		System.out.println("Here's the maze:");
		for (r=0;r<this.numrows;r++){
			for (c=0;c<this.numcols;c++){
				if (r==startrow && c==startcol)		/*outputing the initial*/
					System.out.print("S");			/* maze state*/
				else if (r==endrow && c==endcol)
					System.out.print("x");
				else if (this.myMaze[r][c]!=0)
					System.out.print("|");
				else System.out.print(" ");
			}
			System.out.println("");
		}

		System.out.println("");
		System.out.println("Finding Paths...");

		this.findpath(startrow, startcol, pathsofar, lengthsofar);

		System.out.println("");
		System.out.println("The shortest path found was the following of length "+ this.shortestlength);
		this.showmypath(this.shortestpath, this.shortestlength);

	}

//	private int[][] getSmallerMap(int[][] map, Player player) {
//		int k = (int)(player.getX()/RogueGame.TILE_SIZE) - 15;
//		int[][] a = new int[25][25];
//		for(int i = 0 ; i < 25;i++){
//			for(int j = 0; j < 25; j++){
//				a[0][0]
//			}
//		}
//		return null;
//	}

	/*******************************************************************/
	
	boolean beenhere(int row, int col, int pathsofar[], int lengthsofar){
		/*this private boolean function tells if this spot (row,col) has
		  been visited before*/
		 
		int x;
		int target = row*numcols+col;  /*this computation gives a unique
										id to each maze square*/

		for (x=0;x<lengthsofar;x++)
			if (pathsofar[x]==target) return true;

		return false;
	}

	/*******************************************************************/
	
	public void showmypath(int mypath[], int mylength){
		/*this function prints out the maze and the path traveled so
		  far.*/

		int r,c;

		for (r=0;r<numrows;r++){
			for(c=0;c<numcols;c++){
				if (myMaze[r][c]==1)
					System.out.print("|");			     /*  | for walls   */
				else if (r==startrow && c==startcol)
					System.out.print("S");			     /*  S for start   */
				else if (r==endrow && c==endcol)
					System.out.print("X");			     /*  X for exit   */
				else if (beenhere(r,c,mypath,mylength))
					System.out.print("o");			     /*  o for traveled   */
				else
					System.out.print(" ");			     /*    empty space  */
			}
			System.out.println("");
		}
	}
	/*******************************************************************/
	public void findpath(int row, int col, int pathsofar[], int lengthsofar){
		/*This is the recursive function that finds the paths.  When it
			does find a valid path, it outputs it then stores it into
			shortestpath if is is shorter that what is currently held*/

		/*These 3 statements are the termination conditions:
			out of bounds,  wall, and previously visited, respectively*/
		if(foundPath == true)return;
		if (row<0 || col<0 || row>=numrows || col>=numcols)
			return;
		if (myMaze[row][col]==1) return ;
		if (beenhere(row, col, pathsofar, lengthsofar)) return;

		int mypath[]=new int[lengthsofar+1];

		System.arraycopy(pathsofar, 0, mypath, 0, lengthsofar);  
			/*for local copy for proper backtracking*/
							
		
		mypath[lengthsofar++]=row*numcols+col;  /*adds this square to
													traveled path*/

		if (row==endrow && col==endcol){		
			/*Reached the end, thus finding a valid path*/
			foundPath = true;
			System.out.println("Found path of length "+lengthsofar+"!:");
			showmypath(mypath, lengthsofar);

			if (lengthsofar<=shortestlength){ /*New shortest path?*/
				shortestlength=lengthsofar;
				System.arraycopy(mypath, 0, shortestpath, 0, lengthsofar);
				System.out.println(" (New shortest path of length " + lengthsofar + ")");
			}
			System.out.println("");
			return;
		}

		/*Below, recursively call to go to other squares*/
		if(lengthsofar < 15){
			
			findpath(row-1, col, mypath, lengthsofar);
			findpath(row, col-1, mypath, lengthsofar);
			findpath(row, col+1, mypath, lengthsofar);
			findpath(row+1, col, mypath, lengthsofar);
		}
	}
			
	/*******************************************************************/
//	public static void main(String[] args) 
//	{
//		/* The main function initializes appropriate variables,
//			outputs the initial maze, finds the path, and outputs it*/
//
//		int r,c,x;				/*various counters*/
//		int pathsofar[];		/*Path to get started with*/
//		int lengthsofar;		/*length to get started with*/
//
//		PathFinder daMaze=new PathFinder();	  /*the maze object*/
//
//		pathsofar=new int[daMaze.numrows*daMaze.numcols];  /*for max size*/
//		
//		for (x=0;x<daMaze.numrows*daMaze.numcols;x++){
//			daMaze.shortestpath[x]=-1;  /* initializing the path arrays*/
//			pathsofar[x]=-1;
//		}
//
//		/*initial lengths*/
//		daMaze.shortestlength=daMaze.numrows*daMaze.numcols+1;
//		lengthsofar=0;
//
//		System.out.println("Here's the maze:");
//		for (r=0;r<daMaze.numrows;r++){
//			for (c=0;c<daMaze.numcols;c++){
//				if (r==startrow && c==startcol)		/*outputing the initial*/
//					System.out.print("S");			/* maze state*/
//				else if (r==endrow && c==endcol)
//					System.out.print("x");
//				else if (daMaze.myMaze[r][c]==0)
//					System.out.print(" ");
//				else System.out.print("|");
//			}
//			System.out.println("");
//		}
//
//		System.out.println("");
//		System.out.println("Finding Paths...");
//
//		daMaze.findpath(startrow, startcol, pathsofar, lengthsofar);
//
//		System.out.println("");
//		System.out.println("The shortest path found was the following of length "+ daMaze.shortestlength);
//		daMaze.showmypath(daMaze.shortestpath, daMaze.shortestlength);
//
//	}
}