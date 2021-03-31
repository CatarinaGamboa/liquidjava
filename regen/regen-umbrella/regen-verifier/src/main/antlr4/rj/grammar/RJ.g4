grammar RJ;


prog: start;
start:
		pred;
//	|	'type' alias
//	|	'ghost'? ghost;

//predicate: | expression ;
//
//expression:
//		'(' expression ')' 
//	|	expression BIN_OP expression predicate
//	|	literalExpression;
//	|	UNARY_OP expression
//	|	expression '?' expression ':' expression;

pred:
		'(' pred ')'
	|	'!' exp pred
	|	pred LOGOP pred
	|	exp
	|;
	
exp: 
		'(' exp ')'
	|	exp BOOLOP exp
	|	operand;
//	|	ite
	
operand:
	|	literalExpression
	|	operand ARITHOP	operand
	|	operand '-'	operand
	|	'-' operand;

	 
literalExpression:
		'(' literalExpression ')'
	|	literal
	| 	ID followVar
	|	functionCall;
	
 followVar:
 	|	'.' functionCall
 	|	; 
 	
 functionCall:
 	ID '(' args ')';	

args:	pred multipleArgs; 

multipleArgs:
		',' args 
	|	;
 
literal: 
		BOOL
	|	STRING
	|	INT
	|	REAL;
//	
//alias:
//	ALIAS_ID '(' argDecl ')' '{' expression '}';
//
//
//ghost: 
//	type VAR '(' argDecl ')';
//	
//argDecl:
//	type VAR argDecl2;
//	
//argDecl2:
//	',' argDecl | ;
//	
//type:
//		'int'
//	|	'double' 
//	|	'float'
//	|	OBJECT_TYPE;  


////UNARY_OP: '!' | '-' | '+';
LOGOP   : '&&'|'||'| '-->';
BOOLOP	 : '=='|'!='|'>='|'>'|'<='|'<';
ARITHOP : '+'|'*'|'/'|'%';//|'-';
//BIN_OP   : '&&'|'||'|'=='|'!='|'>='|'>'|'<='|
//          '<'|'+'|'*'|'/'|'%';//|'-';

//BIN_OP	: '&&' | '||' | '-->'|'=='|'!='|'>='|'>'|'<='|'<'|'+'|'-'|'*'|'/'|'%';
BOOL    : 'true' | 'false';
ID     : '#'*[a-zA-Z_][a-zA-Z0-9_]*;
STRING  : '"'(~["])*'"';
INT     : 	(([0-9]+) |	([0-9]+('_'[0-9]+)*));
REAL   	: (([0-9]+('.'[0-9]+)?) | '.'[0-9]+);
OBJECT_TYPE 
		: ([A-Z][a-zA-Z0-9]*) | (([a-zA-Z][a-zA-Z0-9]+) ('.' [a-zA-Z][a-zA-Z0-9])+); 
ALIAS_ID: ([A-Z][a-zA-Z0-9]+) ; 
WS		:  (' '|'\t'|'\n'|'\r')+ -> channel(HIDDEN);
