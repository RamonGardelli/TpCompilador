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
   29,   29,   29,   29,   29,   29,   29,   29,   30,   30,
   30,   30,   30,   14,   32,   32,   32,   31,   31,   31,
   31,   31,   31,   34,   34,   34,   34,   34,   34,   34,
   33,   33,   18,   18,
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
    4,    4,    4,    4,    5,    5,    5,    6,    4,    3,
    3,    3,    4,    3,    3,    1,    3,    3,    6,    6,
    9,    3,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,  143,  144,    0,    0,    3,   39,   40,
   41,    0,    0,    0,    0,    0,    0,   45,    0,    0,
    0,    0,    0,    0,    0,    0,    1,    2,    4,    5,
    0,    0,    0,   82,   83,   84,   85,   46,    0,    0,
    0,   43,    0,    0,    0,    0,   29,   21,    0,   51,
   28,   30,   48,   53,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   69,    0,    0,    0,
    0,    0,   67,   71,    0,    0,    0,    0,    0,    0,
  136,  137,  138,  139,  140,  134,  135,    0,    0,  126,
    0,    0,    0,   37,   17,    0,    0,    0,    0,    0,
    0,   44,   42,   47,    0,    0,    0,    0,    0,    0,
   50,    0,   27,   54,   52,   81,    0,    0,   56,    0,
    0,    0,    0,    0,    0,  121,    0,  122,   18,   16,
    0,   92,    0,    0,    0,   70,   94,    0,   90,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   89,
    0,    0,    0,  141,  142,  124,    0,    0,    0,    0,
  120,    0,    0,   58,    0,   57,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   80,   76,  112,
    0,  114,    0,    0,  113,    0,  123,  119,    0,   73,
    0,   91,   86,   95,   93,   87,    0,    0,   65,   66,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  111,    0,  132,    0,    0,  127,  125,    0,    0,
   75,    0,   78,   59,   55,   77,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   31,    0,  115,  110,  116,  117,    0,   72,
    0,    8,    9,    0,   10,    0,  105,    0,  107,    0,
    0,  108,    0,  106,    0,    0,    7,    0,   79,   74,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   32,   34,    0,   35,   33,    0,  118,    0,   11,
    6,    0,    0,    0,    0,  109,   97,    0,    0,    0,
    0,    0,    0,    0,    0,   25,   26,    0,   24,   23,
    0,    0,    0,   36,   22,   88,   99,  101,  102,    0,
  103,  100,    0,    0,  130,   98,   20,   61,    0,    0,
    0,    0,    0,    0,  104,   96,    0,   12,    0,   19,
    0,    0,    0,   15,   13,    0,  131,   14,
};
final static short yydgoto[] = {                          2,
    7,   27,    8,   29,   30,   31,   32,   48,  227,   49,
   50,   51,   52,   33,    9,   10,   11,  228,   13,   14,
   15,  229,   72,   73,   74,  118,   34,   35,   36,   37,
   90,   91,  157,   92,
};
final static short yysindex[] = {                      -239,
 -112,    0,  -34,    0,    0, -192,  -35,    0,    0,    0,
    0, -167,  -10,   -9,   23,   67, -181,    0,  -24,  -29,
  184,  271,  171,  -31,  174,  360,    0,    0,    0,    0,
  177, -141, -155,    0,    0,    0,    0,    0,   12,   92,
  -23,    0, -133,   18,   -5,   -9,    0,    0,   97,    0,
    0,    0,    0,    0,   17, -124,  101,  107,  154,  116,
  526,   28,  100,  184, -178,  127,    0,  500, -131,   14,
   74,  118,    0,    0,  -94, -205,  116,   47,   77,  143,
    0,    0,    0,    0,    0,    0,    0,  175,   48,    0,
   32,  174,  -49,    0,    0,  116,  116,  111,   88,  180,
 -124,    0,    0,    0,  183,  126,  -57,   57,  220,  232,
    0,  286,    0,    0,    0,    0,   91,  298,    0,  325,
   -8,  304,  359,  349,  139,    0,   73,    0,    0,    0,
  -12,    0,  377,  487,  369,    0,    0,   53,    0,  174,
  174,  174,  174,  184,  184,  178,  -95,    4,  -69,    0,
  371,  174,  340,    0,    0,    0,  513,  161,  184,  195,
    0,  131,  412,    0,  -21,    0,  418,  346,  422,  130,
  -33,  360,  152,  424,  473,   -3,  346,    0,    0,    0,
  -36,    0,  407,  414,    0,  430,    0,    0,  439,    0,
  174,    0,    0,    0,    0,    0,  118,  118,    0,    0,
  216,  218,  184,  219,  116,  432,  116,  434,  -45,  116,
  436,    0,  134,    0,  456,  161,    0,    0,  236,  116,
    0,   34,    0,    0,    0,    0,  463,  466,  161,  346,
  467,  328,  471,  474,  483,   48,  484,  156,  458,  469,
   13,  470,    0,  490,    0,    0,    0,    0,  481,    0,
  137,    0,    0, -145,    0,  277,    0,  282,    0,   83,
   75,    0,  285,    0,  477,  174,    0,  291,    0,    0,
  492,  174,  511,  346,  287,  -19,  289,  290,  174,  346,
  517,    0,    0,  307,    0,    0,  299,    0,  416,    0,
    0,  505,  507,  508,  -46,    0,    0,  509,  346,  142,
  510,  306,  148,  518,  529,    0,    0,  519,    0,    0,
  157,  535,  346,    0,    0,    0,    0,    0,    0,   85,
    0,    0,  539,  161,    0,    0,    0,    0,  314,  522,
  316,  477,  524,  543,    0,    0,  174,    0,  319,    0,
  323,  531,  160,    0,    0,  331,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,   33,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   33,    0,    0,    0,    0,    0,    0,    0,    0,
  332,    0,    0,    0,    0,    0,    0,    0,    0,   36,
    0,    0,   46,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -138,    0,  427,    0,    0,    0,    0,
    0,  438,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   87,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -40,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  449,  460,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -16,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -17,    0,
    0,    0,    0,    0,    0,  550,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   22,    0,    0,    0,    0,    0,    0,
    0,  553,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  587,  744,   21,   -7,    0,  782,  495,    0, -106,  -27,
  556,  355,    0,    6,    0,    0,    0,    1,  182,    0,
    0,  -22,   68,  -58,    0,  -56,    0,    0,    0,    0,
  -18,    0,    0,  -79,
};
final static int YYTABLESIZE=1020;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         71,
  133,   12,   79,   89,   26,   17,  232,   12,   61,  153,
   61,   69,  321,  262,   12,   59,  108,   57,  133,  225,
   43,  308,  246,   60,  128,   62,   88,   28,  190,   78,
   26,    1,  181,   43,  109,  103,  106,  242,   89,  100,
   71,  163,  128,   26,  167,  134,   12,  138,   42,  135,
  182,   99,  110,  284,  153,  243,  117,   26,   69,  145,
   43,   88,  129,   18,  233,  234,   28,   26,  133,  158,
  244,  285,  156,  146,  270,  115,   48,  129,  170,   48,
  129,   54,    4,  199,  200,    5,   26,  130,   38,   49,
  140,   48,  141,   39,   48,  140,  172,  141,  117,  165,
  173,  117,   96,   40,   49,  222,   56,   87,   85,   86,
  290,  196,   26,   97,  174,  176,  140,   38,  141,  140,
  291,  141,   26,  273,   95,  276,   29,   38,  164,  213,
  216,  101,  139,  297,   89,  150,   26,  104,  218,    4,
  136,  119,    5,  336,   69,  238,  116,  120,    3,  236,
  162,    4,   89,  215,    5,   26,  153,   88,    6,  142,
   70,  205,  117,  206,  143,   26,  131,  305,  251,   26,
  144,  221,  235,  312,  265,   88,  140,  289,  141,  140,
  241,  141,  325,  151,  140,  299,  141,  210,  328,  211,
  140,   26,  141,   41,  124,   26,   55,  332,  125,  140,
  347,  141,  140,  140,  141,  141,  334,  197,  198,  299,
   26,  260,  320,  261,  152,  159,   26,   26,   69,  245,
  166,   93,  168,   26,   19,    3,   77,   20,    4,   21,
    4,    5,  102,    5,  224,   22,   16,   66,   67,   60,
  133,  133,   23,  300,   24,   58,  307,   25,   60,  303,
   19,    3,  299,   20,    4,   44,  311,    5,  189,   61,
  207,   22,  208,   19,  128,  128,   20,   45,   21,  137,
   24,  175,  114,   25,   63,    4,  324,   19,    5,  105,
   20,   23,   98,   24,   66,   67,   25,   19,   63,  269,
   20,   48,   21,   53,   45,   23,  127,   24,   63,  323,
   25,   49,  129,  129,  148,   23,   19,   24,  195,   20,
   25,   21,  154,  155,  343,   69,   19,   63,  171,   20,
   81,   82,   83,   84,   23,  177,   24,   63,  187,   25,
  296,   70,   19,   45,   23,   20,   24,   21,  179,   25,
  335,  294,   19,   63,  183,   20,   29,   21,   29,   29,
   23,    4,   24,   63,    5,   25,   19,   29,  112,   20,
   23,  178,   24,   29,   29,   25,   29,   63,  275,   29,
   66,   67,   69,   45,    4,   19,   24,    5,   20,   25,
   21,  116,   68,  180,   69,   19,   63,  169,   20,   19,
   69,  231,   20,   23,    4,   24,   63,    5,   25,  184,
   63,  116,   45,  113,   69,   24,   45,  185,   25,   24,
  186,   19,   25,  237,   20,   19,  191,  281,   20,   87,
   85,   86,   63,  121,  122,  123,   63,  194,   45,  212,
   19,   24,   45,   20,   25,   24,   19,   19,   25,   20,
   20,   63,  203,   19,   66,   67,   20,   63,   63,   75,
   24,  220,  223,   25,   63,   93,   24,   24,  226,   25,
   25,  230,  113,   24,  239,  247,   25,   68,   68,   68,
  249,   68,  248,   68,  316,   87,   85,   86,   64,  250,
   64,  252,   64,  253,  255,   68,   68,   68,   68,   62,
  257,   62,  259,   62,  264,  266,   64,   64,   64,   64,
   63,  267,   63,  271,   63,  272,  274,   62,   62,   62,
   62,  277,   61,  240,  278,   65,  282,   69,   63,   63,
   63,   63,  279,  280,  113,   94,   18,  283,  286,  140,
  287,  141,   87,   85,   86,  292,   87,   85,   86,  288,
  293,   66,   67,  298,   69,  193,   87,   85,   86,  301,
  302,  304,  306,   68,  309,  310,  313,   69,   94,   87,
   85,   86,  314,  317,  315,  318,  319,  322,  326,  330,
   69,  327,   87,   85,   86,  333,  329,  331,  337,  338,
  339,  340,  341,  342,  344,   87,   85,   86,  345,  346,
   60,    4,  113,   61,    5,  214,  348,   38,   66,   67,
   46,  111,    0,    4,    0,    0,    5,    0,    0,    4,
   66,   67,    5,    0,    0,    0,   66,   67,    0,    0,
    0,    0,    0,    4,    0,    0,    5,    0,    0,   80,
   66,   67,   81,   82,   83,   84,    0,    0,  201,  202,
  204,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  219,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   68,    0,    0,    0,    0,    0,   81,   82,
   83,   84,   68,   64,    0,    0,    0,  254,    0,   68,
   68,   68,   68,   64,   62,    0,    0,   68,   68,    0,
   64,   64,   64,   64,   62,   63,    0,    0,   64,   64,
    0,   62,   62,   62,   62,   63,    0,    0,    0,   62,
   62,    0,   63,   63,   63,   63,    4,    0,    0,    5,
   63,   63,  192,   66,   67,   81,   82,   83,   84,   81,
   82,   83,   84,    0,    0,  132,    0,    0,    0,   81,
   82,   83,   84,    4,    0,    0,    5,    0,  217,    0,
   66,   67,   81,   82,   83,   84,    4,    0,    0,    5,
    0,    0,    0,   66,   67,   81,   82,   83,   84,    4,
    0,    0,    5,    0,    0,   47,   66,   67,   81,   82,
   83,   84,   64,  126,   76,  128,    0,    0,    0,    0,
    0,    0,   64,    0,    0,    0,    0,    0,    0,    0,
  147,  149,    0,    0,    0,  107,    0,   47,    0,    0,
   47,    0,    0,    0,    0,    0,    0,    0,    0,  160,
  161,    0,    0,    0,    0,   64,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  188,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   47,    0,  107,
    0,  209,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   64,   64,   64,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   64,    0,    0,    0,    0,    0,    0,    0,  256,    0,
  258,   47,    0,  263,   47,    0,    0,    0,    0,    0,
    0,    0,    0,  268,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   64,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  295,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   47,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         22,
   41,    1,   25,   26,   40,   40,   40,    7,   40,   89,
   40,   45,   59,   59,   14,   40,   44,   17,   59,   41,
   44,   41,   59,   41,   41,   20,   26,    7,   41,   24,
   40,  271,   41,   44,   40,   59,   44,   41,   61,   39,
   63,   98,   59,   40,  101,   68,   46,   70,   59,   68,
   59,   40,   58,   41,  134,   59,   56,   40,   45,  265,
   44,   61,   41,  256,  171,  172,   46,   40,   68,   92,
  177,   59,   41,  279,   41,   59,   44,  256,  106,   44,
   59,   59,  264,  142,  143,  267,   40,  266,  256,   44,
   43,   59,   45,  261,   59,   43,   40,   45,   98,   99,
  108,  101,  258,  271,   59,  162,   40,   60,   61,   62,
  256,   59,   40,  269,  109,  110,   43,  256,   45,   43,
  266,   45,   40,  230,  266,  232,   40,  266,   41,  152,
  153,   40,   59,   59,  157,   59,   40,  271,  157,  264,
  272,   41,  267,   59,   45,  173,  271,   41,  261,  172,
   40,  264,  175,  153,  267,   40,  236,  157,  271,   42,
   61,  257,  162,  259,   47,   40,   40,  274,  191,   40,
  265,   41,  172,  280,   41,  175,   43,   41,   45,   43,
  175,   45,   41,   41,   43,  265,   45,  257,   41,  259,
   43,   40,   45,   12,   41,   40,   15,   41,   45,   43,
   41,   45,   43,   43,   45,   45,  313,  140,  141,  289,
   40,  257,  259,  259,   40,  265,   40,   40,   45,  256,
   41,  279,   40,   40,  260,  261,  258,  263,  264,  265,
  264,  267,  256,  267,  256,  271,  271,  271,  272,  269,
  281,  282,  278,  266,  280,  270,  266,  283,  266,  272,
  260,  261,  332,  263,  264,  265,  279,  267,  271,   40,
  257,  271,  259,  260,  281,  282,  263,  277,  265,  256,
  280,   40,  256,  283,  271,  264,  299,  260,  267,  262,
  263,  278,  271,  280,  271,  272,  283,  260,  271,  256,
  263,  256,  265,  271,  277,  278,  269,  280,  271,  299,
  283,  256,  281,  282,  258,  278,  260,  280,  256,  263,
  283,  265,  281,  282,  337,   45,  260,  271,  262,  263,
  273,  274,  275,  276,  278,   40,  280,  271,  256,  283,
  256,   61,  260,  277,  278,  263,  280,  265,   41,  283,
  256,  259,  260,  271,   41,  263,  260,  265,  262,  263,
  278,  264,  280,  271,  267,  283,  260,  271,  262,  263,
  278,  271,  280,  277,  278,  283,  280,  271,   41,  283,
  271,  272,   45,  277,  264,  260,  280,  267,  263,  283,
  265,  271,  283,   59,   45,  260,  271,  262,  263,  260,
   45,  262,  263,  278,  264,  280,  271,  267,  283,   41,
  271,  271,  277,   49,   45,  280,  277,   59,  283,  280,
  272,  260,  283,  262,  263,  260,   40,  262,  263,   60,
   61,   62,  271,  270,  271,  272,  271,   59,  277,   59,
  260,  280,  277,  263,  283,  280,  260,  260,  283,  263,
  263,  271,  265,  260,  271,  272,  263,  271,  271,  279,
  280,  257,   41,  283,  271,  279,  280,  280,   41,  283,
  283,   40,  108,  280,   41,   59,  283,   41,   42,   43,
   41,   45,   59,   47,   59,   60,   61,   62,   41,   41,
   43,  266,   45,  266,  266,   59,   60,   61,   62,   41,
   59,   43,   59,   45,   59,   40,   59,   60,   61,   62,
   41,  266,   43,   41,   45,   40,   40,   59,   60,   61,
   62,   41,   40,   41,   41,   21,   59,   45,   59,   60,
   61,   62,   40,   40,  170,   31,  256,   59,   59,   43,
   41,   45,   60,   61,   62,  259,   60,   61,   62,   59,
  259,  271,  272,  259,   45,   59,   60,   61,   62,  259,
   59,   41,  266,  283,  266,  266,   40,   45,   64,   60,
   61,   62,  256,   59,  266,   59,   59,   59,   59,   41,
   45,  266,   60,   61,   62,   41,   59,   59,   40,  266,
   59,  266,   59,   41,  266,   60,   61,   62,  266,   59,
   41,  264,  238,   41,  267,  256,  266,  266,  271,  272,
   14,   46,   -1,  264,   -1,   -1,  267,   -1,   -1,  264,
  271,  272,  267,   -1,   -1,   -1,  271,  272,   -1,   -1,
   -1,   -1,   -1,  264,   -1,   -1,  267,   -1,   -1,  270,
  271,  272,  273,  274,  275,  276,   -1,   -1,  144,  145,
  146,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  159,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,  273,  274,
  275,  276,  266,  256,   -1,   -1,   -1,  203,   -1,  273,
  274,  275,  276,  266,  256,   -1,   -1,  281,  282,   -1,
  273,  274,  275,  276,  266,  256,   -1,   -1,  281,  282,
   -1,  273,  274,  275,  276,  266,   -1,   -1,   -1,  281,
  282,   -1,  273,  274,  275,  276,  264,   -1,   -1,  267,
  281,  282,  256,  271,  272,  273,  274,  275,  276,  273,
  274,  275,  276,   -1,   -1,  256,   -1,   -1,   -1,  273,
  274,  275,  276,  264,   -1,   -1,  267,   -1,  256,   -1,
  271,  272,  273,  274,  275,  276,  264,   -1,   -1,  267,
   -1,   -1,   -1,  271,  272,  273,  274,  275,  276,  264,
   -1,   -1,  267,   -1,   -1,   14,  271,  272,  273,  274,
  275,  276,   21,   60,   23,   62,   -1,   -1,   -1,   -1,
   -1,   -1,   31,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   77,   78,   -1,   -1,   -1,   44,   -1,   46,   -1,   -1,
   49,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   96,
   97,   -1,   -1,   -1,   -1,   64,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  127,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  106,   -1,  108,
   -1,  148,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  144,  145,  146,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  159,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  205,   -1,
  207,  170,   -1,  210,  173,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  220,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  203,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  260,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  238,
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
"sentenciaPRINT : PRINT '(' '-' CTE ')' ';'",
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

