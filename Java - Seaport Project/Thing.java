/*
* File Name: Thing.java
* Date: March 31, 2019
* Author: Christopher Scullin
* Purpose: Parent/Grandparent of most classes in this program.
* Assigns name, index, and parent values to all child classes.
*/

import java.util.Scanner;

public class Thing {
    
    int index = 0;
    String name = "";
    int parent = 0;
    
    public Thing(Scanner sc) {
        if (sc.hasNext()) name = sc.next();
        if (sc.hasNextInt()) index = sc.nextInt();
        if (sc.hasNextInt()) parent = sc.nextInt();
    } // End Scanner constructor   
    
    public String toString () {
        String st = name + " " + index;
        return st;
    } // end method toString
} // End class Thing
