//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"

package com.compilador.analizadorSintactico;

import com.compilador.analizadorLexico.AnalizadorLexico;
import com.compilador.analizadorSintactico.AnalizadorSintactico;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;



//#line 30 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ELSE=257;
public final static short THEN=258;
public final static short ENDIF=259;
public final static short PRINT=260;
public final static short FUNC=261;
public final static short RETURN=262;
public final static short WHILE=263;
public final static short LONG=264;
public final static short BEGIN=265;
public final static short END=266;
public final static short SINGLE=267;
public final static short BREAK=268;
public final static short DO=269;
public final static short CADENA=270;
public final static short ID=271;
public final static short CTE=272;
public final static short IGUAL=273;
public final static short DISTINTO=274;
public final static short MAYORIG=275;
public final static short MENORIG=276;
public final static short CONTRACT=277;
public final static short TRY=278;
public final static short CATCH=279;
public final static short IF=280;
public final static short AND=281;
public final static short OR=282;
public final static short ASIGN=283;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    6,    7,    7,    7,    5,
    5,    3,    3,    3,   13,    8,    8,   14,   14,   14,
   15,   15,   15,   16,   16,   16,   17,   17,   12,   12,
   18,    4,    4,    4,    4,    4,    4,    4,    4,    9,
   19,   19,   21,   21,   21,   21,   22,   22,   22,   22,
   22,   22,   20,   20,   10,   10,   11,   11,
};
final static short yylen[] = {                            2,
    3,    2,    1,    6,    3,    7,    2,    7,    1,    2,
    1,    3,    3,    3,    5,    1,    4,    3,    3,    1,
    3,    3,    1,    1,    1,    1,    4,    3,    6,    5,
    2,    4,    7,    8,    6,    5,    5,    5,    4,    3,
    3,    1,    3,    6,    6,    9,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    3,    1,
};
final static short yydefred[] = {                         0,
    0,    0,   55,   56,    0,    3,    0,    0,    0,    0,
    0,    1,    2,    0,   58,    0,    0,    0,    0,    0,
    0,    0,   11,    0,    0,    0,    0,   12,    0,    0,
   13,   14,    0,    0,    0,    0,    0,    5,   10,    0,
    0,    0,   57,    9,    0,    0,    0,    0,    0,   25,
    0,    0,    0,   23,   26,    0,   42,    0,    0,    0,
    0,    0,   30,    0,    0,   15,    0,    0,    7,    0,
    0,    0,    0,    0,   49,   50,   51,   52,    0,    0,
   47,   48,    0,    0,    0,   53,   54,   40,    0,   39,
    0,   32,    0,    0,   31,   29,    0,    0,   36,   37,
   38,   28,    0,    0,    0,    0,    0,    0,   21,   22,
   41,    0,    0,    0,    4,    0,    0,    0,    0,   27,
    0,    0,    0,    0,   35,    0,    0,    0,    0,    0,
   33,    0,    6,    0,    0,    0,    0,   45,   34,   17,
    8,    0,    0,   46,
};
final static short yydgoto[] = {                          2,
    5,   12,    6,   23,   24,   31,   45,  116,   35,    7,
   16,    8,    9,   52,   53,   54,   55,   65,   56,   89,
   57,   83,
};
final static short yysindex[] = {                      -254,
 -171,    0,    0,    0, -162,    0, -194, -171, -213, -206,
 -206,    0,    0,  -35,    0,  -19, -125,   -7,   32,   57,
 -166,   57,    0, -180, -151,   93, -171,    0, -136, -206,
    0,    0, -127, -152, -132, -152, -117,    0,    0, -119,
  -38,  112,    0,    0, -192,  113,  114,  115,  117,    0,
  118,   -1,   71,    0,    0,  -34,    0, -189,  119,   19,
 -189, -206,    0, -111,  120,    0,  122,  105,    0,  106,
  107,  108,  -18, -124,    0,    0,    0,    0, -124, -124,
    0,    0, -152, -124, -124,    0,    0,    0, -152,    0,
 -124,    0, -224, -176,    0,    0, -152,  124,    0,    0,
    0,    0,  127,   66,   71,   71,  129,   30,    0,    0,
    0,   80, -189,  111,    0,  130,  132,   30,   57,    0,
  -51, -124,  116,  -86,    0,  -92, -124,  135, -152,   81,
    0,  121,    0,   86,  123,  137,   30,    0,    0,    0,
    0, -124,   91,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
    0,    0,  -31,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -21,  -11,    0,  -25,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  138,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -15,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  170,   -5,   10,   36,  125,    0,    0,    0,   -9,    9,
  172,    0,    0,  -28,   70,   67,    0,  110,    0,    0,
   95,   64,
};
final static int YYTABLESIZE=275;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         24,
   24,   24,   63,   24,   27,   24,   88,   60,   82,   20,
   81,   20,   37,   20,   13,   43,    1,   24,   24,   18,
   24,   18,  102,   18,   29,   44,   13,   20,   20,   19,
   20,   19,  113,   19,  114,   42,   29,   18,   18,   28,
   18,   79,   51,   80,   59,  104,   25,   19,   19,   64,
   19,   32,   90,   19,  108,   93,   20,   15,   82,   39,
   81,   79,  112,   80,   21,   44,   14,   19,  118,   67,
   20,   33,   79,   22,   80,   10,   15,   92,   21,   19,
   69,   64,   20,   19,   68,   38,   20,   22,   11,  115,
   21,  107,    3,  130,   21,    4,   34,   51,  134,   22,
  137,    3,   10,   22,    4,  117,  121,  124,   79,  128,
   80,    3,   84,  143,    4,   11,   36,   85,   49,   50,
  123,  138,   79,   79,   80,   80,  140,   40,   79,   39,
   80,  144,   41,   79,   43,   80,   58,  136,    3,   30,
   61,    4,   46,   47,   48,   62,   49,   50,  105,  106,
  109,  110,   66,   70,   71,   72,   73,   74,   91,   95,
   96,   97,   98,  119,   99,  100,  101,  120,  122,  125,
  126,  127,  132,  133,  131,  135,  142,   17,   16,  139,
   18,  141,  103,  111,  129,    0,   94,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   75,   76,   77,   78,    3,    0,    0,    4,    0,
    0,   24,   24,   24,   24,   26,    0,    0,    0,   24,
   24,   20,   20,   20,   20,    3,   86,   87,    4,   20,
   20,   18,   18,   18,   18,   43,   43,    0,    0,   18,
   18,   19,   19,   19,   19,   44,   44,    0,    0,   19,
   19,   75,   76,   77,   78,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   41,   45,   40,   47,   41,   36,   60,   41,
   62,   43,   22,   45,    5,   41,  271,   59,   60,   41,
   62,   43,   41,   45,   44,   41,   17,   59,   60,   41,
   62,   43,  257,   45,  259,   27,   44,   59,   60,   59,
   62,   43,   34,   45,   36,   74,   11,   59,   60,   41,
   62,   59,   58,  260,   83,   61,  263,  271,   60,   24,
   62,   43,   91,   45,  271,   30,  261,  260,   97,  262,
  263,   40,   43,  280,   45,  265,  271,   59,  271,  260,
   45,   73,  263,  260,  277,  266,  263,  280,  278,  266,
  271,   83,  264,  122,  271,  267,   40,   89,  127,  280,
  129,  264,  265,  280,  267,   97,   41,  113,   43,  119,
   45,  264,   42,  142,  267,  278,  283,   47,  271,  272,
   41,   41,   43,   43,   45,   45,   41,  279,   43,   94,
   45,   41,   40,   43,  271,   45,  269,  129,  264,  265,
  258,  267,  270,  271,  272,  265,  271,  272,   79,   80,
   84,   85,   41,   41,   41,   41,   40,   40,   40,  271,
   41,   40,   58,   40,   59,   59,   59,   41,   40,   59,
   41,   40,  259,  266,   59,   41,   40,    8,   41,   59,
    9,   59,   73,   89,  121,   -1,   62,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  273,  274,  275,  276,  264,   -1,   -1,  267,   -1,
   -1,  273,  274,  275,  276,  271,   -1,   -1,   -1,  281,
  282,  273,  274,  275,  276,  264,  281,  282,  267,  281,
  282,  273,  274,  275,  276,  281,  282,   -1,   -1,  281,
  282,  273,  274,  275,  276,  281,  282,   -1,   -1,  281,
  282,  273,  274,  275,  276,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=283;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'",null,"'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"ELSE","THEN","ENDIF","PRINT","FUNC","RETURN",
