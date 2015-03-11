//import Lab10.SListNode;

//import Lab7ListNode;

//--------------------------------------------------------------------
//
//  Laboratory 8                                         List.java
//
//  List Interface:  will allow declaration of any kind of list
//   object as long as the list variant class 'implements List'
//
//--------------------------------------------------------------------

public class List        // Constants & Methods common to list ADTs
{
	// Data members
	protected ListNode head,     // Reference to the beginning of the list
		             cursor,   // Reference to current cursor position
		             saved;    // Reference to prior cursor position
	int size;
    
	// Constructors & Helper Method
	List (  )                  // Default constructor: Creates an empty list
	{
		size = 0;
		head = null;
		cursor = head;
		saved = null;
	}

	public void setSize (int s)
	{
		size = s;
	}
	
    public void insert(Object newElement)    
    // Inserts newElement after the cursor. If the list is empty, then
    // newElement is inserted as the first (and only) element in the list.
    // In either case, moves the cursor to newElement.
    {
    	// If it is our first element, we make it the head
    	if (size==0)
    	{
    		head = new ListNode(newElement, null, null);
    		cursor = head;
    		size++;
    	}
    	/*else if (cursor.getNext() == null && cursor.getPrev() == null)
    	{
    		SListNode temp = cursor;
    		SListNode el = new SListNode(newElement,cursor.getNext(), temp);
    		temp.getNext().setPrev(el);
    		temp.setNext (el);
    		
    		cursor = cursor.getNext();
			
			size++;
    	}*/
    	else if (cursor == head)
    	{
    		ListNode temp = cursor;
    		ListNode el = new ListNode(newElement,cursor.getNext (), temp);
    		//temp.getNext().setPrev(el);
    		temp.setNext (el);
  
    		cursor = cursor.getNext();
    		size++;
    	}
    	else if (cursor.getNext() == null && cursor.getPrev() != null)
    	{
    		ListNode temp = cursor;
    		ListNode el = new ListNode(newElement,cursor.getNext(), temp);
    		//temp.getNext().setPrev(el);
    		temp.setNext (el);
    		
    		cursor = cursor.getNext();
			
		
			
			size++;
    	}
    	else if (cursor.getNext() != null && cursor.getPrev() != null)
    	{
    		ListNode temp = cursor;
    		ListNode el = new ListNode(newElement,cursor.getNext(), temp);
    		temp.getNext().setPrev(el);
    		temp.setNext (el);
    		
    		cursor = cursor.getNext();
    		
    		size++;
    	}
    }

    public void remove( )
    // Removes the element marked by the cursor from a list. Moves the
    // cursor to the next element in the list. Assumes that the first list
    // element "follows" the last list element.
    {
    	//TODO: set unused value to null
    	//TODO: fix prior again if needed
    	
    	if (cursor.getNext() != null)
    	{
    		if (cursor.getPrev() != null)
    		{
    			//System.out.println("do1");
    			ListNode temp = cursor.getPrev();
    			cursor = cursor.getNext();
    			cursor.setNext(cursor.getNext());
    			
    			temp.setNext(cursor);
    			cursor.setPrev(temp);
    			size--;
    		}
    		else
    		{
    			cursor = cursor.getNext();
    			cursor.setPrev(null);
    			head = cursor;
    			size--;
    		}
    	}
    	else
    	{
    		if (cursor.getPrev() == null)
    		{
    			System.out.println("Problem!");
    			clear();
    		}
    		else
    		{
    			//System.out.println("no problem!");
    			cursor = cursor.getPrev();
    			cursor.setNext(null);
    			size--;	
    		}
    	}
    }

    public void replace (Object newElement)
    // Replaces the element marked by the cursor with newElement and
    // leaves the cursor at newElement.
    {
    	cursor.setElement(newElement);
    }

    public void clear( )                      
    // Removes all elements in a list
    {
    	System.out.println("clear called!");
    	
    	head = null;
    	cursor = head;
    	size = 0;
    }
    
    public boolean isEmpty( )
    // Returns true if list is empty, else returns false
    {
    	return (size==0);
    }

    public boolean isFull( )
    // Returns true if list is full, else returns false
    // Always returns false because Java automatically 
    //  generates an OutOfMemory error otherwise.
    {
    	return false;
    }

    public boolean gotoBeginning( )            
    // If list not empty, moves cursor to beginning of list & returns true, else returns false
    {
    	if (size!=0)
    	{
    		cursor = head;
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    public boolean gotoEnd( )
    // If list not empty, moves cursor toend of list & returns true,else returns false
    {
    	if (size!=0)
    	{
    		//for(;cursor.getNext()!=null; cursor.setNext(cursor.getNext()));
    		if (cursor == null)
    		{
    			System.out.println("Big problem!");
    			
    		}
    		while (cursor.getNext()!=null)
    		{
    			cursor = cursor.getNext();
    		}
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    
    public boolean gotoNext( )
    // If cursor not at end of list, moves cursor to next element in list & returns true
    //  else returns false  
    {
    	if (cursor.getNext() != null)
    	{
    		cursor = cursor.getNext();
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public boolean gotoPrior( )  
    // If cursor not at beginning of list, moves cursor to preceding element 
    //   in list & returns true, else returns false 
    {
    	if (cursor.getPrev()!=null)
    	{
    		cursor = cursor.getPrev();
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    public Object getCursor( )                
    // Returns the element at the cursor
    {
    	return cursor.getElement();
    }

    public void showStructure( )         
    // Outputs the elements in a list. If the list is empty, outputs
    // "Empty list". This operation is intended for testing and
    // debugging purposes only.
    {
    	ListNode temp = head;
    	if (isEmpty())
    	{
    		System.out.println ("Empty List\n");
    	}
    	else
    	{	
    		do
    		{
    			System.out.print (temp.getElement());
    			temp = temp.getNext ();
    		} while (temp != null);
    		System.out.println ();
    	}
    }
    
    public void saveCursor()
    {
    	saved = cursor;
    }
    
    public void restoreCursor()
    {
    	cursor = saved;
    }
    
    void moveToBeginning ( )                    // Move to beginning
	{
    	ListNode temp;
    	temp = cursor;
    	
    	remove ();
    	gotoBeginning ();
    	temp.setNext(cursor);
    	temp.setPrev (null);
    	cursor.setPrev (temp);
    	head = temp;
    	cursor = head;
    }

    void insertBefore ( Object newElement )     // Insert before cursor
	{
    	if (size == 0)
    	{
    		insert (newElement);
    	}
    	else if (cursor == head)
    	{
    		ListNode el = new ListNode (newElement, cursor, null);
    		cursor.setPrev (el);
    		head = el;
    		cursor = head;
    		
    	}
    	else
    	{
    		gotoPrior ();
    		insert (newElement);
    		
    	}
    }
    
} // interface List
