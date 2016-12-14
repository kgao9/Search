import java.util.*;

public class Search 
{
    //for our search, we need nodes
    //each node has a state which is the
    //number of liters in jug1 and the number of liters in jug2
    //each node will be able to check if they equal to each other
    //i.e. if they have the same state
    //each node can also create successors and test to see if it's a goal
    private static class Node 
    {
        //each node has a state - quantity in its jugs
        private int jug1;
        private int jug2;

        //each node should have back pointers
        //each node memorizes its path
        private Queue <Node> backPointers;
		
        /**
	* The purpose of this method is to instantiate the state of each node
        * @param jug1, jug2, backpointers
        * No returns
        */
		
        private Node(int jug1, int jug2, Queue <Node> backPointers)
        {
            if(jug1 < 0)
                throw new IllegalArgumentException();
			
            else if(jug2 < 0)
                throw new IllegalArgumentException();
			
            this.jug1 = jug1;
            this.jug2 = jug2;
            this.backPointers = backPointers;
        }
		
        /**
        * Initialize the first state
        * no params
        * no returns
        */
		
        private Node()
        {
            this.jug1 = 0;
            this.jug2 = 0;
            this.backPointers = new LinkedList <Node>();
        }
		
        /**
        * The purpose of this method is to check if two nodes are equal
        * @param a node
        * @return true if equal; false otherwise
        */
        private boolean isEqual(Node test) 
        {
             if(test.jug1 == this.jug1 && test.jug2 == this.jug2)
                 return true;
						
             else
                 return false;
        }
	
        /**
        * The purpose of this method is to return the path
        * that was taken to get to this node
        * No parameters 
        * @return copy of path
        */	

        private Queue <Node> getBackPointers()
        {
            //create temporary storage and copy of the path
            Queue <Node> temp = new LinkedList <Node> ();
            Queue <Node> copy = new LinkedList <Node> ();
	
            //grab items in the private path var
            //put them in temp that preserves position
            //Place in copy as well		
            while(!this.backPointers.isEmpty())
            {
                //cycle through
                Node dequeue = this.backPointers.remove();
				
                copy.add(dequeue);
				
                temp.add(dequeue);
            }

            //restore path			
            while(!temp.isEmpty())
            {
                Node dequeue = temp.remove();
				
                this.backPointers.add(dequeue);
            }
			
            return copy;
        }

        /**
        * The purpose of this method is to check if the node is a duplicate
        * @param array list of nodes
        * @return true if it is; false otherwise
        */
       
        private boolean isDuplicate(ArrayList <Node> successors)
        {
            for(int i = 0; i < successors.size(); i++)
            {
                if(this.isEqual(successors.get(i)))
                    return true;
            }

            return false;
        }
		
        /**
        * The purpose of this method is to create successor nodes
        * @param quantity1 - amount of liters first jug can fill
        * @param quantity2 - amount of liters second jug can fill
        * @return list of successors
        */
		
        private ArrayList <Node> createSuccessors(int quantity1, int quantity2)
        {	
            ArrayList <Node> newSuccessors = new ArrayList <Node>();
			
            Queue <Node> path = getBackPointers();
            path.add(this);
			
            //fill m first

            Node fillM = new Node(quantity1, this.jug2, path);

            if(!this.isEqual(fillM))
                newSuccessors.add(new Node(quantity1, this.jug2, path));

            			
            //fill n
            Node fillN = (new Node(this.jug1, quantity2, path));

            if(!(fillN.isDuplicate(newSuccessors)) && !this.isEqual(fillN))
                newSuccessors.add(new Node(this.jug1, quantity2, path));

            //empty m
            Node emptyM = (new Node(0, this.jug2, path));

            if(!(emptyM.isDuplicate(newSuccessors)) && !this.isEqual(emptyM))			
                newSuccessors.add(new Node(0, this.jug2, path));
			
            //empty n
            Node emptyN = new Node(this.jug1, 0, path);

            if(!(emptyN.isDuplicate(newSuccessors)) && !this.isEqual(emptyN))
                newSuccessors.add(new Node(this.jug1, 0, path));
			
            //m pour n
            //if we can pour m into n without overflowing, do so
            if(this.jug1 + this.jug2 <= quantity2)
            {
                Node mPN = new Node(0, this.jug1 + this.jug2, path);

                if(!(mPN.isDuplicate(newSuccessors)) && !(this.isEqual(mPN)))
                    newSuccessors.add(new Node(0, this.jug1 + this.jug2, path));
            }
			
            //if it overflows, we need to pour it so it fills up quantity 2 and leaves 
            //extra in quantity 1
            else
            {
                //this.jug1 subtracted by the amount needed to fill quantity 2
                int left = this.jug1 - (quantity2 - this.jug2);

                Node mPN = new Node(left, quantity2, path);

                if(!(mPN.isDuplicate(newSuccessors)) && !(this.isEqual(mPN)))
                    newSuccessors.add(new Node(left, quantity2, path));
            }
			
            //n pour m
            //if we can pour m into n without overflowing, do so
            if(this.jug1 + this.jug2 <= quantity1)
            {
                Node nPM = new Node(this.jug1 + this.jug2, 0, path);
    
                if(!(nPM.isDuplicate(newSuccessors)) && !this.isEqual(nPM))
                    newSuccessors.add(new Node(this.jug1 + this.jug2, 0, path));
            }
			
            //if it overflows, we need to pour it so it fills up quantity 2 and leaves 
            //extra in quantity 1
            else
            {
                //this.jug1 subtracted by the amount needed to fill quantity 2
                int left = this.jug2 - (quantity1 - this.jug1);

                Node nPM = (new Node(quantity1, left, path));

                if(!(nPM.isDuplicate(newSuccessors)) && !this.isEqual(nPM))
                    newSuccessors.add(new Node(quantity1, left, path));
            }
			
            return newSuccessors;
        }
	