//#line 639 "gramatica.y"


public int yylex() {
    int value = AnalizadorLexico.yylex();
    yylval = new ParserVal(AnalizadorLexico.refTDS); 
    return value;
}

public void yyerror(String string) {
	//AnalizadorSintactico.agregarError("Parser token error: " + string);
	//System.out.println("token error en linea  "+ AnalizadorLexico.numLinea + ": " +string);
}
//#line 687 "Parser.java"
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
        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
        yyval=new ParserVal (new Nodo(val_peek(2).sval+AnalizadorSintactico.ambitoActual, (Nodo)val_peek(0).obj, null));
        
        }
break;
case 51:
//#line 194 "gramatica.y"
{
	        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
	        
	        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
	         
	        yyval=new ParserVal (new Nodo(val_peek(1).sval+AnalizadorSintactico.ambitoActual, (Nodo)val_peek(0).obj, null));
	   }
break;
case 52:
//#line 203 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 53:
//#line 205 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 54:
//#line 206 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 55:
//#line 209 "gramatica.y"
{
            AnalizadorSintactico.tipoActual = val_peek(4).sval;
           }
break;
case 56:
//#line 212 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 57:
//#line 213 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 58:
//#line 214 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 59:
//#line 215 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 60:
//#line 219 "gramatica.y"
{yyval=val_peek(0);}
break;
case 62:
//#line 225 "gramatica.y"
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
//#line 235 "gramatica.y"
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
//#line 245 "gramatica.y"
{
	                 yyval=val_peek(0);
	                 }
