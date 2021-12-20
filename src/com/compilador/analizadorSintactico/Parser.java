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
    0,    1,    1,    2,    5,    5,    5,    5,    5,    5,
    4,    4,    4,    8,    8,    8,    8,    8,    8,    8,
    9,    9,   11,   11,    6,    6,    6,    6,    6,    6,
    7,    7,    3,    3,    3,   14,   14,   14,   14,   14,
   18,   18,   18,   15,   15,   16,   16,   16,   20,   20,
   20,   20,   20,   10,   10,   21,   21,   21,   22,   22,
   22,   23,   23,   23,   23,   24,   19,   19,   19,   19,
   19,   25,   25,   12,   12,   12,   12,   26,   26,   26,
   26,   26,   26,   26,   26,   26,   26,   27,   27,   27,
   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,
   27,   28,   28,   28,   28,   28,   28,   28,   28,   28,
   29,   29,   29,   29,   29,   13,   31,   31,   31,   30,
   30,   30,   30,   30,   30,   33,   33,   33,   33,   33,
   33,   33,   32,   32,   17,   17,
};
final static short yylen[] = {                            2,
    3,    2,    1,    1,    6,    5,    5,    5,    5,    6,
    3,    2,    3,    8,    7,    6,    6,    6,    6,    6,
    2,    1,    1,    1,    4,    5,    5,    5,    5,    6,
    2,    1,    1,    1,    1,    3,    2,    3,    2,    2,
    3,    1,    2,    3,    2,    3,    2,    3,    5,    4,
    4,    4,    5,    1,    4,    3,    3,    1,    3,    3,
    1,    1,    1,    2,    1,    4,    6,    5,    5,    5,
    6,    2,    1,    1,    1,    1,    1,    4,    7,    4,
    7,    3,    3,    4,    3,    3,    4,    8,    6,    7,
    7,    7,    7,    7,    7,    8,    5,    5,    5,    5,
    6,    5,    4,    4,    4,    4,    5,    5,    5,    6,
    4,    3,    3,    3,    4,    3,    3,    1,    3,    3,
    6,    6,    9,    3,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,  135,  136,    0,    0,    3,   33,   34,
   35,    0,    0,    0,    0,    0,    0,   39,    0,    0,
    0,    0,    0,    0,    0,    1,    2,    4,    0,    0,
    0,   74,   75,   76,   77,   40,    0,    0,    0,   37,
    0,    0,    0,    0,    0,   24,    0,   45,    0,   22,
   23,   42,   47,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   63,    0,    0,    0,    0,    0,
   61,   65,    0,    0,    0,    0,  128,  129,  130,  131,
  132,  126,  127,    0,    0,  118,    0,    0,   12,   31,
    0,    0,    0,    0,    0,    0,   38,   36,   41,    0,
    0,    0,    0,    0,    0,   44,    0,    0,   21,   48,
   46,   73,    0,    0,   50,    0,    0,    0,    0,    0,
    0,  113,    0,    0,  114,   13,   11,    0,   85,    0,
    0,    0,   64,   86,    0,   83,    0,    0,    0,    0,
    0,    0,    0,   82,    0,    0,    0,    0,  133,  134,
  116,    0,    0,    0,  112,    0,    0,   52,    0,   51,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   72,   68,  104,    0,  106,    0,    0,  105,
    0,  115,  111,    0,    0,   84,   78,   87,   80,    0,
    0,   59,   60,    0,    0,    0,    0,    0,    0,    0,
  103,    0,    0,  124,    0,    0,  119,  117,    0,    0,
   70,   53,   49,   69,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   25,    0,    0,    0,
    0,    0,    0,  107,  102,  108,  109,    0,   66,    0,
    0,   97,    0,   99,    0,    0,  100,    0,   98,    0,
    0,    0,    0,   71,   67,    0,    0,    0,    0,    0,
    0,    0,   26,   28,    0,   29,   27,    7,    8,    0,
    9,    6,    0,  110,    0,    0,    0,    0,    0,  101,
   89,    0,    0,    0,    0,    0,    0,    0,   19,   20,
    0,   18,   17,    0,   30,   10,    5,   16,   81,   91,
   93,   94,    0,   95,   92,    0,    0,   79,  122,   90,
   15,   55,    0,    0,   96,   88,    0,   14,    0,  123,
};
final static short yydgoto[] = {                          2,
    7,   26,    8,   28,   46,   47,   29,   48,   49,  215,
   50,   30,   31,    9,   10,   11,   12,   13,   14,   15,
   85,   70,   71,   72,  114,   32,   33,   34,   35,   86,
   87,  152,   88,
};
final static short yysindex[] = {                      -246,
  223,    0,  -37,    0,    0, -223,  -18,    0,    0,    0,
    0, -196,   10,  -39,  -52,   18, -157,    0,  -34,  -36,
  109,   -8,  -35,   85,  431,    0,    0,    0, -224,  109,
 -206,    0,    0,    0,    0,    0,   27,   41,  -29,    0,
 -207,   -4,  -17, -253,  -39,    0, -160,    0,    9,    0,
    0,    0,    0,   34,  -89,   84,   92,  225,   95,  470,
   54,  115, -186,  105,    0,  444, -119,   50,   57,   45,
    0,    0,   95,   68,   58,  124,    0,    0,    0,    0,
    0,    0,    0,  144,  417,    0,  -27,   85,    0,    0,
   95,   95,   65,   -7,  136,  -89,    0,    0,    0,  140,
   22,  167,  172,   53, -147,    0,   78,  174,    0,    0,
    0,    0,   12,  196,    0,  251,  -28,  289,  294,  291,
   87,    0,  314,   82,    0,    0,    0,   96,    0,  323,
  413,  311,    0,    0,   61,    0,   85,   85,   85,   85,
  -86,   44,  -64,    0,  325,   85,  119,  271,    0,    0,
    0,  457,   94,  142,    0,  -89,  365,    0,  -25,    0,
  366,  110,  175,  431,  369,  404,  -12,  109,  109,  108,
  109,  110,    0,    0,    0,  -51,    0,  362,  379,    0,
  399,    0,    0,  400,   85,    0,    0,    0,    0,   45,
   45,    0,    0,   95,  384,   95,  389,  -41,   95,  395,
    0,  111,  178,    0,  423,   94,    0,    0,   95,   32,
    0,    0,    0,    0,  427,  430,   94,  236,  439,  441,
  161,  417,  429,  442,   -2,  450,    0,  232,  233,  109,
  244,  245,  471,    0,    0,    0,    0,  454,    0,  121,
  261,    0,  262,    0,   81,  -50,    0,  263,    0,  356,
   85,   85,  269,    0,    0,  474,   85,  250,  -14,  268,
  270,   85,    0,    0,  273,    0,    0,    0,    0, -122,
    0,    0,  274,    0,  367,  478,  482,  485,  -42,    0,
    0,  486,  110,   70,  392,  487,  281,  414,    0,    0,
  489,    0,    0,  426,    0,    0,    0,    0,    0,    0,
    0,    0,   64,    0,    0,  509,   94,    0,    0,    0,
    0,    0,  284,  356,    0,    0,   85,    0,  440,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,   24,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   24,    0,    0,    0,    0,    0,    0,    0, -120,
    0,    0,    0,    0,    0,    0,    0,   52,    0,    0,
   55,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  127,    0,    0,    0,    0,    0,  138,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -31,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  149,
  391,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   15,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    2,    0,    0,    0,
    0,  510,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   31,    0,    0,    0,
    0,    0,    0,  511,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  539,  666,   14,    0,    0,  512,  355,  513,  515,  -87,
  -30,   37,   25,    0,    0,    0,   -5,   97,    0,    0,
  -22,   66,  257,    0,  -58,    0,    0,    0,    0,  -55,
    0,    0,  264,
};
final static int YYTABLESIZE=911;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         69,
   25,   75,   17,   60,   60,   58,   53,  235,  281,  125,
  132,   56,  176,  151,   41,  213,  304,  247,  109,   84,
   27,   25,  102,   43,    1,  104,  291,  125,  226,   98,
  177,   95,   18,  158,  157,   25,   67,  161,  265,   69,
  103,   89,   54,  131,   61,  135,  227,   74,   25,  113,
   51,   91,   68,   41,  123,  120,  266,   55,   27,   36,
  130,  164,   92,   99,   37,  153,   94,   42,   40,  126,
  109,  121,  255,  120,   38,  219,  220,   41,   51,  127,
   96,   51,   42,   25,  233,   51,  139,  113,  159,  121,
  113,  140,  111,   25,   67,   42,  208,  210,   43,  137,
  137,  138,  138,  137,  156,  138,    4,   25,   39,    5,
   42,   54,  137,   43,  138,  136,  144,  169,  107,  189,
   25,   25,  316,  202,  115,  206,  165,  167,  308,   67,
  259,  170,  116,  296,   25,   32,  137,   51,  138,  217,
  217,  222,  205,  297,  128,   32,  123,   25,   25,  217,
  113,  250,  133,  137,   67,  138,  216,  216,  221,   67,
  123,  275,  240,  137,  145,  138,  216,   62,   62,   62,
  194,   62,  195,   62,    4,   68,  160,    5,   58,  162,
   58,  112,   58,  146,  147,   62,   62,   62,   62,   56,
  225,   56,  199,   56,  200,  217,   58,   58,   58,   58,
  262,  147,  190,  191,  234,  280,   60,   56,   56,   56,
   56,  166,  216,  172,  218,  245,  303,  246,   52,   67,
   19,    3,   73,   20,    4,   42,   97,    5,  284,  285,
  212,   22,   59,   16,  288,   57,  174,   43,   44,  294,
   23,   19,    3,   24,   20,    4,   21,   18,    5,  125,
  125,  290,   22,  149,  150,   19,    4,  100,   20,    5,
  307,   23,   64,   65,   24,  120,   62,   54,   19,  121,
  108,   20,   43,   44,   66,   23,  258,  306,   24,   62,
   67,   19,  173,  163,   20,   43,   44,  254,   23,  110,
    4,   24,   62,    5,  319,  120,  120,   93,   43,   44,
  196,   23,  197,   19,   24,  134,   20,   42,   21,  175,
   43,  121,  121,   19,   62,   67,   20,  168,   21,  315,
   64,   65,  124,   23,   62,  142,   24,   19,    4,  178,
   20,    5,   21,   23,  179,  112,   24,  182,   62,  278,
   19,   19,  171,   20,   20,   21,   21,   23,  148,  180,
   24,   62,   62,  146,   19,   64,   65,   20,  181,   21,
   23,   23,  185,   24,   24,   62,  184,   19,   19,  188,
   20,   20,  230,    4,   23,   63,    5,   24,   62,   62,
   64,   65,   62,  201,   90,   64,   65,   23,   23,  203,
   24,   24,   62,   58,  148,  192,  193,   66,  209,   62,
   62,   62,   62,   58,   56,  211,  214,   62,   62,  223,
   58,   58,   58,   58,   56,   83,   81,   82,   58,   58,
  236,   56,   56,   56,   56,  299,   83,   81,   82,   56,
   56,   57,  309,   57,  137,   57,  138,  237,    4,  238,
  239,    5,  242,   60,  224,   64,   65,  244,   67,   57,
   57,   57,   57,  249,  312,  137,  137,  138,  138,  137,
  251,  138,  252,   83,   81,   82,  314,  256,  137,  257,
  138,  187,   83,   81,   82,   67,   83,   81,   82,  260,
  320,  261,  137,    3,  138,  148,    4,  263,   67,    5,
   83,   81,   82,    6,  117,  118,  119,  268,  269,    4,
  264,   67,    5,   83,   81,   82,   64,   65,  267,  271,
  272,  273,  274,  283,   67,  289,   83,   81,   82,  276,
  277,  282,  228,  229,  231,  232,  204,  286,  295,   83,
   81,   82,  287,  292,    4,  293,  300,    5,  283,  298,
  301,   64,   65,  302,  305,  310,  311,  313,  317,  318,
   54,   55,   45,    0,    0,  105,  101,  106,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  283,    0,    0,
    0,    0,    0,    0,  270,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   77,   78,
   79,   80,    0,    0,    0,    0,    0,    0,    0,   77,
   78,   79,   80,    0,    0,    0,   57,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   57,    0,    0,    0,
    0,    0,    0,   57,   57,   57,   57,    4,  186,    0,
    5,   57,   57,    0,   64,   65,   77,   78,   79,   80,
    0,    0,    0,    0,    0,   77,   78,   79,   80,   77,
   78,   79,   80,    0,    4,    0,    0,    5,    0,  129,
   76,   64,   65,   77,   78,   79,   80,    4,    0,    0,
    5,    0,  207,    0,   64,   65,   77,   78,   79,   80,
    4,    0,    0,    5,  122,    0,  125,   64,   65,   77,
   78,   79,   80,    4,    0,    0,    5,    0,  141,  143,
   64,   65,   77,   78,   79,   80,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  154,  155,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  183,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  198,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  241,
    0,  243,    0,    0,  248,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  253,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  279,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         22,
   40,   24,   40,   40,   40,   40,   59,   59,   59,   41,
   66,   17,   41,   41,   44,   41,   59,   59,   49,   25,
    7,   40,   40,  277,  271,  279,   41,   59,   41,   59,
   59,   37,  256,   41,   93,   40,   45,   96,   41,   62,
   58,  266,   41,   66,   20,   68,   59,   23,   40,   55,
   14,  258,   61,   44,   60,   41,   59,   40,   45,  256,
   66,   40,  269,  271,  261,   88,   40,   44,   59,  256,
  101,   41,   41,   59,  271,  163,  164,   44,   42,  266,
   40,   45,   59,   40,  172,   49,   42,   93,   94,   59,
   96,   47,   59,   40,   45,   44,  152,  156,   44,   43,
   43,   45,   45,   43,   40,   45,  264,   40,   12,  267,
   59,   15,   43,   59,   45,   59,   59,  265,  279,   59,
   40,   40,   59,  146,   41,  148,  102,  103,   59,   45,
  218,  279,   41,  256,   40,  256,   43,  101,   45,  162,
  163,  164,  148,  266,   40,  266,  152,   40,   40,  172,
  156,   41,  272,   43,   45,   45,  162,  163,  164,   45,
  166,   41,  185,   43,   41,   45,  172,   41,   42,   43,
  257,   45,  259,   47,  264,   61,   41,  267,   41,   40,
   43,  271,   45,   40,   41,   59,   60,   61,   62,   41,
  166,   43,  257,   45,  259,  218,   59,   60,   61,   62,
   40,   41,  137,  138,  256,  256,   40,   59,   60,   61,
   62,   40,  218,   40,   40,  257,  259,  259,  271,   45,
  260,  261,  258,  263,  264,  265,  256,  267,  251,  252,
  256,  271,  269,  271,  257,  270,   41,  277,  278,  262,
  280,  260,  261,  283,  263,  264,  265,  256,  267,  281,
  282,  266,  271,  281,  282,  260,  264,  262,  263,  267,
  283,  280,  271,  272,  283,   41,  271,  266,  260,   45,
  262,  263,  277,  278,  283,  280,   41,  283,  283,  271,
   45,  260,  271,  262,  263,  277,  278,  256,  280,  256,
  264,  283,  271,  267,  317,  281,  282,  271,  277,  278,
  257,  280,  259,  260,  283,  256,  263,  256,  265,   59,
  256,  281,  282,  260,  271,   45,  263,  265,  265,  256,
  271,  272,  269,  280,  271,  258,  283,  260,  264,   41,
  263,  267,  265,  280,   41,  271,  283,  256,  271,  259,
  260,  260,  265,  263,  263,  265,  265,  280,   85,   59,
  283,  271,  271,   40,  260,  271,  272,  263,  272,  265,
  280,  280,   40,  283,  283,  271,  271,  260,  260,   59,
  263,  263,  265,  264,  280,   21,  267,  283,  271,  271,
  271,  272,  256,   59,   30,  271,  272,  280,  280,  271,
  283,  283,  266,  256,  131,  139,  140,  283,  257,  273,
  274,  275,  276,  266,  256,   41,   41,  281,  282,   41,
  273,  274,  275,  276,  266,   60,   61,   62,  281,  282,
   59,  273,  274,  275,  276,   59,   60,   61,   62,  281,
  282,   41,   41,   43,   43,   45,   45,   59,  264,   41,
   41,  267,   59,   40,   41,  271,  272,   59,   45,   59,
   60,   61,   62,   59,   41,   43,   43,   45,   45,   43,
  283,   45,   40,   60,   61,   62,   41,   41,   43,   40,
   45,   59,   60,   61,   62,   45,   60,   61,   62,   41,
   41,   41,   43,  261,   45,  222,  264,   59,   45,  267,
   60,   61,   62,  271,  270,  271,  272,  266,  266,  264,
   59,   45,  267,   60,   61,   62,  271,  272,   59,  266,
  266,   41,   59,  250,   45,  266,   60,   61,   62,  259,
  259,  259,  168,  169,  170,  171,  256,  259,  256,   60,
   61,   62,   59,  266,  264,  266,   59,  267,  275,  266,
   59,  271,  272,   59,   59,   59,  266,   59,   40,  266,
   41,   41,   14,   -1,   -1,   44,   42,   45,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  314,   -1,   -1,
   -1,   -1,   -1,   -1,  230,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  273,  274,
  275,  276,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  273,
  274,  275,  276,   -1,   -1,   -1,  256,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  266,   -1,   -1,   -1,
   -1,   -1,   -1,  273,  274,  275,  276,  264,  256,   -1,
  267,  281,  282,   -1,  271,  272,  273,  274,  275,  276,
   -1,   -1,   -1,   -1,   -1,  273,  274,  275,  276,  273,
  274,  275,  276,   -1,  264,   -1,   -1,  267,   -1,  256,
  270,  271,  272,  273,  274,  275,  276,  264,   -1,   -1,
  267,   -1,  256,   -1,  271,  272,  273,  274,  275,  276,
  264,   -1,   -1,  267,   59,   -1,   61,  271,  272,  273,
  274,  275,  276,  264,   -1,   -1,  267,   -1,   73,   74,
  271,  272,  273,  274,  275,  276,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   91,   92,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  124,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  142,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  194,
   -1,  196,   -1,   -1,  199,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  209,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  245,
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
"bloqueEjecutable : bloqueEjecutableNormal",
"bloqueTRYCATCH : TRY sentenciaCONTRACT CATCH BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : sentenciaCONTRACT CATCH BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : TRY CATCH BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : TRY sentenciaCONTRACT BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : TRY sentenciaCONTRACT CATCH sentSoloEjecutables END",
"bloqueTRYCATCH : TRY sentenciaCONTRACT CATCH BEGIN sentSoloEjecutables error",
"bloqueEjecutableNormal : BEGIN sentSoloEjecutables END",
"bloqueEjecutableNormal : sentSoloEjecutables END",
"bloqueEjecutableNormal : BEGIN sentSoloEjecutables error",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' retorno ')' ';' END",
"bloqueEjecutableFunc : BEGIN RETURN '(' retorno ')' ';' END",
"bloqueEjecutableFunc : sentEjecutableFunc RETURN '(' retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc '(' retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' retorno END",
"sentEjecutableFunc : sentEjecutableFunc sentEjecutablesFunc",
"sentEjecutableFunc : sentEjecutablesFunc",
"sentEjecutablesFunc : sentEjecutables",
"sentEjecutablesFunc : bloqueTRYCATCH",
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
"declaracionFunc : tipo FUNC ID '(' parametro ')'",
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
"asignacion : '(' tipo ')' ID ASIGN expAritmetica ';'",
"asignacion : ID '=' expAritmetica ';'",
"asignacion : ID ASIGN tipo '(' expAritmetica ')' ';'",
"asignacion : ASIGN expAritmetica ';'",
"asignacion : ID expAritmetica ';'",
"asignacion : ID ASIGN expAritmetica error",
"asignacion : ID ASIGN error",
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

