
package IR;

import Types.*;
import AST.*;
import IR.Instructions.*;
import Env.*;
import java.util.*;
import java.io.*;

public class IrGen extends IrVisitor {
    private LabelAllocator labels;
    private TempAllocator temps;
    private Environment<String, FunctionIR> fEnv;
    private Environment<String, Temp> vEnv;
    private FunctionIR currFunc;
    private List<Instruction> instructions;
    private ProgramIR program;

    public IrGen() {
        this.fEnv = new StackEnvironment<String, FunctionIR>();
        this.vEnv = new StackEnvironment<String, Temp>();
        this.instructions = new ArrayList<Instruction>();
        this.program = new ProgramIR();
    }

    public ProgramIR getProgram() {
        return this.program;
    }

    public void setProgramName(String s) {
        this.program.setName(s);
    }

    public Temp visit(AddExpression ad) {

        Temp e1 = ad.getExpression1().accept(this);
        Temp e2 = ad.getExpression2().accept(this);
        Temp temp = this.temps.allocateTemp(e1.getType(), VarClass.TEMP);
        Instruction ins = new BinaryOpAssignment(e1, e2, BinaryOp.PLUS, temp);
        this.instructions.add(ins);
        return temp;
    }

    public Temp visit(ArrayAssignmentStatement aas) {
        Temp id = this.vEnv.lookup(aas.getIdentifier().getValue().getValue());
        Temp index = aas.getIndex().accept(this);
        Temp assignment = aas.getAssignment().accept(this);
        Instruction ins = new ArrayIndexAssignment(id, assignment, index);
        this.instructions.add(ins);
        return null;

    }

    public Temp visit(ArrayReference ar) {
        Temp id = this.vEnv.lookup(ar.getId().getValue().getValue());
        Temp temp = this.temps.allocateTemp(((ArrayType) id.getType()).getType(), VarClass.TEMP);
        Temp expr = ar.getExpression().accept(this);
        Instruction ins = new ArrayReferenceAssignment(temp, id, expr);
        this.instructions.add(ins);
        return temp;

    }

    public Temp visit(AssignmentStatement as) {
        Temp assignment = this.vEnv.lookup(as.getId().getValue().getValue());
        Temp expr = as.getExpression().accept(this);
        Instruction ins = new OperandAssignment(assignment, expr);
        this.instructions.add(ins);
        return null;
    }

    public Temp visit(Block b) {
        b.getStatementList().accept(this);
        return null;
    }

    public Temp visit(BooleanLiteral bl) {
        Temp temp = this.temps.allocateTemp(new BooleanType(), VarClass.TEMP);
        Instruction ins = new BooleanConstantAssignment(temp, bl.getValue());
        this.instructions.add(ins);
        return temp;
    }

    public Temp visit(CharacterLiteral cl) {
        Temp temp = this.temps.allocateTemp(new CharType(), VarClass.TEMP);
        Instruction ins = new CharConstantAssignment(temp, cl.getValue());
        this.instructions.add(ins);
        return temp;
    }

    public Temp visit(EmptyStatement es) {
        return null;
    }

    public Temp visit(EqualityExpression ee) {
        Temp e1 = ee.getExpression1().accept(this);
        Temp e2 = ee.getExpression2().accept(this);
        Temp temp = this.temps.allocateTemp(new BooleanType(), VarClass.TEMP);
        Instruction ins = new BinaryOpAssignment(e1, e2, BinaryOp.EQUALITY, temp);
        this.instructions.add(ins);
        return temp;
    }

    public Temp visit(ExpressionList el) {
        return null;
    }

    public Temp visit(ExpressionStatement es) {
        return es.getExpression().accept(this);

    }

    public Temp visit(FloatLiteral fl) {
        Temp temp = this.temps.allocateTemp(new FloatType(), VarClass.TEMP);
        Instruction ins = new FloatConstantAssignment(temp, fl.getValue());
        this.instructions.add(ins);
        return temp;
    }

    public Temp visit(FormalParameter fp) {
        Type type = fp.getType().getType();
        String id = fp.getIdentifier().getValue().getValue();
        Temp temp = this.temps.allocateVar(id, type, VarClass.PARAM);
        this.vEnv.add(id, temp);
        return null;
    }

    public Temp visit(FormalParameterList fpl) {

        for (int i = 0; i < fpl.getSize(); i++) {
            fpl.getElement(i).accept(this);
        }
        return null;
    }

