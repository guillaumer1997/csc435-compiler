package TypeCheck;

import AST.*;
import Types.*;
import Env.*;

public class TypeCheckVisitor extends TypeVisitor {
    private Environment<String, Function> fEnv;
    private Environment<String, Type> vEnv;
    private String currFuncName;

    public TypeCheckVisitor() {
        this.fEnv = new StackEnvironment<String, Function>();
        this.vEnv = new StackEnvironment<String, Type>();

    }

    public Type visit(AddExpression ad) throws SemanticException {
        Type leftType = ad.getExpression1().accept(this);
        Type rightType = ad.getExpression2().accept(this);

        if (!leftType.equals(rightType)) {
            throw new SemanticException("types do not match", ad.getLine(), ad.getOffset());
        } else {
            if (leftType instanceof BooleanType || leftType instanceof VoidType) {
                throw new SemanticException("cannot add expressions of type " + leftType.toString(), ad.getLine(),
                        ad.getOffset());
            } else {
                return leftType;
            }
        }
    }

    public Type visit(ArrayAssignmentStatement aas) throws SemanticException {
        Type idType = aas.getIdentifier().accept(this);
        Type iType = aas.getIndex().accept(this);
        Type aType = aas.getAssignment().accept(this);
        if (!(iType instanceof IntegerType)) {
            throw new SemanticException("array index must be an integer", aas.getLine(), aas.getOffset());
        }
        if (!(((ArrayType) idType).getType().equals(aType))) {
            throw new SemanticException(
                    "cannot assign type " + aType.toString() + " to array of type " + idType.toString(),
                    aas.getLine(), aas.getOffset());
        }

        return null;
    }

    public Type visit(ArrayReference ar) throws SemanticException {
        Type idType = ar.getId().accept(this);
        Type eType = ar.getExpression().accept(this);
        if (!(eType instanceof IntegerType)) {
            throw new SemanticException("array index must be an integer", ar.getLine(), ar.getOffset());
        }
        return ((ArrayType) idType).getType();
    }

    public Type visit(AssignmentStatement as) throws SemanticException {
        String varName = as.getId().getValue().getValue();
        if (!this.vEnv.inCurrentScope(varName)) {
            throw new SemanticException(varName + " has not been defined", as.getId().getLine(),
                    as.getId().getOffset());
        }

        Type exprType = as.getExpression().accept(this);
        Type varType = this.vEnv.lookup(varName);

        if (!exprType.equals(varType)) {
            throw new SemanticException("variable of Type " + varType + " cannot be assigned value of type " + exprType,
                    as.getExpression().getLine(), as.getExpression().getOffset());
        }

        return null;
    }

    public Type visit(Block b) throws SemanticException {
        b.getStatementList().accept(this);
        return null;
    }

    public Type visit(BooleanLiteral bl) throws SemanticException {
        return new BooleanType();
    }

    public Type visit(CharacterLiteral cl) throws SemanticException {
        return new CharType();
    }

    public Type visit(EmptyStatement es) throws SemanticException {
        return null;
    }

    public Type visit(EqualityExpression ee) throws SemanticException {
        Type leftType = ee.getExpression1().accept(this);
        Type rightType = ee.getExpression2().accept(this);
        if (!leftType.equals(rightType)) {
            throw new SemanticException("cannot compare types " + leftType.toString() + " and " + rightType.toString(),
                    ee.getLine(), ee.getOffset());
        } else {
            if (leftType instanceof VoidType) {
                throw new SemanticException("cannot compare void types", ee.getLine(), ee.getOffset());
            } else {
                return new BooleanType();
            }
        }

    }

    public Type visit(ExpressionList el) throws SemanticException {
        int elSize = el.getSize();
        for (int i = 0; i < elSize; i++) {
            el.getElement(i).accept(this);
        }
        return null;
    }

    public Type visit(ExpressionStatement es) throws SemanticException {
        es.getExpression().accept(this);
        return null;
    }

    public Type visit(FloatLiteral fl) throws SemanticException {
        return new FloatType();
    }

    public Type visit(FormalParameter fp) throws SemanticException {

        // check for void ty

        return fp.getType().accept(this);
    }

    public Type visit(FormalParameterList fpl) throws SemanticException {
        int fplSize = fpl.getSize();

        for (int i = 0; i < fplSize; i++) {
            FormalParameter fp = fpl.getElement(i);

            if (fp.getType().accept(this).equals(new VoidType())) {
                throw new SemanticException("a formal parameter cannot have type void", fp.getLine(), fp.getOffset());
            }
            if (this.vEnv.inCurrentScope(fp.getIdentifier().getValue().getValue())) {
                throw new SemanticException("a formal parameter of the name " + fp.getIdentifier().getValue().getValue()
                        + " already exists", fp.getLine(), fp.getOffset());
            }
            this.vEnv.add(fp.getIdentifier().getValue().getValue(), fp.getType().accept(this));
        }

        return null;
    }

