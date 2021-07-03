
/**
 * Name: Christopher Scullin
 * 
 * Class Description: This class constructs and manipulates 
 * a stack which is used for the stack object in the depth first search method of
 * AbstractGraph.java. 
 * 
 */

public class MyStack<T extends Object> { 
    private T[] stackArray;
	private int stackSize;    
    private int currentStack;
     
    @SuppressWarnings("unchecked")
    public MyStack(int size) {
        this.stackSize = size;
        this.stackArray = (T[]) new Object[stackSize];
        this.currentStack = -1;
    } // End constructor
 
    public void push(T entry) {
        if(this.isFull()) {
            System.out.println(("Stack is full. Increasing capacity."));
            this.increaseStackSize();
        } // End if statement
        this.stackArray[++currentStack] = entry;
    } // End method
 
    public T pop() throws Exception {
        if(this.isEmpty()) {
            throw new Exception("Stack is empty. No element to remove.");
        } // End if statement
        T entry = this.stackArray[currentStack--];
        return entry;
    } // End method   
    
    public T peek() {
        return stackArray[currentStack];
    } // End method
    
    public boolean isEmpty() {
        return (currentStack == -1);
    } // End method

    public boolean isFull() {
        return (currentStack == stackSize - 1);      
    } // End method
    
    private void increaseStackSize() {        
        @SuppressWarnings("unchecked")
        T[] newStack = (T[]) new Object[this.stackSize * 2];
        for(int i = 0; i < stackSize; i++) {
            newStack[i] = this.stackArray[i];
        } // End for loop
        this.stackArray = newStack;
        this.stackSize = this.stackSize * 2;
    } // End method
} // End class