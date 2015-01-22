import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Player extends Remote {
	public static final String[] VALID_ANSWER_TO_NO = {"No", "no","N", "n"};
	public static final String[] VALID_ANSWER_TO_YES = {"Yes","yes", "Y", "y"};
	public void print(String text) throws RemoteException;
	public void setGame(Game game) throws RemoteException;
	public boolean isSimilar(Player player) throws RemoteException;
	public String getName() throws RemoteException;
	public void setPlayerMarker(String Marker) throws RemoteException;
	public String getPlayerMarker() throws RemoteException;
	public boolean stillAlive() throws RemoteException;
	public void rematch(int status) throws RemoteException;
	
}
