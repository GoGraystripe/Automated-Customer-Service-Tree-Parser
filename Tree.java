/**This is the tree class, which defines a tree as having a root TreeNode
 * 
 * @author Otto Halbhuber
 * ID: 116150792
 * Recitation: R30
 */

public class Tree{
    private TreeNode root;

    /** sets the root of the tree
     * @param root
     *      the TreeNode that is the root
     */
    public Tree(TreeNode root){
        this.root = root;
    }


    /** adds a node to the tree
     * @param label
     *      the label String the new node has
     * @param prompt
     *      the prompt String the new node has
     * @param message
     *      the message String the new node has
     * @param parentLabel
     *      the label of the parent of the new node
     * @return
     *      returns true if the node is not already in the tree
     */
    public boolean addNode(String label, String prompt, String message, String parentLabel){

        TreeNode parent = getNodeReference(parentLabel);

        TreeNode child = null;

        for(int i = 0; i < TreeNode.maxChildren; i++){

            if(child != null && child.equals(this.getNodeReference(label))) return false;
            
            child = parent.getChildren()[i];

            if(child == null){
                child = new TreeNode(label, message, prompt);
                child.setParent(parent);
                parent.getChildren()[i] = child;
                return true;
            }

        }

        return false;

    }

    /**returns the node reference of the node with the given String label
     * @param label
     *      the label of the node whose reference will be returned
     * @return
     *      the reference of the node with the given label
     */
    public TreeNode getNodeReference(String label){

        return getNodeReference(label, root);

    }

    /**returns the node reference of th node with the given String label in the tree/subtree with the given root
     * @param label
     *      the label of the node whose reference will be returned
     * @param root
     *      the root of the tree or subtree where the node is being searched for
     * @return
     *      the reference of the node with the given label
     */
    public TreeNode getNodeReference(String label, TreeNode root){

        if(root == null) return null;

        if(label.equals(root.getLabel())) return root;

        TreeNode child = null;

        for(int i = 0; i < TreeNode.maxChildren; i++){

            child = root.getChildren()[i];
            TreeNode reference = getNodeReference(label, child);
            if(reference != null) return reference;
            

        }

        return null;

    }

    /** removes the given node from the tree/subtree with the given root
     * @param node
     *      the node to be removed
     * @param root
     *      the root of the tree/subtree where node exists
     * @return 
     *      the node being removed
     */
    public TreeNode remove(TreeNode node, TreeNode root){

        if(root == null) return null;

        if(node == root){

            /* special case for ACTUAL root of the tree */

            if(root.equals(this.root)){
                return null;
            }

            if(!root.equals(this.root)){ /* base case */

                int lastNodeToShift;

                TreeNode[] arr = root.getParent().getChildren();

                for(int i = 0; i < arr.length; i++){

                    if(arr[i].equals(root)){

                        lastNodeToShift = i;

                        /* remove it, shift everything over */

                        for(int j = i + 1; j < arr.length; j++){

                            arr[j - 1] = arr[j];

                            lastNodeToShift = j;

                        }

                        arr[lastNodeToShift] = null;

                        return node;

                    }

                }

            }

        }
        else{ /* recursive case */

            TreeNode[] children = root.getChildren();

            for(int i = 0; i < children.length; i++){

                TreeNode result = remove(node, children[i]);

                if(result != null) return result;

            }

        }

        return null;




    }

    /** prints the tree in preorder traversal */
    public void preOrder(){
        preOrder(root);
    }

    /** prints the tree in preorder traversal
     * @param root
     *      the root of the tree/subtree to be printed in preorder traversal
     */
    public void preOrder(TreeNode root){

        if(root == null) return;

        System.out.println("Label: " + root.getLabel());
        System.out.println("Prompt: " + root.getPrompt());
        System.out.println("Message: " + root.getMessage());
        System.out.println();
        
        for(int i = 0; i < TreeNode.maxChildren; i++){
            
            TreeNode child = root.getChildren()[i];

            preOrder(child);

        }

    }

    /**root getter
     * @return 
     *      the root of the tree
     */
    public TreeNode getRoot(){
        return root;
    }




}