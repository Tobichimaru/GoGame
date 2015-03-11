//--------------------------------------------------------------------
//
//  Laboratory 7                                       SListNode.jshl
//
//  Class definition for the singly linked list implementation of 
//  the List ADT
//
//  The student is to complete all missing or incomplete method 
//     implementations for each class (SList and SListNode)
//
//--------------------------------------------------------------------

class ListNode
{
    // Data members
    private Object element;         // List element
    private ListNode next;         // Reference to the next element
    private ListNode prev;         // Reference to previous element
    
    
    // Constructor
    ListNode ( Object elem, ListNode nextPtr, ListNode prevPtr )
        // Creates a list node containing element elem and next pointer
        // nextPtr.
    {
    	element = elem;
    	next = nextPtr;
    	prev = prevPtr;
    }
    
    // Class Methods used by client class
    ListNode getNext( )                    // Return reference to next element
    {
    	return next;
    }
    
    ListNode setNext( ListNode nextVal )  // Set reference to next element
    {                                       //  & return that reference
    	next = nextVal;
    	return next;
    }
    
     //	Class Methods used by client class
	 ListNode getPrev( )                    // Return reference to next element
	 {
		 return prev;
	 }
    
	 ListNode setPrev( ListNode prevVal )  // Set reference to next element
	 {                                       //  & return that reference
		 prev = prevVal;
		 return prev;
	 }
    
    Object getElement( )             // Return the element in the current node
    {
    	return element;
    }
    
    void setElement(Object newElem)         // Set current element to newElem 
    {                                 
    	element = newElem;
    }
    
} // class ListNode
