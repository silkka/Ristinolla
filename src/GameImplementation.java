import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;


public class GameImplementation extends UnicastRemoteObject implements Game {

	private static final long serialVersionUID = 1L;
	private Player player1;
	private Player player2;
	private Player playing;
	private boolean stillPlaying;
	private String[] grid;
	private int player1victories;
	private int player2victories;
	private int player1Rematch;
	private int player2Rematch;
	
	public GameImplementation(Player player1, Player player2) throws RemoteException {
		super();

		player1victories = 0;
		player2victories = 0;
		player1Rematch = -1;
		player2Rematch = -1;
		grid = new String[9];
		initGrid();
		this.player1 = player1;
		this.player2 = player2;
		this.player1.setPlayerMarker("X");
		this.player2.setPlayerMarker("O");
		this.player1.setGame(this);
		this.player2.setGame(this);
		
		this.player1.print("");
		this.player1.print("Game is starting..");
		this.player1.print("You are X.");
		this.player2.print("");
		this.player2.print("Game is starting..");
		this.player2.print("You are O.");
		stillPlaying = true;
		this.playing = player1;
		
		
		
	}


	@Override
	public int getGameState(Player player) throws RemoteException {
		if(!stillPlaying) return GAME_OVER;
//		if(playing.isSimilar(player)){
//			System.out.println(MY_TURN);
//			return MY_TURN;
//		}
		if(playing.isSimilar(player)){
			//System.out.println(MY_TURN);
			return MY_TURN;
		}
		else{
			//System.out.println(OPPONENTS_TURN);
			return OPPONENTS_TURN;
		}
	}


	@Override
	public int getPlayerNumber(Player player) throws RemoteException {
		if(player.isSimilar(player1)) return 1;
		else if (player.isSimilar(player2)) return 2;
		else return 0;
	}


	@Override
	public String[] getGrid() throws RemoteException {
		return grid;
	}


	@Override
	public void makeMove(int move, String marker) throws RemoteException {
		
		grid[move] = marker;
		if(stalemate()){//Check for a stalemate
			stillPlaying = false;
			playing = null;
			player1.print("");
			player1.print("Stalemate!");
			player2.print("");
			player2.print("Stalemate!");
		}else{//Check for a winner
			
			Player winner = getWinner();
			if(winner != null){	
				stillPlaying = false;
				playing = null;
				winner.print("Victory is yours!");
				if(winner.isSimilar(player1)){
					player1victories++;
					player2.print("You have been defeated by one of the craftiest players in human history!");
					
				}
				else{
					player2victories++;
					player1.print("You have been defeated by one of the craftiest players in human history!");
				}
		
				
			}
		}
	
		
		//Switch the playing player
		if(playing != null){
			if(playing.isSimilar(player1)) playing = player2;
			else if (playing.isSimilar(player2)) playing = player1;
		}
		
	}


	

	
	
	private void initGrid(){
		for(int i = 0; i<grid.length;i++){
			grid[i] = (i+1) + "";
		}
	}


	@Override
	public boolean isGridSlotFree(int slot) throws RemoteException {
		if(slot >=0 && slot < grid.length && !grid[slot].equals("X") && !grid[slot].equals("O") ){
			return true;
			
		}
			
		return false;
	}
	
	
	private Player getWinner(){
		for(int i = 0;i<WINNING_COMBINATIONS.length;i++){
			if(slotsContainSameMarkers(WINNING_COMBINATIONS[i], "X")){
				return player1;
			}
		}
		for(int i = 0;i<WINNING_COMBINATIONS.length;i++){
			if(slotsContainSameMarkers(WINNING_COMBINATIONS[i], "O")){
				return player2;
			}
		}
		
		
		return null;
	}
	
	private boolean slotsContainSameMarkers(int[] slots, String marker){
		
		for(int slot: slots){
			if(!grid[slot].equals(marker)) return false;
		}
		
		return true;
	}


	@Override
	public String getOverallScore() throws RemoteException {
		return player1.getName() + " - " + player1victories + " | " + player2.getName() + " - " + player2victories;
	}


	@Override
	public void rematch(Player player,int yes) throws RemoteException {
		if(player.isSimilar(player1)) player1Rematch = yes;
		else if(player.isSimilar(player2)) player2Rematch = yes;
		
		if(player1Rematch == 1 && player2Rematch == 1){
			initGrid();
			try{
				player1.stillAlive();
				player1.print("");
				player1.print("Rematch is starting");
				
				
				player2.stillAlive();
				player2.print("");
				player2.print("Rematch is starting");
				
				stillPlaying = true;
				//Random player starts the new game.
				int startingNumber = (int) (Math.random() +0.5);
				if(startingNumber == 0) playing = player2;
				else playing = player1;
				player1.rematch(1);
				player2.rematch(1);
				player1Rematch = 0;
				player2Rematch = 0;
			}catch (RemoteException e){
				player1Rematch = 2;
			}
			
		}
		
		if(player1Rematch == 2 || player2Rematch == 2){
			try{
				player1.rematch(0);
			}catch (RemoteException e){
			}
			try{
				player2.rematch(0);
			}catch (RemoteException e){
			}
		}
		
	}


	@Override
	public boolean otherPlayerAlive(Player you) throws RemoteException {
		if(you.isSimilar(player1)){
			try{
				return player2.stillAlive();
			}catch (RemoteException e){
				return false;
			}
			
		}
		if(you.isSimilar(player2)){
			try{
				return player1.stillAlive();
			}catch (RemoteException e){
				return false;
			}
		}
		return false;
		
	}


	@Override
	public boolean playersStillPresent() throws RemoteException {

		return (player1.stillAlive() && player2.stillAlive());


	}
	
	private boolean stalemate() {
		for (int i = 0; i<grid.length;i++){
			if(!grid[i].equals("X") && !grid[i].equals("O"))
				return false;
		}
		return true;
	}
	
}
