grammar ul;

options {backtrack=true;}

@header{
	import AST.*;
	import Types.*;
 }

@members
{
protected void mismatch (IntStream input, int ttype, BitSet follow)
        throws RecognitionException
{
        throw new MismatchedTokenException(ttype, input);
}
public Object recoverFromMismatchedSet (IntStream input,
                                      RecognitionException e,
                                      BitSet follow)
        throws RecognitionException
{
        reportError(e);
        throw e;
}
}

@rulecatch {
        catch (RecognitionException ex) {
                reportError(ex);
                throw ex;
        }
}

/*
 * This is a subset of the ulGrammar to show you how
 * to make new production rules.
 * You will need to:
 *  - change type to be compoundType and include appropriate productions
 *  - introduce optional formalParameters
 *  - change functionBody to include variable declarations and statements 
 */

program returns [Program prog]
@init {
	prog = new Program();
}
:  (f1 = function {prog.add(f1);})+ EOF
	;

function returns [Function func]:
	funcDecl = functionDecl funcBod = functionBody
	{
		func = new Function(funcDecl, funcBod);
	}
	;

functionDecl returns [FunctionDeclaration funcDecl]: 
	comType = compoundType id = identifier OPENPAREN (formParam = formalParameters)? CLOSEPAREN
	{
		funcDecl = new FunctionDeclaration(comType, id, formParam);
		funcDecl.setLine(id.getLine());
		funcDecl.setLine(id.getOffset());
	}
	;
formalParameters returns [FormalParameterList formParams] 
	@init { 
		formParams = new FormalParameterList();
	}
	: (comType = compoundType id = identifier{
		formParam = new FormalParameter(comType, id);
		formParam.setLine(id.getLine());
		formParam.setOffset(id.getOffset());
		formParams.add(formParam);
	
	}) (formParam = moreFormals{ formParams.add(formParam);})* 
	|
		
;
moreFormals returns [FormalParameter formParam]
	: COMMA comType = compoundType id = identifier{
		formParam = new FormalParameter(comType, id);
		formParam.setLine(id.getLine());
		formParam.setOffset(id.getOffset());
	}
	;

functionBody returns [FunctionBody funcBod]
: OPENBRACE varDeclLs = varDeclList statLs = statementList  CLOSEBRACE{
	funcBod = new FunctionBody();
	funcBod.setStatementList(statLs);
	funcBod.setVarDeclList(varDeclLs);
}
	;

varDeclList returns [VariableDeclarationList varDeclLs]
@init { 
	varDeclLs = new VariableDeclarationList();
}
:	(vd = varDecl {varDeclLs.addVarDecl(vd);})*
;

statementList returns [StatementList statLs]
@init {
	statLs = new StatementList();
 }
 : (st = statement { statLs.addStatement(st);})*
;


atom returns [Expression e] :	
		id = identifier {e=id;}|
		lit = literal {e=lit;}|
		ar = arrayExpr {e=ar;}|
		fc = functionCall {e=fc;}|
		pe = parenExpr {e=pe;}

;


arrayExpr returns [ArrayReference ar] : 
	id = identifier OPENBRACKET e = expr CLOSEBRACKET
	{ ar = new ArrayReference(id, e);
	  ar.setLine(id.getLine());
	  ar.setOffset(id.getOffset());}
			;

functionCall returns [FunctionCall fc] : 
	id = identifier OPENPAREN el = exprList CLOSEPAREN
	{fc = new FunctionCall(id, el);
	 fc.setLine(id.getLine());
	 fc.setOffset(id.getOffset());}
		 ;

parenExpr returns [ParenExpression pe] :
	OPENPAREN e = expr CLOSEPAREN
	{pe = new ParenExpression (e);
	 pe.setLine(e.getLine());
	 pe.setOffset(e.getOffset());}
		  ;





statement returns [Statement stat]:
			es =  emptyStatement {stat = es;}|
 			exprs = exprStatement{stat = exprs;}|
			rs =  returnStatement{stat = rs;}|
			ps = printStatement{stat=ps;}|
			printlns = printlnStatement{stat=printlns;}|
			as = assignmentStatement{stat=as;}|
			arrs = arrayStatement{stat=arrs;}|
			is = ifStatement{stat=is;}|
			ws = whileStatement{stat=ws;}
			;

emptyStatement returns [EmptyStatement es] :
			 SEMICOLON
			 { es = new EmptyStatement();}
				;

returnStatement returns [ReturnStatement rs] :
	r = RETURN (e = expr)? SEMICOLON
	{ rs = new ReturnStatement();
	  rs.setExpression(e);
	  rs.setLine(r.getLine());
	  rs.setOffset(r.getCharPositionInLine());
	}
				;


