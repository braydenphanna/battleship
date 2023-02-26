/**BATTLESHIP GUI
 * 
 * @braydenHanna
 * @2/8
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
public class gui {
    
    public static int gridTileS = 60;
    public static JPanel shipDragPanel = new JPanel();
    public static JLabel fleetDisplay = new JLabel();
    public static JPanel markerPanel = new JPanel();
    public static JPanel gridPanel = new JPanel(new GridLayout(10, 10));
    public static JButton start = new JButton();
    public static JLabel chatBox = new JLabel();

    public gui()
    {
        Border border = BorderFactory.createLineBorder(Color.blue, 1);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 800, 800);
        layeredPane.setPreferredSize(new Dimension(1100, gridTileS * 12));

        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(null);
        masterPanel.setBounds(0, 0, 800, 800);
        masterPanel.setPreferredSize(new Dimension(1100, gridTileS * 12));

        gridPanel.setBounds(60, 60, gridTileS * game.userBoard.length, gridTileS * game.userBoard[0].length);
        for (int i = 0; i < game.userBoard.length; i++) {
            for (int j = 0; j < game.userBoard[0].length; j++) {
                JLabel label = new JLabel();
                label.setBorder(border);
                label.setOpaque(false);
                gridPanel.add(label);
            }

            gridPanel.setOpaque(false);
        }
        layeredPane.add(gridPanel, JLayeredPane.PALETTE_LAYER);

        fleetDisplay.setBounds(gridTileS*1, gridTileS*11, gridTileS*10, gridTileS);
        fleetDisplay.setHorizontalTextPosition(SwingConstants.CENTER);
        fleetDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        fleetDisplay.setText("Your Fleet");
        fleetDisplay.setFont(new Font("Dialog", Font.BOLD, 14));
        fleetDisplay.setForeground(Color.blue);
        fleetDisplay.setOpaque(false);
        layeredPane.add(fleetDisplay, JLayeredPane.POPUP_LAYER);

        shipDragPanel.setLayout(null);
        shipDragPanel.setBounds(0, 0, 1000, gridTileS * 12);
        shipDragPanel.setOpaque(false);
        layeredPane.add(shipDragPanel, JLayeredPane.POPUP_LAYER);

        markerPanel.setLayout(null);
        markerPanel.setBounds(0, 0, 1000, gridTileS * 12);
        markerPanel.setOpaque(false);
        layeredPane.add(markerPanel, JLayeredPane.DRAG_LAYER);

        JPanel sidePanel = new JPanel();
        sidePanel.setOpaque(false);
        sidePanel.setBounds(gridTileS * 12, gridTileS, gridTileS * 5 + 20, gridTileS * 10);
        sidePanel.setLayout(null);

        start.setBounds(0, 0, 160, gridTileS);
        start.setBorder(border);
        start.setOpaque(false);
        start.setText("Start");
        start.setFont(new Font("Dialog", Font.BOLD, 14));
        start.setForeground(Color.blue);
        start.setFocusPainted(false);
        start.setContentAreaFilled(false);
        start.setBorderPainted(true);
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(game.gameState == 0){
                    //check for overlapping ships
                    for (ship s : game.ships) {
                        int count = 0;
                        for (int i = 0; i < game.userBoard.length; i++) {
                            for (int j = 0; j < game.userBoard[0].length; j++) {
                                if(game.userBoard[i][j] == s.size) count++;
                            }
                        }
                        if(count != s.size) {
                            game.chatText += "<br>Ships cannot overlap";
                            game.updateChat();
                            return;
                        }
                    }

                    game.start();
                    start.setText("");

                    gui.fleetDisplay.setText("Enemy Fleet");
                }
                else if(game.gameState == 2) {
                    game.generateMarkers(game.opponentBoard);
                    shipDragPanel.setVisible(false);
                    shipDragPanel.setEnabled(false);
                    start.setText("");
                    game.gameState = 1;
                    gui.fleetDisplay.setText("Enemy Fleet");
                }
            }
        });

        sidePanel.add(start);

        JButton settings = new JButton();
        settings.setBounds(160, 0, 160, gridTileS);
        settings.setBorder(border);
        settings.setText("Settings");
        settings.setForeground(Color.blue);
        settings.setFont(new Font("Dialog", Font.BOLD, 14));
        settings.setFocusPainted(false);
        settings.setOpaque(false);
        settings.setContentAreaFilled(false);
        settings.setBorderPainted(true);
        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.chatText += "<br>Settings coming soon";
                game.updateChat();
            }
        });
        sidePanel.add(settings);

        chatBox.setBounds(0, gridTileS, gridTileS * 5 + 20, gridTileS * 8+20);
        chatBox.setBorder(border);
        chatBox.setOpaque(false);
        chatBox.setFont(new Font("Dialog", Font.PLAIN, 12));
        chatBox.setForeground(Color.blue);
        chatBox.setVerticalTextPosition( SwingConstants.BOTTOM);
        chatBox.setVerticalAlignment(SwingConstants.BOTTOM);
        sidePanel.add(chatBox);

        JTextField chatEnter = new JTextField();
        chatEnter.setBounds(0, gridTileS * 9 +20, gridTileS * 5 + 20, gridTileS-20);
        chatEnter.setBorder(border);
        chatEnter.setFont(new Font("Dialog", Font.PLAIN, 12));
        chatEnter.setForeground(Color.blue);
        chatEnter.setOpaque(false);
        chatEnter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //add chat typing
            }
        });
        sidePanel.add(chatEnter);

        layeredPane.add(sidePanel, JLayeredPane.PALETTE_LAYER);

        JLabel space = new JLabel(util.scaleImage(new ImageIcon("space.jpg"), 0.4));
        space.setBounds(0, 0, 1200, 800);
        layeredPane.add(space, JLayeredPane.DEFAULT_LAYER);

        placeShips(game.ships);

        frame.add(layeredPane);
        frame.pack();
        frame.setVisible(true);
    }
    public static void placeMarker(int x, int y, boolean hit)
    {
        JLabel marker = new JLabel();
        marker.setBounds(y*gridTileS+gridTileS,x*gridTileS+gridTileS, gridTileS, gridTileS);
        if(!hit)marker.setIcon(new ImageIcon("MarkerWhite.png"));
        else if(hit)marker.setIcon(new ImageIcon("MarkerRed.png"));
        marker.setOpaque(false);
        markerPanel.add(marker);
        markerPanel.revalidate();
        markerPanel.repaint();
    }
    public void placeShips(ship[] ships)
    {
        for(ship s : ships)
        {
            s.setLocation(gridTileS, gridTileS);
            s.setSize(new Dimension(s.getWidth(),s.getHeight()));
            shipDragPanel.add(s);
            
        }
    }
}