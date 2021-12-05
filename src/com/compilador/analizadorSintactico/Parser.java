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
import com.compilador.analizadorLexico.TDSObject;
import com.compilador.analizadorSintactico.AnalizadorSintactico;
import com.compilador.arbolSintactico.Nodo;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;



//#line 32 "Parser.java"




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
    0,    1,    1,    2,    2,    4,    4,    4,    4,    4,
    4,    8,    8,    8,    8,    5,    5,    5,   11,   11,
   11,   11,   11,   11,   11,   11,   10,   10,   12,   12,
   13,   13,   13,   13,   13,   13,    7,    7,    3,    3,
    3,   15,   15,   15,   15,   15,   19,   19,   19,   16,
   16,   17,   17,   17,   21,   21,   21,   21,   21,    9,
    9,   22,   22,   22,   23,   23,   23,   24,   24,   24,
   24,   25,   25,   20,   20,   20,   20,   20,   20,   26,
   26,    6,    6,    6,    6,   27,   27,   27,   27,   27,
   27,   27,   27,   27,   27,   28,   28,   28,   28,   28,
   28,   28,   28,   28,   28,   28,   28,   28,   28,   29,
   29,   29,   29,   29,   29,   29,   29,   30,   30,   30,
   30,   30,   14,   32,   32,   32,   31,   31,   31,   31,
   31,   31,   34,   34,   34,   34,   34,   34,   34,   33,
   33,   18,   18,
};
final static short yylen[] = {                            2,
    3,    2,    1,    1,    1,    6,    5,    5,    5,    5,
    6,    8,    9,   10,    9,    3,    2,    3,    8,    7,
    1,    6,    6,    6,    6,    6,    2,    1,    1,    1,
    4,    5,    5,    5,    5,    6,    2,    1,    1,    1,
    1,    3,    2,    3,    2,    2,    3,    1,    2,    3,
    2,    3,    2,    3,    5,    4,    4,    4,    5,    1,
    4,    3,    3,    1,    3,    3,    1,    1,    1,    2,
    1,    4,    3,    6,    5,    5,    5,    5,    6,    2,
    1,    1,    1,    1,    1,    4,    4,    7,    3,    3,
    4,    3,    4,    3,    4,    8,    6,    7,    7,    7,
    7,    7,    7,    8,    5,    5,    5,    5,    6,    5,
    4,    4,    4,    4,    5,    5,    5,    4,    3,    3,
    3,    4,    3,    3,    1,    3,    3,    6,    6,    9,
    3,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,  142,  143,    0,    0,    3,   39,   40,
   41,    0,    0,    0,    0,    0,    0,   45,    0,    0,
    0,    0,    0,    0,    0,    0,    1,    2,    4,    5,
    0,    0,    0,   82,   83,   84,   85,   46,    0,    0,
    0,   43,    0,    0,    0,    0,   29,   21,    0,   51,
   28,   30,   48,   53,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   69,    0,    0,    0,
    0,    0,   67,   71,    0,    0,    0,    0,    0,    0,
  135,  136,  137,  138,  139,  133,  134,    0,    0,  125,
    0,    0,    0,   37,   17,    0,    0,    0,    0,    0,
    0,   44,   42,   47,    0,    0,    0,    0,    0,    0,
   50,    0,   27,   54,   52,   81,    0,    0,   56,    0,
    0,    0,    0,    0,  120,    0,  121,   18,   16,    0,
   92,    0,    0,    0,   70,   94,    0,   90,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   89,    0,
    0,    0,  140,  141,  123,    0,    0,    0,    0,  119,
    0,    0,   58,    0,   57,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   80,   76,  112,    0,
  114,    0,    0,  113,  122,  118,    0,   73,    0,   91,
   86,   95,   93,   87,    0,    0,   65,   66,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  111,
    0,  131,    0,    0,  126,  124,    0,    0,   75,    0,
   78,   59,   55,   77,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   31,    0,  115,  110,  116,  117,   72,    0,    8,    9,
    0,   10,    0,  105,    0,  107,    0,    0,  108,    0,
  106,    0,    0,    7,    0,   79,   74,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   32,   34,
    0,   35,   33,    0,    0,   11,    6,    0,    0,    0,
    0,  109,   97,    0,    0,    0,    0,    0,    0,    0,
    0,   25,   26,    0,   24,   23,    0,    0,    0,   36,
   22,   88,   99,  101,  102,    0,  103,  100,    0,    0,
  129,   98,   20,   61,    0,    0,    0,    0,    0,    0,
  104,   96,    0,   12,    0,   19,    0,    0,    0,   15,
   13,    0,  130,   14,
};
final static short yydgoto[] = {                          2,
    7,   27,    8,   29,   30,   31,   32,   48,  225,   49,
   50,   51,   52,   33,    9,   10,   11,  226,   13,   14,
   15,  227,   72,   73,   74,  118,   34,   35,   36,   37,
   90,   91,  156,   92,
};
final static short yysindex[] = {                      -208,
 -147,    0,  -34,    0,    0, -184,  -35,    0,    0,    0,
    0, -148,   16,   -8,  -36,   40, -145,    0,  -24,    2,
  210,  -32,  178,  -39,   20,  335,    0,    0,    0,    0,
  183, -171, -202,    0,    0,    0,    0,    0,  -33,   75,
  -23,    0, -135,   24,   -7,   -8,    0,    0,  105,    0,
    0,    0,    0,    0,   62, -127,  102,  110,    9,  121,
  527,   34,   65,  210, -170,  119,    0,  361, -112,   60,
   48,   37,    0,    0, -164, -213,  121,   50,   73,  124,
    0,    0,    0,    0,    0,    0,    0,  129,  499,    0,
    8,   20,  -85,    0,    0,  121,  121,  181,   42,  144,
 -127,    0,    0,    0,  155,  131,  -75,   63,  160,  165,
    0,  168,    0,    0,    0,    0,  -59,  169,    0,  163,
  -16,  174,  259,  260,    0,   64,    0,    0,    0,  -13,
    0,  298,  446,  290,    0,    0,   66,    0,   20,   20,
   20,   20,  210,  210,  204,  -73,    5,  -61,    0,  293,
   20,  386,    0,    0,    0,  513,  156,  210,   96,    0,
  201,  320,    0,  -29,    0,  322,  355,  330,  147,  319,
  335,  157,  336,  492,   12,  355,    0,    0,    0,  -42,
    0,  301,  324,    0,    0,    0,  338,    0,   20,    0,
    0,    0,    0,    0,   37,   37,    0,    0,  132,  137,
  210,  139,  121,  328,  121,  331,  -50,  121,  353,    0,
  113,    0,  349,  156,    0,    0,  150,  121,    0,   18,
    0,    0,    0,    0,  372,  375,  156,  355,  392,  384,
  385,  398,  402,  499,  407,  173,  396,  400,   17,  401,
    0,  410,    0,    0,    0,    0,    0,  123,    0,    0,
 -168,    0,  212,    0,  215,    0,   91,  -40,    0,  220,
    0,  569,   20,    0,  224,    0,    0,  426,   20,  445,
  355,  222,   27,  226,  233,   20,  355,  461,    0,    0,
  247,    0,    0,  238,  534,    0,    0,  451,  453,  455,
  -48,    0,    0,  456,  355,  133,  462,  257,  136,  466,
  485,    0,    0,  475,    0,    0,  148,  495,  355,    0,
    0,    0,    0,    0,    0,   26,    0,    0,  503,  156,
    0,    0,    0,    0,  279,  487,  281,  569,  489,  508,
    0,    0,   20,    0,  284,    0,  285,  496,  149,    0,
    0,  296,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,   33,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   33,    0,    0,    0,    0,    0,    0,    0,    0,
  297,    0,    0,    0,    0,    0,    0,    0,    0,   83,
    0,    0,   89,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -128,    0,  435,    0,    0,    0,    0,
    0,  457,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   95,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -21,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  468,  479,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -15,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   32,    0,    0,    0,
    0,    0,    0,  515,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   -4,
    0,    0,    0,    0,    0,    0,    0,  523,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  551,  610,   15,  -30,    0,  783,  536,    0,  -89,   -9,
  520,  -19,    0,  511,    0,    0,    0,    1,  158,    0,
    0,  -22,    7,   61,    0,  -67,    0,    0,    0,    0,
  -44,    0,    0,  -79,
};
final static int YYTABLESIZE=1019;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         71,
   61,   12,   79,   89,   26,   17,   99,   12,  259,  152,
  317,  223,   69,  106,   12,   59,  244,   57,  293,  132,
   43,   28,   54,  134,  180,  127,   88,  188,   70,  113,
  162,   26,  109,  166,  108,  103,  128,  132,   89,  100,
   71,   61,  181,  127,   26,  133,   12,  137,  155,  124,
  110,  144,  240,  152,  128,   96,  117,  281,  267,   43,
   28,   88,    1,   26,   69,  145,   97,  304,  132,  157,
  241,   18,   60,   26,   42,  282,   48,  172,  141,   56,
  231,  232,  163,  142,  332,  128,  242,  286,  113,   26,
  139,   48,  140,  220,   95,  129,  169,  287,  117,  164,
  143,  117,  171,   26,   69,   43,  138,   38,  139,   69,
  140,  216,   39,    3,  101,  139,    4,  140,    4,    5,
  115,    5,   40,    6,  194,   70,   48,   38,  211,  214,
   26,  149,   49,   89,   29,  104,    4,   38,  270,    5,
  273,   48,  119,  116,   26,  195,  196,   49,  234,  113,
  120,   89,  213,  262,  152,  139,   88,  140,  130,  135,
   26,  117,  236,  285,  150,  139,  248,  140,  151,   41,
   26,  233,   55,  321,   88,  139,  324,  140,  139,  158,
  140,  301,  295,  203,  165,  204,   26,  308,  328,  343,
  139,  139,  140,  140,  167,  208,   26,  209,  139,   61,
  140,  197,  198,   93,  174,  295,  257,  176,  258,  178,
  316,  177,   26,  243,  182,  292,  113,   26,   77,  330,
  161,  179,   26,   18,   19,    3,  222,   20,    4,   21,
    4,    5,  102,    5,   53,   22,   16,   98,   66,   67,
  296,  219,   23,   26,   24,   58,  299,   25,  295,   26,
   68,   19,    3,  307,   20,    4,   44,  187,    5,  132,
  132,  205,   22,  206,   19,  127,  127,   20,   45,   21,
   60,   24,  320,  266,   25,   63,  128,  128,  121,  122,
  123,  331,   23,   19,   24,  105,   20,   25,  153,  154,
   66,   67,  303,   19,   63,  319,   20,   60,   21,  183,
   45,   23,  126,   24,   63,    4,   25,  147,    5,   19,
  339,   23,   20,   24,   21,  136,   25,  114,  184,  185,
   63,  193,   19,   19,  170,   20,   20,   23,   21,   24,
   66,   67,   25,   63,   63,   66,   67,  189,   48,   45,
   23,   23,   24,   24,   49,   25,   25,   68,  192,  290,
   19,  210,  218,   20,   29,   21,   29,   29,  230,  245,
  221,   63,  224,   69,   19,   29,  112,   20,   23,  228,
   24,   29,   29,   25,   29,   63,  237,   29,  247,   69,
   19,   45,  246,   20,   24,   21,  254,   25,  263,  256,
   19,   63,  168,   20,   87,   85,   86,  249,   23,   69,
   24,   63,  250,   25,  252,   69,   19,   45,  229,   20,
   24,  261,  268,   25,  269,  264,   19,   63,  235,   20,
   87,   85,   86,   45,  272,  274,   24,   63,   69,   25,
   69,  271,   19,   45,  278,   20,   24,   19,  275,   25,
   20,  276,   19,   63,    4,   20,  277,    5,   63,   45,
  284,  116,   24,   63,  279,   25,   75,   24,  280,  283,
   25,   93,   24,   19,    4,   25,   20,    5,  201,   19,
  288,  116,   20,  289,   63,   68,   68,   68,  294,   68,
   63,   68,  297,   24,  298,  300,   25,  302,  139,   24,
  140,  305,   25,   68,   68,   68,   68,   64,  306,   64,
  309,   64,  310,  311,  191,   87,   85,   86,   62,  313,
   62,  314,   62,  315,  318,   64,   64,   64,   64,   63,
  322,   63,  323,   63,  325,  326,   62,   62,   62,   62,
   62,   61,  238,  327,   78,  329,   69,   63,   63,   63,
   63,  139,  333,  140,  334,  335,  336,  337,  338,  340,
  341,   87,   85,   86,  342,   60,   65,   69,   87,   85,
   86,  344,   38,   61,   46,  111,   94,    0,    0,    0,
    0,   69,   87,   85,   86,    0,    0,    0,    0,    0,
    0,    0,    4,    0,    0,    5,   87,   85,   86,   66,
   67,    0,  312,   87,   85,   86,    0,    0,    4,   94,
    0,    5,    0,    0,   80,   66,   67,   81,   82,   83,
   84,    0,    0,    0,    0,    0,  131,    0,    4,  173,
  175,    5,    0,    0,    4,   66,   67,    5,   87,   85,
   86,   66,   67,   81,   82,   83,   84,    0,    0,    0,
    0,  212,    0,    0,    0,    0,    0,    4,    0,    4,
    5,    0,    5,    0,   66,   67,   66,   67,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  125,
    0,  127,    0,    0,    0,    0,    0,    0,  199,  200,
  202,    0,    0,    0,  239,    0,  146,  148,    0,    0,
   68,    0,    0,  217,    0,    0,    0,    0,    0,    0,
   68,  190,    0,    0,    0,  159,  160,   68,   68,   68,
   68,    0,   64,    0,    0,   68,   68,    0,   81,   82,
   83,   84,   64,   62,    0,    0,    0,    0,    0,   64,
   64,   64,   64,   62,   63,  186,  251,   64,   64,    0,
   62,   62,   62,   62,   63,    0,    0,    0,   62,   62,
    0,   63,   63,   63,   63,    4,  207,    0,    5,   63,
   63,    0,   66,   67,   81,   82,   83,   84,  215,    0,
    0,   81,   82,   83,   84,    0,    4,    0,    0,    5,
    0,    0,    0,   66,   67,   81,   82,   83,   84,    0,
    4,    0,    0,    5,    0,    0,   47,   66,   67,   81,
   82,   83,   84,   64,    0,   76,   81,   82,   83,   84,
    0,    0,  253,   64,  255,    0,    0,  260,    0,    0,
    0,    0,    0,    0,    0,    0,  107,  265,   47,    0,
    0,   47,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   81,   82,   83,   84,    0,   64,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  291,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   47,    0,
  107,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   64,   64,   64,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   64,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   47,    0,    0,   47,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   64,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   47,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         22,
   40,    1,   25,   26,   40,   40,   40,    7,   59,   89,
   59,   41,   45,   44,   14,   40,   59,   17,   59,   41,
   44,    7,   59,   68,   41,   41,   26,   41,   61,   49,
   98,   40,   40,  101,   44,   59,   41,   59,   61,   39,
   63,   40,   59,   59,   40,   68,   46,   70,   41,   41,
   58,  265,   41,  133,   59,  258,   56,   41,   41,   44,
   46,   61,  271,   40,   45,  279,  269,   41,   68,   92,
   59,  256,   41,   40,   59,   59,   44,  108,   42,   40,
  170,  171,   41,   47,   59,  256,  176,  256,  108,   40,
   43,   59,   45,  161,  266,  266,  106,  266,   98,   99,
  265,  101,   40,   40,   45,   44,   59,  256,   43,   45,
   45,  156,  261,  261,   40,   43,  264,   45,  264,  267,
   59,  267,  271,  271,   59,   61,   44,  256,  151,  152,
   40,   59,   44,  156,   40,  271,  264,  266,  228,  267,
  230,   59,   41,  271,   40,  139,  140,   59,  171,  169,
   41,  174,  152,   41,  234,   43,  156,   45,   40,  272,
   40,  161,  172,   41,   41,   43,  189,   45,   40,   12,
   40,  171,   15,   41,  174,   43,   41,   45,   43,  265,
   45,  271,  262,  257,   41,  259,   40,  277,   41,   41,
   43,   43,   45,   45,   40,  257,   40,  259,   43,   40,
   45,  141,  142,  279,   40,  285,  257,   40,  259,   41,
  259,  271,   40,  256,   41,  256,  236,   40,  258,  309,
   40,   59,   40,  256,  260,  261,  256,  263,  264,  265,
  264,  267,  256,  267,  271,  271,  271,  271,  271,  272,
  263,   41,  278,   40,  280,  270,  269,  283,  328,   40,
  283,  260,  261,  276,  263,  264,  265,  271,  267,  281,
  282,  257,  271,  259,  260,  281,  282,  263,  277,  265,
  269,  280,  295,  256,  283,  271,  281,  282,  270,  271,
  272,  256,  278,  260,  280,  262,  263,  283,  281,  282,
  271,  272,  266,  260,  271,  295,  263,  266,  265,   41,
  277,  278,  269,  280,  271,  264,  283,  258,  267,  260,
  333,  278,  263,  280,  265,  256,  283,  256,   59,  256,
  271,  256,  260,  260,  262,  263,  263,  278,  265,  280,
  271,  272,  283,  271,  271,  271,  272,   40,  256,  277,
  278,  278,  280,  280,  256,  283,  283,  283,   59,  259,
  260,   59,  257,  263,  260,  265,  262,  263,   40,   59,
   41,  271,   41,   45,  260,  271,  262,  263,  278,   40,
  280,  277,  278,  283,  280,  271,   41,  283,   41,   45,
  260,  277,   59,  263,  280,  265,   59,  283,   40,   59,
  260,  271,  262,  263,   60,   61,   62,  266,  278,   45,
  280,  271,  266,  283,  266,   45,  260,  277,  262,  263,
  280,   59,   41,  283,   40,  266,  260,  271,  262,  263,
   60,   61,   62,  277,   41,   41,  280,  271,   45,  283,
   45,   40,  260,  277,  262,  263,  280,  260,   41,  283,
  263,   40,  260,  271,  264,  263,   40,  267,  271,  277,
   41,  271,  280,  271,   59,  283,  279,  280,   59,   59,
  283,  279,  280,  260,  264,  283,  263,  267,  265,  260,
  259,  271,  263,  259,  271,   41,   42,   43,  259,   45,
  271,   47,  259,  280,   59,   41,  283,  266,   43,  280,
   45,  266,  283,   59,   60,   61,   62,   41,  266,   43,
   40,   45,  256,  266,   59,   60,   61,   62,   41,   59,
   43,   59,   45,   59,   59,   59,   60,   61,   62,   41,
   59,   43,  266,   45,   59,   41,   59,   60,   61,   62,
   20,   40,   41,   59,   24,   41,   45,   59,   60,   61,
   62,   43,   40,   45,  266,   59,  266,   59,   41,  266,
  266,   60,   61,   62,   59,   41,   21,   45,   60,   61,
   62,  266,  266,   41,   14,   46,   31,   -1,   -1,   -1,
   -1,   45,   60,   61,   62,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  264,   -1,   -1,  267,   60,   61,   62,  271,
  272,   -1,   59,   60,   61,   62,   -1,   -1,  264,   64,
   -1,  267,   -1,   -1,  270,  271,  272,  273,  274,  275,
  276,   -1,   -1,   -1,   -1,   -1,  256,   -1,  264,  109,
  110,  267,   -1,   -1,  264,  271,  272,  267,   60,   61,
   62,  271,  272,  273,  274,  275,  276,   -1,   -1,   -1,
   -1,  256,   -1,   -1,   -1,   -1,   -1,  264,   -1,  264,
  267,   -1,  267,   -1,  271,  272,  271,  272,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   60,
   -1,   62,   -1,   -1,   -1,   -1,   -1,   -1,  143,  144,
  145,   -1,   -1,   -1,  174,   -1,   77,   78,   -1,   -1,
  256,   -1,   -1,  158,   -1,   -1,   -1,   -1,   -1,   -1,
  266,  256,   -1,   -1,   -1,   96,   97,  273,  274,  275,
  276,   -1,  256,   -1,   -1,  281,  282,   -1,  273,  274,
  275,  276,  266,  256,   -1,   -1,   -1,   -1,   -1,  273,
  274,  275,  276,  266,  256,  126,  201,  281,  282,   -1,
  273,  274,  275,  276,  266,   -1,   -1,   -1,  281,  282,
   -1,  273,  274,  275,  276,  264,  147,   -1,  267,  281,
  282,   -1,  271,  272,  273,  274,  275,  276,  256,   -1,
   -1,  273,  274,  275,  276,   -1,  264,   -1,   -1,  267,
   -1,   -1,   -1,  271,  272,  273,  274,  275,  276,   -1,
  264,   -1,   -1,  267,   -1,   -1,   14,  271,  272,  273,
  274,  275,  276,   21,   -1,   23,  273,  274,  275,  276,
   -1,   -1,  203,   31,  205,   -1,   -1,  208,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   44,  218,   46,   -1,
   -1,   49,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  273,  274,  275,  276,   -1,   64,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  106,   -1,
  108,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  143,  144,  145,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  158,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  169,   -1,   -1,  172,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  201,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  236,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=283;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
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
"bloqueEjecutable : bloqueTRYCATCH",
"bloqueEjecutable : bloqueEjecutableNormal",
"bloqueTRYCATCH : TRY sentEjecutables CATCH BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : sentEjecutables CATCH BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : TRY CATCH BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : TRY sentEjecutables BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : TRY sentEjecutables CATCH sentSoloEjecutables END",
"bloqueTRYCATCH : TRY sentEjecutables CATCH BEGIN sentSoloEjecutables error",
"TRYCATCHFunc : BEGIN bloqueTRYCATCH RETURN '(' retorno ')' ';' END",
"TRYCATCHFunc : BEGIN sentEjecutableFunc bloqueTRYCATCH RETURN '(' retorno ')' ';' END",
"TRYCATCHFunc : BEGIN sentEjecutableFunc bloqueTRYCATCH sentEjecutableFunc RETURN '(' retorno ')' ';' END",
"TRYCATCHFunc : BEGIN bloqueTRYCATCH sentEjecutableFunc RETURN '(' retorno ')' ';' END",
"bloqueEjecutableNormal : BEGIN sentSoloEjecutables END",
"bloqueEjecutableNormal : sentSoloEjecutables END",
"bloqueEjecutableNormal : BEGIN sentSoloEjecutables error",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' retorno ')' ';' END",
"bloqueEjecutableFunc : BEGIN RETURN '(' retorno ')' ';' END",
"bloqueEjecutableFunc : TRYCATCHFunc",
"bloqueEjecutableFunc : sentEjecutableFunc RETURN '(' retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc '(' retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' retorno END",
"sentEjecutableFunc : sentEjecutableFunc sentEjecutablesFunc",
"sentEjecutableFunc : sentEjecutablesFunc",
"sentEjecutablesFunc : sentEjecutables",
"sentEjecutablesFunc : sentenciaCONTRACT",
"sentenciaCONTRACT : CONTRACT ':' condicion ';'",
"sentenciaCONTRACT : CONTRACT '(' condicion ')' ';'",
"sentenciaCONTRACT : CONTRACT ':' condicion ')' ';'",
"sentenciaCONTRACT : CONTRACT ':' '(' ')' ';'",
"sentenciaCONTRACT : CONTRACT ':' '(' condicion ';'",
"sentenciaCONTRACT : CONTRACT ':' '(' condicion ')' error",
"sentSoloEjecutables : sentEjecutables sentSoloEjecutables",
"sentSoloEjecutables : sentEjecutables",
"sentenciasDeclarativas : declaraVariable",
"sentenciasDeclarativas : declaraFunc",
"sentenciasDeclarativas : declaraVarFunc",
"declaraVariable : tipo listaVariables ';'",
"declaraVariable : listaVariables ';'",
"declaraVariable : tipo listaVariables error",
"declaraVariable : ID error",
"declaraVariable : tipo error",
"listaVariables : listaVariables ',' ID",
"listaVariables : ID",
"listaVariables : listaVariables ','",
"declaraFunc : declaracionFunc bloqueDeclarativo bloqueEjecutableFunc",
"declaraFunc : declaracionFunc bloqueEjecutableFunc",
"declaraVarFunc : encabezadoFunc listaVariables ';'",
"declaraVarFunc : encabezadoFunc ';'",
"declaraVarFunc : encabezadoFunc listaVariables error",
"encabezadoFunc : tipo FUNC '(' tipo ')'",
"encabezadoFunc : FUNC '(' tipo ')'",
"encabezadoFunc : tipo FUNC tipo ')'",
"encabezadoFunc : tipo FUNC '(' ')'",
"encabezadoFunc : tipo FUNC '(' tipo error",
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
"factor : '-' CTE",
"factor : llamadoFunc",
"llamadoFunc : ID '(' ID ')'",
"llamadoFunc : ID '(' ')'",
"declaracionFunc : tipo FUNC ID '(' parametro ')'",
"declaracionFunc : tipo FUNC ID '(' ')'",
"declaracionFunc : FUNC ID '(' parametro ')'",
"declaracionFunc : tipo ID '(' parametro ')'",
"declaracionFunc : tipo FUNC ID parametro ')'",
"declaracionFunc : tipo FUNC ID '(' parametro error",
"parametro : tipo ID",
"parametro : ID",
"sentEjecutables : asignacion",
"sentEjecutables : sentenciaIF",
"sentEjecutables : sentenciaPRINT",
"sentEjecutables : sentenciaWHILE",
"asignacion : ID ASIGN expAritmetica ';'",
"asignacion : ID '=' expAritmetica ';'",
"asignacion : ID ASIGN tipo '(' expAritmetica ')' ';'",
"asignacion : ASIGN expAritmetica ';'",
"asignacion : ID expAritmetica ';'",
"asignacion : ID ASIGN expAritmetica error",
"asignacion : ID ASIGN error",
"asignacion : ID '=' expAritmetica error",
"asignacion : ID '=' error",
"asignacion : ID ASIGN comparacion ';'",
"sentenciaIF : IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';'",
"sentenciaIF : IF condicion THEN bloqueEjecutable ENDIF ';'",
"sentenciaIF : condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';'",
"sentenciaIF : IF THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF ';'",
"sentenciaIF : IF condicion bloqueEjecutable ELSE bloqueEjecutable ENDIF ';'",
"sentenciaIF : IF condicion THEN ELSE bloqueEjecutable ENDIF ';'",
"sentenciaIF : IF condicion THEN bloqueEjecutable ELSE ENDIF ';'",
"sentenciaIF : IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ';'",
"sentenciaIF : IF condicion THEN bloqueEjecutable ELSE bloqueEjecutable ENDIF error",
"sentenciaIF : IF THEN bloqueEjecutable ENDIF ';'",
"sentenciaIF : IF condicion bloqueEjecutable ENDIF ';'",
"sentenciaIF : IF condicion THEN ENDIF ';'",
"sentenciaIF : IF condicion THEN bloqueEjecutable ';'",
"sentenciaIF : IF condicion THEN bloqueEjecutable ENDIF error",
"sentenciaPRINT : PRINT '(' CADENA ')' ';'",
"sentenciaPRINT : '(' CADENA ')' ';'",
"sentenciaPRINT : PRINT CADENA ')' ';'",
"sentenciaPRINT : PRINT '(' ')' ';'",
"sentenciaPRINT : PRINT '(' CADENA ';'",
"sentenciaPRINT : PRINT '(' CADENA ')' error",
"sentenciaPRINT : PRINT '(' ID ')' ';'",
"sentenciaPRINT : PRINT '(' CTE ')' ';'",
"sentenciaWHILE : WHILE condicion DO bloqueEjecutable",
"sentenciaWHILE : condicion DO bloqueEjecutable",
"sentenciaWHILE : WHILE DO bloqueEjecutable",
"sentenciaWHILE : WHILE condicion bloqueEjecutable",
"sentenciaWHILE : WHILE condicion DO error",
"condicion : '(' comparaciones ')'",
"comparaciones : comparaciones opLogico comparacion",
"comparaciones : comparacion",
"comparaciones : comparaciones opLogico error",
"comparacion : expAritmetica comparador expAritmetica",
"comparacion : tipo '(' expAritmetica ')' comparador expAritmetica",
"comparacion : expAritmetica comparador tipo '(' expAritmetica ')'",
"comparacion : tipo '(' expAritmetica ')' comparador tipo '(' expAritmetica ')'",
"comparacion : expAritmetica comparador error",
"comparacion : comparador expAritmetica",
"comparador : '>'",
"comparador : '<'",
"comparador : IGUAL",
"comparador : DISTINTO",
"comparador : MAYORIG",
"comparador : MENORIG",
"comparador : '='",
"opLogico : AND",
"opLogico : OR",
"tipo : LONG",
"tipo : SINGLE",
};