//#line 636 "gramatica.y"


public int yylex() {
    int value = AnalizadorLexico.yylex();
    yylval = new ParserVal(AnalizadorLexico.refTDS); 
    return value;
}

public void yyerror(String string) {
	//AnalizadorSintactico.agregarError("Parser token error: " + string);
	//System.out.println("token error en linea  "+ AnalizadorLexico.numLinea + ": " +string);
}
//#line 648 "Parser.java"
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
//#line 50 "gramatica.y"
{yyval= new ParserVal (new Nodo("TC", (Nodo)val_peek(4).obj, (Nodo)val_peek(1).obj));}
break;
case 6:
//#line 51 "gramatica.y"
{AnalizadorSintactico.agregarError("error: falta TRY en (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 7:
//#line 52 "gramatica.y"
{AnalizadorSintactico.agregarError("error TRY vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 8:
//#line 53 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta CATCH (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 9:
//#line 54 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 10:
//#line 55 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 11:
//#line 61 "gramatica.y"
{
			    yyval=val_peek(1);
			}
break;
case 12:
//#line 64 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 13:
//#line 65 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 14:
//#line 70 "gramatica.y"
{
                yyval = new ParserVal(new Nodo("BF",(Nodo)val_peek(6).obj,(Nodo)val_peek(3).obj));
            }
break;
case 15:
//#line 73 "gramatica.y"
{
                yyval = new ParserVal(new Nodo("BF",null,(Nodo)val_peek(3).obj));
            }
break;
case 16:
//#line 76 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 17:
//#line 77 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta RETURN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 18:
//#line 78 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 19:
//#line 79 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta retorno (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 20:
//#line 80 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 21:
//#line 86 "gramatica.y"
{ if ((val_peek(1).obj != null) && (val_peek(0).obj != null))
                     {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, (Nodo)val_peek(1).obj));}
                  else if((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, null));}
                       else if ((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", null, (Nodo)val_peek(1).obj));}
                }
break;
case 22:
//#line 92 "gramatica.y"
{
                    if(val_peek(0).obj == null){
                       yyval= new ParserVal(new Nodo("S",null, null));
                    }else{
                        yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
                    }
                  }
break;
case 23:
//#line 101 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 24:
//#line 102 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 25:
//#line 105 "gramatica.y"
{

            AnalizadorSintactico.agregarAnalisis("sent contract (Linea " + AnalizadorLexico.numLinea + ")");
            if(val_peek(1).obj == null)
                break;
            yyval =  new ParserVal(new Nodo("CONTRACT",(Nodo)val_peek(0).obj,null));
            }
break;
case 26:
//#line 112 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 27:
//#line 113 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 28:
//#line 114 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 29:
//#line 115 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 30:
//#line 116 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 31:
//#line 122 "gramatica.y"
{ if ((val_peek(0).obj != null) && (val_peek(1).obj != null))
                     {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, (Nodo)val_peek(0).obj));}
                  else if((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));}
                       else if ((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", null, (Nodo)val_peek(0).obj));}
                }
break;
case 32:
//#line 128 "gramatica.y"
{
                    if(val_peek(0).obj == null){
                       yyval= new ParserVal(new Nodo("S",null, null));
                    }else{
                       yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
                    }
                }
break;
case 34:
//#line 139 "gramatica.y"
{yyval=val_peek(0);}
break;
case 36:
//#line 143 "gramatica.y"
{
                    AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 37:
//#line 146 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 38:
//#line 147 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 39:
//#line 148 "gramatica.y"
{
	       	        AnalizadorLexico.tablaDeSimbolos.remove(val_peek(1).sval);
           	        AnalizadorLexico.listaDeErrores.add("Tipo de variable debe ser en mayuscula (Linea " + (AnalizadorLexico.numLinea-1) + ")");
           	      }
break;
case 40:
//#line 152 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 41:
//#line 155 "gramatica.y"
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
case 42:
//#line 164 "gramatica.y"
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
case 43:
//#line 173 "gramatica.y"
{AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 44:
//#line 177 "gramatica.y"
{
        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
        yyval=new ParserVal (new Nodo(val_peek(2).sval+AnalizadorSintactico.ambitoActual, (Nodo)val_peek(0).obj, null));
        
        }
break;
case 45:
//#line 183 "gramatica.y"
{
	        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
	        
	        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
	         
	        yyval=new ParserVal (new Nodo(val_peek(1).sval+AnalizadorSintactico.ambitoActual, (Nodo)val_peek(0).obj, null));
	   }
break;
case 46:
//#line 192 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 47:
//#line 194 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 48:
//#line 195 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 49:
//#line 198 "gramatica.y"
{
            AnalizadorSintactico.tipoActual = val_peek(4).sval;
           }
break;
case 50:
//#line 201 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 51:
//#line 202 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 52:
//#line 203 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 53:
//#line 204 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 54:
//#line 208 "gramatica.y"
{yyval=val_peek(0);}
break;
case 56:
//#line 214 "gramatica.y"
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
case 57:
//#line 224 "gramatica.y"
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
case 58:
//#line 234 "gramatica.y"
{
	                 yyval=val_peek(0);
	                 }
break;
case 59:
//#line 243 "gramatica.y"
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
case 60:
//#line 253 "gramatica.y"
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
case 61:
//#line 264 "gramatica.y"
{
	                        yyval = val_peek(0);
	                        }
break;
case 62:
//#line 273 "gramatica.y"
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
case 63:
//#line 288 "gramatica.y"
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
case 64:
//#line 304 "gramatica.y"
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
case 65:
//#line 328 "gramatica.y"
{yyval=val_peek(0);}
break;
case 66:
//#line 332 "gramatica.y"
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
case 67:
//#line 360 "gramatica.y"
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
case 68:
//#line 375 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 69:
//#line 376 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 70:
//#line 377 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 71:
//#line 378 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 72:
//#line 382 "gramatica.y"
{
                    Object[] obj = new Object[2] ;
                    obj[0] = val_peek(0).sval;
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                    aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    obj[1] = aux;
                    yyval= new ParserVal(obj);
		     }
break;
case 73:
//#line 390 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 74:
//#line 395 "gramatica.y"
{yyval=val_peek(0);}
break;
case 75:
//#line 396 "gramatica.y"
{yyval=val_peek(0);}
break;
case 76:
//#line 397 "gramatica.y"
{yyval=val_peek(0);}
break;
case 77:
//#line 398 "gramatica.y"
{yyval=val_peek(0);}
break;
case 78:
//#line 401 "gramatica.y"
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
case 79:
//#line 425 "gramatica.y"
{
						if (val_peek(5).sval != "SINGLE") 
						{
							AnalizadorSintactico.agregarError("No se puede castear a este tipo (Linea " + AnalizadorLexico.numLinea + ")");
							AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
						}
						else 
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
									((Nodo)aux.obj).setTipo("SINGLE"); /*Mirarlo */
									TDSObject value = AnalizadorLexico.getLexemaObject(variable);
									yyval= new ParserVal(new Nodo(":=", (Nodo)aux.obj, (Nodo)val_peek(1).obj));
									if( (((Nodo)val_peek(1).obj).getTipo()).equals("SINGLE")){
										((Nodo)yyval.obj).setTipo(((Nodo)aux.obj).getTipo());
									}else{
										AnalizadorSintactico.agregarError("Tipo Incompatible (SINGLE, " + ((Nodo)val_peek(1).obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
									}
								}
                     
							}
					}
break;
case 80:
//#line 456 "gramatica.y"
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
case 81:
//#line 479 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 82:
//#line 480 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 83:
//#line 481 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 84:
//#line 482 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 85:
//#line 483 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 86:
//#line 484 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ':'(Linea " + (AnalizadorLexico.numLinea -1) + ")");}
break;
case 87:
//#line 485 "gramatica.y"
{AnalizadorSintactico.agregarError("Error, no puede asignarse un comparador(Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 88:
//#line 489 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if(val_peek(6).obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 89:
//#line 497 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");
	                                                    if(val_peek(4).obj == null)
                                                           break;
                                                        ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(2).obj, null));
                                                        ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,null));
                                                        yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(4).obj, (Nodo)auxCuerpo.obj));}
