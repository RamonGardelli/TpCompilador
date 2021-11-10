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
   11,   11,   11,   11,   11,   10,   10,   10,   12,   12,
   12,   12,   12,   12,    7,    7,    3,    3,    3,   14,
   14,   14,   15,   15,   16,   16,   16,   20,   20,   20,
   20,   20,    9,    9,   21,   21,   21,   22,   22,   22,
   23,   23,   23,   23,   24,   24,   19,   19,   19,   19,
   19,   19,   25,   25,    6,    6,    6,    6,   26,   26,
   26,   26,   26,   27,   27,   27,   27,   27,   27,   27,
   27,   27,   27,   27,   27,   27,   27,   28,   28,   28,
   28,   28,   28,   28,   28,   29,   29,   29,   29,   29,
   13,   30,   30,   30,   32,   32,   32,   32,   32,   32,
   33,   33,   33,   33,   33,   33,   31,   31,   17,   17,
   18,   18,   18,
};
final static short yylen[] = {                            2,
    3,    2,    1,    1,    1,    6,    5,    5,    5,    5,
    6,    8,    9,   10,    9,    3,    2,    3,    8,    1,
    6,    6,    6,    6,    6,    2,    2,    1,    6,    5,
    5,    5,    5,    6,    2,    1,    1,    1,    1,    3,
    2,    3,    3,    2,    3,    2,    3,    5,    4,    4,
    4,    5,    1,    4,    3,    3,    1,    3,    3,    1,
    1,    1,    2,    1,    4,    4,    6,    5,    5,    5,
    5,    6,    2,    1,    1,    1,    1,    1,    4,    7,
    3,    3,    4,    8,    6,    7,    7,    7,    7,    7,
    7,    8,    5,    5,    5,    5,    6,    5,    4,    4,
    4,    4,    5,    5,    5,    4,    3,    3,    3,    4,
    3,    3,    1,    3,    3,    6,    6,    9,    3,    2,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    3,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,  129,  130,  132,    0,    3,   37,   38,
   39,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    1,    2,    4,    5,    0,
    0,    0,   75,   76,   77,   78,    0,    0,    0,   41,
    0,    0,    0,   28,   20,    0,   44,   46,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   36,    0,    0,
   62,    0,    0,    0,    0,   60,   64,    0,    0,    0,
    0,    0,    0,  123,  124,  125,  126,  121,  122,    0,
    0,    0,  113,    0,    0,   17,   35,    0,    0,    0,
    0,    0,    0,   42,   40,  131,    0,    0,    0,   43,
    0,    0,   26,   27,   47,   45,   74,    0,    0,   49,
    0,    0,    0,    0,    0,  108,    0,  109,   18,   16,
    0,    0,    0,   63,   82,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   81,    0,    0,    0,  127,
  128,  111,    0,    0,    0,    0,  107,    0,    0,   51,
    0,   50,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   73,   69,  100,    0,  102,    0,    0,  101,
  110,  106,    0,    0,    0,   83,   79,    0,    0,   58,
   59,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   99,    0,  119,    0,    0,  114,  112,    0,
    0,   68,    0,   71,   52,   48,   70,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  103,   98,  104,  105,   66,   65,    0,    8,
    9,    0,   10,    0,   93,    0,   95,    0,    0,   96,
    0,   94,    0,    0,    7,    0,   72,   67,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   11,    6,    0,    0,    0,    0,
   97,   85,    0,    0,    0,    0,    0,    0,   24,   25,
    0,   23,    0,   22,    0,    0,    0,   21,   30,   32,
    0,   33,   31,   80,   87,   89,   90,    0,   91,   88,
    0,    0,  117,   86,    0,    0,    0,   54,    0,    0,
    0,   34,   29,   92,   84,    0,   12,    0,   19,    0,
    0,    0,   15,   13,    0,  118,   14,
};
final static short yydgoto[] = {                          2,
    7,   26,    8,   28,   29,   30,   31,   45,  211,   46,
   47,  104,   32,    9,   10,   11,  212,   13,   14,   15,
  213,   65,   66,   67,  109,   33,   34,   35,   36,   82,
  143,   83,   84,
};
final static short yysindex[] = {                      -225,
 -162,    0,  -35,    0,    0,    0,  -33,    0,    0,    0,
    0, -221,  -20,   -9,  -49,   11, -176,  -31,  -29,  246,
   88,   -3,  -38,   97,  546,    0,    0,    0,    0, -167,
  157, -220,    0,    0,    0,    0,   57,   19,   62,    0,
 -158,  170,   -9,    0,    0,  103,    0,    0,   76, -156,
   75,   82,  298,  110,  572,   25,   88,    0,  116,   79,
    0,  371, -144,   23,   28,    0,    0, -127, -185,  110,
   39,   33,   98,    0,    0,    0,    0,    0,    0,  100,
  413,  -40,    0,   97, -124,    0,    0,  110,  110,  143,
  -18,  105, -156,    0,    0,    0,  184, -167,   29,    0,
  107,  -13,    0,    0,    0,    0,    0, -122,  114,    0,
   99,   -6,  120,  131,  104,    0,   60,    0,    0,    0,
  317,  117,   42,    0,    0,   97,   97,   97,   97,  246,
  246,  171, -128,    4, -125,    0,  111,   97,  358,    0,
    0,    0,  559,   38,  246,  -81,    0,  336,  136,    0,
  -27,    0,  139,  153,  129,  233,  546,  197, -167,  371,
  162,  174,    0,    0,    0,  -44,    0,  125,  132,    0,
    0,    0,  166,  175,   97,    0,    0,   28,   28,    0,
    0,  203,  219,  246,  232,  110,  211,  110,  242,  -51,
  110,  256,    0,  130,    0,  181,   38,    0,    0,  253,
  110,    0,   -8,    0,    0,    0,    0,  371,  316,  356,
  343,  345,   38,  353,  355,  413,  368,  142,  357,  386,
  533,  388,    0,    0,    0,    0,    0,    0,  144,    0,
    0,  155,    0,  -61,    0,  165,    0,   70,  -41,    0,
  173,    0,  383,   97,    0,  180,    0,    0,  408,  371,
  186,   15,  195,   97,  196,   97,  371,  425,  204,  412,
  417,    2,  419,  422,    0,    0,  428,  429,  435,  -46,
    0,    0,  437,  371,  145,  442,  452,  431,    0,    0,
  455,    0,  151,    0,  158,  443,  371,    0,    0,    0,
  -39,    0,    0,    0,    0,    0,    0,  122,    0,    0,
  478,   38,    0,    0,  254,  462,  257,    0,  383,  463,
  484,    0,    0,    0,    0,   97,    0,  261,    0,  269,
  479,  164,    0,    0,  278,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    3,    0,    0,    0,    0,    0,    0,    0,    0,  271,
    0,    0,    0,    0,    0,    0,    0,   86,    0,    0,
   93,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  498,
    0,    0,    0,    0,  448,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   74,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -16,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   84,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  487,  520,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   32,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   17,    0,    0,  507,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   45,    0,    0,    0,    0,    0,    0,  509,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  538,  771,   21,  -10,    0,  835,   34,    0,  -83,   10,
  510,    0,   -2,    0,    0,    0,    5,   78,    0,    0,
  -21,  -55,  -25,    0,  -64,    0,    0,    0,    0,    0,
    0,  416,  -65,
};
final static int YYTABLESIZE=1067;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         64,
  142,   55,   72,   81,   17,   12,   25,  240,   53,   48,
   55,   12,  299,  206,  224,  139,   56,  272,   12,  313,
   71,   51,  150,   41,  120,  149,  161,   27,  153,   80,
   25,   97,  248,   81,  166,   64,   25,   88,   40,   37,
  123,   92,  291,   25,  162,    1,  132,   12,   89,   38,
   50,   99,  167,   59,  108,  281,  174,   53,   93,   80,
  292,  132,  144,   27,   25,  126,  122,  127,  157,  128,
  178,  179,  115,  214,  129,  126,  219,  127,   25,  131,
  126,  125,  127,  203,  126,  116,  127,    4,  158,   39,
    5,  136,   49,  132,  108,  151,   91,  108,    3,   25,
  177,    4,  180,  181,    5,   41,  155,    4,    6,   25,
    5,   85,   96,   28,  107,  110,  194,  197,  121,   41,
   95,   81,  111,   26,  249,  108,  252,  124,  186,  132,
  187,  191,   63,  192,  106,  216,  133,  130,  137,  138,
  145,   63,   25,  196,  132,  152,  160,   80,  163,   25,
  139,  133,  108,  229,  164,   25,  175,  165,  220,  222,
  168,  215,  170,  182,  183,  185,  278,  218,   25,  193,
  243,  169,  126,  286,  127,  201,  204,  274,  200,  207,
  315,   25,  148,  225,  264,  303,  126,  126,  127,  127,
  226,  308,  208,  126,   25,  127,   25,  267,  309,   81,
  126,   55,  127,  311,  326,  238,  126,  239,  127,   25,
   25,  223,  298,  221,  271,  228,  312,  232,  262,   70,
  244,    6,  275,   25,  227,   80,   18,    3,  205,   19,
    4,   20,  283,    5,  285,   16,   25,   21,   52,   54,
  140,  141,   25,  274,   22,    4,   23,  247,    5,   24,
   18,    3,  302,   19,    4,   42,   18,    5,   25,   19,
  188,   21,  189,   18,  120,  120,   19,   57,   20,  235,
   23,   25,  210,   24,   57,   68,   23,   63,  301,   24,
  280,   22,   53,   23,   18,   25,   24,   19,   18,   20,
  156,   19,   25,  117,  322,   57,  134,  176,   18,   57,
  237,   19,   22,   20,   23,  102,   22,   24,   23,   57,
   36,   24,  115,  115,  242,  171,   22,   94,   23,   18,
    4,   24,   19,    5,   20,  116,  116,   90,  269,   18,
   57,  105,   19,   28,   20,   28,   28,   22,  115,   23,
   57,  132,   24,   26,   28,   26,   26,   22,  133,   23,
   28,   28,   24,   28,   26,  250,   28,  173,   60,   61,
   26,   26,   18,   26,  101,   19,   26,   60,   61,   18,
   62,  119,   19,   57,   20,   18,  202,  314,   19,  102,
   57,  120,   23,  253,  254,   24,   57,   22,   18,   23,
  209,   19,   24,  255,  256,   23,  251,  259,   24,   57,
   63,   18,   63,  258,   19,  102,    4,  257,   23,    5,
  265,   24,   57,  107,   18,   63,   18,   19,  102,   19,
  266,   23,   86,  268,   24,   57,  260,   57,  263,   18,
   18,  273,   19,   19,   23,  184,   23,   24,  276,   24,
   57,   57,   79,   18,   78,  154,   19,   22,  277,   23,
   23,  279,   24,   24,   57,  126,   18,  127,  217,   19,
  282,  284,   18,   23,  287,   19,   24,   57,  230,  288,
  289,  306,   79,   57,   78,  290,   23,  293,   18,   24,
  294,   19,   23,  310,  231,   24,  295,  296,   57,   57,
   57,   18,   57,  297,   19,  300,    4,  233,   23,    5,
  304,   24,   57,   60,   61,   18,   57,   57,   19,   57,
  305,   23,   18,  307,   24,   19,   57,  316,  245,  317,
  318,  320,  319,   57,  321,   23,  323,   55,   24,   55,
   36,   55,   23,   36,  324,   24,   36,  325,   61,   61,
   61,   36,   61,  327,   61,   55,   55,   53,   55,   54,
   36,   43,  100,   36,    0,    0,   61,   61,  199,   61,
   56,    0,   56,    0,   56,    0,    0,  112,  113,  114,
    0,    0,   55,  261,    0,    0,    0,   63,   56,   56,
    4,   56,    0,    5,    0,    0,    0,  107,    0,    0,
   63,    0,   79,    0,   78,    0,    0,    0,    0,    4,
    0,    0,    5,   63,    0,   79,  107,   78,    0,    0,
    0,    0,    0,  195,    0,    0,   63,    0,   79,    4,
   78,    4,    5,    0,    5,    0,   60,   61,   60,   61,
    0,   79,    0,   78,    4,    0,    0,    5,    0,    0,
    0,   60,   61,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   74,   75,   76,   77,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   74,   75,   76,   77,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   57,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   57,    0,    0,    0,    0,    0,    0,
   57,   57,   57,   57,    0,    0,    0,    0,   57,   57,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   55,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   55,   61,    0,    0,    0,    0,    0,   55,
   55,   55,   55,   61,    0,    0,    0,   55,   55,    0,
   61,   61,   61,   61,    0,   56,    0,    0,   61,   61,
    0,    0,    0,    0,    0,   56,    0,    0,    0,    0,
    0,    0,   56,   56,   56,   56,    4,    0,    0,    5,
   56,   56,    0,   60,   61,   74,   75,   76,   77,    4,
    0,    0,    5,    0,  198,   73,   60,   61,   74,   75,
   76,   77,    4,    0,  116,    5,  118,    0,    0,   60,
   61,   74,   75,   76,   77,    4,    0,    0,    5,    0,
  133,  135,   60,   61,   74,   75,   76,   77,   44,    0,
    0,    0,    0,    0,   58,    0,   69,    0,  146,  147,
    0,    0,    0,    0,    0,   87,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   98,   44,    0,    0,
  103,    0,    0,    0,    0,    0,    0,  172,    0,    0,
    0,    0,    0,   87,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  190,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   44,    0,  159,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  234,    0,  236,    0,
    0,  241,    0,    0,   58,   58,   58,    0,    0,    0,
    0,  246,    0,    0,    0,    0,    0,    0,    0,   58,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  103,
    0,    0,   44,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  270,    0,
    0,    0,    0,    0,    0,    0,   87,   87,   58,   87,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   87,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  103,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   87,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         21,
   41,   40,   24,   25,   40,    1,   40,   59,   40,   59,
   40,    7,   59,   41,   59,   81,   19,   59,   14,   59,
   23,   17,   41,   44,   41,   90,   40,    7,   93,   25,
   40,   42,   41,   55,   41,   57,   40,  258,   59,  261,
   62,   37,   41,   40,   58,  271,   44,   43,  269,  271,
   40,   42,   59,   20,   50,   41,  121,   41,   40,   55,
   59,   59,   84,   43,   40,   43,   62,   45,   40,   42,
  126,  127,   41,  157,   47,   43,  160,   45,   40,  265,
   43,   59,   45,  148,   43,   41,   45,  264,   99,   12,
  267,   59,   15,  279,   90,   91,   40,   93,  261,   40,
   59,  264,  128,  129,  267,   44,   97,  264,  271,   40,
  267,  279,  271,   40,  271,   41,  138,  139,   40,   44,
   59,  143,   41,   40,  208,  121,  210,  272,  257,   44,
  259,  257,   45,  259,   59,  157,   44,  265,   41,   40,
  265,   45,   40,  139,   59,   41,   40,  143,  271,   40,
  216,   59,  148,  175,   41,   40,   40,   59,  161,  162,
   41,  157,   59,  130,  131,  132,  250,  158,   40,   59,
   41,   41,   43,  257,   45,  257,   41,  243,  145,   41,
   59,   40,   40,   59,   41,   41,   43,   43,   45,   45,
   59,   41,   40,   43,   40,   45,   40,  259,   41,  221,
   43,   40,   45,  287,   41,  257,   43,  259,   45,   40,
   40,  256,  259,   40,  256,   41,  256,  184,  221,  258,
   40,  271,  244,   40,   59,  221,  260,  261,  256,  263,
  264,  265,  254,  267,  256,  271,   40,  271,  270,  269,
  281,  282,   40,  309,  278,  264,  280,  256,  267,  283,
  260,  261,  274,  263,  264,  265,  260,  267,   40,  263,
  257,  271,  259,  260,  281,  282,  263,  271,  265,   59,
  280,   40,   40,  283,  271,  279,  280,   45,  274,  283,
  266,  278,  266,  280,  260,   40,  283,  263,  260,  265,
  262,  263,   40,  269,  316,  271,  258,  256,  260,  271,
   59,  263,  278,  265,  280,  277,  278,  283,  280,  271,
   40,  283,  281,  282,   59,  256,  278,  256,  280,  260,
  264,  283,  263,  267,  265,  281,  282,  271,  259,  260,
  271,  256,  263,  260,  265,  262,  263,  278,   41,  280,
  271,  256,  283,  260,  271,  262,  263,  278,  256,  280,
  277,  278,  283,  280,  271,   40,  283,   41,  271,  272,
  277,  278,  260,  280,  262,  263,  283,  271,  272,  260,
  283,  256,  263,  271,  265,  260,   41,  256,  263,  277,
  271,  266,  280,   41,   40,  283,  271,  278,  260,  280,
  262,  263,  283,   41,   40,  280,   41,   41,  283,  271,
   45,  260,   45,  262,  263,  277,  264,   40,  280,  267,
  256,  283,  271,  271,  260,   45,  260,  263,  277,  263,
  266,  280,  266,  259,  283,  271,   41,  271,   41,  260,
  260,  259,  263,  263,  280,  265,  280,  283,  259,  283,
  271,  271,   60,  260,   62,  262,  263,  278,   41,  280,
  280,  266,  283,  283,  271,   43,  260,   45,  262,  263,
  266,  266,  260,  280,   40,  263,  283,  271,  266,  266,
   59,   41,   60,  271,   62,   59,  280,   59,  260,  283,
   59,  263,  280,   41,  266,  283,   59,   59,   41,  271,
   43,  260,   45,   59,  263,   59,  264,  266,  280,  267,
   59,  283,  271,  271,  272,  260,   59,   60,  263,   62,
   59,  280,  260,   59,  283,  263,  271,   40,  266,  266,
   59,   59,  266,  271,   41,  280,  266,   41,  283,   43,
  260,   45,  280,  263,  266,  283,  266,   59,   41,   42,
   43,  271,   45,  266,   47,   59,   60,   41,   62,   41,
  280,   14,   43,  283,   -1,   -1,   59,   60,  143,   62,
   41,   -1,   43,   -1,   45,   -1,   -1,  270,  271,  272,
   -1,   -1,   40,   41,   -1,   -1,   -1,   45,   59,   60,
  264,   62,   -1,  267,   -1,   -1,   -1,  271,   -1,   -1,
   45,   -1,   60,   -1,   62,   -1,   -1,   -1,   -1,  264,
   -1,   -1,  267,   45,   -1,   60,  271,   62,   -1,   -1,
   -1,   -1,   -1,  256,   -1,   -1,   45,   -1,   60,  264,
   62,  264,  267,   -1,  267,   -1,  271,  272,  271,  272,
   -1,   60,   -1,   62,  264,   -1,   -1,  267,   -1,   -1,
   -1,  271,  272,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  273,  274,  275,  276,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  273,  274,  275,  276,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  266,   -1,   -1,   -1,   -1,   -1,   -1,
  273,  274,  275,  276,   -1,   -1,   -1,   -1,  281,  282,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  266,  256,   -1,   -1,   -1,   -1,   -1,  273,
  274,  275,  276,  266,   -1,   -1,   -1,  281,  282,   -1,
  273,  274,  275,  276,   -1,  256,   -1,   -1,  281,  282,
   -1,   -1,   -1,   -1,   -1,  266,   -1,   -1,   -1,   -1,
   -1,   -1,  273,  274,  275,  276,  264,   -1,   -1,  267,
  281,  282,   -1,  271,  272,  273,  274,  275,  276,  264,
   -1,   -1,  267,   -1,  256,  270,  271,  272,  273,  274,
  275,  276,  264,   -1,   54,  267,   56,   -1,   -1,  271,
  272,  273,  274,  275,  276,  264,   -1,   -1,  267,   -1,
   70,   71,  271,  272,  273,  274,  275,  276,   14,   -1,
   -1,   -1,   -1,   -1,   20,   -1,   22,   -1,   88,   89,
   -1,   -1,   -1,   -1,   -1,   31,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   42,   43,   -1,   -1,
   46,   -1,   -1,   -1,   -1,   -1,   -1,  117,   -1,   -1,
   -1,   -1,   -1,   59,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  134,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   97,   -1,   99,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  186,   -1,  188,   -1,
   -1,  191,   -1,   -1,  130,  131,  132,   -1,   -1,   -1,
   -1,  201,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  145,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  155,
   -1,   -1,  158,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  238,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  182,  183,  184,  185,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  200,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  218,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  232,
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
"bloqueEjecutableFunc : TRYCATCHFunc",
"bloqueEjecutableFunc : sentEjecutableFunc RETURN '(' retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc '(' retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' retorno END",
"sentEjecutableFunc : sentEjecutableFunc sentEjecutables",
"sentEjecutableFunc : sentEjecutableFunc sentenciaCONTRACT",
"sentEjecutableFunc : sentEjecutables",
"sentenciaCONTRACT : CONTRACT ':' '(' condicion ')' ';'",
"sentenciaCONTRACT : CONTRACT '(' condicion ')' ';'",
"sentenciaCONTRACT : CONTRACT ':' condicion ')' ';'",
"sentenciaCONTRACT : CONTRACT ':' '(' ')' ';'",
"sentenciaCONTRACT : CONTRACT ':' '(' condicion ';'",
"sentenciaCONTRACT : CONTRACT ':' '(' condicion ')' error",
"sentSoloEjecutables : sentSoloEjecutables sentEjecutables",
"sentSoloEjecutables : sentEjecutables",
"sentenciasDeclarativas : declaraVariable",
"sentenciasDeclarativas : declaraFunc",
"sentenciasDeclarativas : declaraVarFunc",
"declaraVariable : tipo listaVariables ';'",
"declaraVariable : listaVariables ';'",
"declaraVariable : tipo listaVariables error",
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
"llamadoFunc : ID '(' parametro ')'",
"llamadoFunc : ID '(' ')' ';'",
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
"asignacion : ID ASIGN tipo '(' expAritmetica ')' ';'",
"asignacion : ASIGN expAritmetica ';'",
"asignacion : ID expAritmetica ';'",
"asignacion : ID ASIGN expAritmetica error",
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
"opLogico : AND",
"opLogico : OR",
"tipo : LONG",
"tipo : SINGLE",
"listaVariables : listaVariables ',' ID",
"listaVariables : ID",
"listaVariables : listaVariables ','",
};

