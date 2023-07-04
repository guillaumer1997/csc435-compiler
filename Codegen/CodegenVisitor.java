package Codegen;

import IR.*;
import IR.Instructions.*;

import AST.*;
import Types.*;

import java.util.*;
import java.io.*;

public class CodegenVisitor {

    private ProgramIR program;
    private int label_num;
    private PrintWriter out;

    public CodegenVisitor(ProgramIR p) throws IOException {
        this.program = p;
        this.label_num = 0;
        this.out = new PrintWriter(new BufferedWriter(new FileWriter(p.getName() + ".j")));
    }

    public void genCode() throws IOException {
        this.out.print(".source " + this.program.getName() + ".ir\n");
        this.out.print(".class public " + this.program.getName() + "\n");
        this.out.print(".super java/lang/Object\n");

        // visit function nodes here
        List<FunctionIR> funcs = this.program.getFunctions();

        for (int i = 0; i < funcs.size(); i++) {
            funcs.get(i).accept(this);
        }

        this.out.print(";--------------------------------------------;\n");
        this.out.print(";                                            ;\n");
        this.out.print(";                Boilerplate                 ;\n");
        this.out.print(";                                            ;\n");
        this.out.print(";--------------------------------------------;\n\n");

        this.out.print(".method public static main([Ljava/lang/String;)V\n");
        this.out.print("\t.limit locals 1\n");
        this.out.print("\t.limit stack 4\n");
        this.out.print("\tinvokestatic " + this.program.getName() + "/__main()V\n");
        this.out.print("\treturn\n");
        this.out.print(".end method\n");
        this.out.print(".method public <init>()V\n");
        this.out.print("\taload_0\n");
        this.out.print("\tinvokenonvirtual java/lang/Object/<init>()V\n");
        this.out.print("\treturn\n");
        this.out.print(".end method\n");

        this.out.close();

    }

    public void visit(FunctionIR func) throws IOException {

        int startLabel = this.label_num;
        this.label_num++;
        int endLabel = this.label_num;
        label_num++;
        List<Instruction> instructions = func.getInstructions();
        String name = func.getName();
        List<Type> params = func.getParams();
        List<Temp> vars = func.getVars();

        this.out.print(".method public static ");

        if (name.equals("main")) {
            this.out.print("__");
        }

        this.out.print(name + "(");

        for (int i = 0; i < params.size(); i++) {
            this.out.print(toJasminString(params.get(i)));
        }
        this.out.print(")" + toJasminString(func.getReturnType()) + "\n");
        this.out.print("\t.limit locals " + vars.size() + "\n");

        for (int i = 0; i < vars.size(); i++) {
            this.out.print("\t.var " + i + " is ");
            if (vars.get(i).getTempClass().equals(VarClass.LOCAL)
                    || vars.get(i).getTempClass().equals(VarClass.PARAM)) {
                this.out.print(vars.get(i).getName());
            } else {
                this.out.print("T" + vars.get(i).getNumber());
            }
            this.out.print(
                    " " + toJasminString(vars.get(i).getType()) + " from L_" + startLabel + " to L_" + endLabel + "\n");

        }

        this.out.print("\t.limit stack 16\n");

        // print label here not readt yet
        this.out.print("L_" + startLabel + ":\n");

        for (int i = 0; i < vars.size(); i++) {
            if (!(vars.get(i).getTempClass().equals(VarClass.PARAM))) {
                if (vars.get(i).getType() instanceof IntegerType || vars.get(i).getType() instanceof BooleanType
                        || vars.get(i).getType() instanceof CharType) {
                    this.out.print("\tldc 0\n");
                    this.out.print("\tistore " + vars.get(i).getNumber() + "\n");

                } else if (vars.get(i).getType() instanceof FloatType) {
                    this.out.print("\tldc 0.0\n");
                    this.out.print("\tfstore " + vars.get(i).getNumber() + "\n");

                }

                else if (vars.get(i).getType() instanceof StringType || vars.get(i).getType() instanceof ArrayType) {
                    this.out.print("\taconst_null\n");
                    this.out.print("\tastore " + vars.get(i).getNumber() + "\n");
                }
            }
        }

        for (int i = 0; i < instructions.size(); i++) {
            this.out.print(";" + instructions.get(i).toString() + "\n");
            instructions.get(i).accept(this);
        }

        this.out.print("L_" + endLabel + ":\n");
        this.out.print(".end method\n");

    }