        /**
        * The purpose of this method is to print the node's state
        * in the form (amount in jug1, amount in jug2)
        * no params
        * no returns
        */
	
        private void print()
        {
            String jug1Str = String.valueOf(this.jug1);
			
            System.out.print("(" + jug1Str + "," + String.valueOf(this.jug2) + ") ");
        }
	
        /**
        * The purpose of this method is to check if we have reached the goal
        * state, which is (value we want, X)
        * @param int goal
        * @ return true if it is the goal; false otherwise
        */
	
        private boolean goalCheck(int goal)
        {
            return (this.jug1 == goal && this.jug2 == 0); 
        }
    }

    /**
    * The purpose of this method is to check if a node is in the queue
    * @param queue of nodes, node
    * @return true if the node is in the queue; false otherwise
    */
   	
    private static boolean inQueue(Queue <Node> open, Node getNode)
    {
        //tell us if found
        boolean found = false;
		
        //create temporary hold
        Queue <Node> temp = new LinkedList <Node>();
	
        //pop everything out of queue and check if it's the node
        //we are looking for	
        while(!open.isEmpty())
        {
            //cycle through
            Node dequeue = open.remove();
			
            //test to see if found
            if(getNode.isEqual(dequeue))
            {
                found = true;
            }
			
            temp.add(dequeue);
        }
           
	//restore the queue	
        while(!temp.isEmpty())
        {
            Node dequeue = temp.remove();
			
            open.add(dequeue);
        }
		
        return found;
    }

    /**
    * The purpose of this method is to print out all the nodes in the queue
    * @param queue of nodes
    * no returns
    */
	
    private static void printQueue(Queue <Node> open)
    {	
        Queue <Node> temp = new LinkedList <Node>();
	
        //remove every node and print	
        while(!open.isEmpty())
        {
            //cycle through
            Node dequeue = open.remove();
			
            dequeue.print();
			
            temp.add(dequeue);
        }
	
        //restore queue	
        while(!temp.isEmpty())
        {
            Node dequeue = temp.remove();
			
            open.add(dequeue);
        }
    }