//#line 602 "gramatica.y"


public int yylex() {
    int value = AnalizadorLexico.yylex();
    yylval = new ParserVal(AnalizadorLexico.refTDS); 
    return value;
}

public void yyerror(String string) {
	//AnalizadorSintactico.agregarError("Parser token error: " + string);
	//System.out.println("token error en linea  "+ AnalizadorLexico.numLinea + ": " +string);
}
//#line 686 "Parser.java"
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
//#line 25 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Programa reconocido. (Linea " + AnalizadorLexico.numLinea + ")");
			yyval= new ParserVal(new Nodo(val_peek(2).sval, (Nodo)val_peek(0).obj, null));
			AnalizadorSintactico.arbol = (Nodo)yyval.obj;}
break;
case 2:
//#line 31 "gramatica.y"
{
			   if (val_peek(0).obj != null)
			   { if (val_peek(1).obj != null)
				 {yyval= new ParserVal(new Nodo("Func", (Nodo)val_peek(0).obj, (Nodo)val_peek(1).obj));}
			     else {yyval= new ParserVal(new Nodo("Func", (Nodo)val_peek(0).obj, null));}
			     AnalizadorSintactico.arbolFunc = (Nodo)yyval.obj;
			   }
			   
			}
break;
case 3:
//#line 40 "gramatica.y"
{if (val_peek(0).obj != null){
						yyval= new ParserVal(new Nodo("Func", (Nodo)val_peek(0).obj, null));
						AnalizadorSintactico.arbolFunc = (Nodo)yyval.obj;}}
