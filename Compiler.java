/*
 * Compiler.java
 *
 * A starting place for the unnamed language compiler for CSC 435/535
 *
 */

import org.antlr.runtime.*;
import java.io.*;
import AST.*;
import TypeCheck.*;
import IR.*;
import Codegen.*;

public class Compiler {
	public static void main(String[] args) throws Exception {
		ANTLRInputStream input;

		if (args.length == 0) {
			System.out.println("Usage: Compiler filename.ul");
			return;
		} else {
			input = new ANTLRInputStream(new FileInputStream(args[0]));
		}

		// The name of the grammar here is "ulNoActions",
		// so ANTLR generates ulNoActionsLexer and ulNoActionsParser
		ulLexer lexer = new ulLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ulParser parser = new ulParser(tokens);

		Visitor v = new PrintVisitor();
		TypeVisitor tCheck = new TypeCheckVisitor();

		String filename = args[0];
		int startIndex = filename.lastIndexOf('/');
		int endIndex = filename.lastIndexOf('.');
		filename = filename.substring(startIndex + 1, endIndex);

		IrVisitor ir = new IrGen();

		ir.setProgramName(filename);

		try {

			Program p = parser.program();
			// v.visit(p);
			tCheck.visit(p);
			ir.visit(p);
			ProgramIR pIR = ir.getProgram();
			CodegenVisitor c = new CodegenVisitor(pIR);
			c.genCode();

		} catch (RecognitionException e) {
			// A lexical or parsing error occured.
			// ANTLR will have already printed information on the
			// console due to code added to the grammar. So there is
			// nothing to do here.
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
