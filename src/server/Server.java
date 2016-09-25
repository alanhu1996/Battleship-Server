//Server
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;


public class Server {

    /**
     * Runs the server.
     */

    private static ArrayList<Game> gameList = new ArrayList<Game>();
    
    public static void main(String[] args) throws IOException {
        
        System.out.println("Server is running !");
        
        
        ServerSocket listener = new ServerSocket(9090);
        try {
            int playerAmount = 0;
            while (true) {
                Game newGame = new Game();
                newGame.addPlayerToGame(new Player(listener.accept(), "PLAYER1", 0));
                newGame.addPlayerToGame(new Player(listener.accept(), "PLAYER2", 1));
                String total1 = "";

                if (newGame.isReady()) {
                    newGame.init();
                    gameList.add(newGame);
                }
                newGame.startGame();

            }

        } finally {
            listener.close();
        }
    }
}



