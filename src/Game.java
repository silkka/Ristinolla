import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Game extends Remote{
	
	public static final int MY_TURN = 1;
	public static final int OPPONENTS_TURN = -1;
	public static final int GAME_OVER = 0;
	public static final String[] GRID_POSITIONS = {"1","2","3","4","5","6","7","8","9"};
	public static final int[][] WINNING_COMBINATIONS = {
		{0,1,2},//Rows
		{3,4,5},
		{6,7,8},
		
		{0,3,6},//Columns
		{1,4,7},
		{2,5,8},
		
		{0,4,8},//Diagonals
		{2,4,6}
	};
	
	/**
	 * Queries the game for the state of the game.
	 * Returns Integer MY_TURN if it's the turn of the player given as the parameter.
	 * Returns Integer OPPONENTS_TURN if it's not the turn of the player given as the parameter.
	 * Returns Integer GAME_OVER if the game is over.
	 * @param player != null
	 * @throws RemoteException
	 */
	public int getGameState(Player player) throws RemoteException;
	public int getPlayerNumber(Player player) throws RemoteException;
	public String[] getGrid() throws RemoteException;
	public void makeMove(int move, String marker) throws RemoteException;
	public boolean isGridSlotFree(int slot) throws RemoteException;
	public String getOverallScore() throws RemoteException;
	public void rematch(Player player, int yes) throws RemoteException;
	public boolean otherPlayerAlive(Player you) throws RemoteException;
	public boolean playersStillPresent() throws RemoteException;
}
