import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Scanner;


public class PlayerImplementation extends UnicastRemoteObject implements Player,Runnable {
	
	
	private String name;
	private Lobby lobby;
	private Game game;

	private String playerMarker;
	private Scanner scn;
	
	public PlayerImplementation(String name, Lobby lobby) throws RemoteException{
		super();
		this.name = name;
		this.lobby = lobby;
		this.game = null;
		this.playerMarker = "a";
		this.scn = new Scanner(System.in);
	}

	@Override
	public void print(String text) throws RemoteException {
		System.out.println(text);
	}
	
	
	private boolean waitingForOpponent = false;
	private boolean waitingForRematch = false;
	private boolean waitingForMatch = false;
	
	@Override
	public void run() {
		try {
			lobby.findGame(this);
		} catch (RemoteException e) {
			System.out.println("Lost connection");
			System.exit(0);
		}
		
		while(true){
			
			try {
				if(game != null && game.otherPlayerAlive(this)){//Game is alive and the other guy has not left
																//If the other player has left unannounced then this will throw
																//RemoteException.
					
					if(waitingForRematch){//If we have opted for rematch and there has been no answer from the other player.
						waitDotFunction(waitingForRematch, "Waiting for rematch");
						Thread.sleep(2000);
						continue;
					}
					
					if(game.getGameState(this) == Game.GAME_OVER){//If the current game comes to an end we show the score and ask for a rematch.
						System.out.println();
						System.out.println(game.getOverallScore());
						
						String input = getPlayerInput(joinTwoArrays(VALID_ANSWER_TO_NO, VALID_ANSWER_TO_YES), "Rematch? (Y/N)");
						if(checkPlayerInput(VALID_ANSWER_TO_YES, input)){
							waitingForRematch = true;
							game.rematch(this,1);
							waitDotFunction(waitingForRematch, "Waiting for rematch");
							
						}else{
							game.rematch(this,2);
							System.out.println("Thanks for playing!");
							System.exit(0);
						}
						
						
					}
					else if(game.getGameState(this) == Game.MY_TURN){//My turn begins
						System.out.println();
						System.out.println("Your turn");
						System.out.println("You are " + playerMarker + " and the current grid is: ");
						
						printGrid(game.getGrid());
						boolean valid = false;
						int move = 0;
						do{
							move = Integer.parseInt(getPlayerInput(Game.GRID_POSITIONS, "Select one of the free slots: "));
							if(game.isGridSlotFree(move-1)){
								valid = true;
							}
						}while(!valid);
						
						
						game.makeMove(move-1,playerMarker);

						printGrid(game.getGrid());
						waitingForOpponent = false;
					}
					else if(game.getGameState(this) == Game.OPPONENTS_TURN){//Waiting for the opponent to finnish
						waitDotFunction(waitingForOpponent, "Waiting for the opponent to make his move.");
						waitingForOpponent = true;
						Thread.sleep(2000);
					}
					
				}else if (game == null){//Waiting for the game
					waitDotFunction(waitingForMatch, "Waiting for a game");
					waitingForMatch = true;
					Thread.sleep(2000);
				}
			
			
			} catch (RemoteException e) {
				System.out.println();
				System.out.println("Lost connection");
				System.exit(1);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void setGame(Game game) throws RemoteException {
		this.game = game;
		
	}

	@Override
	public boolean isSimilar(Player player) throws RemoteException {
		if(player == null) return false;
		
		if(this.name.equals(player.getName()) && this.playerMarker.equals(player.getPlayerMarker())){
			return true;
		}
		
		return false;
	}


	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public void setPlayerMarker(String marker) throws RemoteException {
		playerMarker = marker;
	}

	@Override
	public String getPlayerMarker() throws RemoteException {
		return playerMarker;
	}
		
	private void printGrid(String[] grid){

		for(int i = 0;i<grid.length;i++){
			if(i%3 == 0){
				System.out.println();
				System.out.println("+-+-+-+");
				System.out.print("|");
			}
			System.out.print(grid[i] + "|");
		}
		System.out.println();
		System.out.println("+-+-+-+");
	}
	
	private String getPlayerInput(String[] validInputs, String textForPlayer){
		String input;
		while(true){
			System.out.println(textForPlayer);
			
			input = scn.next();
			if(checkPlayerInput(validInputs, input)){
				break;
			}
		}
		
		return input;
	}
	
	private boolean checkPlayerInput(String[] validInputs, String input){
		
		for(int i = 0; i<validInputs.length; i++){
			if(validInputs[i].equals(input)){
				return true;
			}
		}
		return false;
	}
	
	
	public static String[] joinTwoArrays(String[] first, String[] second){
		String[] result = Arrays.copyOf(first, first.length + second.length);
		for(int i = 0;i<second.length; i++){
			result[first.length + i] = second[i];
		}
		return result;
	}
	
	//For coolness purposes
	private void waitDotFunction(boolean waitingBoolean, String message){
		if(!waitingBoolean){
			System.out.println();
			System.out.print(message);  
		}
		else {
			System.out.print(".");
		}
	}

	@Override
	public boolean stillAlive() throws RemoteException {
		return true;
	}

	@Override
	/**
	 * If status = 0 player will exit.
	 * if status = 1 player will stop waiting for a rematch and start playing.
	 */
	public void rematch(int status) throws RemoteException {
		if(status == 0){
			System.out.println();
			System.out.print("Your opponent left. Exiting...");
			System.exit(0);
		}
		
		if(status == 1)
			this.waitingForRematch = false;
		}
		
	}

