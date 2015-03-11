public class MoveStack {
	private int size;
	List moves;
	
	MoveStack ()
	{
		moves = new List ();
		size = 0;
	}
	
	public int getSize ()
	{
		return size;
	}
	
	public boolean isEmpty ()
	{
		boolean result;
		
		if (size == 0)
			result = true;
		else
			result = false;
		
		return result;
	}
	
	public Saved_State Pop ()
	{
		Saved_State result;
		
		moves.gotoEnd ();
		result = (Saved_State) moves.getCursor ();
		moves.remove ();
		
		size--;
	
		return result;		
	}
	
	public void Push (Saved_State m)
	{
		moves.gotoEnd ();
		moves.insert (m);
		
		size++;
	}
	
	public Saved_State Top ()
	{
		Saved_State result;
		
		moves.gotoEnd ();
		result = (Saved_State) moves.getCursor ();
		moves.remove ();
		
		return result;		
	}
}