exprStatement returns [ExpressionStatement exprs] :
	e = expr s = SEMICOLON
	{exprs = new ExpressionStatement(e);
	 e.setLine(s.getLine());
	 e.setOffset(s.getCharPositionInLine());}
			;
assignmentStatement returns [AssignmentStatement as]: 
	id = identifier EQUAL e = expr SEMICOLON
	{as = new AssignmentStatement(id, e);
	 as.setLine(id.getLine());
	 as.setOffset(id.getOffset());}
					;

arrayStatement returns [ArrayAssignmentStatement arrs]: 
	id = identifier OPENBRACKET e1 = expr CLOSEBRACKET EQUAL e2 = expr SEMICOLON
	{ arrs = new ArrayAssignmentStatement(id, e1, e2);
	  arrs.setLine(id.getLine());
	  arrs.setOffset(id.getOffset());}
				;

printStatement returns [PrintStatement ps]:
	p = PRINT e = expr SEMICOLON
	{ ps = new PrintStatement(e);
	  ps.setLine(p.getLine());
	  ps.setOffset(p.getCharPositionInLine());}
				;

printlnStatement returns [PrintLnStatement printlns] : 
	p = PRINTLN e = expr SEMICOLON
	{ printlns = new PrintLnStatement(e);
	  printlns.setLine(p.getLine());
	  printlns.setOffset(p.getCharPositionInLine());}
				;


ifStatement returns [IfStatement is] :
        i = IF OPENPAREN e = expr CLOSEPAREN b1 = block ELSE b2 = block
		{ is = new IfStatement(e,b1,b2);
		  is.setLine(i.getLine());
		  is.setOffset(i.getCharPositionInLine());}
        | i = IF OPENPAREN e = expr CLOSEPAREN b = block
		{ is = new IfStatement(e, b);
		  is.setLine(i.getLine());
		  is.setOffset(i.getCharPositionInLine());}
        ;

whileStatement returns [WhileStatement ws] :
	w = WHILE OPENPAREN e = expr CLOSEPAREN b = block
	{ ws = new WhileStatement(e,b);
	  ws.setLine(w.getLine());
	  ws.setOffset(w.getCharPositionInLine());}
				;


block returns [Block b]
	
	: o = OPENBRACE s = statementList CLOSEBRACE
	{ b = new Block();
	  b.setStatementList(s);
	  b.setLine(o.getLine());
	  b.setOffset(o.getCharPositionInLine());}
	;

			




expr returns [Expression e]
	@init{Expression it = null;}
	@after{ e = it;}
	: e1 = lessThanExpr {it=e1;} (eq = EQUALITY e2 =  lessThanExpr
	{it = new EqualityExpression(it, e2);
	 it.setLine(eq.getLine());
	 it.setOffset(eq.getCharPositionInLine());})*
	   ;

lessThanExpr returns [Expression e]
	@init{Expression it = null;}
	@after {e = it;} : e1 = addExpr {it=e1;} (ls = LESSTHAN e2 = addExpr
	{it = new LessThanExpression(it,e2);
	 it.setLine(ls.getLine());
	 it.setOffset(ls.getCharPositionInLine());})*
			;

addExpr returns [Expression e]
	@init{Expression it = null;}
	@after{e = it;}: e1 = multExpr {it=e1;} ((pl = PLUS e2 = multExpr 
	{it = new AddExpression(it, e2);
	it.setLine(pl.getLine());
	it.setOffset(pl.getCharPositionInLine());})|(mi = MINUS e2 = multExpr 
	{it = new SubstractExpression(it,e2);
	it.setLine(mi.getLine());
	it.setOffset(mi.getCharPositionInLine());}))*
		;

multExpr returns [Expression e] 
	@init{Expression it= null;}
	@after{e = it;}: e1 = atom {it = e1;} (mu = MULT e2= atom 
	{it = new MultExpression(it, e2);
	 it.setLine(mu.getLine());
	 it.setOffset(mu.getCharPositionInLine());})*
		;



exprList returns [ExpressionList es]
	@init{es = new ExpressionList();} : (e1 = expr {es.addExpression(e1);}) (COMMA e2 = expr {es.addExpression(e2);})*|
		;





varDecl returns [VariableDeclaration varDecll]
	: comType = compoundType id = identifier s = SEMICOLON {
		varDecll = new VariableDeclaration(comType, id);
		varDecll.setLine(s.getLine());
		varDecll.setOffset(s.getCharPositionInLine());
	}
	;

identifier returns [Identifier id] :
  i =  ID
  {	   
	   IdentifierValue val = new IdentifierValue(i.getText());
	   id = new Identifier(val);
	   id.setLine(i.getLine());
	   id.setOffset(i.getCharPositionInLine()); 
  }
	;