break;
case 90:
//#line 503 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 91:
//#line 504 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 92:
//#line 505 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 93:
//#line 506 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 94:
//#line 507 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 95:
//#line 508 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 96:
//#line 509 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 97:
//#line 510 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 98:
//#line 511 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 99:
//#line 512 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 100:
//#line 513 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 514 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 102:
//#line 517 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 103:
//#line 520 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 104:
//#line 521 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 105:
//#line 522 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 106:
//#line 523 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 107:
//#line 524 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 108:
//#line 525 "gramatica.y"
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
case 109:
//#line 539 "gramatica.y"
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
case 110:
//#line 553 "gramatica.y"
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
case 111:
//#line 569 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		 if(val_peek(2).obj == null)
             break;
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 112:
//#line 573 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 113:
//#line 574 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 114:
//#line 575 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 115:
//#line 576 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 116:
//#line 580 "gramatica.y"
{yyval=val_peek(1);}
break;
case 118:
//#line 584 "gramatica.y"
{
	     if(val_peek(0).obj == null)
             break;
	     yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(0).obj, null));}
break;
case 119:
//#line 588 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 120:
//#line 592 "gramatica.y"
{
		  if(val_peek(2).obj == null || val_peek(0).obj == null){
             break;
          }else{
		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
		  }
		}
break;
case 124:
//#line 602 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 125:
//#line 603 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 126:
//#line 606 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 127:
//#line 607 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 128:
//#line 608 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 129:
//#line 609 "gramatica.y"
{yyval = new ParserVal("<>");}
break;
case 130:
//#line 610 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 131:
//#line 611 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 132:
//#line 612 "gramatica.y"
{
	           AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea +": se esperaba comparacion ==.");
	           yyval = new ParserVal("==");
	        }
break;
case 133:
//#line 618 "gramatica.y"
{yyval= new ParserVal("&&");}
break;
case 134:
//#line 619 "gramatica.y"
{yyval= new ParserVal("||");}
break;
case 135:
//#line 623 "gramatica.y"
{AnalizadorSintactico.tipoActual = "LONG";
             yyval = new ParserVal("LONG");
            }
break;
case 136:
//#line 626 "gramatica.y"
{AnalizadorSintactico.tipoActual = "SINGLE";
             yyval = new ParserVal("SINGLE");
             }
break;
//#line 1673 "Parser.java"
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
