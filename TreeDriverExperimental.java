/** Same as TreeDriver class but allows for exclusive paths by blocking options that are  
 * exclusive with options already selected.
 * 
 * Upload the file "SampleME.txt" (ME stands for Mutually Exclusive). Any node labels written after an "me"
 * denote which nodes cannot be chosen with the given node. Something like this may be implemented in
 * a video game with an evolution or decision tree.
 * 
 * @author Otto Halbhuber
 * ID: 116150792
 * Recitation: R30
 */

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class TreeDriverExperimental{

    public static Tree tree = null;
    public static HashMap<TreeNode, Boolean> wasVisited = new HashMap<TreeNode, Boolean>();
    public static HashMap<TreeNode, List<String>> isExclusiveWith = new HashMap<TreeNode, List<String>>();
    //public static HashMap<TreeNode, ArrayList<TreeNode>> isExclusiveWith = new HashMap<TreeNode, ArrayList<TreeNode>>();
    public static HashMap<TreeNode, TreeNode> isBlocked = new HashMap<TreeNode, TreeNode>();
    public static HashMap<Integer, Boolean> canSelect = new HashMap<Integer, Boolean>();

    public static void main(String[] args) throws FileNotFoundException, NullPointerException{
        
        while(true){
            mainMenu();
        
            Scanner scanner = new Scanner(System.in);
            
            System.out.print("Choice> ");

            String line = scanner.next();

            if(line.equals("L")){

                String filename = introPrompt();

                try{    
                    buildTree(filename);
                
                    System.out.println();

                    System.out.println("Tree created successfully!");

                    System.out.println();

                }
                catch(FileNotFoundException e){
                    System.out.println("File not found");
                }
                catch(IllegalArgumentException e){
                    System.out.println("File must start with root");
                }

            }

            else if(line.equals("H")){
                if(tree == null){
                    System.out.println("Tree does not exist");
                    System.out.println();

                }

                else{
                    int answer = -1;
                    String response;
                    TreeNode temp = tree.getNodeReference("root");

                    int numChildren = 0;

                    while(temp != null){

                        System.out.println();

                        System.out.println("Message: " + temp.getMessage());

                        if(temp.getChildren()[0] == null) break; /*if first child is null, must be a leaf, ready to terminate*/

                        for(int i = 0; i < TreeNode.maxChildren; i++){

                            TreeNode child = temp.getChildren()[i];

                            if(child != null){

                                if(isBlocked.containsKey(child)){
                                    System.out.println((i + 1) + " " + child.getPrompt() + " [CANNOT SELECT: IS MUTUALLY EXCLUSIVE WITH " + isBlocked.get(child).getLabel() + "]");
                                    canSelect.put(i, false);
                                }

                                else{
                                    System.out.println((i + 1) + " " + child.getPrompt());
                                }
                            }
                            else{
                                numChildren = i;
                                break;
                            }

                        }

                        System.out.print("Choice> ");

                        response = scanner.next().trim();

                        try{
                            answer = Integer.parseInt(response) - 1;

                            if(canSelect.containsKey(answer) && (answer > numChildren || !canSelect.get(answer))){
                        
                                System.out.println("Invalid input.");
                                
                                //break;

                            }
                        }
                        catch(NumberFormatException e){
                            
                            if(response.equals("B")) temp = temp.getParent();

                            else if(response.equals("Q")){

                                System.out.println("Returning to main menu");
                                //scanner.close();
                                break;
                                
                            }

                            else{
                                System.out.println("Invalid input.");
                            }

                        }

                        
                        if(!response.trim().equals("B") && answer < numChildren
                            && temp.getChildren()[answer] != null && (canSelect.get(answer) == null || canSelect.get(answer))){
                            temp = temp.getChildren()[answer];

                            List<String> list = isExclusiveWith.get(temp);

                            //System.out.println(list.toString());

                            for(String siblingLabel : list){

                                TreeNode sibling = tree.getNodeReference(siblingLabel);

                                isBlocked.put(sibling, temp);

                            }
                        }

                    }
                }
        
            }
    

            else if(line.equals("T")){

                if(tree == null){
                    System.out.println("Tree does not exist");
                    System.out.println();
                }

                else{
                    System.out.println("Traversing the tree in preorder.");
                    tree.preOrder();
                }
                
            }

            else if(line.equals("Q")){

                System.out.println("Thank you for using our automated help services");

                break;

            }
        }
    }
                

        
    

    public static void mainMenu(){
        
        System.out.println("Welcome to the Main Menu. Please select an option.");
        System.out.println("L) Load input file and build a new tree");
        System.out.println("H) Start help session");
        System.out.println("T) Traverse the tree in preorder");
        System.out.println("B) Go to previous node in the tree");
        System.out.println("Q) Quit the program");
        

    }

    

    public static String introPrompt(){     
        System.out.println();

        System.out.println("Welcome to the Automated Customer Service Helpline. We are in Beta Testing. Please be patient.");

        System.out.println("What file would you like to load?");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter filename>");

        String filename = scanner.next();

        return filename;
    }

    public static void buildTree(String filepath) throws FileNotFoundException{

        String line;
        String label;
        String message;
        String prompt;
        String[] mutuallyExclusive;

        String parentLabel = null;

        int numChildren = 0;

        int childCount = 0;
        boolean initialized = false;
        
        /* FOR MUTUALLY EXCLUSIVE PATHS. WARNING: EXPERIMENAL CODE. BUGS MAY OCCUR */
        boolean mutuallyExclusiveRead = false;
        String cachedLabel = null;

        TreeNode parent;

        File file = new File(filepath);
        
        Scanner scanner = new Scanner(file);

        int counter = 0;

        while(scanner.hasNextLine()){

            label = scanner.nextLine().trim();
            
            if(counter == 0 && !label.trim().equals("root")){
                throw new IllegalArgumentException();
            }

            if(label.trim().equals("root")){

                System.out.println("root is here");

                counter++;
            
                prompt = scanner.nextLine().trim();
                message = scanner.nextLine().trim();
                
                TreeNode root = new TreeNode(label, message, prompt);

                tree = new Tree(root);

                numChildren = Integer.parseInt(evaluatePair(scanner.nextLine().trim())[1]);

                // String l = scanner.nextLine();
                // System.out.println("line: " + l);

                childCount = 0;

                parent = root;

                parentLabel = label;

                wasVisited.put(tree.getNodeReference(label), false);


            }

            else if(childCount < numChildren){

                // System.out.println("Looping through children. Counter: " + childCount);

                prompt = scanner.nextLine().trim();
                message = scanner.nextLine().trim();
                mutuallyExclusive = scanner.nextLine().trim().split(" ");

            
                tree.addNode(label, prompt, message, parentLabel);

                isExclusiveWith.put(tree.getNodeReference(label), Arrays.asList(mutuallyExclusive));

                childCount++;

            }
            else{

                String[] pair = evaluatePair(label);

                parentLabel = pair[0];

                // System.out.println("Looking for parent: " + parentLabel);

                childCount = 0;

                numChildren = Integer.parseInt(pair[1]);
                
            }

        }

        scanner.close();

    }
           
        
    

    public static String[] evaluatePair(String str){

        return str.split(" ");

    }

}