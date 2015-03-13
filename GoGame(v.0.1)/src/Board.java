import java.awt.*;

public class Board extends Component implements IBoard {
    private Image board, pwhite, pblack;
    private List pieces;
    private int width, height, num_squares; //width and height of each square and number of squares
    private int xmax, xmin, ymin, ymax; //edges of board
    private MoveStack undo_stack, redo_stack;
    private Player p1, p2;
	
    Board () {
        pieces = new List ();
    }

    Board(Image img, Image w, Image b, Player p1, Player p2) {
        System.out.println("Board()");
        pieces = new List ();
        undo_stack = new MoveStack ();
        redo_stack = new MoveStack ();
        board = img;
        pwhite = w;
        pblack = b;
        width = 19;
        height = 19;
        ymin = 80;
        ymax = 615;
        xmin = 35;
        xmax = 560;
        this.p1 = p1;
        this.p2 = p2;
    }

    public void setImage(Image img){
        board = img;
    }
        
    public void SaveState() {
        List l_save = new List ();
        Saved_State save_state = new Saved_State ();
        Piece el = new Piece (0, 0, 0);
        boolean loop = true;

        pieces.gotoBeginning ();
        while (loop == true)	
        {
                el.setX (((Piece)pieces.getCursor()).getX());
                el.setY (((Piece)pieces.getCursor()).getY());
                el.setColor (((Piece)pieces.getCursor()).getColor());
                el.setLiberties (((Piece)pieces.getCursor()).getLiberties());
                l_save.insert (el);

                loop = pieces.gotoNext ();
        }

        save_state.setP1_Moves(p1.getMoves ());
        save_state.setP2_Moves(p2.getMoves ());
        save_state.setPieces(l_save);

        undo_stack.Push (save_state);
    }

    public void Undo() {
        Saved_State last = new Saved_State();
        Piece el = new Piece (0, 0, 0);
        boolean loop = true;

        if (!undo_stack.isEmpty ()) {
            SaveState ();
            redo_stack.Push (undo_stack.Pop ());
            last = undo_stack.Pop ();
        } else {
            pieces = new List ();
            undo_stack = new MoveStack ();
            p1.setMoves (0);
            p2.setMoves (0);
            return;
        }
        pieces.clear ();
        p1.setMoves (last.getP1_Moves ());
        p2.setMoves (last.getP2_Moves ());	

        last.getPieces().gotoBeginning ();
        while (loop == true) {
            el.setX (((Piece)last.getPieces ().getCursor()).getX());
            el.setY (((Piece)last.getPieces ().getCursor()).getY());
            el.setColor (((Piece)last.getPieces ().getCursor()).getColor());
            el.setLiberties (((Piece)last.getPieces ().getCursor()).getLiberties());
            pieces.insert (el);

            loop = last.getPieces ().gotoNext ();
        }	
    }

    public void Redo() {
        Saved_State last = new Saved_State();
        Piece el = new Piece(0, 0, 0);
        boolean loop = true;

        if (!redo_stack.isEmpty()) {
            SaveState();		
            last = redo_stack.Pop();
        } else {
            return;
        }
        pieces.clear ();
        p1.setMoves(last.getP1_Moves ());
        p2.setMoves(last.getP2_Moves ());	

        last.getPieces().gotoBeginning();
        while (loop == true) {
            el.setX (((Piece)last.getPieces ().getCursor()).getX());
            el.setY (((Piece)last.getPieces ().getCursor()).getY());
            el.setColor (((Piece)last.getPieces ().getCursor()).getColor());
            el.setLiberties (((Piece)last.getPieces ().getCursor()).getLiberties());
            pieces.insert (el);

            loop = last.getPieces ().gotoNext ();
        }	
    }

    //Return True if piece is at (x,y) or if (x,y) is off the board
    private boolean isPieceAt(int x, int y) {
        boolean result, loop=true;
        result = false;

        pieces.gotoEnd();
        while (loop == true) {	//checkNext returns the next node without moving cursor
            if (((Piece)pieces.getCursor()).getX () == x &&
                ((Piece)pieces.getCursor()).getY () == y) {	
                result = true;
                break;
            }
            loop = pieces.gotoPrior();
        }

        return result;
    }

    public boolean removePiece(int x, int y) {
        boolean result = true;
        if (isPieceAt(x, y)) {
            System.out.println("REMOVE:" + ((Piece)pieces.getCursor()).getX() + 
                    " " + ((Piece)pieces.getCursor()).getY ());
            pieces.remove ();
            result = true;
        }
        return result;
    }

    private boolean isSurrounded(int x, int y, int color) {
        boolean result = false;

        if (color == 0)
            color = 1;
        else
            color = 0;

        if ((isPieceAt (x, y - height) && ((Piece) pieces.getCursor ()).getColor () == color) 
                && (isPieceAt (x, y + height) && ((Piece) pieces.getCursor ()).getColor () == color)
                        && (isPieceAt (x - width, y) && ((Piece) pieces.getCursor ()).getColor () == color)
                                && (isPieceAt (x + width, y) && ((Piece) pieces.getCursor ()).getColor () == color))
            result = true;

        return result;
    }