"WHILE","LONG","BEGIN","END","SINGLE","BREAK","DO","CADENA","ID","CTE","IGUAL",
"DISTINTO","MAYORIG","MENORIG","CONTRACT","TRY","CATCH","IF","AND","OR","ASIGN",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID bloqueDeclarativo bloqueEjecutable",
"bloqueDeclarativo : bloqueDeclarativo sentenciasDeclarativas",
"bloqueDeclarativo : sentenciasDeclarativas",
"bloqueEjecutable : TRY sentEjecutables CATCH BEGIN sentSoloEjecutables END",
"bloqueEjecutable : BEGIN sentSoloEjecutables END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' retorno ')' END",
"sentEjecutableFunc : sentEjecutableFunc sentEjecutables",
"sentEjecutableFunc : sentEjecutableFunc CONTRACT ':' '(' condicion ')' ';'",
"sentEjecutableFunc : sentEjecutables",
"sentSoloEjecutables : sentSoloEjecutables sentEjecutables",
"sentSoloEjecutables : sentEjecutables",
"sentenciasDeclarativas : tipo listaVariables ';'",
"sentenciasDeclarativas : declaracionFunc bloqueDeclarativo bloqueEjecutableFunc",
"sentenciasDeclarativas : encabezadoFunc listaVariables ';'",
"encabezadoFunc : tipo FUNC '(' tipo ')'",
"retorno : expAritmetica",
"retorno : tipo '(' expAritmetica ')'",
"expAritmetica : expAritmetica '+' termino",
"expAritmetica : expAritmetica '-' termino",
"expAritmetica : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : llamadoFunc",
"llamadoFunc : ID '(' parametro ')'",
"llamadoFunc : ID '(' ')'",
"declaracionFunc : tipo FUNC ID '(' parametro ')'",
"declaracionFunc : tipo FUNC ID '(' ')'",
"parametro : tipo ID",
"sentEjecutables : ID ASIGN expAritmetica ';'",
"sentEjecutables : ID ASIGN tipo '(' expAritmetica ')' ';'",
"sentEjecutables : IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';'",
"sentEjecutables : IF condicion THEN bloqueEjecutable ENDIF ';'",
"sentEjecutables : PRINT '(' CADENA ')' ';'",
"sentEjecutables : PRINT '(' ID ')' ';'",
"sentEjecutables : PRINT '(' CTE ')' ';'",
"sentEjecutables : WHILE condicion DO bloqueEjecutable",
"condicion : '(' comparaciones ')'",
"comparaciones : comparaciones opLogico comparacion",
"comparaciones : comparacion",
"comparacion : expAritmetica comparador expAritmetica",
"comparacion : tipo '(' expAritmetica ')' comparador expAritmetica",
"comparacion : expAritmetica comparador tipo '(' expAritmetica ')'",
"comparacion : tipo '(' expAritmetica ')' comparador tipo '(' expAritmetica ')'",
"comparador : '>'",
"comparador : '<'",
"comparador : IGUAL",
"comparador : DISTINTO",
"comparador : MAYORIG",
"comparador : MENORIG",
"opLogico : AND",
"opLogico : OR",
"tipo : LONG",
"tipo : SINGLE",
"listaVariables : listaVariables ',' ID",
"listaVariables : ID",
};

//#line 139 "gramatica.y"


public int yylex() {
    return AnalizadorLexico.yylex();
}

public void yyerror(String string) {
	AnalizadorSintactico.agregarError("Parser: " + string);
}
//#line 368 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 23 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Programa reconocido. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 12:
//#line 48 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 13:
//#line 50 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 14:
//#line 51 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 29:
//#line 84 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 30:
//#line 85 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 32:
//#line 91 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 34:
//#line 93 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");}
break;
case 35:
//#line 94 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 39:
//#line 98 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
//#line 557 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
