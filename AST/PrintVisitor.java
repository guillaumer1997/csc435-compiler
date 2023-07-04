package AST;

import Types.*;

public class PrintVisitor extends Visitor {

    private int indentLevel;

    public PrintVisitor() {
        this.indentLevel = 1;
    }

    public void visit(AddExpression ad) {
        ad.getExpression1().accept(this);
        System.out.print("+");
        ad.getExpression2().accept(this);

    }

    public void visit(ArrayAssignmentStatement aas) {
        printIndentation();
        aas.getIdentifier().accept(this);
        System.out.print("[");
        aas.getIndex().accept(this);
        System.out.print("] = ");
        aas.getAssignment().accept(this);
        System.out.println(";");

    }

    public void visit(ArrayReference ar) {
        ar.getId().accept(this);
        System.out.print("[");
        ar.getExpression().accept(this);
        System.out.print("]");

    }

    public void visit(AssignmentStatement as) {
        printIndentation();
        as.getId().accept(this);
        System.out.print(" = ");
        as.getExpression().accept(this);
        System.out.println(";");

    }

    public void visit(Block b) {
        printIndentation();
        System.out.println("{");
        this.indentLevel = this.indentLevel + 1;
        StatementList statements = b.getStatementList();
        int size = statements.getSize();

        for (int i = 0; i < size; i++) {

            statements.getElement(i).accept(this);
        }
        this.indentLevel = this.indentLevel - 1;
        printIndentation();
        System.out.println("}");

    }

    public void visit(BooleanLiteral bl) {
        System.out.print(bl.getValue());

    }

    public void visit(CharacterLiteral cl) {
        System.out.print("\u0027");
        System.out.print(cl.getValue());
        System.out.print("\u0027");

    }

    public void visit(EmptyStatement es) {
        printIndentation();
        System.out.println(';');

    }

    public void visit(EqualityExpression ee) {
        ee.getExpression1().accept(this);
        System.out.print("==");
        ee.getExpression2().accept(this);

    }

    public void visit(ExpressionList el) {
        System.out.print("(");
        int size = el.getSize();
        for (int i = 0; i < size; i++) {
            if (i == (size - 1)) {
                el.getElement(i).accept(this);
            } else {
                el.getElement(i).accept(this);
                System.out.print(", ");
            }
        }
        System.out.print(")");

    }

    public void visit(ExpressionStatement es) {
        printIndentation();
        es.getExpression().accept(this);
        System.out.println(";");

    }

    public void visit(FloatLiteral fl) {
        System.out.print(fl.getValue());

    }

    public void visit(FormalParameter fp) {
        fp.getType().accept(this);
        System.out.print(" ");
        fp.getIdentifier().accept(this);

    }

    public void visit(FormalParameterList fpl) {
        int size = fpl.getSize();
        for (int i = 0; i < size; i++) {
            if (i == (size - 1)) {
                fpl.getElement(i).accept(this);
            } else {
                fpl.getElement(i).accept(this);
                System.out.print(", ");
            }
        }

    }

    public void visit(Function f) {
        f.getDeclaration().accept(this);
        f.getFunctionBody().accept(this);
    }

    public void visit(StatementList sl) {
        for (int i = 0; i < sl.getSize(); i++) {
            sl.getElement(i).accept(this);
        }
    }

    public void visit(FunctionBody fb) {
        System.out.println("{");
        fb.getVarDeclList().accept(this);
        fb.getStatements().accept(this);
        /*
         * for(int i =0; i<statements.getSize();i++){
         * statements.getElement(i).accept(this);
         * }
         */
        System.out.println("}");

    }

    public void visit(FunctionCall fc) {
        fc.getId().accept(this);
        fc.getExpressions().accept(this);

    }

    public void visit(FunctionDeclaration fd) {
        fd.getType().accept(this);
        System.out.print(" ");
        fd.getid().accept(this);
        System.out.print(" ");
        System.out.print("(");
        fd.getParams().accept(this);
        System.out.println(')');

    }

    public void visit(Identifier id) {
        System.out.print(id.getValue().getValue());

    }

    public void visit(IfStatement is) {
        printIndentation();
        System.out.print("if (");
        is.getExpression().accept(this);
        System.out.println(")");

        is.getIfBlock().accept(this);

        if (is.isElse()) {
            printIndentation();
            System.out.println("else");

            is.getElseBlock().accept(this);

        }

    }

    public void visit(IntegerLiteral il) {
        System.out.print(il.getValue());

    }

    public void visit(LessThanExpression lte) {
        lte.getExpression1().accept(this);
        System.out.print("<");
        lte.getExpression2().accept(this);

    }

    public void visit(MultExpression me) {
        me.getExpression1().accept(this);
        System.out.print("*");
        me.getExpression2().accept(this);

    }

    public void visit(ParenExpression pe) {
        System.out.print("(");
        pe.getExpression().accept(this);
        System.out.print(")");

    }

    public void visit(PrintLnStatement pls) {
        printIndentation();
        System.out.print("println ");
        pls.getExpression().accept(this);
        System.out.println(';');

    }

    public void visit(PrintStatement ps) {
        printIndentation();
        System.out.print("print ");
        ps.getExpression().accept(this);
        System.out.println(";");

    }

    public void visit(Program p) {
        int func_list_size = p.getSize();
        for (int i = 0; i < func_list_size; i++) {
            p.getElement(i).accept(this);

        }

    }

    public void visit(ReturnStatement rs) {
        printIndentation();
        System.out.print("return ");
        if (rs.getExpression() != null) {
            rs.getExpression().accept(this);
        }
        System.out.println(";");

    }

    public void visit(StringLiteral sl) {
        System.out.print("\"");
        System.out.print(sl.getValue());
        System.out.print("\"");

    }

    public void visit(SubstractExpression se) {

        se.getExpression1().accept(this);
        System.out.print("-");
        se.getExpression2().accept(this);

    }

    public void visit(TypeNode tn) {
        tn.getType().accept(this);

    }

    public void visit(VariableDeclaration vd) {
        printIndentation();
        vd.getType().accept(this);
        System.out.print(" ");
        vd.getId().accept(this);
        System.out.println(";");

    }

    public void visit(VariableDeclarationList vdl) {
        int size = vdl.getSize();

        for (int i = 0; i < size; i++) {
            vdl.getElement(i).accept(this);
        }

    }

    public void visit(WhileStatement ws) {
        printIndentation();
        System.out.print("while (");
        ws.getExpression().accept(this);
        System.out.println(")");
        ws.getBlock().accept(this);
        System.out.println("");

    }

    public void visit(ArrayType at) {
        at.getType().accept(this);
        System.out.print("[");
        System.out.print(at.getSize());
        System.out.print("]");
    }

    public void visit(BooleanType bt) {
        System.out.print(bt.toString());
    }

    public void visit(CharType ct) {
        System.out.print(ct.toString());
    }

    public void visit(FloatType ft) {
        System.out.print(ft.toString());
    }

    public void visit(StringType st) {
        System.out.print(st.toString());
    }

    public void visit(IntegerType it) {
        System.out.print(it.toString());
    }

    public void visit(VoidType vt) {
        System.out.print(vt.toString());
    }

    public void printIndentation() {

        for (int i = 0; i < indentLevel; i++) {
            System.out.print("    ");
        }
    }
}