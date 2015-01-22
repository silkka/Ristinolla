import java.rmi.*;



public interface Lobby extends Remote {
	public static final String NAMING = "lobby";
	public void findGame(Player player) throws RemoteException;
	
}
