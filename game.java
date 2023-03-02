
/**BATTLESHIP GAME
 * 
 * @braydenHanna
 * @2/8
 */
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.event.MouseInputAdapter;

public class game {

    public static char [][] userBoard = new char[10][10];
    public static ship[] ships = new ship[3];
    public static boolean multiplayer = false;
    public static boolean won = false;
    public static boolean userTurn = false;
    public static char[][] opponentBoard = new char[10][10];
    public static int shotX = 0;
    public static int shotY = 0;
    public static String chatText = "";
    public static int gameState = 0;

    /*
     * 0 = unstarted
     * 1 = started & user can shoot
     * 2 = started & user can't shoot
     * 3 = dead
     */
    public static void main(String args[]) {
        chatText +="<br>Welcome to STAR WARS BATTLESHIP [BETA]";
        updateChat();

        generateEmptyBoard(userBoard);
        generateEmptyBoard(opponentBoard);

        ship v = new ship("Carrier");
        ships[0] = v;
        ship f = new  ship("Battleship");
        ships[1] = f;
        ship y = new  ship("Destroyer");
        ships[2] = y;
        new gui();
    }
    public static char[][] generateEmptyBoard(char[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = '-';
            }
        }
        return arr;
    }
    public static char[][] generateRandomBoard() {
        char[][] arr = new char[10][10];
        
        Random random = new Random();
        for (ship s : ships) {
            if (random.nextBoolean()) {
                int empty = 1;
                int x = 0;
                int y = 0;
                while (empty > 0) {
                    empty = 0;
                    x = (int) (Math.random() * (10 - s.size));
                    y = (int) (Math.random() * 10);
                    for (int i = 0; i < s.size; i++) {
                        empty += arr[x + i][y];
                    }
                }
                for (int i = 0; i < s.size; i++) {
                    arr[x + i][y] = s.letter;
                }
            } else {
                int empty = 1;
                int x = 0;
                int y = 0;
                while (empty > 0) {
                    empty = 0;
                    x = (int) (Math.random() * 10);
                    y = (int) (Math.random() * (10 - s.size));
                    for (int i = 0; i < s.size; i++) {
                        empty += arr[x][y + i];
                    }
                }
                for (int i = 0; i < s.size; i++) {
                    arr[x][y + i] = s.letter;
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(arr[i][j] == 0)
                {
                    arr[i][j] = '-';
                }
                    
            }
        }
        return arr;
    }

    public static void start() {
        System.out.print("Game started");
        gameState = 1;
        gui.shipDragPanel.setVisible(false);
        gui.shipDragPanel.setEnabled(false);
        for (ship s : ships) {
            s.locked = true;
        }

        if (!multiplayer) {
            opponentBoard = generateRandomBoard();
        }

        GeneralMouseListener shoot = new GeneralMouseListener();
        gui.gridPanel.addMouseListener(shoot);
        gui.gridPanel.addMouseMotionListener(shoot);
    }

    public static void getOpponentsTurn() {
        if (multiplayer) {

        } 
        else {
            int x = 0;
            int y = 0;
            while(userBoard[y][x] == 'h' || userBoard[y][x] == 'm'){
                x = (int) (Math.random() * 10);
                y = (int) (Math.random() * 10);
            }
            if (userBoard[y][x] != '-') {
                char letter = userBoard[y][x];
                userBoard[y][x] = 'h';
                generateMarkers(userBoard);
                gui.fleetDisplay.setText("Your Fleet");
                gui.shipDragPanel.setVisible(true);
                gui.shipDragPanel.setEnabled(true);
                chatText += "<br>Opponent hit your ship at (" + (x + 1) + ", " + (y + 1) + ")";
                updateChat();

                int count = 0;
                for (int i = 0; i < userBoard.length; i++) {
                    for (int j = 0; j < userBoard[0].length; j++) {
                        if(userBoard[i][j] == letter)
                        {
                            count++;
                        }
                    }
                }
                if(letter == 'c' && count==0) game.chatText += "<br>The enemy has sunk your Carrier";
                else if(letter == 'b' && count==0) game.chatText += "<br>The enemy has sunk your Battleship";
                else if(letter == 'd' && count==0) game.chatText += "<br>The enemy has sunk your Destroyer";
                game.updateChat();


                gui.start.setText("Continue");
                gameState = 2;
            }
            else{
                userBoard[y][x] = 'm';
            }
        }
    }
    public static void generateMarkers(char[][] arr){
        
        gui.markerPanel.removeAll();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(arr[i][j]=='m'){
                    gui.placeMarker(i, j, false);
                }
                else if(arr[i][j]=='h'){
                    gui.placeMarker(i, j, true);
                }
            }
        }
        gui.markerPanel.revalidate();
        gui.markerPanel.repaint();
    }
    public static void updateChat(){
        gui.chatBox.setText("<html>" + chatText + "</html>");
    }
    public static boolean checkForWin(char[][] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(arr[i][j] != 'm' && arr[i][j] != 'h' && arr[i][j] != '-')
                    return false;
            }
        }
        gameState = 3;
        return true;
    }
}

class GeneralMouseListener extends MouseInputAdapter {
    int x;
    int y;
    Component component;
    Point location;
    MouseEvent pressed;

    public void mousePressed(MouseEvent me){
        pressed = me;

    
        if (game.gameState == 2) {

            game.chatText+="<br>You must click the continue button to move on";
            game.updateChat();
        }
        if (game.gameState == 1) {

            game.shotX = (int) (me.getX() / gui.gridTileS);
            game.shotY = (int) (me.getY() / gui.gridTileS);
        }
        if (game.opponentBoard[game.shotY][game.shotX]  == 'h' || game.opponentBoard[game.shotY][game.shotX]  == 'm') return;

        if (game.opponentBoard[game.shotY][game.shotX]  !='h'&&game.opponentBoard[game.shotY][game.shotX]  != 'm'&&game.opponentBoard[game.shotY][game.shotX]  != '-') {
            System.out.println("Hit: " + game.shotY + ", " + game.shotX);
            
            for (ship s : game.ships) {
                if(s.letter == game.opponentBoard[game.shotY][game.shotX]){
                    s.health-=1;
                    if(s.health == 0) 
                    {game.chatText += "<br> You have sunk the enemy's " +s.type;
                    game.updateChat();}
                }
            }
            
            game.opponentBoard[game.shotY][game.shotX]  =  'h';
           
        } else if (game.opponentBoard[game.shotY][game.shotX]  == '-') {
            System.out.println("Miss: " + game.shotY + ", " + game.shotX);
            game.opponentBoard[game.shotY][game.shotX] = 'm';
        }
        
        game.generateMarkers(game.opponentBoard);

        if(game.checkForWin(game.opponentBoard)) {
            game.chatText  += "<br>Your are victorious";
            game.updateChat();
        }

        game.userTurn = false;
        game.getOpponentsTurn();
        

        if(game.checkForWin(game.userBoard)){
            game.chatText += "<br>The enemy is victorious";
            game.updateChat();       
        } 

        System.out.println("grid clicked");
        System.out.println("---------------------------");

        for (int i = 0; i < game.userBoard.length; i++) {
            for (int j = 0; j < game.userBoard[0].length; j++) {
                System.out.print(game.userBoard[i][j] + "  ");
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
        for (int i = 0; i < game.opponentBoard.length; i++) {
            for (int j = 0; j < game.opponentBoard[0].length; j++) {
                System.out.print(game.opponentBoard[i][j] + "  ");
            }
            System.out.print("\n");
        }
    }
}