    public boolean isPlayable(int x, int y, int color) {
        boolean result = true;

        System.out.println ("PLAY"+ x + " " + y);
        //if a peice can't be played there return false else return true
        if (x < xmin || y < ymin || x > xmax || y > ymax) {
            result = false;
        } else if (pieces.isEmpty () == false) {
            if ((isPieceAt (x, y)) || (isSurrounded (x, y, color))) {
                result = false;
            }
        }

        return result;
    }

    public Piece getPieceAt(int x, int y) {
        Piece result = null;
        boolean loop=true;

        if (!(isPieceAt (x, y)))
            return null;

        pieces.gotoEnd();
        while (loop == true) {	//checkNext returns the next node without moving cursor
            if (((Piece)pieces.getCursor()).getX () == x && 
                ((Piece)pieces.getCursor()).getY () == y) {
                result = (Piece)pieces.getCursor ();
                break;
            }
            loop = pieces.gotoPrior ();
        }
        return result;
    }

    public boolean DeleteLibsOf(Piece p, Piece caller) {
        Piece here;
        boolean result = true, loop;
        loop = true;
        pieces.gotoEnd();

        while (loop == true) {
            if (((Piece)pieces.getCursor()).getChecked() == 1 &&
                ((Piece)pieces.getCursor()).getColor() == p.getColor()) {
                pieces.remove ();
            }
            loop = pieces.gotoPrior ();
        }
        return result;
    }

    public void CheckLibs (Piece p) {
        boolean loop = true;
        Piece here;

        here = getPieceAt (p.getX()-height, p.getY());

        if (here != null && here.getChecked () != 1) {
            if (here.getLiberties () == 4) {
                System.out.println ("4 Libs");
                if (CheckSub (here, null)) {
                    System.out.println ("1: DELETE!!!!");		
                    DeleteLibsOf (here, null);
                    setCheckedZero ();
                }
            }
        }	
        here = getPieceAt (p.getX()+height, p.getY());

        if (here != null && here.getChecked() != 1){
            if (here.getLiberties () == 4) {
                System.out.println("4 Libs");
                if (CheckSub (here, null)) {
                    System.out.println("2: DELETE!!!!");		
                    DeleteLibsOf(here, null);
                    setCheckedZero();
                }
            }	
        }
        here = getPieceAt (p.getX(), p.getY()-height);

        if (here != null && here.getChecked() != 1) {
            if (here.getLiberties() == 4) {
                System.out.println("4 Libs");
                if (CheckSub(here, null)) {
                    System.out.println("3: DELETE!!!!");		
                    DeleteLibsOf(here, null);
                    setCheckedZero();
                }
            }	
        }
        here = getPieceAt (p.getX(), p.getY()+height);

        if (here != null && here.getChecked() != 1) {
            if (here.getLiberties() == 4) {
                System.out.println("4 Libs");
                if (CheckSub(here, null)) {
                    System.out.println ("4: DELETE!!!!");		
                    DeleteLibsOf (here, null);
                    setCheckedZero ();
                }
            }	
        }
    }

    public boolean CheckSub(Piece p, Piece caller) {
        Piece here;
        if (p.getChecked() == 1) {
            return p.getBool ();
        } else if (p.getLiberties() != 4) {
            p.setChecked(1);
            p.setBool (false);
            return false;
        } else {
            p.setChecked (1);
            p.setBool (true);

            here = getPieceAt(p.getX()-height, p.getY()); 
            if (here != null && here.getColor() == p.getColor() && here != caller) {
                if (!CheckSub(here, p)) {
                    p.setBool(false);
                    return false;
                }
                p.setBool (true);
            }

            here = getPieceAt(p.getX()+height, p.getY()); 
            if (here != null && here.getColor() == p.getColor() && here != caller) {
                if (!CheckSub (here, p)) {
                    p.setBool (false);
                    return false;
                }
                p.setBool (true);
            }

            here = getPieceAt (p.getX(), p.getY()-height); 
            if (here != null && here.getColor() == p.getColor() && here != caller) {
                if (!CheckSub (here, p)) {
                    p.setBool (false);
                    return false;
                }
                p.setBool (true);
            }

            here = getPieceAt(p.getX(), p.getY()+height); 
            if (here != null && here.getColor() == p.getColor() && here != caller) {
                if (!CheckSub (here, p)) {
                    p.setBool (false);
                    return false;
                }
                p.setBool (true);
            }
        }

        return true;
    }

    private void setCheckedZero () {
        boolean result=true;

        pieces.gotoBeginning ();
        if (pieces.isEmpty () == false) {
            while (result == true) { //checkNext returns the next node without moving cursor
                ((Piece) pieces.getCursor ()).setChecked (0);
                result = pieces.gotoNext ();
            }
        }
    }