    public Type visit(Function f) throws SemanticException {
        this.vEnv.beginScope();
        this.currFuncName = f.getDeclaration().getid().getValue().getValue();
        f.getDeclaration().accept(this);
        f.getFunctionBody().accept(this);
        this.vEnv.endScope();
        return null;
    }

    public Type visit(FunctionBody fb) throws SemanticException {
        fb.getVarDeclList().accept(this);
        fb.getStatements().accept(this);
        return null;
    }

    public Type visit(FunctionCall fc) throws SemanticException {
        String funcId = fc.getId().getValue().getValue();
        if (!this.fEnv.inCurrentScope(funcId)) {
            throw new SemanticException("the function " + funcId + " does not exist",
                    fc.getLine(), fc.getOffset());
        } else {
            Function funcCalled = this.fEnv.lookup(funcId);
            FormalParameterList funcCalledParams = funcCalled.getDeclaration().getParams();

            if (funcCalledParams.getSize() != fc.getExpressions().getSize()) {
                throw new SemanticException("formal parameter size and arguments do not match",
                        fc.getLine(), fc.getOffset());
            } else {
                for (int i = 0; i < funcCalledParams.getSize(); i++) {
                    Type formParamType = funcCalled.getDeclaration().getParams().getElement(i).accept(this);
                    Type argType = fc.getExpressions().getElement(i).accept(this);
                    if (!formParamType.equals(argType)) {
                        throw new SemanticException(
                                "Arguments of types " + argType.toString() + " and " + formParamType.toString()
                                        + " do not match",
                                fc.getLine(), fc.getOffset());
                    }
                }
                return funcCalled.getDeclaration().getType().accept(this);
            }

        }

    }

    public Type visit(FunctionDeclaration fd) throws SemanticException {
        // perform main check
        if (fd.getid().getValue().getValue().equals("main")) {
            // System.out.print(fd.getType().getType());
            if (!fd.getType().accept(this).equals(new VoidType())) {
                throw new SemanticException("main must have return type void", fd.getLine(), fd.getOffset());
            }
            if (!(fd.getParams().getSize() == 0)) {
                throw new SemanticException("main cannot have any arguments", fd.getLine(), fd.getOffset());
            }

        }

        fd.getParams().accept(this);

        return null;
    }

    public Type visit(Identifier id) throws SemanticException {
        if (this.vEnv.inCurrentScope(id.getValue().getValue())) {
            return this.vEnv.lookup(id.getValue().getValue());
        } else {
            throw new SemanticException("variable of name " + id.getValue().getValue() + " does not exist",
                    id.getLine(), id.getOffset());
        }

    }

    public Type visit(IfStatement is) throws SemanticException {
        if (!is.getExpression().accept(this).equals(new BooleanType())) {
            throw new SemanticException("type of expression must be boolean", is.getLine(), is.getOffset());
        }
        is.getIfBlock().accept(this);
        if (is.getElseBlock() != null) {
            is.getElseBlock().accept(this);
        }
        return null;
    }

    public Type visit(IntegerLiteral il) throws SemanticException {
        return new IntegerType();
    }

    public Type visit(LessThanExpression lte) throws SemanticException {
        Type leftType = lte.getExpression1().accept(this);
        Type rightType = lte.getExpression2().accept(this);

        if (!leftType.equals(rightType)) {
            throw new SemanticException("cannot compare types " + leftType.toString() + " and " + rightType.toString(),
                    lte.getLine(), lte.getOffset());
        }

        else {
            if (leftType instanceof VoidType) {
                throw new SemanticException("cannot compare void types", lte.getLine(), lte.getOffset());
            } else {
                return new BooleanType();
            }
        }

    }

    public Type visit(MultExpression me) throws SemanticException {
        Type leftType = me.getExpression1().accept(this);
        Type rightType = me.getExpression2().accept(this);

        if (!leftType.equals(rightType)) {
            throw new SemanticException("Types do not match!", me.getLine(), me.getOffset());
        }

        if (leftType instanceof IntegerType && rightType instanceof IntegerType) {
            return new IntegerType();
        } else if (leftType instanceof FloatType && rightType instanceof FloatType) {
            return new FloatType();
        }

        else {
            throw new SemanticException(
                    "Cannot multiply expressions of type " + leftType.toString() + " and " + rightType.toString(),
                    me.getLine(), me.getOffset());
        }
    }

    public Type visit(ParenExpression pe) throws SemanticException {
        return pe.getExpression().accept(this);
    }

    public Type visit(PrintLnStatement pls) throws SemanticException {
        Type eType = pls.getExpression().accept(this);
        if (eType instanceof VoidType || eType instanceof ArrayType) {
            throw new SemanticException("expression cannot be of type " + eType.toString(), pls.getLine(),
                    pls.getOffset());
        }
        return null;
    }

    public Type visit(PrintStatement ps) throws SemanticException {
        Type eType = ps.getExpression().accept(this);
        if (eType instanceof VoidType || eType instanceof ArrayType) {
            throw new SemanticException("expression cannot be of type " + eType.toString(), ps.getLine(),
                    ps.getOffset());
        }
        return null;
    }

