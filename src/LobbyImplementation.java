import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;



public class LobbyImplementation extends UnicastRemoteObject implements Lobby {
	private static final long serialVersionUID = 1L;
	ArrayList<Player> players;
	ArrayList<Game> games;
	public LobbyImplementation() throws RemoteException {
		super();
		players = new ArrayList<Player>();
		games = new ArrayList<Game>();
	}
	
	
	@Override
	public synchronized void findGame(Player player) throws RemoteException {
		players.add(player);
		if(players.size() ==  2 && players.get(0) != null && players.get(1) != null){
			boolean alive = false;
			try {
				alive = players.get(0).stillAlive();
			}catch (RemoteException e){
				players.remove(0);
			}
			if(alive){
				games.add(new GameImplementation(players.remove(0), players.remove(0)));
				System.out.println("Game started");
			}
		}
		//Remove the finished games
		for(int i = 0;i<games.size();i++){
			try{
				games.get(i).playersStillPresent();
			
			
			}catch (RemoteException e){
				System.out.println("Expired game cleared!");
				games.remove(i);
				i--;
			}
			
		}
		
	}



	
	
}
