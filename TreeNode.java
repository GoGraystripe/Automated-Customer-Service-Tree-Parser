/**This is the TreeNode class, which defines a TreeNode object as having children, a parent, a label, a message, and a prompt.
 * 
 * @author Otto Halbhuber
 * ID: 116150792
 * Recitation: R30
 * 
 */

public class TreeNode{
    private TreeNode[] children;

    private TreeNode parent;

    public static final int maxChildren = 9;
    public static final int maxMutuallyExclusive = maxChildren - 1;

    private String label;
    private String message;
    private String prompt;

    public TreeNode(String label, String message, String prompt){

        this.label = label;
        this.message = message;
        this.prompt = prompt;

        parent = null;
        children = new TreeNode[maxChildren];
        
    }

    /**children getter
     * @return
     *      the children TreeNode array
     */
    public TreeNode[] getChildren(){
        return children;
    }
     

    /**children setter
     * @param children
     *      the new children TreeNode array
     */
    public void setChildren(TreeNode[] children){
        this.children = children;
    }

    /**label getter
     * @return 
     *      the label String
     */

    public String getLabel(){
        return label;
    }

    /**label setter
     * @param label
     *      the new label
     */
    public void setLabel(String label){
        this.label = label;
    }

    /**message getter
     * @return
     *      the message
     */
    public String getMessage(){
        return message;
    }

    /**message setter
     * @param message
     *      the new message
     */

    public void setMessage(String message){
        this.message = message;
    }

    /**prompt getter 
     * @return          
     *      the prompt
    */

    public String getPrompt(){
        return prompt;
    }

    /**prompt setter
     * @param prompt
     *      the new prompt
     */

    public void setPrompt(String prompt){
        this.prompt = prompt;
    }

    public TreeNode getParent(){
        return parent;
    }

    public void setParent(TreeNode parent){
        this.parent = parent;
    }

    

    

    

}