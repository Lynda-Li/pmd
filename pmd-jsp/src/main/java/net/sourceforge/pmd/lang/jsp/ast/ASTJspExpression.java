/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
/* Generated By:JJTree: Do not edit this line. ASTJspExpression.java */

package net.sourceforge.pmd.lang.jsp.ast;

public class ASTJspExpression extends AbstractJspNode {
    ASTJspExpression(int id) {
        super(id);
    }

    /**
     * Accept the visitor. *
     */
    @Override
    public Object jjtAccept(JspParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