    public boolean Play (Piece p) {
        boolean result = true, loop = true;

        if (isPlayable(p.getX(), p.getY(), p.getColor())) {
            if (!pieces.isEmpty())
                SaveState();

            if (!redo_stack.isEmpty())
                redo_stack = new MoveStack ();

            pieces.gotoEnd ();
            pieces.insert (p);

            int l = p.getX () - height;
            int h = p.getY ();
            int liberties = 0;
            Piece here;

            for (int i=0;i<4;i++) {
                if (isPieceAt (l, h)) {
                    liberties++;
                    here = getPieceAt (l, h);
                    here.setLiberties (here.getLiberties () + 1);

                    System.out.println (here.getLiberties());
                } else if (l < xmin || l > xmax || h > ymax || h < ymin) {
                    liberties++;
                }       

                if (i==0) {
                    l = p.getX ();
                    h = p.getY () - height;
                } else if (i == 1) {
                    l = p.getX () + height;
                    h = p.getY ();
                } else if (i == 2) {
                    l = p.getX ();
                    h = p.getY () + height;
                }
            }

            p.setLiberties (liberties);
            CheckLibs (p);
            setCheckedZero ();
        } else {
            result = false;
        }

        return result;
    }

    protected void paintComponent (Graphics g, Frame a) {
        System.out.println("Board paint!");
        boolean result = true;
        g.drawImage(board, 0, 45, a);

        //Draw all Pieces
        pieces.gotoBeginning ();
        if (pieces.isEmpty () == false) {	
            while (result == true) {	//checkNext returns the next node without moving cursor
                if (((Piece)(pieces.getCursor())).getColor() == 0)
                    ((Piece)(pieces.getCursor())).paintComponent(g, a, ((Piece)(pieces.getCursor())).getX(), ((Piece)(pieces.getCursor ())).getY (), pblack);
                else if (((Piece)(pieces.getCursor ())).getColor() == 1)
                    ((Piece)(pieces.getCursor())).paintComponent(g, a, ((Piece)(pieces.getCursor())).getX(), ((Piece)(pieces.getCursor ())).getY (), pwhite);

                result = pieces.gotoNext ();
            }
        }
    }

    public void Score (Player p1, Player p2) {
        int black_score=0, white_score=0;
        int x, y, tmp=0;
        for (x = xmin;x <= xmax; x += height) {
            for (y = ymin; y <= ymax; y += height) {
                if (!isPieceAt(x, y)) {
                    tmp = Score_Sub(x, y, 0);
                }
                if (tmp < 0) {
                    tmp = 0;
                }
                black_score += tmp;
                tmp=0;
            }
        }

        for (x = xmin;x <= xmax; x += height) {
            for (y = ymin; y <= ymax; y += height) {
                if (isPieceAt (x, y)) {
                    if (((Piece)pieces.getCursor()).getColor () == 2) {
                        pieces.remove();
                    }
                }
            }
        }

        for (x = xmin;x <= xmax; x += height) {
            for (y = ymin; y <= ymax; y += height) {
                if (!isPieceAt (x, y)) {
                    tmp = Score_Sub (x, y, 1);
                }
                if (tmp < 0) {
                    tmp = 0;
                }
                white_score += tmp;
                tmp=0;
            }
        }

        for (x = xmin;x <= xmax; x += height) {
            for (y = ymin; y <= ymax; y += height) {
                if (isPieceAt (x, y)) {
                    if (((Piece)pieces.getCursor()).getColor () == 2) {
                        pieces.remove();
                    }	
                }
            }
        }

        black_score += Capture_Score (p2, 0);
        white_score += Capture_Score (p1, 1);

        p1.setScore (black_score);
        p2.setScore (white_score);
    }

    private int Score_Sub(int x, int y, int color) {
        int score=0;
        int opp_color = color + 1;
        Piece p;

        if (color == 1)
            opp_color = 0;


        if (x >= xmin && y >= ymin && x <= xmax && y <= ymax) {
            p = new Piece (x, y, 2);
            pieces.gotoEnd ();
            pieces.insert (p);
            score = 1;

            if (!isPieceAt (x+height, y))
                score += Score_Sub (x+height, y, color);
            else if (getPieceAt (x+height, y).getColor () == opp_color)
                score = -600;
            if (!isPieceAt (x-height, y))
                score += Score_Sub (x-height, y, color);
            else if (getPieceAt (x-height, y).getColor () == opp_color)
                score = -600;
            if (!isPieceAt (x, y+height))
                score += Score_Sub (x, y+height, color);
            else if (getPieceAt (x, y+height).getColor () == opp_color)
                score = -600;
            if (!isPieceAt (x, y-height))
                score += Score_Sub (x, y-height, color);
            else if (getPieceAt (x, y-height).getColor () == opp_color)
                score = -600;
        }

        return score;
    }

    private int Capture_Score (Player p, int color) {
        int opp_color = color + 1;
        int num_opp=0;

        if (color == 1)
            opp_color = 0;

        for (int x = xmin; x <= xmax; x += height) {
            for (int y = ymin; y <= ymax;y += height) {
                if (isPieceAt(x, y)) {
                    if (((Piece)pieces.getCursor()).getColor() == opp_color) {
                        num_opp++;
                    }
                }
            }		
        }

        return (p.getMoves() - num_opp);
    }

}
