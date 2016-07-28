//Server
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import static server.Server.cbLabels;
import static server.Server.pbLabels;

public class Server {

    /**
     * Runs the server.
     */
    public static String[][] pbLabels = new String[10][10], cbLabels = new String[10][10];
    public static int counter = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("Server is running !");
        ServerSocket listener = new ServerSocket(9090);
        try {
            int playerAmount = 0;
            while (true) {
                Player player1 = new Player(listener.accept(), "PLAYER1", pbLabels, 0);
                Player player2 = new Player(listener.accept(), "PLAYER2", cbLabels, 1);
                String total1 = "";

                if (Server.counter == 2) {
                    player1.enableLogin = true;
                    player2.enableLogin = true;
                    player2.enemy = player1.output;
                    player1.enemy = player2.output;
                    player2.enemy.println("READY");
                    for (int row = 0; row < 10; row++) {
                        for (int col = 0; col < 10; col++) {
                            total1 += cbLabels[row][col] + " ";
                        }
                    }
                    player2.enemy.println(total1);
                    total1 = "";
                    player1.enemy.println("READY");
                    for (int row = 0; row < 10; row++) {
                        for (int col = 0; col < 10; col++) {
                            total1 += pbLabels[row][col] + " ";
                        }
                    }
                    player1.enemy.println(total1);

                    //Server.counter++;
                    //player1.output.println("true");
                }

                player1.setOpponent(player2);
                player2.setOpponent(player1);
                //game.currentPlayer = player1;
                player1.start();
                //player1.join();
                player2.start();
                if (Server.counter == 2) {
                    Server.counter = 0;
                }

            }

        } finally {
            listener.close();
        }
    }
}