    public Temp visit(Function f) {
        this.vEnv.beginScope();
        this.labels = new LabelAllocator();
        this.temps = new TempAllocator();
        this.instructions = new ArrayList<Instruction>();
        f.getDeclaration().accept(this);
        f.getFunctionBody().accept(this);

        this.vEnv.endScope();

        Instruction ret = new ReturnAssignment();
        this.instructions.add(ret);
        this.currFunc.setInstructions(this.instructions);
        this.currFunc.setVarList(this.temps.getTemps());
        this.program.addFunction(this.currFunc);

        return null;
    }

    public Temp visit(TypeNode tn) {
        return null;
    }

    public Temp visit(FunctionBody fb) {
        fb.getVarDeclList().accept(this);
        fb.getStatements().accept(this);

        return null;
    }

    public Temp visit(FunctionCall fc) {
        // get function call expressions
        List<Temp> TempList = new ArrayList<Temp>();
        ExpressionList exprList = fc.getExpressions();
        for (int i = 0; i < exprList.getSize(); i++) {
            TempList.add(exprList.getElement(i).accept(this));
        }
        FunctionIR func = this.fEnv.lookup(fc.getId().getValue().getValue());
        Type returnType = func.getReturnType();
        Temp temp = null;
        if (returnType instanceof VoidType) {
            Instruction ins = new CallInstruction(func, TempList);
            this.instructions.add(ins);
        }

        else {
            temp = this.temps.allocateTemp(returnType, VarClass.TEMP);
            Instruction ins = new CallAssignment(temp, func, TempList);
            this.instructions.add(ins);
        }

        return temp;
    }

    public Temp visit(FunctionDeclaration fd) {

        fd.getParams().accept(this);
        String id = fd.getid().getValue().getValue();
        Type type = fd.getType().getType();
        List<Type> paramTypes = new ArrayList<Type>();

        for (int i = 0; i < fd.getParams().getSize(); i++) {
            paramTypes.add(fd.getParams().getElement(i).getType().getType());

        }
        FunctionIR curr = new FunctionIR(id, type);
        curr.setParams(paramTypes);
        this.currFunc = curr;
        return null;
    }

    public Temp visit(Identifier id) {
        Temp temp = this.vEnv.lookup(id.getValue().getValue());

        return temp;
    }

    public Temp visit(IfStatement is) {
        Label l1 = this.labels.allocate();
        Label l2 = this.labels.allocate();

        Temp expr = is.getExpression().accept(this);
        Temp temp = this.temps.allocateTemp(new BooleanType(), VarClass.TEMP);
        Instruction ins = new OperandAssignment(temp, expr);
        this.instructions.add(ins);

        Temp negatedExpression = this.temps.allocateTemp(new BooleanType(), VarClass.TEMP);
        Instruction negatedInstruction = new UnaryOpAssignment(temp, negatedExpression, UnaryOp.INVERSION);
        this.instructions.add(negatedInstruction);

        // do negation logic

        Instruction gotoElse = new ConditionalGOTO(negatedExpression, l1);
        this.instructions.add(gotoElse);
        is.getIfBlock().accept(this);
        Instruction gotoEnd = new UnconditionalGOTO(l2);
        this.instructions.add(gotoEnd);
        this.instructions.add(l1);
        if (!(is.getElseBlock() == null)) {
            is.getElseBlock().accept(this);
        }
        this.instructions.add(l2);

        return null;
    }

    public Temp visit(IntegerLiteral il) {
        Temp temp = this.temps.allocateTemp(new IntegerType(), VarClass.TEMP);
        Instruction ins = new IntConstantAssignment(temp, il.getValue());
        this.instructions.add(ins);
        return temp;
    }

    public Temp visit(LessThanExpression lte) {
        Temp e1 = lte.getExpression1().accept(this);
        Temp e2 = lte.getExpression2().accept(this);
        Temp temp = this.temps.allocateTemp(new BooleanType(), VarClass.TEMP);
        Instruction ins = new BinaryOpAssignment(e1, e2, BinaryOp.LESSTHAN, temp);
        this.instructions.add(ins);
        return temp;
    }

    public Temp visit(MultExpression me) {
        Temp e1 = me.getExpression1().accept(this);
        Temp e2 = me.getExpression2().accept(this);
        Temp temp = this.temps.allocateTemp(e1.getType(), VarClass.TEMP);
        Instruction ins = new BinaryOpAssignment(e1, e2, BinaryOp.MULT, temp);
        this.instructions.add(ins);
        return temp;
    }

    public Temp visit(ParenExpression pe) {
        Temp temp = pe.getExpression().accept(this);
        return temp;
    }