    public Type visit(Program p) throws SemanticException {
        this.fEnv.beginScope();
        int fSize = p.getSize();
        for (int i = 0; i < fSize; i++) {
            Function currFunc = p.getElement(i);
            String currFuncValue = currFunc.getDeclaration().getid().getValue().getValue();
            if (!fEnv.inCurrentScope(currFuncValue)) {
                this.fEnv.add(currFuncValue, currFunc);
            } else {
                throw new SemanticException("a function with the name " + currFuncValue + " already exists",
                        currFunc.getDeclaration().getLine(), currFunc.getDeclaration().getOffset());
            }

        }
        if (this.fEnv.isEmpty()) {
            String message = "program must contain a function";
            throw new SemanticException(message, p.getLine(), p.getOffset());
        }
        if (!this.fEnv.inCurrentScope("main")) {
            String message = "no main function found!";
            throw new SemanticException(message, p.getLine(), p.getOffset());
        }

        for (int i = 0; i < fSize; i++) {
            p.getElement(i).accept(this);
        }
        this.fEnv.endScope();
        return null;
    }

    public Type visit(ReturnStatement rs) throws SemanticException {
        Type funcType = this.fEnv.lookup(this.currFuncName).getDeclaration().getType().accept(this);
        if (funcType instanceof VoidType) {
            if (!(rs.getExpression() == null)) {
                throw new SemanticException("function of type void cannot return a value", rs.getLine(),
                        rs.getOffset());
            }
        } else {

            if (rs.getExpression() == null) {
                throw new SemanticException("function of type " + funcType.toString() + " must return something",
                        rs.getLine(), rs.getOffset());
            } else {
                Type eType = rs.getExpression().accept(this);
                if (!(eType.equals(funcType))) {
                    throw new SemanticException(
                            "function of type " + funcType.toString() + " cannot return value of type "
                                    + eType.toString(),
                            rs.getLine(), rs.getOffset());
                }
            }
        }
        return null;
    }

    public Type visit(StringLiteral sl) throws SemanticException {
        return new StringType();
    }

    public Type visit(SubstractExpression se) throws SemanticException {
        Type leftType = se.getExpression1().accept(this);
        Type rightType = se.getExpression2().accept(this);

        if (!leftType.equals(rightType)) {
            throw new SemanticException(
                    "cannot substract types " + leftType.toString() + " and " + rightType.toString(), se.getLine(),
                    se.getOffset());
        } else {
            if (leftType instanceof BooleanType || leftType instanceof VoidType || leftType instanceof StringType) {
                throw new SemanticException("cannot substract expressions of type " + leftType.toString(), se.getLine(),
                        se.getOffset());
            } else {
                return leftType;
            }
        }

    }

    public Type visit(TypeNode tn) throws SemanticException {
        return tn.getType().accept(this);
    }

    public Type visit(VariableDeclaration vd) throws SemanticException {
        // check void type

        if (vd.getType().accept(this).equals(new VoidType())) {
            throw new SemanticException("variable cannot have type void", vd.getLine(), vd.getOffset());
        }

        if (this.vEnv.inCurrentScope(vd.getId().getValue().getValue())) {
            throw new SemanticException(
                    "a variable of the name " + vd.getId().getValue().getValue() + " already exists", vd.getLine(),
                    vd.getOffset());
        }
        this.vEnv.add(vd.getId().getValue().getValue(), vd.getType().accept(this));
        return null;
    }

    public Type visit(VariableDeclarationList vdl) throws SemanticException {
        int vdlSize = vdl.getSize();
        for (int i = 0; i < vdlSize; i++) {
            vdl.getElement(i).accept(this);
        }
        return null;
    }

    public Type visit(WhileStatement ws) throws SemanticException {
        Type eType = ws.getExpression().accept(this);

        if (!(eType instanceof BooleanType)) {
            throw new SemanticException("while expression must be of type boolean",
                    ws.getLine(), ws.getOffset());
        } else {
            ws.getBlock().accept(this);
        }
        return null;
    }

    public Type visit(ArrayType at) throws SemanticException {
        return new ArrayType(at.getType(), at.getSize());
    }

    public Type visit(BooleanType bt) throws SemanticException {
        return new BooleanType();
    }

    public Type visit(CharType ct) throws SemanticException {
        return new CharType();
    }

    public Type visit(FloatType ft) throws SemanticException {
        return new FloatType();
    }

    public Type visit(StringType st) throws SemanticException {
        return new StringType();
    }

    public Type visit(IntegerType it) throws SemanticException {
        return new IntegerType();
    }

    public Type visit(VoidType vt) throws SemanticException {
        return new VoidType();
    }

    public Type visit(StatementList sl) throws SemanticException {
        int slSize = sl.getSize();
        for (int i = 0; i < slSize; i++) {
            sl.getElement(i).accept(this);
        }

        return null;
    }

}