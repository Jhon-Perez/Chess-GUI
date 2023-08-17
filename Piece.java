import java.util.List;
import java.util.LinkedList;

public class Piece {
    int index;
    int xp;
    int yp;
    int x;
    int y;
    boolean isWhite;
    String name;

    public Piece(int index, int xp, int yp, boolean isWhite, String n) {
        this.index = index;
        this.xp = xp;
        this.yp = yp;
        x = xp*64;
        y = yp*64;
        this.isWhite = isWhite;
        name = n;
        ChessBoard.pieces.add(this);
        //ChessBoard.board[xp + yp * ChessBoard.BOARD_SIZE] = this;
    }

    public void move(int xp, int yp) {
        if (isWhite != ChessBoard.isWhiteTurn) {
            ChessBoard.isLegal = false;
            x = this.xp*64;
            y = this.yp*64;
            return;
        }
        if (ChessBoard.getPiece(xp*64, yp*64) != null) {
            if (ChessBoard.getPiece(xp*64, yp*64).isWhite != isWhite) {
                ChessBoard.getPiece(xp*64, yp*64).delete();
            } else {
                ChessBoard.isLegal = false;
                x = this.xp*64;
                y = this.yp*64;
                return;
            }
        }
        ChessBoard.board[this.xp + this.yp * ChessBoard.BOARD_SIZE] = null;
        ChessBoard.board[xp + yp * ChessBoard.BOARD_SIZE] = this;
        ChessBoard.selectedPiece = null;
        ChessBoard.isLegal = true;
        ChessBoard.isWhiteTurn = !ChessBoard.isWhiteTurn;
        this.xp = xp;
        this.yp = yp;
        x = xp * 64;
        y = yp * 64;

        // for debugging purposes
        // System.out.println();
        // for(int i = 0; i < ChessBoard.board.length; i++) {
        //     if (i%8 == 0) {
        //         System.out.println();
        //     }
        //     if (ChessBoard.board[i] == null) {
        //         System.out.printf("%10s", "null ");
        //     } else {
        //         System.out.printf("%10s", (ChessBoard.board[i].isWhite ? "w " : "b ") + ChessBoard.board[i].name + " ");
        //     }
        // }
    }

    public void delete() {
        ChessBoard.pieces.remove(this);
    }
}

class King extends Piece {
    // indicies the piece can move to
    final int MOVES[] = {-9, -8, -7, -1, 1, 7, 8, 9};

    //boolean canCastle = true;

    public King(int index, int xp, int yp, boolean isWhite) {
        super(index, xp, yp, isWhite, "king");
    }

    public List<Integer> getLegalMoves() {
        List<Integer> legalMoves = new LinkedList<>();

        for(int m : MOVES) {
            // TODO - make the if statement check if the move is outside the board or there is a piece in the way
            if (m == 0) {
                // TODO - change this to actual coordinates instead of array values.
                legalMoves.add(m);
            }
        }

        return legalMoves;
    }

}

class Queen extends Piece {
    final int MOVES[] = {-9, -8, -7, -1, 1, 7, 8, 9};

    public Queen(int index, int xp, int yp, boolean isWhite) {
        super(index, xp, yp, isWhite, "queen");
    }

    public List<Integer> getLegalMoves() {
        List<Integer> legalMoves = new LinkedList<>();

        for(int m : MOVES) {
            for (int i = 1; m == 0; i++) {
                legalMoves.add(m * i);
            }
        }

        return legalMoves;
    }

}

class Rook extends Piece {
    final int MOVES[] = {-8, -1, 1, 8};

    public Rook(int index, int xp, int yp, boolean isWhite) {
        super(index, xp, yp, isWhite, "rook");
    }

    public List<Integer> getLegalMoves() {
        List<Integer> legalMoves = new LinkedList<>();

        for(int m : MOVES) {
            for (int i = 1; m == 0; i++) {
                legalMoves.add(m * i);
            }
        }

        return legalMoves;
    }

}

class Bishop extends Piece {
    final int MOVES[] = {-9, -7, 7, 9};

    public Bishop(int index, int xp, int yp, boolean isWhite) {
        super(index, xp, yp, isWhite, "bishop");
    }

    public List<Integer> getLegalMoves() {
        List<Integer> legalMoves = new LinkedList<>();

        for(int m : MOVES) {
            for (int i = 1; m == 0; i++) {
                legalMoves.add(m * i);
            }
        }

        return legalMoves;
    }

}

class Knight extends Piece {
    final int MOVES[] = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(int index, int xp, int yp, boolean isWhite) {
        super(index, xp, yp, isWhite, "knight");
    }

    public List<Integer> getLegalMoves() {
        List<Integer> legalMoves = new LinkedList<>();

        for(int m : MOVES) {
            if (m == 0) {
                legalMoves.add(m);
            }
        }

        return legalMoves;
    }

}

class Pawn extends Piece {
    final int MOVES[] = {9, 8, 7};
    public boolean firstMove = true;

    public Pawn(int index, int xp, int yp, boolean isWhite) {
        super(index, xp, yp, isWhite, "pawn");
    }

    public List<Integer> getLegalMoves() {
        List<Integer> legalMoves = new LinkedList<>();

        for(int m : MOVES) {
            if (this.isWhite) {
                if (firstMove) {
                    legalMoves.add(-16);
                }
                if (ChessBoard.board[this.index - m] != null) {
                    legalMoves.add(-m);
                }
            } else {
                if (firstMove) {
                    legalMoves.add(16);
                }
                if (ChessBoard.board[this.index + m] != null) {
                    legalMoves.add(m);
                }
            }
             
        }

        firstMove = false;
        return legalMoves;
    }
    
}