compoundType returns [TypeNode t]:
			 i = type {t = new TypeNode(i);}
	      	| i = type OPENBRACKET val = INT CLOSEBRACKET
			  {
				  Type temp = new ArrayType(i, Integer.parseInt(val.getText()));
				  t = new TypeNode(temp);
			  }
	;
type returns [Type t]:	
i = TYPE
{

	if(i.getText().equals("int")){
		t = new IntegerType();
	}
	else if(i.getText().equals("float")){
		t = new FloatType();
	}
	else if(i.getText().equals("char")){
		t = new CharType();
	}
	else if(i.getText().equals("string")){
		t = new StringType();
	}
	else if(i.getText().equals("boolean")){
		t = new BooleanType();
	}
	else{ 
		t = new VoidType();
	}
}
	;

literal returns [Literal lit]
:		 slit = stringConstant {lit=slit;}|
		 ilit = integerConstant {lit=ilit;}|
		 flit = floatConstant {lit=flit;}|
		 clit = characterConstant {lit=clit;}|
		 blit = booleanConstant {lit=blit;}
		 ;

stringConstant returns [StringLiteral slit]
	: 
	i = STRING
	{
		String val = i.getText();
		val = val.replaceAll("\"", "");
		slit = new StringLiteral(
			val

		);
		slit.setLine(i.getLine());
		slit.setOffset(i.getCharPositionInLine());
	}
	;

integerConstant returns [IntegerLiteral ilit]
	:
	i = INT
	{ 
		ilit = new IntegerLiteral(
			Integer.parseInt(i.getText())

		);
		ilit.setLine(i.getLine());
		ilit.setOffset(i.getCharPositionInLine());
	}
	;

floatConstant returns [FloatLiteral flit]
	:
	i = FLOAT
	{
		flit = new FloatLiteral(
			Float.parseFloat(i.getText())

		);
		flit.setLine(i.getLine());
		flit.setOffset(i.getCharPositionInLine());
	}			 
	;

characterConstant returns [CharacterLiteral clit]
	: 
	i = CHAR
	{
		clit = new CharacterLiteral(
			i.getText().charAt(1)

		);
		clit.setLine(i.getLine());
		clit.setOffset(i.getCharPositionInLine());
	}
	;

booleanConstant returns [BooleanLiteral blit]
	: 
	 i = TRUE 
	{
		blit = new BooleanLiteral(
			true

		);
		blit.setLine(i.getLine());
		blit.setOffset(i.getCharPositionInLine());
	}
	
	|i = FALSE
	{
		blit = new BooleanLiteral(
			false

		);
		blit.setLine(i.getLine());
		blit.setOffset(i.getCharPositionInLine());
	}
					
	; 


/* Lexer */
	 
IF	: 'if'
	;


ELSE	: 'else'
	;

PRINT   : 'print'
	;
WHILE	: 'while'
	;
PRINTLN	: 'println'
	;
RETURN	: 'return'
	;
TRUE	: 'true'
	;
FALSE	: 'false'
	;
COMMA	: ','
	;
SEMICOLON : ';'
	;

EQUAL : '='
	;

LESSTHAN : '<'
	;

PLUS : '+'
	;

MULT : '*'
	;

MINUS : '-'
	;

EQUALITY : '=='
	;

OPENPAREN: '('
	 ;
CLOSEPAREN: ')'
	 ;
OPENBRACE: '{'
	 ;
CLOSEBRACE: '}'
	  ;
OPENBRACKET: '['
	   ;
CLOSEBRACKET: ']'
	   ; 
/* Fixme: add the other types here */
TYPE	: 'int'|
	  'float'|
	  'char'|
	  'string'|
	  'boolean'|
	  'void'
	;


INT	:('0'..'9')+
	;

FLOAT : ('0'..'9')+'.'('0'..'9')+('('('0'..'9')+')')? 
	;

CHAR : '\u0027' ('a'..'z' | 'A'..'Z'|'!'|','|'.'|':'|'_'|'{'|'}'|' ') '\u0027'
	 ;

STRING : '"' ('a'..'z' | 'A'..'Z'|'0'..'9'|'!'|','|'.'|':'|'_'|'{'|'}'|' ')+ '"'
		;


/*
 * FIXME:
 * Change this to match the specification for identifier
 * 
 */
ID	: ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* 
	;

/* These two lines match whitespace and comments 
 * and ignore them.
 * You want to leave these as last in the file.  
 * Add new lexical rules above 
 */
WS      : ( '\t' | ' ' | ('\r' | '\n') )+ { $channel = HIDDEN;}
        ;

COMMENT : '//' ~('\r' | '\n')* ('\r' | '\n') { $channel = HIDDEN;}
		;