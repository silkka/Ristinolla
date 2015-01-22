import java.net.MalformedURLException;
import java.rmi.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Client{
	private static int PLAYER_NAME_PARAMETER_INDEX = 0;
	private static int LOBBY_IP_PARAMETER_INDEX = 1;
	private static String name;
	
	public static void main(String... asd){
		
		if(asd != null && asd.length > PLAYER_NAME_PARAMETER_INDEX && asd[PLAYER_NAME_PARAMETER_INDEX] != null)
			name = asd[PLAYER_NAME_PARAMETER_INDEX];		
		else 
			name = "Player " + ((int) (Math.random() * 1000 + 1)); // Not all that random a name

		System.setSecurityManager(new RMISecurityManager());
		try {
			Lobby lobby;
			
			if(asd.length > LOBBY_IP_PARAMETER_INDEX && asd[LOBBY_IP_PARAMETER_INDEX] != null){
				lobby = (Lobby)Naming.lookup("rmi://"+ asd[LOBBY_IP_PARAMETER_INDEX] +"/" + Lobby.NAMING);
			}else{
				lobby = 	(Lobby)Naming.lookup("rmi://localhost/" + Lobby.NAMING);
			}

			PlayerImplementation player = new PlayerImplementation(name, lobby);
			new Thread(player).start();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			System.out.println("Connection to lobby failed.");
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