    public void visit(ArrayIndexAssignment aia) throws IOException {
        this.out.print("\taload " + aia.getLeft().getNumber() + "\n");
        this.out.print("\tiload " + aia.getIndex().getNumber() + "\n");
        this.out.print("\t" + toShort(aia.getRight().getType()) + "load " + aia.getRight().getNumber() + "\n");
        if (aia.getRight().getType() instanceof CharType) {
            this.out.print("\tcastore\n");
        } else if (aia.getRight().getType() instanceof BooleanType) {
            this.out.print("\tbastore\n");
        } else {
            this.out.print("\t" + toShort(aia.getRight().getType()) + "astore\n");
        }

    }

    public void visit(ArrayInitAssignment ai) throws IOException {

        this.out.print("\tldc " + ai.getSize() + "\n");
        if (ai.getType() instanceof FloatType) {
            this.out.print("\tnewarray float\n");
        } else if (ai.getType() instanceof IntegerType) {
            this.out.print("\tnewarray int\n");

        } else if (ai.getType() instanceof CharType) {
            this.out.print("\tnewarray char\n");

        } else if (ai.getType() instanceof BooleanType) {
            this.out.print("\tnewarray boolean\n");

        } else if (ai.getType() instanceof StringType) {

            this.out.print("\tanewarray java/lang/String\n");

        }

        this.out.print("\tastore " + ai.getTemp().getNumber() + "\n");

    }

    public void visit(ArrayReferenceAssignment ara) throws IOException {
        this.out.print("\taload " + ara.getRight().getNumber() + "\n");
        this.out.print("\tiload " + ara.getIndex().getNumber() + "\n");
        if (ara.getLeft().getType() instanceof BooleanType) {
            this.out.print("\tbaload\n");

        } else if (ara.getLeft().getType() instanceof CharType) {
            this.out.print("\tcaload\n");
        }

        else {

            this.out.print("\t" + toShort(ara.getLeft().getType()) + "aload\n");
        }
        this.out.print("\t" + toShort(ara.getLeft().getType()) + "store " + ara.getLeft().getNumber() + "\n");

    }

