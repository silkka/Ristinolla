import java.rmi.Naming;
import java.rmi.RMISecurityManager;


public class Server {
	public static void main(String... asd){
		try {
			System.setSecurityManager(new RMISecurityManager());
			LobbyImplementation lobby = new LobbyImplementation();
			Naming.rebind(Lobby.NAMING, lobby);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