break;
case 4:
//#line 47 "gramatica.y"
{yyval=val_peek(0);}
break;
case 5:
//#line 48 "gramatica.y"
{yyval=val_peek(0);}
break;
case 6:
//#line 51 "gramatica.y"
{yyval= new ParserVal (new Nodo("TC", (Nodo)val_peek(4).obj, (Nodo)val_peek(1).obj));}
break;
case 7:
//#line 52 "gramatica.y"
{AnalizadorSintactico.agregarError("error: falta TRY en (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 8:
//#line 53 "gramatica.y"
{AnalizadorSintactico.agregarError("error TRY-CATCH vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 9:
//#line 54 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta CATCH (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 10:
//#line 55 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 11:
//#line 56 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 12:
//#line 59 "gramatica.y"
{ParserVal aux= new ParserVal (new Nodo("S", (Nodo)val_peek(6).obj, null));
																	yyval= new ParserVal(new Nodo("BF", (Nodo)aux.obj, (Nodo)val_peek(3).obj));}
break;
case 13:
//#line 61 "gramatica.y"
{ParserVal aux= new ParserVal (new Nodo("S", (Nodo)val_peek(7).obj, (Nodo)val_peek(6).obj));
																					  yyval= new ParserVal(new Nodo("BF", (Nodo)aux.obj, (Nodo)val_peek(3).obj));}
break;
case 14:
//#line 63 "gramatica.y"
{ParserVal aux= new ParserVal (new Nodo("S", (Nodo)val_peek(8).obj, (Nodo)val_peek(7).obj));
																										 ParserVal aux2= new ParserVal (new Nodo("S", (Nodo)aux.obj, (Nodo)val_peek(6).obj));
																										 yyval= new ParserVal(new Nodo("BF", (Nodo)aux.obj, (Nodo)val_peek(3).obj));}
break;
case 15:
//#line 66 "gramatica.y"
{ParserVal aux= new ParserVal (new Nodo("S", (Nodo)val_peek(7).obj, (Nodo)val_peek(6).obj));
																					  yyval= new ParserVal(new Nodo("BF", (Nodo)aux.obj, (Nodo)val_peek(3).obj));}
break;
case 16:
//#line 71 "gramatica.y"
{
			    yyval=val_peek(1);
			}
break;
case 17:
//#line 74 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 18:
//#line 75 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 19:
//#line 80 "gramatica.y"
{
                yyval = new ParserVal(new Nodo("BF",(Nodo)val_peek(6).obj,(Nodo)val_peek(3).obj));
            }
break;
case 20:
//#line 83 "gramatica.y"
{
                yyval = new ParserVal(new Nodo("BF",null,(Nodo)val_peek(3).obj));
            }
break;
case 21:
//#line 86 "gramatica.y"
{yyval=val_peek(0);}
break;
case 22:
//#line 87 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 23:
//#line 88 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta RETURN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 24:
//#line 89 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 25:
//#line 90 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta retorno (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 26:
//#line 91 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 27:
//#line 97 "gramatica.y"
{ if ((val_peek(1).obj != null) && (val_peek(0).obj != null))
                     {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, (Nodo)val_peek(1).obj));}
                  else if((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, null));}
                       else if ((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", null, (Nodo)val_peek(1).obj));}
                }
break;
case 28:
//#line 103 "gramatica.y"
{
                    if(val_peek(0).obj == null){
                       yyval= new ParserVal(new Nodo("S",null, null));
                    }else{
                        yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
                    }
                  }
break;
case 29:
//#line 112 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 30:
//#line 113 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 31:
//#line 116 "gramatica.y"
{

            AnalizadorSintactico.agregarAnalisis("sent contract (Linea " + AnalizadorLexico.numLinea + ")");
            if(val_peek(1).obj == null)
                break;
            yyval =  new ParserVal(new Nodo("CONTRACT",(Nodo)val_peek(0).obj,null));
            }
break;
case 32:
//#line 123 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 33:
//#line 124 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 34:
//#line 125 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 35:
//#line 126 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 36:
//#line 127 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 37:
//#line 133 "gramatica.y"
{ if ((val_peek(0).obj != null) && (val_peek(1).obj != null))
                     {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, (Nodo)val_peek(0).obj));}
                  else if((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));}
                       else if ((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", null, (Nodo)val_peek(0).obj));}
                }
break;
case 38:
//#line 139 "gramatica.y"
{
                    if(val_peek(0).obj == null){
                       yyval= new ParserVal(new Nodo("S",null, null));
                    }else{
                       yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
                    }
                }
break;
case 40:
//#line 150 "gramatica.y"
{yyval=val_peek(0);}
break;
case 42:
//#line 154 "gramatica.y"
{
                    AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 43:
//#line 157 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 44:
//#line 158 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 45:
//#line 159 "gramatica.y"
{
	       	        AnalizadorLexico.tablaDeSimbolos.remove(val_peek(1).sval);
           	        AnalizadorLexico.listaDeErrores.add("Tipo de variable debe ser en mayuscula (Linea " + (AnalizadorLexico.numLinea-1) + ")");
           	      }
break;
case 46:
//#line 163 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 47:
//#line 166 "gramatica.y"
{
                		     if( AnalizadorSintactico.esVariableRedeclarada(val_peek(0).sval + AnalizadorSintactico.ambitoActual)){
                                   AnalizadorSintactico.agregarError("Variable redeclarada (Linea " + AnalizadorLexico.numLinea + ")");
                             }else{
                                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                                aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                                AnalizadorLexico.tablaDeSimbolos.put(val_peek(0).sval + AnalizadorSintactico.ambitoActual,aux);
                             }
                            }
break;
case 48:
//#line 175 "gramatica.y"
{
	                if( AnalizadorSintactico.esVariableRedeclarada(val_peek(0).sval + AnalizadorSintactico.ambitoActual)){
                        AnalizadorSintactico.agregarError("Variable redeclarada (Linea " + AnalizadorLexico.numLinea + ")");
                    }else{
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                     aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    AnalizadorLexico.tablaDeSimbolos.put(val_peek(0).sval + AnalizadorSintactico.ambitoActual,aux);
                }
	      }
break;
case 49:
//#line 184 "gramatica.y"
{AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 50:
//#line 188 "gramatica.y"
{
        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
        System.out.println("Primero: "+ val_peek(2).sval+AnalizadorSintactico.ambitoActual);
        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
        yyval=new ParserVal (new Nodo(val_peek(2).sval+AnalizadorSintactico.ambitoActual, (Nodo)val_peek(0).obj, null));
        System.out.println("Segundo: "+val_peek(2).sval+AnalizadorSintactico.ambitoActual);
        
        }
break;
case 51:
//#line 196 "gramatica.y"
{
	        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
	                System.out.println("Primero: "+ val_peek(1).sval+AnalizadorSintactico.ambitoActual);
	        
	        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
	                System.out.println("Segundo: "+val_peek(1).sval+AnalizadorSintactico.ambitoActual);
	        
	        yyval=new ParserVal (new Nodo(val_peek(1).sval+AnalizadorSintactico.ambitoActual, (Nodo)val_peek(0).obj, null));
	   }
break;
case 52:
//#line 207 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 53:
//#line 209 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 54:
//#line 210 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 55:
//#line 213 "gramatica.y"
{
            AnalizadorSintactico.tipoActual = val_peek(4).sval;
           }
break;
case 56:
//#line 216 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 57:
//#line 217 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 58:
//#line 218 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 59:
//#line 219 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 60:
//#line 223 "gramatica.y"
{yyval=val_peek(0);}
break;
case 62:
//#line 229 "gramatica.y"
{
                       if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                       	break;
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("+", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos + (Linea " + AnalizadorLexico.numLinea + ")");
                      }
                      }
break;
case 63:
//#line 239 "gramatica.y"
{
	                   if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                       	break;
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("-", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos - (Linea " + AnalizadorLexico.numLinea + ")");
                      }
					  }
break;
case 64:
//#line 249 "gramatica.y"
{
	                 yyval=val_peek(0);
	                 }
break;
case 65:
//#line 258 "gramatica.y"
{
                        if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                        	break;
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("*", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos * (Linea " + AnalizadorLexico.numLinea + ")");
                      }
				                }
break;
case 66:
//#line 268 "gramatica.y"
{

	            if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
    	            break;
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("/", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos / (Linea " + AnalizadorLexico.numLinea + ")");
                      }
				            }
break;
case 67:
//#line 279 "gramatica.y"
{
	                        yyval = val_peek(0);
	                        }
break;
case 68:
//#line 288 "gramatica.y"
{
                     String lexema = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(0).sval);
                     if(lexema != null){
                        AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                        TDSObject value = AnalizadorLexico.getLexemaObject(lexema);
                        yyval= new ParserVal(new Nodo(lexema));
                        ((Nodo)yyval.obj).setTipo(value.getTipoContenido());
                        /*((Nodo)$$.obj).setTipoContenido("VAR");*/
                     }else{
                         AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                         AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                         /*stop generacion de arbol*/
                         
                     }
                }
break;
case 69:
//#line 303 "gramatica.y"
{
	                String var = val_peek(0).sval;
	                TDSObject value = AnalizadorLexico.getLexemaObject(var);
                    if (value.getTipoVariable() == "LONG"){
                      if(var.equals("2147483648")){
                           var = "2147483647";
                           TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                           AnalizadorLexico.tablaDeSimbolos.put(var,aux);
                      }
                    }
                    yyval= new ParserVal(new Nodo(var));
                    if( value != null){
                         ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                    }

	            }
break;
case 70:
//#line 319 "gramatica.y"
{
	                AnalizadorLexico.agregarNegativoTDS(val_peek(0).sval);
			        yyval= new ParserVal(new Nodo("-"+val_peek(0).sval));
			        TDSObject value = AnalizadorLexico.getLexemaObject("-"+val_peek(0).sval);
                    if( value != null){
                        if ( value.getTipoVariable() == "LONG" ) {
                                long l = Long.parseLong("-"+val_peek(0).sval);
                                if( !((l >= -2147483648) && (l <= 2147483647))){
                                    AnalizadorLexico.listaDeWarnings.add("Warning Linea " + AnalizadorLexico.numLinea + " : constante LONG fuera de rango.");
                                }else{
                                      ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                                }
                        }else{
                                float f = Float.parseFloat(("-"+val_peek(0).sval).replace('S','E'));
                                if( f != 0.0f  ){
                                    if( !((f > -3.40282347E+38) && (f < -1.17549435E-38 ))){
                                       AnalizadorLexico.listaDeWarnings.add("Warning Linea " + AnalizadorLexico.numLinea + " : constante FLOAT fuera de rango.");;
                                     }else{
                                        ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                                     }
                                }else{((Nodo)yyval.obj).setTipo(value.getTipoVariable());}
                        }
                    }
			    }
break;
case 71:
//#line 343 "gramatica.y"
{yyval=val_peek(0);}
break;
case 72:
//#line 347 "gramatica.y"
{
		  String variable = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(3).sval);
		  String variable2 = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(1).sval);
		  if(variable != null && variable2!= null){
		    AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
			AnalizadorLexico.tablaDeSimbolos.remove(val_peek(1).sval);
		    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
			TDSObject value2 = AnalizadorLexico.getLexemaObject(variable2);
			if (!value.getTipoParametro().equals(value2.getTipoContenido())){
			    AnalizadorSintactico.agregarError("El tipo enviado como parametro es distinto al esperado (Linea " + AnalizadorLexico.numLinea + ")");
			}
			
			ParserVal aux= new ParserVal(new Nodo(variable, null, null ));
			ParserVal aux2= new ParserVal(new Nodo(variable2, null, null ));
		
		    
		    yyval= new ParserVal(new Nodo("LF",(Nodo)aux.obj, (Nodo)aux2.obj ));
		    ((Nodo)yyval.obj).setTipo(value.getTipoContenido());
		  }else{
             AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
             /*error*/
		  }
		  }
break;
case 73:
//#line 370 "gramatica.y"
{
	   		  String variable = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(2).sval);
       		  if(variable != null){
       		    AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
       		    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
       		    ParserVal aux= new ParserVal(new Nodo(variable, null, null ));
       		    yyval= new ParserVal(new Nodo("LF",(Nodo)aux.obj, null ));
       		    ((Nodo)yyval.obj).setTipo(value.getTipoContenido());
       		  }else{
                    AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
                    /*error*/
       		  }
	   }
