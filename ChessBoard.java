import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.List;
import java.util.LinkedList;

public class ChessBoard {
    public static Piece board[] = new Piece[64];
    public static List<Piece> pieces = new LinkedList<>();

    public static final int BOARD_SIZE = 8;
    public static Piece selectedPiece = null;
    public static boolean isWhiteTurn = true;
    public static boolean isLegal = true;
    private static boolean hasDragged = false;

    public static void main(String[] args) throws IOException {
        generateFen();
        BufferedImage all = ImageIO.read(new File("chess.png"));
        Image imgs[] = new Image[12];
        int ind = 0;
        for(int y = 0; y < 400; y += 200) {
            for(int x = 0; x < 1200; x+= 200) {
                imgs[ind] = all.getSubimage(x, y, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
                ind++;
            }
        }
        JFrame frame = new JFrame();
        frame.setBounds(10, 10, 512, 512); // was 512 then to 548
        frame.setUndecorated(true);
        JPanel pn = new JPanel() { 
            @Override
            public void paint(Graphics g) {
                for(int y = 0; y < 8; y++) {
                    for(int x = 0; x < 8; x++) {
                        if (selectedPiece != null && selectedPiece.xp == x && selectedPiece.yp == y) {
                            g.setColor(Color.yellow);
                        } else if ((x + y) % 2 == 0) {
                            g.setColor(Color.white);
                        } else {
                            g.setColor(Color.darkGray);
                        }
                        g.fillRect(x*64, y*64, 64, 64);
                    }
                }
                for(Piece p : pieces) {
                    int ind = switch (p.name) {
                        case "king" -> 0;
                        case "queen" -> 1;
                        case "bishop" -> 2;
                        case "knight" -> 3;
                        case "rook" -> 4;
                        case "pawn" -> 5;
                        default -> 5;
                    };
                    if (!p.isWhite) {
                        ind += 6;
                    }
                    g.drawImage(imgs[ind], p.x, p.y, this);
                }
            }
        };
        
        frame.add(pn);
        frame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPiece != null) {
                    selectedPiece.x = e.getX() - 32;
                    selectedPiece.y = e.getY() - 32;
                    frame.repaint();
                }
                hasDragged = true;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
            
        });
        
        frame.addMouseListener(new MouseListener() {

            // do not use mouseClicked method it sucks
            @Override
            public void mouseClicked(MouseEvent e) {
                // System.out.println("clicked");
                // if (selectedPiece == null) {
                //     System.out.println(selectedPiece == null);
                //     selectedPiece = getPiece(e.getX(), e.getY());
                // } else {
                //     System.out.println(selectedPiece == null);
                //     selectedPiece.move(e.getX()/64, e.getY()/64);
                //     frame.repaint();
                //     selectedPiece = null;
                // }
                // hasClicked = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // if (hasClicked) {
                //     System.out.println("\nselected");
                //     selectedPiece = getPiece(e.getX(), e.getY());
                // }
                // hasClicked = true;
                if (selectedPiece == null) {
                    System.out.println(selectedPiece == null);
                    selectedPiece = getPiece(e.getX(), e.getY());
                } else if (!hasDragged) {
                    if (isLegal) {
                        selectedPiece.move(e.getX()/64, e.getY()/64);
                    } else {
                        selectedPiece = getPiece(e.getX(), e.getY());
                    }
                    System.out.println(selectedPiece == null);
                }
                frame.repaint();
                hasDragged = false;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //if (hasClicked) {
                    
                //}
                //hasClicked = true;
                if (hasDragged) {
                    System.out.println("mouse has been released");
                    selectedPiece.move(e.getX()/64, e.getY()/64);
                    frame.repaint();
                    //selectedPiece = null;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
            
        });
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
    }

    public static Piece getPiece(int x, int y) {
        int xp = x/64;
        int yp = y/64;

        return board[xp + yp * BOARD_SIZE];

        // for(Piece p : pieces) {
        //     if (p.xp == xp && p.yp == yp) {
        //         return p;
        //     }
        // }
        // return null;
    }

    public static String createFen() {
        String fen = new String();

        // TODO - make this work after making board generation work
        // note to self: this is going to be hard after finding out that you
        // also need to add the side to move, whether or not white or black
        // can still castle either king or queen side, if there is an available
        // en passant, the number of half moves since the last capture or pawn move,
        // and finally the number of moves that have occured in the game. Now some
        // of this should not be too difficult but en passant and castling might
        // be a bit tricky to figure out without there being bugs.

        return fen;
    }
    // example of how a fen string would look like.
    // Don't want to use one string but rather use parameters instead for importing different FENs
    final static String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    // as of right now this only reads the pieces on the board and nothing after the first whitespace occurs
    public static void generateFen() {
        String fenBoard = fen.split("\\s+", 5)[0];
        int file = 0;
        int rank = 0;

        for(char symbol : fenBoard.toCharArray()) {
            if (symbol == '/') {
                file = 0;
                rank++;
            } else {
                if (Character.isDigit(symbol)) {
                    file += Character.getNumericValue(symbol);
                } else {
                    int index = file + rank * BOARD_SIZE;
                    boolean isWhite = Character.isUpperCase(symbol);
                    board[index] = switch (Character.toLowerCase(symbol)) {
                        case 'k' -> new King(index, file, rank, isWhite);
                        case 'q' -> new Queen(index, file, rank, isWhite);
                        case 'r' -> new Rook(index, file, rank, isWhite);
                        case 'b' -> new Bishop(index, file, rank, isWhite);
                        case 'n' -> new Knight(index, file, rank, isWhite);
                        case 'p' -> new Pawn(index, file, rank, isWhite);
                        default -> null;
                    };
                    file++;
                }
            }
        }
    }
}