    public void visit(BinaryOpAssignment boa) throws IOException {

        Type t = boa.getRight().getType();
        BinaryOp op = boa.getOp();

        if (op.equals(BinaryOp.PLUS)) {

            if (t instanceof IntegerType) {
                this.out.print("\tiload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tiload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tiadd\n");
                this.out.print("\nistore " + boa.getAssignment().getNumber() + "\n");
            } else if (t instanceof FloatType) {
                this.out.print("\tfload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tfload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tfadd\n");
                this.out.print("\tfstore " + boa.getAssignment().getNumber() + "\n");

            } else if (t instanceof CharType) {
                this.out.print("\tiload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tiload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tiadd\n");
                this.out.print("\ti2c\n");
                this.out.print("\tistore " + boa.getAssignment().getNumber() + "\n");
            }

            else if (t instanceof StringType) {
                this.out.print("\tnew java/lang/StringBuffer\n");
                this.out.print("\tdup\n");
                this.out.print("\tinvokenonvirtual java/lang/StringBuffer/<init>()V\n");
                this.out.print("\taload " + boa.getLeft().getNumber() + "\n");
                this.out.print(
                        "\tinvokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;\n");
                this.out.print("\taload " + boa.getRight().getNumber() + "\n");
                this.out.print(
                        "\tinvokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;\n");
                this.out.print("\tinvokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;\n");
                this.out.print("\tastore " + boa.getAssignment().getNumber() + "\n");
            }

        } else if (op.equals(BinaryOp.MINUS)) {
            if (t instanceof IntegerType) {
                this.out.print("\tiload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tiload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tisub\n");
                this.out.print("\nistore " + boa.getAssignment().getNumber() + "\n");
            } else if (t instanceof FloatType) {
                this.out.print("\tfload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tfload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tfsub\n");
                this.out.print("\tfstore " + boa.getAssignment().getNumber() + "\n");

            } else if (t instanceof CharType) {
                this.out.print("\tiload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tiload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tisub\n");
                this.out.print("\ti2c\n");
                this.out.print("\tistore " + boa.getAssignment().getNumber() + "\n");
            }

        }

        else if (op.equals(BinaryOp.MULT)) {
            if (t instanceof IntegerType) {
                this.out.print("\tiload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tiload " + boa.getRight().getNumber() + "\n");
                this.out.print("\timul\n");
                this.out.print("\nistore " + boa.getAssignment().getNumber() + "\n");
            } else if (t instanceof FloatType) {
                this.out.print("\tfload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tfload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tfmul\n");
                this.out.print("\tfstore " + boa.getAssignment().getNumber() + "\n");

            }
        }

        else if (op.equals(BinaryOp.EQUALITY)) {
            if (t instanceof IntegerType || t instanceof CharType) {

                this.out.print("\tiload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tiload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tisub\n");
                this.out.print("\tifeq L_" + label_num + "\n");
                int L1_index = this.label_num;
                this.label_num++;
                this.out.print("\tldc 0\n");
                this.out.print("\tgoto L_" + label_num + "\n");
                int L2_index = this.label_num;
                this.label_num++;
                this.out.print("L_" + L1_index + ":\n");
                this.out.print("\tldc 1\n");
                this.out.print("L_" + L2_index + ":\n");
                this.out.print("\tistore " + boa.getAssignment().getNumber() + "\n");

            } else if (t instanceof FloatType) {
                this.out.print("\tfload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tfload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tfcmpg\n");
                this.out.print("\tifeq L_" + label_num + "\n");
                int L1_index = this.label_num;
                this.label_num++;
                this.out.print("\tldc 0\n");
                this.out.print("\tgoto L_" + label_num + "\n");
                int L2_index = this.label_num;
                this.label_num++;
                this.out.print("L_" + L1_index + ":\n");
                this.out.print("\tldc 1\n");
                this.out.print("L_" + L2_index + ":\n");
                this.out.print("\tistore " + boa.getAssignment().getNumber() + "\n");

            } else if (t instanceof BooleanType) {
                this.out.print("\tiload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tiload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tisub\n");
                this.out.print("\tifeq L_" + label_num + "\n");
                int L1_index = this.label_num;
                this.label_num++;
                this.out.print("\tldc 0\n");
                this.out.print("\tgoto L_" + label_num + "\n");
                int L2_index = this.label_num;
                this.label_num++;
                this.out.print("L_" + L1_index + ":\n");
                this.out.print("\tldc 1\n");
                this.out.print("L_" + L2_index + ":\n");
                this.out.print("\tistore " + boa.getAssignment().getNumber() + "\n");
            } else if (t instanceof StringType) {
                this.out.print("\taload " + boa.getRight().getNumber() + "\n");
                this.out.print("\taload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tinvokevirtual java/lang/String/compareTo(Ljava/lang/String;)I\n");
                this.out.print("\tifeq L_" + label_num + "\n");
                int L1_index = this.label_num;
                this.label_num++;
                this.out.print("\tldc 0\n");
                this.out.print("\tgoto L_" + label_num + "\n");
                int L2_index = this.label_num;
                this.label_num++;
                this.out.print("L_" + L1_index + ":\n");
                this.out.print("\tldc 1\n");
                this.out.print("L_" + L2_index + ":\n");
                this.out.print("\tistore " + boa.getAssignment().getNumber() + "\n");
            }

        }

        else if (op.equals(BinaryOp.NOTEQUAL)) {

        } else if (op.equals(BinaryOp.LESSTHAN)) {

            if (t instanceof IntegerType || t instanceof CharType) {
                this.out.print("\tiload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tiload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tisub\n");
                this.out.print("\tiflt L_" + label_num + "\n");
                int L1_index = this.label_num;
                this.label_num++;
                this.out.print("\tldc 0\n");
                this.out.print("\tgoto L_" + label_num + "\n");
                int L2_index = this.label_num;
                this.label_num++;
                this.out.print("L_" + L1_index + ":\n");
                this.out.print("\tldc 1\n");
                this.out.print("L_" + L2_index + ":\n");
                this.out.print("\tistore " + boa.getAssignment().getNumber() + "\n");

            } else if (t instanceof FloatType) {
                this.out.print("\tfload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tfload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tfcmpg\n");
                this.out.print("\tiflt L_" + label_num + "\n");
                int L1_index = this.label_num;
                this.label_num++;
                this.out.print("\tldc 0\n");
                this.out.print("\tgoto L_" + label_num + "\n");
                int L2_index = this.label_num;
                this.label_num++;
                this.out.print("L_" + L1_index + ":\n");
                this.out.print("\tldc 1\n");
                this.out.print("L_" + L2_index + ":\n");
                this.out.print("\tistore " + boa.getAssignment().getNumber() + "\n");

            } else if (t instanceof BooleanType) {
                this.out.print("\tiload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\tiload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tisub\n");
                this.out.print("\tifeq L_" + label_num + "\n");
                int L1_index = this.label_num;
                this.label_num++;
                this.out.print("\tldc 0\n");
                this.out.print("\tgoto L_" + label_num + "\n");
                int L2_index = this.label_num;
                this.label_num++;
                this.out.print("L_" + L1_index + ":\n");
                this.out.print("\tldc 1\n");
                this.out.print("L_" + L2_index + ":\n");
                this.out.print("\tistore " + boa.getAssignment().getNumber() + "\n");
            } else if (t instanceof StringType) {
                this.out.print("\taload " + boa.getLeft().getNumber() + "\n");
                this.out.print("\taload " + boa.getRight().getNumber() + "\n");
                this.out.print("\tinvokevirtual java/lang/String/compareTo(Ljava/lang/String;)I\n");
                this.out.print("\tiflt L_" + label_num + "\n");
                int L1_index = this.label_num;
                this.label_num++;
                this.out.print("\tldc 0\n");
                this.out.print("\tgoto L_" + label_num + "\n");
                int L2_index = this.label_num;
                this.label_num++;
                this.out.print("L_" + L1_index + ":\n");
                this.out.print("\tldc 1\n");
                this.out.print("L_" + L2_index + ":\n");
                this.out.print("\tistore " + boa.getAssignment().getNumber() + "\n");
            }

        }

    }