    public Temp visit(PrintLnStatement pls) {
        Temp expr = pls.getExpression().accept(this);
        Instruction ins = new PrintlnAssignment(expr, expr.getType());
        this.instructions.add(ins);
        return null;
    }

    public Temp visit(PrintStatement ps) {
        Temp expr = ps.getExpression().accept(this);
        Instruction ins = new PrintAssignment(expr, expr.getType());
        this.instructions.add(ins);
        return null;
    }

    public Temp visit(Program p) {

        this.fEnv.beginScope();

        for (int i = 0; i < p.getSize(); i++) {
            Function t = p.getElement(i);
            this.fEnv.add(t.getDeclaration().getid().getValue().getValue(),
                    new FunctionIR(t.getDeclaration().getid().getValue().getValue(),
                            t.getDeclaration().getType().getType()));
        }

        for (int i = 0; i < p.getSize(); i++) {
            Function temp = p.getElement(i);
            temp.accept(this);
        }

        this.fEnv.endScope();

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(program.getName() + ".ir")));
            out.print(program);
            out.close();
        } catch (IOException i) {
            i.printStackTrace();
        }

        return null;
    }

    public Temp visit(ReturnStatement rs) {
        Instruction ins;
        if (rs.getExpression() == null) {
            ins = new ReturnAssignment();
        } else {
            Temp expr = rs.getExpression().accept(this);
            ins = new ReturnAssignment(expr);
        }
        this.instructions.add(ins);
        return null;
    }

    public Temp visit(StringLiteral sl) {
        Temp temp = this.temps.allocateTemp(new StringType(), VarClass.TEMP);
        Instruction ins = new StringConstantAssignment(temp, sl.getValue());
        this.instructions.add(ins);
        return temp;
    }

    public Temp visit(SubstractExpression se) {
        Temp e1 = se.getExpression1().accept(this);
        Temp e2 = se.getExpression2().accept(this);
        Temp temp = this.temps.allocateTemp(e1.getType(), VarClass.TEMP);
        Instruction ins = new BinaryOpAssignment(e1, e2, BinaryOp.MINUS, temp);
        this.instructions.add(ins);
        return temp;
    }

    public Temp visit(VariableDeclaration vd) {
        Type type = vd.getType().getType();
        String id = vd.getId().getValue().getValue();
        Temp temp = this.temps.allocateVar(id, type, VarClass.LOCAL);
        this.vEnv.add(id, temp);
        if (type instanceof ArrayType) {
            Instruction ins = new ArrayInitAssignment(temp, ((ArrayType) type).getType(), ((ArrayType) type).getSize());
            this.instructions.add(ins);

        }
        return null;
    }

    public Temp visit(VariableDeclarationList vdl) {
        for (int i = 0; i < vdl.getSize(); i++) {
            vdl.getElement(i).accept(this);
        }
        return null;
    }

    public Temp visit(WhileStatement ws) {
        Label loopl = this.labels.allocate();
        Label breakl = this.labels.allocate();
        this.instructions.add(loopl);
        Temp expr = ws.getExpression().accept(this);
        Temp temp = this.temps.allocateTemp(new BooleanType(), VarClass.TEMP);
        Instruction assignment = new OperandAssignment(temp, expr);
        this.instructions.add(assignment);

        // do negation logic
        Temp negatedExpression = this.temps.allocateTemp(new BooleanType(), VarClass.TEMP);
        Instruction negatedInstruction = new UnaryOpAssignment(temp, negatedExpression, UnaryOp.INVERSION);
        this.instructions.add(negatedInstruction);
        Instruction condGoto = new ConditionalGOTO(negatedExpression, breakl);
        Instruction uncondGoto = new UnconditionalGOTO(loopl);

        this.instructions.add(condGoto);
        ws.getBlock().accept(this);
        this.instructions.add(uncondGoto);
        this.instructions.add(breakl);

        return null;

    }

    public Temp visit(ArrayType at) {
        return null;
    }

    public Temp visit(BooleanType bt) {
        return null;
    }

    public Temp visit(CharType ct) {
        return null;
    }

    public Temp visit(FloatType ft) {
        return null;
    }

    public Temp visit(StringType st) {
        return null;
    }

    public Temp visit(IntegerType it) {
        return null;
    }

    public Temp visit(VoidType vt) {
        return null;
    }

    public Temp visit(StatementList sl) {

        for (int i = 0; i < sl.getSize(); i++) {
            sl.getElement(i).accept(this);
        }

        return null;

    }
}
