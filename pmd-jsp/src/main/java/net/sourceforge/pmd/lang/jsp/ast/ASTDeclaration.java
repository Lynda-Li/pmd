/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
/* Generated By:JJTree: Do not edit this line. ASTDeclaration.java */

package net.sourceforge.pmd.lang.jsp.ast;

public class ASTDeclaration extends AbstractJspNode {

    private String name;

    ASTDeclaration(int id) {
        super(id);
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accept the visitor. *
     */
    @Override
    public Object jjtAccept(JspParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