    public void visit(CallAssignment ca) throws IOException {
        List<Temp> params = ca.getTemps();
        for (int i = 0; i < params.size(); i++) {
            this.out.print("\t" + toShort(params.get(i).getType()) + "load " + params.get(i).getNumber() + "\n");
        }
        this.out.print("\tinvokestatic " + this.program.getName() + "/" + ca.getFunc().getName() + "(");
        // List<Type> args = ca.getFunc().getParams();

        for (int i = 0; i < params.size(); i++) {
            this.out.print(toJasminString(params.get(i).getType()));
            // System.out.println(params.get(i).getType());
        }
        this.out.print(")");
        this.out.print(toJasminString(ca.getFunc().getReturnType()) + "\n");
        this.out.print("\t" + toShort(ca.getLeft().getType()) + "store " + ca.getLeft().getNumber() + "\n");

    }

    public void visit(CallInstruction ci) throws IOException {
        List<Temp> params = ci.getTemps();
        for (int i = 0; i < params.size(); i++) {
            this.out.print("\t" + toShort(params.get(i).getType()) + "load " + params.get(i).getNumber() + "\n");
        }
        this.out.print("\tinvokestatic " + this.program.getName() + "/" + ci.getFunc().getName() + "(");
        List<Type> args = ci.getFunc().getParams();
        for (int i = 0; i < params.size(); i++) {
            this.out.print(toJasminString(params.get(i).getType()));
        }
        this.out.print(")");
        this.out.print(toJasminString(ci.getFunc().getReturnType()) + "\n");

    }

    public void visit(ConditionalGOTO cGoto) throws IOException {

        this.out.print("\t" + toShort(cGoto.getCond().getType()) + "load " + cGoto.getCond().getNumber() + "\n");
        this.out.print("\tifne L" + cGoto.getLabel().getNumber() + "\n");

    }

    public void visit(Label li) {
        this.out.print("L" + li.getNumber() + ":\n");

    }

    public void visit(OperandAssignment oa) throws IOException {
        this.out.print("\t" + toShort(oa.getRight().getType()) + "load " + oa.getRight().getNumber() + "\n");
        this.out.print("\t" + toShort(oa.getLeft().getType()) + "store " + oa.getLeft().getNumber() + "\n");

    }

    public void visit(PrintAssignment pa) throws IOException {
        this.out.print("\tgetstatic java/lang/System/out Ljava/io/PrintStream;\n");
        this.out.print("\t" + toShort(pa.getTemp().getType()) + "load " + pa.getTemp().getNumber() + "\n");
        this.out.print("\tinvokevirtual java/io/PrintStream/print(" + toJasminString(pa.getTemp().getType()) + ")V\n");

    }