break;
case 65:
//#line 254 "gramatica.y"
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
//#line 264 "gramatica.y"
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
//#line 275 "gramatica.y"
{
	                        yyval = val_peek(0);
	                        }
break;
case 68:
//#line 284 "gramatica.y"
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
//#line 299 "gramatica.y"
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
//#line 315 "gramatica.y"
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
//#line 339 "gramatica.y"
{yyval=val_peek(0);}
break;
case 72:
//#line 343 "gramatica.y"
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
//#line 366 "gramatica.y"
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
//#line 383 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
            if( AnalizadorSintactico.esVariableRedeclarada(val_peek(3).sval + AnalizadorSintactico.ambitoActual)){
                AnalizadorSintactico.agregarError("ERROR: ID ya fue utilizado (Linea " + AnalizadorLexico.numLinea + ")");
            }else{
                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
                aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                aux.setEsFuncion(true);
                aux.setTipoParametro(((TDSObject)((Object[])(val_peek(1).obj))[1]).getTipoContenido());
                aux.setNombreParametro(((Object[])(val_peek(1).obj))[0] + AnalizadorSintactico.ambitoActual+"@"+val_peek(3).sval);
                AnalizadorLexico.tablaDeSimbolos.put(val_peek(3).sval + AnalizadorSintactico.ambitoActual,aux);
                yyval=val_peek(3);
            }
            AnalizadorSintactico.ambitoActual += "@"+ val_peek(3).sval;
            AnalizadorLexico.tablaDeSimbolos.put(((Object[])(val_peek(1).obj))[0] + AnalizadorSintactico.ambitoActual,(TDSObject)((Object[])(val_peek(1).obj))[1]);
			}