//#line 406 "gramatica.y"


public int yylex() {
    int value = AnalizadorLexico.yylex();
    yylval = new ParserVal(AnalizadorLexico.refTDS); 
    return value;
}

public void yyerror(String string) {
	AnalizadorSintactico.agregarError("Parser: " + string);
}
//#line 677 "Parser.java"
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
			yyval= new ParserVal(new Nodo(val_peek(2).sval, null, ((Nodo)val_peek(0).obj)));
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
{yyval=val_peek(0);}
break;
case 21:
//#line 84 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 22:
//#line 85 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta RETURN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 23:
//#line 86 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 24:
//#line 87 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta retorno (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 25:
//#line 88 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 26:
//#line 94 "gramatica.y"
{ if ((val_peek(1).obj != null) && (val_peek(0).obj != null))
				 	{yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, (Nodo)val_peek(1).obj));}
				  else if((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, null));}
				       else if ((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));}
				}
break;
case 27:
//#line 100 "gramatica.y"
{ if ((val_peek(1).obj != null) && (val_peek(0).obj != null))
				 	{yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, (Nodo)val_peek(1).obj));}
				  else if((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, null));}
				       else if ((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));}
				}
break;
case 28:
//#line 106 "gramatica.y"
{
          		  yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
          		}
break;
case 29:
//#line 111 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sent control (Linea " + AnalizadorLexico.numLinea + ")");
            yyval =  new ParserVal(new Nodo("CONTRACT",(Nodo)val_peek(2).obj,null));
            }