    public void visit(PrintlnAssignment pla) throws IOException {
        this.out.print("\tgetstatic java/lang/System/out Ljava/io/PrintStream;\n");
        this.out.print("\t" + toShort(pla.getTemp().getType()) + "load " + pla.getTemp().getNumber() + "\n");
        this.out.print(
                "\tinvokevirtual java/io/PrintStream/println(" + toJasminString(pla.getTemp().getType()) + ")V\n");
    }

    public void visit(ReturnAssignment ra) throws IOException {
        if (ra.getAssignment() == null) {
            this.out.print("\treturn\n");
        } else {
            this.out.print(
                    "\t" + toShort(ra.getAssignment().getType()) + "load " + ra.getAssignment().getNumber() + "\n");
            this.out.print("\t" + toShort(ra.getAssignment().getType()) + "return\n");
        }

    }

    public void visit(UnaryOpAssignment uo) throws IOException {
        Temp t = uo.getRight();
        this.out.print("\t" + toShort(t.getType()) + "load " + t.getNumber() + "\n");

        if (uo.getOp().equals(UnaryOp.INVERSION)) {
            this.out.print("\tldc 1\n");
            this.out.print("\tixor\n");
            this.out.print("\t" + toShort(uo.getLeft().getType()) + "store " + uo.getLeft().getNumber() + "\n");
        } else if (uo.getOp().equals(UnaryOp.NEGATION)) {
            this.out.print("\t" + toShort(t.getType()) + "neg\n");
            this.out.print("\t" + toShort(uo.getLeft().getType()) + "store " + uo.getLeft().getNumber() + "\n");
        }

    }

    public void visit(UnconditionalGOTO unGoto) throws IOException {
        this.out.print("\tgoto L" + unGoto.getLabel().getNumber() + "\n");

    }

    public void visit(StringConstantAssignment sca) throws IOException {
        this.out.print("\tldc \"" + sca.getVal() + "\"\n");
        this.out.print("\t" + toShort(sca.getTemp().getType()) + "store " + sca.getTemp().getNumber() + "\n");

    }

    public void visit(FloatConstantAssignment fca) throws IOException {
        this.out.print("\tldc " + fca.getVal() + "\n");
        this.out.print("\t" + toShort(fca.getTemp().getType()) + "store " + fca.getTemp().getNumber() + "\n");
    }

    public void visit(IntConstantAssignment ica) throws IOException {
        this.out.print("\tldc " + ica.getVal() + "\n");
        this.out.print("\t" + toShort(ica.getTemp().getType()) + "store " + ica.getTemp().getNumber() + "\n");
    }

    public void visit(CharConstantAssignment cca) throws IOException {
        this.out.print("\tldc " + (int) cca.getVal() + "\n");
        this.out.print("\t" + toShort(cca.getTemp().getType()) + "store " + cca.getTemp().getNumber() + "\n");

    }

    public void visit(BooleanConstantAssignment bca) throws IOException {
        if (bca.getVal()) {
            this.out.print("\tldc 1\n");
        } else {
            this.out.print("\tldc 0\n");
        }
        this.out.print("\t" + toShort(bca.getTemp().getType()) + "store " + bca.getTemp().getNumber() + "\n");

    }

    public String toJasminString(Type t) {

        if (t instanceof FloatType) {
            return "F";
        } else if (t instanceof StringType) {
            return "Ljava/lang/String;";
        } else if (t instanceof VoidType) {
            return "V";
        } else if (t instanceof IntegerType) {
            return "I";
        } else if (t instanceof CharType) {
            return "C";
        } else if (t instanceof BooleanType) {
            return "Z";
        } else if (t instanceof ArrayType) {
            return ("[" + toJasminString(((ArrayType) t).getType()));
        } else {
            return "whoops";
        }

    }

    public String toShort(Type t) {
        if (t instanceof FloatType) {
            return "f";
        } else if (t instanceof StringType) {
            return "a";
        } else if (t instanceof IntegerType) {
            return "i";
        } else if (t instanceof CharType) {
            return "i";
        } else if (t instanceof BooleanType) {
            return "i";
        } else if (t instanceof ArrayType) {
            return "a";
        } else {
            return "whoops";
        }
    }

}