break;
case 74:
//#line 387 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
            if( AnalizadorSintactico.esVariableRedeclarada(val_peek(3).sval + AnalizadorSintactico.ambitoActual)){
                AnalizadorSintactico.agregarError("ERROR: ID ya fue utilizado (Linea " + AnalizadorLexico.numLinea + ")");
            }else{
                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
                aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                aux.setEsFuncion(true);
                aux.setTipoParametro(((TDSObject)((Object[])(val_peek(1).obj))[1]).getTipoContenido());
                AnalizadorLexico.tablaDeSimbolos.put(val_peek(3).sval + AnalizadorSintactico.ambitoActual,aux);
                yyval=val_peek(3);
            }
            AnalizadorSintactico.ambitoActual += "@"+ val_peek(3).sval;
            AnalizadorLexico.tablaDeSimbolos.put(((Object[])(val_peek(1).obj))[0] + AnalizadorSintactico.ambitoActual,(TDSObject)((Object[])(val_peek(1).obj))[1]);
			}
break;
case 75:
//#line 401 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
		     if( AnalizadorSintactico.esVariableRedeclarada(val_peek(2).sval + AnalizadorSintactico.ambitoActual)){
                   AnalizadorSintactico.agregarError("ERROR: ID ya fue utilizado (Linea " + AnalizadorLexico.numLinea + ")");
             }else{
                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                aux.setEsFuncion(true);
                AnalizadorLexico.tablaDeSimbolos.put(val_peek(2).sval + AnalizadorSintactico.ambitoActual,aux);
                yyval=val_peek(2);
             }
             AnalizadorSintactico.ambitoActual += "@"+ val_peek(2).sval;
            }