break;
case 75:
//#line 398 "gramatica.y"
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
//#line 410 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 77:
//#line 411 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 78:
//#line 412 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 79:
//#line 413 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 80:
//#line 417 "gramatica.y"
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
//#line 425 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 82:
//#line 430 "gramatica.y"
{yyval=val_peek(0);}
break;
case 83:
//#line 431 "gramatica.y"
{yyval=val_peek(0);}
break;
case 84:
//#line 432 "gramatica.y"
{yyval=val_peek(0);}
break;
case 85:
//#line 433 "gramatica.y"
{yyval=val_peek(0);}
break;
case 86:
//#line 436 "gramatica.y"
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
//#line 458 "gramatica.y"
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
//#line 481 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 89:
//#line 482 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 90:
//#line 483 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 91:
//#line 484 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 92:
//#line 485 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 93:
//#line 486 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' y falta :  (Linea " + (AnalizadorLexico.numLinea -1) + ")");}
break;
case 94:
//#line 487 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' y falta :  (Linea " + (AnalizadorLexico.numLinea -1) + ")");}
break;
case 95:
//#line 488 "gramatica.y"
{AnalizadorSintactico.agregarError("Error, no puede asignarse un comparador(Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 96:
//#line 492 "gramatica.y"
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
//#line 500 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");
	                                                    if(val_peek(4).obj == null)
                                                           break;
                                                        ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(2).obj, null));
                                                        ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,null));
                                                        yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(4).obj, (Nodo)auxCuerpo.obj));}
