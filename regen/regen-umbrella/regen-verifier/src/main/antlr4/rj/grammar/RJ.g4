grammar RJ;


prog: start;
start:
		predicate
	|	'type'? alias_1
	|	'ghost'? ghost;

predicate:
		expression (CONJ_OP predicate)?
	|	predicate '?' predicate ':' predicate (CONJ_OP predicate)? 
	|	'(' predicate ')';
	 
expression: 
		'(' expression ')' 
	|	operand BOOL_OP	operand;
	 
operand:
		literal
	| 	VAR
	|	operand ARITH_OP operand 
	|	UNARY_OP operand
	|	functionCall
	|	VAR '.' functionCall
	|	'(' operand ')';
	

	
functionCall:  
		VAR '(' args? ')' ;
	
args:	operand multipleArgs; 

multipleArgs:
		',' args 
	|	;
 
literal: 
		BOOL
	|	STRING
	|	INT
	|	REAL;
	
alias_1:
	ALIAS_ID '(' argDecl ')' '{' predicate '}';


ghost: 
	type VAR '(' argDecl ')';
	
argDecl:
	type VAR argDecl2;
	
argDecl2:
	',' argDecl | ;
	
type:
		'int'
	|	'double'
	|	'float'
	|	OBJECT_TYPE;  

CONJ_OP: '&&' | '||' | '-->';
UNARY_OP: '!' | '-' | '+';
BOOL_OP	: '=='|'!='|'>='|'>'|'<='|'<';
ARITH_OP: '+'|'-'|'*'|'/'|'%';
BOOL    : 'true' | 'false';
VAR     : '#'*[a-zA-Z_][a-zA-Z0-9_]*;
STRING  : '"'(~["])*'"';
INT     : (([0-9]+) |([0-9]+('_'[0-9]+)*));
REAL   	: (([0-9]+('.'[0-9]+)?) | '.'[0-9]+);
ALIAS_ID: ([A-Z][a-zA-Z0-9]+) ;
OBJECT_TYPE
		: ([A-Z][a-zA-Z0-9]*) 
		| (([a-zA-Z][a-zA-Z0-9]+) ('.' [a-zA-Z][a-zA-Z0-9])+); 
WS		: [ \n\r]+ -> channel(HIDDEN);