    public static void main(String[] args) 
    {
        //comments
        //check args
        //if < 0 quantities, print usage
        //if measurement can't be measured, as in hold too much - print usage
		
        if(args.length != 3)
        {
            System.out.println("USAGE: program Search.java needs exactly 3 params: m,n,d");
            System.exit(0);
        }
		
        //get argument values
        int m = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        int d = Integer.parseInt(args[2]);

        if(m < 0 || n < 0 || d < 0)
        {
            System.out.println("Usage: Jugs cannot hold negative liters");
            System.exit(0);
        }
           
		
        //closed data structure
        Queue <Node> closed = new LinkedList <Node>();
	
        /**
        ALGORITHM:
            We start with (0,0) as the state
            While our queue isn't empty,
            we dequeue and do a goal test
                IF THE GOAL TEST SUCCEEDS, WE PRINT THE PATH TO THE GOAL
            If it fails, we print out the iteration number
            We put our dequeued node into closed.
            We then create successors (state along with backpointers).
            Note: 0 successors are created if the state is closed
            We then enqueue all successors
            We repeat until the queue is empty or the goal test succeeds

            If goal succeeds, we use the goal's backpointers to print the result
            Otherwise, we print UNSOLVABLE
        */
            	
		
        //print out start
        System.out.println("BFS");
        System.out.println("Iteration: ");
		
        //number of iterations
        int itrNum = 0;
		
        //create queue
        //open
        Queue <Node> openQ = new LinkedList <Node> ();
	
        //put in state q0	
        openQ.add(new Node());
	
        //this is where we store our results	
        boolean resultFound = false;
        Node result = new Node();
		
        //bredth-first search
        while(!(openQ.isEmpty()))
        {
            Node enqueue = openQ.remove();
	
            //if goal, break out of loop		
            if(enqueue.goalCheck(d))
            {
                resultFound = true;
                result = enqueue;		
                break;
            }

            if(inQueue(closed, enqueue))
            {
                continue;
            }

            //print iteration number
            System.out.println(String.valueOf(itrNum));

            //add node to closed datastructure		
            closed.add(enqueue);
		
            //generate successors	
            ArrayList <Node> succ = enqueue.createSuccessors(m, n);

            //need to print out each node addded			
            ArrayList <Node> added = new ArrayList <Node> ();

            //for each successor, see if it should be added
            for(int i = 0; i < succ.size(); i++)
            {
                Node getNode = succ.get(i); 
				
                openQ.add(getNode);
                added.add(getNode);
            }
			
            printQueue(closed);
            System.out.println("");
			
            //print each node added
            for(int i = 0; i < added.size(); i++)
            {
                added.get(i).print();
            }
                        
            System.out.println("");

            itrNum++;
        }
		
        //result
        System.out.println("Result: ");
        if(!resultFound)
            System.out.println("Unsolvable");
		
        else
        {
            //System.out.println("");
            printQueue(result.getBackPointers());
            result.print();
        }
		
        //reset

        /**
        ALGORITHM:
            We start with (0,0) as the state
            While our stack isn't empty,
            we pop and do a goal test
                IF THE GOAL TEST SUCCEEDS, WE PRINT THE PATH TO THE GOAL
            If it fails, we print out the iteration number
            If the node is already in closed, we simply print output and continue
            Otherwise,
            We put our popped node into closed.
            We then create successors (state along with backpointers).
            we push all successors into the stack
            We repeat until the stack is empty or the goal test succeeds

            If goal succeeds, we use the goal's backpointers to print the result
            Otherwise, we print UNSOLVABLE
        */

        closed = new LinkedList <Node> ();
		
        //create Stack	
        Stack <Node> openS = new Stack <Node>();
		
        openS.push(new Node());
		
        //print out start
        System.out.println("\nDFS");
        System.out.println("Iteration: ");
				
        //number of iterations
        itrNum = 0;
				
        resultFound = false;
        result = new Node();
				
        //depth-first search
        while(!(openS.isEmpty()))
        {
            //pop from stack
            Node pop = openS.pop();

            //do a goal test
            //if successful, break					
            if(pop.goalCheck(d))
            {
                resultFound = true;
                result = pop;		
                break;
            }
	
            

            if(inQueue(closed, pop))
            {
                continue;
            }

            //print iteration number
            System.out.println(String.valueOf(itrNum));  

            //add to closed data structure                                      
            closed.add(pop);

            //create successors					
            ArrayList <Node> succ = pop.createSuccessors(m, n);
			
            //keep track of added nodes
            ArrayList <Node> added = new ArrayList <Node>();

            //for each successor node, see if we should add it or not					
            for(int i = 0; i < succ.size(); i++)
            {
                Node getNode = succ.get(i); 
						
                openS.push(getNode);
                added.add(getNode);
            }		

            //print closed					
            printQueue(closed);
            System.out.println("");

            //print the new added nodes			
            for(int i = 0; i < added.size(); i++)
            {
                added.get(i).print();
            }
			
            System.out.println("");
					
            itrNum++;
        }
		
        //result
        System.out.println("Result: ");
        if(!resultFound)
            System.out.println("Unsolvable");
				
        else
        {
            //System.out.println("");
            printQueue(result.getBackPointers());
            result.print();
            System.out.println("");
        }
    }
}
