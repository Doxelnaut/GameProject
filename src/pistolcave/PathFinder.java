package pistolcave;


import java.io.*;

/**
 * 
 * @author Corey Amoruso
 * @author Ryan Bergquist
 * @author Zacharias Shufflebarger
 * 
 *	This file is part of Pistol Cave.
 *
 *  Pistol Cave is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Pistol Cave is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Pistol Cave.  If not, see <http://www.gnu.org/licenses/>.
 *
 *	Copyright 2014 Corey Amoruso, Ryan Bergquist, Zacharias Shufflebarger
 */
public class PathFinder 
{
	private static final int Up = 0, UpRight=1, Right=2, DownRight=3, Down=4, DownLeft=5, Left=6, UpLeft=7, CTRL=8;

	/*the below variables are the "INPUT" variables*/
	static int numrows=100;		static 	int numcols=100;
	public int startrow;
	public int startcol;
    public int endrow ;
    public int endcol;
	public int foundPath;
	public float nextX = -1;
	public float nextY = -1;
	public float currentX = -1;
	public float currentY = -1;
	static 	int[][] myMaze;
	Actor enemy;
	Player player;
	/*these two variables keep track of the shortest path found so far*/
	static 	int shortestpath[]=new int[numrows*numcols];
	int shortestlength;
	static int pathsofar[]=new int[numrows*numcols];  /*for max size*/
	int cutoff;
	public PathFinder(Actor enemy, Player player) {
		this.enemy = enemy;
		this.myMaze = PistolCaveGame.map;
		this.player = player;
		foundPath = 0;
		this.startrow=(int) (player.wPosition.getY()/PistolCaveGame.TILE_SIZE);
		this.startcol=(int) (player.wPosition.getX()/PistolCaveGame.TILE_SIZE);
	    this.endrow= (int) (enemy.wPosition.getY() /PistolCaveGame.TILE_SIZE);
	    this.endcol=(int)(enemy.wPosition.getX()/PistolCaveGame.TILE_SIZE);
	    double xx = (endrow - startrow) * (endrow - startrow);
		double y = (endcol-startcol) * (endcol-startcol);
		double z = Math.sqrt(xx+y); //distance formula
		
		if(z < 2){
			cutoff = 3;
		}
		else if( z < 5){
			cutoff = 6;
		}
		else{
			cutoff = 11;
		}
	    //Enemy is close to the user
	    
		int r,c,x;				/*various counters*/
		int lengthsofar;		/*length to get started with*/
		for (x=0;x<this.numrows*this.numcols;x++){
			this.shortestpath[x]=-1;  /* initializing the path arrays*/
			this.pathsofar[x]=-1;
		}

		/*initial lengths*/
		this.shortestlength=this.numrows*this.numcols;
		lengthsofar=0;

		System.out.println("Getting new path");

		this.findpath(startrow, startcol, pathsofar, lengthsofar);

//		System.out.println("");
		System.out.println("Path is length: "+ this.shortestlength);
		
		currentX = endrow;
		currentY = endcol;
		
		findNext();
		System.out.println("currentx: " + currentX + " currentY:" + currentY);
		System.out.println("nextx: " + nextX + " nextY:" + nextY);
		System.out.println();
//		this.showmypath(this.shortestpath, this.shortestlength);

	}

	private void findNext() {
		if (beenhere((int)currentX-1, (int)currentY, shortestpath, shortestlength)) {
			nextX = currentX -1;
			nextY = currentY;
			//return;
		}
		else if (beenhere((int)currentX, (int)currentY-1, shortestpath, shortestlength)) {
			nextX = currentX;
			nextY = currentY-1;
			//return;
		}
		else if (beenhere((int)currentX, (int)currentY+1, shortestpath, shortestlength)) {
			nextX = currentX;
			nextY = currentY+1;
			//return;
		}
		else if (beenhere((int)currentX+1, (int)currentY, shortestpath, shortestlength)) {
			nextX = currentX+1;
			nextY = currentY;
		//	return;
		}
		
		//no current path
		if(nextX == -1 || nextY == -1) return;
		if(currentX > nextX){
			enemy.move(Left);
		}
		if(currentX < nextX){
			enemy.move(Right);
		}
		if(currentY < nextY){
			enemy.move(Down);

		}
		if(currentY > nextY){
			enemy.move(Up);

		}
		return;
		
	}

	/*******************************************************************/
	
	private boolean beenhere(int row, int col, int pathsofar[], int lengthsofar){
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
		if(foundPath == 3)return;
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
			foundPath += 1;
//			System.out.println("Found path of length "+lengthsofar+"!:");
//			showmypath(mypath, lengthsofar);

			if (lengthsofar<=shortestlength){ /*New shortest path?*/
				shortestlength=lengthsofar;
				System.arraycopy(mypath, 0, shortestpath, 0, lengthsofar);
				System.out.println(" (shortest path of length " + lengthsofar + ")");
				showmypath();
			}
			System.out.println("");
			return;
		}

		/*Below, recursively call to go to other squares*/
		if(lengthsofar < cutoff){
			
			findpath(row-1, col, mypath, lengthsofar);
			findpath(row, col-1, mypath, lengthsofar);
			findpath(row, col+1, mypath, lengthsofar);
			findpath(row+1, col, mypath, lengthsofar);
		}
	}
			
	/*******************************************************************/
	
	public void showmypath(){
		/*this function prints out the maze and the path traveled so
		  far.*/

		int r,c;

		for (r=0;r<numrows;r++){
			for(c=0;c<numcols;c++){
				if (myMaze[r][c]!=0)
					System.out.print("|");			     /*  | for walls   */
				else if (r==startrow && c==startcol)
					System.out.print("S");			     /*  S for start   */
				else if (r==endrow && c==endcol)
					System.out.print("X");			     /*  X for exit   */
				else if (beenhere(r,c,this.shortestpath,this.shortestlength))
					System.out.print("o");			     /*  o for traveled   */
				else
					System.out.print(" ");			     /*    empty space  */
			}
			System.out.println("");
		}
	}
}