break;
case 98:
//#line 506 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 99:
//#line 507 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 100:
//#line 508 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 509 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 102:
//#line 510 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 103:
//#line 511 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 104:
//#line 512 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 105:
//#line 513 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 106:
//#line 514 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 107:
//#line 515 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 108:
//#line 516 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 109:
//#line 517 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 110:
//#line 520 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 111:
//#line 523 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 112:
//#line 524 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 113:
//#line 525 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 114:
//#line 526 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 115:
//#line 527 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 116:
//#line 528 "gramatica.y"
{
	       	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
	                           String lexema = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(2).sval);
                               if(lexema != null){
                                  AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                  ParserVal aux = new ParserVal(new Nodo(lexema));
                                  yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                               }else{
                                   AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                   AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                   /*stop generacion de arbol*/

                               }
	      }
break;
case 117:
//#line 542 "gramatica.y"
{
                	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
                               	                           String lexema = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(2).sval);

                               if(lexema != null){
                                  AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                  ParserVal aux = new ParserVal(new Nodo(lexema));
                                  yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                               }else{
                                  AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                  AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                  /*stop generacion de arbol*/
                               }
                               }
break;
case 118:
//#line 556 "gramatica.y"
{
                          	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
                                        String lexema = AnalizadorSintactico.getReferenciaPorAmbito("-"+val_peek(2).sval);

                                         if(lexema != null){
                                            AnalizadorLexico.tablaDeSimbolos.remove("-"+val_peek(2).sval);
                                            ParserVal aux = new ParserVal(new Nodo(lexema));
                                            yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                                         }else{
                                            AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                            AnalizadorLexico.tablaDeSimbolos.remove("-"+val_peek(2).sval);
                                            /*stop generacion de arbol*/
                                         }
	      }
