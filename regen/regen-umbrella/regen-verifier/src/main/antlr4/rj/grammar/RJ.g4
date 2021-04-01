grammar RJ;


prog: start;
start:
		pred	#start_pred
	|	alias	#start_alias
	|	ghost	#start_ghost
	;

//predicate: | expression ;
//
//expression:
//		'(' expression ')' 
//	|	expression BIN_OP expression predicate
//	|	literalExpression;
//	|	UNARY_OP expression
//	|	expression '?' expression ':' expression;

pred:
		'(' pred ')'				#pred_group
	|	'!' pred					#pred_negate
	|	pred LOGOP pred 			#pred_logic
	|	pred '?' pred ':' pred		#ite
	|	exp							#pred_exp
	;
	
exp: 
		'(' exp ')'
	|	exp BOOLOP exp
	|	operand;

	
operand:
		literalExpression			#op_literal
	|	operand ARITHOP	operand		#op_arith
	|	operand '-'	operand			#op_sub
	|	'-' operand					#op_minus
	|	'!' operand					#op_not
	;

	 
literalExpression:
		'(' literalExpression ')'	#lit_group
	|	literal						#lit
	| 	ID 							#var
	|	ID '.' functionCall			#taget_invocation
	|	functionCall				#invocation
	;
	
 functionCall:
 		ghostCall
 	|	aliasCall;
 	
ghostCall:
 	ID '(' args ')';
 
aliasCall:
	ID_UPPER '(' args ')';

args:	pred multipleArgs; 

multipleArgs:
		',' args 
	|	;
 
literal: 
		BOOL
	|	STRING
	|	INT
	|	REAL;
	
//----------------------- Alias -----------------------	

alias:
	'type'? ID_UPPER '(' argDeclID ')' '{' pred '}';


ghost: 
	'ghost'? type ID '(' argDecl ')';

argDecl:
	type ID? argDecl2;	
	
argDeclID:
	type ID argDecl2;
	
argDecl2:
	',' argDecl | ;
	
type:
		'int'
	|	'double' 
	|	'float'
	|	'boolean'
	|	ID_UPPER
	|	OBJECT_TYPE;  


////UNARY_OP: '!' | '-' | '+';
LOGOP   : '&&'|'||'| '-->';
BOOLOP	 : '=='|'!='|'>='|'>'|'<='|'<';
ARITHOP : '+'|'*'|'/'|'%';//|'-';

BOOL    : 'true' | 'false';
ID_UPPER: ([A-Z][a-zA-Z0-9]*);
OBJECT_TYPE: 
		  (([a-zA-Z][a-zA-Z0-9]+) ('.' [a-zA-Z][a-zA-Z0-9]*)+);
ID     	: '#'*[a-zA-Z_][a-zA-Z0-9_]*;
STRING  : '"'(~["])*'"';
INT     : 	(([0-9]+) |	([0-9]+('_'[0-9]+)*));
REAL   	: (([0-9]+('.'[0-9]+)?) | '.'[0-9]+);
  
WS		:  (' '|'\t'|'\n'|'\r')+ -> channel(HIDDEN);