break;
case 76:
//#line 413 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 77:
//#line 414 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 78:
//#line 415 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 79:
//#line 416 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 80:
//#line 420 "gramatica.y"
{
                    Object[] obj = new Object[2] ;
                    obj[0] = val_peek(0).sval;
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                    aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    obj[1] = aux;
                    yyval= new ParserVal(obj);
		     }
break;
case 81:
//#line 428 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 82:
//#line 433 "gramatica.y"
{yyval=val_peek(0);}
break;
case 83:
//#line 434 "gramatica.y"
{yyval=val_peek(0);}
break;
case 84:
//#line 435 "gramatica.y"
{yyval=val_peek(0);}
break;
case 85:
//#line 436 "gramatica.y"
{yyval=val_peek(0);}
break;
case 86:
//#line 439 "gramatica.y"
{

                    String variable = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(3).sval);
                    if(variable == null){
                       AnalizadorSintactico.agregarError("Variable no definida (Linea " + AnalizadorLexico.numLinea + ")");
                       AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
                       /*corta arbol*/
                    }else{
                        AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
                        AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion (Linea " + AnalizadorLexico.numLinea + ")");
                        if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                             break;
					    ParserVal aux = new ParserVal(new Nodo(variable));
					    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
					    yyval= new ParserVal(new Nodo(":=", (Nodo)aux.obj, (Nodo)val_peek(1).obj));
                        if(value.getTipoContenido().equals( ((Nodo)val_peek(1).obj).getTipo())){
                            ((Nodo)yyval.obj).setTipo(((Nodo)aux.obj).getTipo());
                        }else{
                        	 AnalizadorSintactico.agregarError("Tipo Incompatible (" + value.getTipoContenido() + "," +  ((Nodo)val_peek(1).obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
                        }
                     }
					}
break;
case 87:
//#line 461 "gramatica.y"
{

	                        AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea +": falta el simbolo : de la asignacion.");
	                        if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                            	break;
                            String variable = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(3).sval);
                            if(variable == null){
                               AnalizadorSintactico.agregarError("Variable no definida (Linea " + AnalizadorLexico.numLinea + ")");
                               AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
                               /*corta arbol*/
                            }else{
                                AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
                                AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion (Linea " + AnalizadorLexico.numLinea + ")");
        					    ParserVal aux = new ParserVal(new Nodo(variable));
        					    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
        					    yyval= new ParserVal(new Nodo(":=", (Nodo)aux.obj, (Nodo)val_peek(1).obj));
                                if(value.getTipoContenido().equals( ((Nodo)val_peek(1).obj).getTipo())){
                                    ((Nodo)yyval.obj).setTipo(((Nodo)aux.obj).getTipo());
                                }else{
                                	 AnalizadorSintactico.agregarError("Tipo Incompatible (" + value.getTipoContenido() + "," +  ((Nodo)val_peek(1).obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
                                }
                             }
        					}
break;
case 88:
//#line 484 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 89:
//#line 485 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 90:
//#line 486 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 91:
//#line 487 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 92:
//#line 488 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 93:
//#line 489 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' y falta :  (Linea " + (AnalizadorLexico.numLinea -1) + ")");}
break;
case 94:
//#line 490 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' y falta :  (Linea " + (AnalizadorLexico.numLinea -1) + ")");}
break;
case 95:
//#line 491 "gramatica.y"
{AnalizadorSintactico.agregarError("Error, no puede asignarse un comparador(Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 96:
//#line 495 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if(val_peek(6).obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 97:
//#line 503 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");
	                                                    if(val_peek(4).obj == null)
                                                           break;
                                                        ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(2).obj, null));
                                                        ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,null));
                                                        yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(4).obj, (Nodo)auxCuerpo.obj));}