break;
case 119:
//#line 572 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		 if(val_peek(2).obj == null)
             break;
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 120:
//#line 576 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 121:
//#line 577 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 122:
//#line 578 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 123:
//#line 579 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 124:
//#line 583 "gramatica.y"
{yyval=val_peek(1);}
break;
case 126:
//#line 587 "gramatica.y"
{
	     if(val_peek(0).obj == null)
             break;
	     yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(0).obj, null));}
break;
case 127:
//#line 591 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 128:
//#line 595 "gramatica.y"
{
		  if(val_peek(2).obj == null || val_peek(0).obj == null){
             break;
          }else{
		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
		  }
		}
break;
case 132:
//#line 605 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 133:
//#line 606 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 134:
//#line 609 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 135:
//#line 610 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 136:
//#line 611 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 137:
//#line 612 "gramatica.y"
{yyval = new ParserVal("<>");}
break;
case 138:
//#line 613 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 139:
//#line 614 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 140:
//#line 615 "gramatica.y"
{
	           AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea +": se esperaba comparacion ==.");
	           yyval = new ParserVal("==");
	        }
break;
case 141:
//#line 621 "gramatica.y"
{yyval= new ParserVal("&&");}
break;
case 142:
//#line 622 "gramatica.y"
{yyval= new ParserVal("||");}
break;
case 143:
//#line 626 "gramatica.y"
{AnalizadorSintactico.tipoActual = "LONG";
             yyval = new ParserVal("LONG");
            }
break;
case 144:
//#line 629 "gramatica.y"
{AnalizadorSintactico.tipoActual = "SINGLE";
             yyval = new ParserVal("SINGLE");
             }
break;
//#line 1742 "Parser.java"
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