break;
case 30:
//#line 114 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 31:
//#line 115 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 32:
//#line 116 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 33:
//#line 117 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 34:
//#line 118 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 35:
//#line 124 "gramatica.y"
{ if ((val_peek(1).obj != null) && (val_peek(0).obj != null))
				 	{yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, (Nodo)val_peek(1).obj));}
				  else if((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, null));}
				       else if ((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));}
				}
break;
case 36:
//#line 130 "gramatica.y"
{
				    yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));				    
				}
break;
case 38:
//#line 138 "gramatica.y"
{yyval=val_peek(0);}
break;
case 40:
//#line 142 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 41:
//#line 143 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 42:
//#line 144 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 43:
//#line 147 "gramatica.y"
{
        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
        yyval=new ParserVal (new Nodo(val_peek(2).sval, (Nodo)val_peek(0).obj, null));}
break;
case 44:
//#line 150 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 45:
//#line 153 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 46:
//#line 154 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 47:
//#line 155 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 49:
//#line 159 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 50:
//#line 160 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 51:
//#line 161 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 52:
//#line 162 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 53:
//#line 166 "gramatica.y"
{yyval=val_peek(0);}
break;
case 55:
//#line 175 "gramatica.y"
{
					  yyval= new ParserVal(new Nodo("+", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                                          String tipo = AnalizadorSintactico.calculadorTipo("+",((Nodo)val_peek(2).obj).getTipo(),((Nodo)val_peek(0).obj).getTipo());
                                          ((Nodo)yyval.obj).setTipo(tipo);
                                          }
break;
case 56:
//#line 180 "gramatica.y"
{
					                  yyval= new ParserVal(new Nodo("-", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
					                  String tipo = AnalizadorSintactico.calculadorTipo("-",((Nodo)val_peek(2).obj).getTipo(),((Nodo)val_peek(0).obj).getTipo());
                                      ((Nodo)yyval.obj).setTipo(tipo);
					                  }
break;
case 57:
//#line 185 "gramatica.y"
{
	                 yyval=val_peek(0);
	                 }
break;
case 58:
//#line 194 "gramatica.y"
{
				                yyval= new ParserVal(new Nodo("*", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                                String tipo = AnalizadorSintactico.calculadorTipo("*",((Nodo)val_peek(2).obj).getTipo(),((Nodo)val_peek(0).obj).getTipo());
                                ((Nodo)yyval.obj).setTipo(tipo);
				                }
break;
case 59:
//#line 199 "gramatica.y"
{
				            yyval= new ParserVal(new Nodo("/", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                            String tipo = AnalizadorSintactico.calculadorTipo("/",((Nodo)val_peek(2).obj).getTipo(),((Nodo)val_peek(0).obj).getTipo());
                            ((Nodo)yyval.obj).setTipo(tipo);
				            }
break;
case 60:
//#line 204 "gramatica.y"
{
	                        yyval = val_peek(0);
	                        }
break;
case 61:
//#line 213 "gramatica.y"
{    yyval= new ParserVal(new Nodo(val_peek(0).sval));			
                     TDSObject value = AnalizadorLexico.getLexemaObject(val_peek(0).sval);
                     if( value != null){
			
                        ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                     }else{
                        AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                        /*stop generacion de arbol*/
                     }
                }
break;
case 62:
//#line 223 "gramatica.y"
{   yyval= new ParserVal(new Nodo(val_peek(0).sval));
	                TDSObject value = AnalizadorLexico.getLexemaObject(val_peek(0).sval);
	                if( value != null){
                        ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                    }else{
                        AnalizadorSintactico.agregarError("CTE no definida (Linea " + AnalizadorLexico.numLinea + ")");
                        /*stop generacion de arbol*/
                    }
	            }
break;
case 63:
//#line 232 "gramatica.y"
{   AnalizadorLexico.agregarNegativoTDS(val_peek(0).sval);
			        yyval= new ParserVal(new Nodo("-"+val_peek(0).sval));
			        TDSObject value = AnalizadorLexico.getLexemaObject(val_peek(1).sval);
                    if( value != null){
                        ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                    }else{
                        AnalizadorSintactico.agregarError("CTE negativa no definida (Linea " + AnalizadorLexico.numLinea + ")");
                        /*stop generacion de arbol*/
                    }
			    }
break;
case 64:
//#line 242 "gramatica.y"
{yyval=val_peek(0);}
break;
case 65:
//#line 246 "gramatica.y"
{ ParserVal aux= new ParserVal(val_peek(3).sval);
		  yyval= new ParserVal(new Nodo("LF",(Nodo)aux.obj, (Nodo)val_peek(1).obj ));
		  TDSObject value = AnalizadorLexico.getLexemaObject(val_peek(3).sval);
                     if( value != null){
                        ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                     }else{
                        AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                        /*stop generacion de arbol*/
                     }}
break;
case 67:
//#line 260 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
			yyval=val_peek(3);}
break;
case 68:
//#line 262 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 69:
//#line 263 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 70:
//#line 264 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 71:
//#line 265 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 72:
//#line 266 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 73:
//#line 270 "gramatica.y"
{yyval= new ParserVal(new Nodo(val_peek(0).sval));
		     TDSObject value = AnalizadorLexico.getLexemaObject(val_peek(0).sval);
                     if( value != null){
                        ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                     }else{
                        AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                        /*stop generacion de arbol*/
						}
		     }
break;
case 74:
//#line 279 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 75:
//#line 284 "gramatica.y"
{yyval=val_peek(0);}
break;
case 76:
//#line 285 "gramatica.y"
{yyval=val_peek(0);}
break;
case 77:
//#line 286 "gramatica.y"
{yyval=val_peek(0);}
break;
case 78:
//#line 287 "gramatica.y"
{yyval=val_peek(0);}
break;
case 79:
//#line 290 "gramatica.y"
{
                    AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion (Linea " + AnalizadorLexico.numLinea + ")");
					ParserVal aux = new ParserVal(new Nodo(val_peek(3).sval));
					TDSObject value = AnalizadorLexico.getLexemaObject(val_peek(3).sval);
                    if( value != null){
                        ((Nodo)aux.obj).setTipo(value.getTipoVariable());
                    }
					else
					{
                        AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                        /*stop generacion de arbol*/
                        /*return*/
                    }
					yyval= new ParserVal(new Nodo(":=", (Nodo)aux.obj, (Nodo)val_peek(1).obj));
					if(((Nodo)aux.obj).getTipo() == ((Nodo)val_peek(1).obj).getTipo()){
					    ((Nodo)yyval.obj).setTipo(((Nodo)aux.obj).getTipo());
					}else{
					    AnalizadorSintactico.agregarError("Tipo Incompatible (" + ((Nodo)aux.obj).getTipo() + "," +  ((Nodo)val_peek(1).obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
					}
					
					}
break;
case 80:
//#line 311 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 81:
//#line 312 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 82:
//#line 313 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 83:
//#line 314 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 84:
//#line 318 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 85:
//#line 324 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 86:
//#line 325 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 87:
//#line 326 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 88:
//#line 327 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 89:
//#line 328 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 90:
//#line 329 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 91:
//#line 330 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 92:
//#line 331 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 93:
//#line 332 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 94:
//#line 333 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 95:
//#line 334 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 96:
//#line 335 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 97:
//#line 336 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 98:
//#line 339 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 99:
//#line 343 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 100:
//#line 344 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 345 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 102:
//#line 346 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 103:
//#line 347 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 106:
//#line 352 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 107:
//#line 354 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 108:
//#line 355 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 109:
//#line 356 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 110:
//#line 357 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 111:
//#line 361 "gramatica.y"
{yyval=val_peek(1);}
break;
case 113:
//#line 365 "gramatica.y"
{yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(0).obj, null));}
break;
case 114:
//#line 366 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 115:
//#line 370 "gramatica.y"
{ 
		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
		}
break;
case 119:
//#line 376 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 120:
//#line 377 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 121:
//#line 380 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 122:
//#line 381 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 123:
//#line 382 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 124:
//#line 383 "gramatica.y"
{yyval = new ParserVal("!=");}
break;
case 125:
//#line 384 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 126:
//#line 385 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 133:
//#line 400 "gramatica.y"
{AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
//#line 1424 "Parser.java"
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