break;
case 98:
//#line 509 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 99:
//#line 510 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 100:
//#line 511 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 512 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 102:
//#line 513 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 103:
//#line 514 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 104:
//#line 515 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 105:
//#line 516 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 106:
//#line 517 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 107:
//#line 518 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 108:
//#line 519 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 109:
//#line 520 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 110:
//#line 523 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 111:
//#line 526 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 112:
//#line 527 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 113:
//#line 528 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 114:
//#line 529 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 115:
//#line 530 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 118:
//#line 535 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		 if(val_peek(2).obj == null)
             break;
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 119:
//#line 539 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 120:
//#line 540 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 121:
//#line 541 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 122:
//#line 542 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 123:
//#line 546 "gramatica.y"
{yyval=val_peek(1);}
break;
case 125:
//#line 550 "gramatica.y"
{
	     if(val_peek(0).obj == null)
             break;
	     yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(0).obj, null));}
break;
case 126:
//#line 554 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 127:
//#line 558 "gramatica.y"
{
		  if(val_peek(2).obj == null || val_peek(0).obj == null){
             break;
          }else{
		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
		  }
		}
break;
case 131:
//#line 568 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 132:
//#line 569 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 133:
//#line 572 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 134:
//#line 573 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 135:
//#line 574 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 136:
//#line 575 "gramatica.y"
{yyval = new ParserVal("<>");}
break;
case 137:
//#line 576 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 138:
//#line 577 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 139:
//#line 578 "gramatica.y"
{
	           AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea +": se esperaba comparacion ==.");
	           yyval = new ParserVal("==");
	        }
break;
case 140:
//#line 584 "gramatica.y"
{yyval= new ParserVal("&&");}
break;
case 141:
//#line 585 "gramatica.y"
{yyval= new ParserVal("||");}
break;
case 142:
//#line 589 "gramatica.y"
{AnalizadorSintactico.tipoActual = "LONG";
             yyval = new ParserVal("LONG");
            }
break;
case 143:
//#line 592 "gramatica.y"
{AnalizadorSintactico.tipoActual = "SINGLE";
             yyval = new ParserVal("SINGLE");
             }
break;
//#line 1693 "Parser.java"
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
