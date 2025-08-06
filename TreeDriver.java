/** This is the TreeDriver class, which allows the user to import .txt files to be converted into tree objects
 * and traversed
 * 
 * @author Otto Halbhuber
 * ID: 116150792
 * Recitation: R30
 */

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class TreeDriver{

    public static Tree tree = null;

    /**this is the main method, where all the code is actually run */
    public static void main(String[] args) throws FileNotFoundException, NullPointerException, Exception{

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
                    System.out.println();
                }
                catch(IllegalArgumentException e){
                    System.out.println("File must start with root");
                    System.out.println();
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

                        System.out.println(temp.getMessage());

                        if(temp.getChildren()[0] == null) break; /*if first child is null, must be a leaf, ready to terminate*/

                        for(int i = 0; i < TreeNode.maxChildren; i++){

                            TreeNode child = temp.getChildren()[i];

                            if(child != null) System.out.println((i + 1) + " " + child.getPrompt());

                            else{

                                numChildren = i;
                                break;

                            }

                        }

                        System.out.print("Choice> ");

                        response = scanner.next().trim();

                        try{
                            answer = Integer.parseInt(response) - 1;

                            if(answer >= numChildren || answer < 0){
                        
                                System.out.println("Invalid Input");
                                

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
                                System.out.println("Invalid Input.");
                            }

                        }

                        
                        if(!response.trim().equals("B") && answer >= 0 && answer < numChildren && temp.getChildren()[answer] != null)
                            temp = temp.getChildren()[answer];
                        

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

            else{

                System.out.println("Invalid Input");

            }


        }

        
    }


    /**prints the main menu options */
    public static void mainMenu(){
        
        System.out.println("Welcome to the Main Menu. Please select an option.");
        System.out.println("L) Load input file and build a new tree");
        System.out.println("H) Start help session");
        System.out.println("T) Traverse the tree in preorder");
        System.out.println("B) Go to previous node in the tree");
        System.out.println("Q) Quit the program");
        

    }

    
    /** prints the into prompt and returns the file name as a String
     * @return
     *      the file name as a String
     */
    public static String introPrompt(){     
        System.out.println();

        System.out.println("Welcome to the Automated Customer Service Helpline. We are in Beta Testing. Please be patient.");

        System.out.println("What file would you like to load?");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter filename> ");

        String filename = scanner.next().trim();

        return filename;
    }

    /**parses the file with the given filepath and converts it into a tree
     * @param filepath
     *      the filepath of the file to be parsed and converted into a tree
     */
    public static void buildTree(String filepath) throws FileNotFoundException, Exception{

        String line;
        String label;
        String message;
        String prompt;

        String parentLabel = null;

        int numChildren = 0;

        int childCount = 0;
        boolean initialized = false;
        
        /* FOR MUTUALLY EXCLUSIVE PATHS. WARNING: EXPERIMENAL CODE. BUGS MAY OCCUR */
        boolean mutuallyExclusiveRead = false;
        String cachedLabel = null;
        String[][] cachedMutuallyExclusiveSiblings = new String[TreeNode.maxChildren][TreeNode.maxMutuallyExclusive];
        int mutuallyExclusiveCount = 0;

        TreeNode parent;

        File file = new File(filepath);
        
        Scanner scanner = new Scanner(file);

        int counter = 0;

        while(scanner.hasNextLine()){

            // if(!mutuallyExclusiveRead) label = scanner.nextLine().trim();
            // else label = cachedLabel;

            label = scanner.nextLine().trim();

            if(counter == 0 && !label.trim().equals("root")){
                throw new IllegalArgumentException();
            }

            if(label.trim().equals("root")){

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

                // System.out.println("numChildren: " + numChildren);
                // System.out.println("Root label: " + root.getLabel());
                // System.out.println("Root message: " + root.getMessage());
                // System.out.println("Root prompt: " + root.getPrompt());

            }

            else if(childCount < numChildren){

                // System.out.println("Looping through children. Counter: " + childCount);

                prompt = scanner.nextLine().trim();
                message = scanner.nextLine().trim();

                tree.addNode(label, prompt, message, parentLabel);

                // System.out.println("Parent label: " + parentLabel);
                // System.out.println("Child label: " + label);
                // System.out.println("Child message: " + message);
                // System.out.println("Child prompt: " + prompt);

                // cachedLabel = scanner.nextLine().trim();

                // if(cachedLabel.equals("me")){

                //     while(scanner.hasNext()){

                //         mutuallyExclusiveRead = true;
                //         cachedMutuallyExclusiveSiblings[childCount][mutuallyExclusiveCount] = scanner.next();

                //         mutuallyExclusiveCount++;
                //     }
                    
                // }
                // else mutuallyExclusiveRead = false;

                childCount++;

            }
            else{

                /* Time to mark the mutually exclusive relationships, 
                once every node under the given parent has been processed */

                // for(int j = 0; j < childCount++; j++){
                //     for(int k = 0; k < mutuallyExclusiveCount; mutuallyExclusiveCount++){

                //         TreeNode child = tree.getNodeReference(parentLabel).getChildren()[j];
                //         TreeNode sibling = tree.getNodeReference(cachedMutuallyExclusiveSiblings[j][k]);

                //         child.setMutuallyExclusive(sibling);

                //     }
                // }

                //System.out.println(label);
                
                String[] pair = evaluatePair(label);

                parentLabel = pair[0];

                // System.out.println("Looking for parent: " + parentLabel);

                childCount = 0;

                numChildren = Integer.parseInt(pair[1]);
                
            }

        }

        scanner.close();
           
        
    }

    /** takes in a String a returns that String[] array that is the original String split at single space characters " "
     * @param str
     *      the String to be split
     * @return
     *      the split String[] array to be returned
     * 
     * used to evaluate which node has how many children
     */
    public static String[] evaluatePair(String str){

        return str.split(" ");

    }

}