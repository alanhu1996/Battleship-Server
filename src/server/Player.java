package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import server.DBConnect;
import server.Server;
import static server.Server.cbLabels;
import static server.Server.pbLabels;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alanhu
 */
public class Player implements Runnable {

    String mark;
    Player opponent;
    Socket socket;
    String[][] board;
    int row = 0, col = 0;
    int[] cpuHitPoints = {5, 4, 3, 3, 2}, playerHitPoints = {5, 4, 3, 3, 2}, level = {0}, wins = {0};
    String ships = "CBSDP";
    int which;
    BufferedReader input;
    PrintWriter output;
    PrintWriter enemy;
    String enemyName;
    String userName;
    boolean enableLogin = false;

    /**
     * Constructs a handler thread for a given socket and mark initializes the
     * stream fields, displays the first two welcoming messages.
     */
    public Player(Socket socket, String mark, String[][] board, int which) {
        System.out.println("A client has connected");
        Server.counter++;
        this.socket = socket;
        this.mark = mark;
        this.board = board;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                board[row][col] = "*";
            }
        }
        generate(board);

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("WELCOME " + mark);
            String total = "";
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    total += board[row][col] + " ";
                }
            }
            output.println(total); //sends the board

        } catch (IOException e) {
            System.out.println("Player is unable to connect: " + e);
        }
    }

    public synchronized void generate(String[][] board) {
        Random rand = new Random();
        int i = 0;
        int input = 0;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                input = rand.nextInt(rand.nextInt(20) + 1);
                if (row == 9) {
                    input = 1;
                }
                if (i < 5 && input == 1 && (col + playerHitPoints[i] - 1) < 10) {//if it's 1 then output a ship (credits to Cynthia :))
                    for (int a = 0; a < playerHitPoints[i]; a++) {
                        board[row][col] = "" + ships.charAt(i);
                        col++;
                        if (a == playerHitPoints[i] - 1) {
                            col--;
                        }

                    }
                    i++;
                } else {
                    board[row][col] = "*";
                }
            }
        }

    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
    public void start () {
        Thread thread = new Thread();
        
    }

    /**
     * The run method of this thread.
     */
    public void run() {
        try {
            // Repeatedly get commands from the client and process them.
            while (true) {
                String command = input.readLine();

                if (command.length() == 3) {
                    System.out.println(command);
                    //if (makeValidAttack(board, Character.getNumericValue(command.charAt(1)), Character.getNumericValue(command.charAt(2))).equals("M")) {
                    System.out.println("WORKS");
                    row = Character.getNumericValue(command.charAt(1));
                    col = Character.getNumericValue(command.charAt(2));
                    //board[row][col] = "" + command.charAt(0);

                    enemy.println("" + row + col);
                    System.out.println("" + row + col);
                        // } else if (command.charAt(0) != 'M' && command.charAt(0) != 'N') {

                        //} else {
                    //output.println("MESSAGE ?");
                    //}
                } else if (command.startsWith("ENDGAME")) {
                    enemy.println("ENDGAME");
                    DBConnect dataBase = new DBConnect(userName);
                    dataBase.increaseWins(wins);
                    System.out.println("Win increased!");
                    output.println("INCREASEDWINS" + wins[0]);
                } else if (command.startsWith("USER") && enableLogin == true) {
                    enemy.println(command); //sends name to enemy client
                    userName = command.substring(4);

                    DBConnect dataBase = new DBConnect(command.substring(4));
                    if (dataBase.checkUserExist(command.substring(4)) == false) {
                        dataBase.add(command.substring(4));
                        dataBase.loadInfo(level, wins);
                        output.println("LEVEL" + level[0]);
                        output.println("CREATE");
                        System.out.println("ADDED!");
                    } else {
                        dataBase.loadInfo(level, wins);
                        output.println("OLDUSER");
                        output.println("LEVEL" + level[0]);
                        output.println("WINS" + wins[0]);

                    }
                    enemy.println("ENEMYLEVEL" + level[0]);
                    enemy.println("ENEMYWINS" + wins[0]);
                        // } else if (command.startsWith("USER") && enableLogin == false) {
                    //  output.println("UNABLETOLOGIN");
                }
            }
        } catch (IOException e) {
            //System.out.println("Player died: " + e);
            System.out.println("A client has disconnected");
            //Server.counter++;
            pbLabels = new String[10][10];
            cbLabels = new String[10][10];
            board = new String[10][10];
            enemy.println("PLAYERDC");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
