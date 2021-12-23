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
    0,    1,    1,    2,    5,    5,    5,    5,    5,    4,
    4,    4,    7,    7,    7,    7,    7,    7,    8,    8,
   10,   10,   12,   12,   12,   12,   12,   12,    6,    6,
    3,    3,    3,   14,   14,   14,   14,   14,   18,   18,
   18,   15,   15,   16,   16,   16,   20,   20,   20,   20,
   20,    9,    9,   21,   21,   21,   22,   22,   22,   23,
   23,   23,   23,   24,   24,   19,   19,   19,   19,   19,
   19,   19,   25,   25,   11,   11,   11,   11,   11,   26,
   26,   26,   26,   26,   26,   26,   26,   26,   26,   27,
   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,
   27,   27,   27,   27,   27,   27,   27,   28,   28,   28,
   28,   28,   28,   28,   28,   28,   29,   29,   29,   29,
   29,   13,   31,   31,   31,   30,   30,   30,   33,   33,
   33,   33,   33,   33,   33,   32,   32,   17,   17,
};
final static short yylen[] = {                            2,
    3,    2,    1,    1,    6,    5,    5,    5,    6,    3,
    2,    3,    8,    7,    6,    6,    6,    6,    2,    1,
    1,    1,    4,    5,    5,    5,    5,    6,    2,    1,
    1,    1,    1,    3,    2,    3,    2,    2,    3,    1,
    2,    3,    2,    3,    2,    3,    5,    4,    4,    4,
    5,    1,    4,    3,    3,    1,    3,    3,    1,    1,
    1,    2,    1,    4,    3,    6,    5,    6,    5,    5,
    5,    6,    2,    1,    1,    1,    1,    1,    1,    4,
    7,    4,    7,    3,    3,    4,    3,    3,    4,    8,
    6,    8,    8,    8,    6,    7,    7,    7,    7,    7,
    7,    8,    5,    5,    5,    5,    6,    5,    4,    4,
    4,    4,    5,    5,    5,    6,    4,    3,    3,    3,
    4,    3,    3,    1,    2,    3,    3,    2,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,  138,  139,    0,    0,    3,   31,   32,
   33,    0,    0,    0,    0,    0,    0,   37,    0,    0,
    0,    0,    0,    0,    0,    0,    1,    2,    4,   79,
    0,    0,    0,   75,   76,   77,   78,   38,    0,    0,
    0,   35,    0,    0,    0,    0,   43,    0,    0,   21,
   22,   40,   45,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   61,    0,    0,    0,    0,    0,
   59,   63,    0,    0,    0,    0,    0,    0,  131,  132,
  133,  134,  136,  137,  135,  129,  130,    0,    0,    0,
    0,    0,    0,   11,   29,    0,    0,    0,    0,    0,
    0,   36,   34,   39,    0,    0,    0,    0,   42,    0,
   19,   46,   44,   74,    0,    0,   48,    0,    0,    0,
    0,    0,    0,  119,    0,  120,   12,   10,    0,   87,
    0,    0,    0,   62,   88,    0,   85,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   84,    0,    0,
    0,    0,  122,  125,    0,    0,  118,    0,    0,   50,
    0,   49,    0,    0,    0,    0,    0,    0,    0,   73,
   69,  110,    0,  112,    0,    0,  111,    0,  121,  117,
    0,   65,    0,   86,   80,   89,   82,    0,    0,   57,
   58,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  109,    0,  127,    0,  123,    0,   67,
    0,   71,   51,   47,   70,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   23,    0,  113,  108,  114,  115,
    0,   64,    0,    6,    7,    0,    8,    0,  103,    0,
  105,    0,    0,  106,    0,    0,    0,  104,    0,    0,
   72,   66,   68,    0,    0,    0,    0,    0,   24,   26,
    0,   27,   25,    0,  116,    0,    9,    5,    0,    0,
    0,    0,    0,  107,   91,    0,    0,   95,    0,    0,
    0,    0,    0,   17,   18,    0,   16,   28,   15,   83,
   97,   99,  100,    0,  101,    0,    0,    0,   98,   81,
   96,   14,   53,    0,  102,   90,   93,   92,   94,   13,
};
final static short yydgoto[] = {                          2,
    7,   27,    8,   29,   30,   31,   47,   48,  216,   49,
   32,   51,   33,    9,   10,   11,   12,   13,   14,   15,
   89,   70,   71,   72,  116,   34,   35,   36,   37,   90,
   91,   92,   93,
};
final static short yysindex[] = {                      -230,
 -132,    0,  -34,    0,    0, -207,  -18,    0,    0,    0,
    0, -174,  -13,  -39,  -42,   18, -134,    0,  -22,  -10,
  145,   73,   87,  -27,   11,  376,    0,    0,    0,    0,
 -199,  145, -192,    0,    0,    0,    0,    0,  136,   58,
  -40,    0, -172,    8,   -1,  -39,    0, -151,   91,    0,
    0,    0,    0,   45, -152,   95,   99,    2,  112,  445,
   24,   81, -150,  104,    0,  478, -117,   47,   16,   53,
    0,    0, -105, -177,  112,   34,   17,  124,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  128,  465, -248,
  129,  445,   11,    0,    0,  112,  112,  147,   44,  130,
 -152,    0,    0,    0,  127,  -90,  134,  135,    0,  137,
    0,    0,    0,    0,  -93,  140,    0,  123,    4,  143,
  148,  131,  -89,    0,   50,    0,    0,    0,  -17,    0,
  146,  457,  132,    0,    0,   48,    0,   11,   11,   11,
   11,  145,  145,  113, -108,   -2, -101,    0,  133,  -83,
  264,  445,    0,    0,  116,  -64,    0,  182,  153,    0,
   42,    0,  154,  232,  -37,  155,  158,   12,  232,    0,
    0,    0,  -45,    0,  138,  141,    0,  160,    0,    0,
  161,    0,   11,    0,    0,    0,    0,   53,   53,    0,
    0,  -58,  -56,  145,  -54,  112,  156,  112,  205,  -52,
   63,  112,  221,    0,  -79,    0,  116,    0,  112,    0,
   40,    0,    0,    0,    0,  165,  174,  116,   60,  176,
  231,  241,   14,  257,    0,  297,    0,    0,    0,    0,
  280,    0,  100,    0,    0, -142,    0,   90,    0,  102,
    0,   77,  -43,    0,  112,  304,  120,    0,   11,  121,
    0,    0,    0,  322,   11,  122,  -26,  149,    0,    0,
  157,    0,    0,  169,    0,  323,    0,    0,  328,  335,
  342,  -50,  126,    0,    0,  167,  139,    0,  353,   51,
  361,  178,  105,    0,    0,  365,    0,    0,    0,    0,
    0,    0,    0,   79,    0,  368,  389,  391,    0,    0,
    0,    0,    0,  185,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,   10,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   10,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -214,    0,    0,    0,    0,    0,    0,    0,   69,
    0,    0,  103,    0,    0,    0,    0,    0,  193,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  400,    0,    0,    0,    0,    0,  411,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  416,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -31,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  422,  433,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  192,    0,    0,    0,    0,    0,  -15,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    9,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  192,    0,    0,    0,  192,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  450,  566,   22,    0,    0,  -21,  420,   76, -133,    0,
   -9,    0,    1,    0,    0,    0,  536,  142,    0,    0,
  560,  -60,   23,    0,  -78,    0,    0,    0,    0,  402,
  -80,  379,  -62,
};
final static int YYTABLESIZE=815;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         63,
   26,   74,  219,   43,   50,   17,  244,   67,  295,  128,
   95,  154,   60,  228,  286,  275,   53,   58,  103,  159,
   61,   26,  163,  182,   76,  126,  151,  128,   28,   60,
   43,  220,   83,   84,   50,  226,   50,   26,  107,   50,
    1,   30,  122,  126,  173,   42,  123,   26,   18,   52,
   30,   30,  224,   40,  261,   67,  108,   55,  138,  138,
  139,  139,  174,   26,   30,   96,   94,   28,   40,  151,
  225,  208,  262,   26,  137,  148,   97,  188,  189,  211,
  252,   38,  214,  253,  160,  257,   39,  143,   43,   26,
  138,   67,  139,  138,  140,  139,   40,  101,  104,  141,
  256,  144,   26,  113,   67,  127,  187,  166,  168,  300,
  110,    4,   40,  267,    5,  128,   26,   67,  114,  106,
  192,  193,  195,  268,  111,   67,   26,   40,    3,    4,
   26,    4,    5,   68,    5,  117,  201,  306,    6,  118,
  266,   68,  138,  129,  139,  303,   41,  138,  196,  139,
  197,   26,   26,   41,  134,  202,   54,  203,  138,  142,
  139,   41,  190,  191,  149,   26,  164,  223,  150,  153,
  162,  165,  236,   60,  167,   99,  169,  170,   26,   95,
  171,  172,  178,  175,   26,  183,  158,  205,  176,  177,
  186,  204,  209,  212,  215,  221,  229,   60,  222,  230,
  231,  232,   67,  249,  242,  254,  243,  234,  294,  235,
  227,  237,  274,  255,  239,  102,  258,   87,   85,   86,
   19,    3,  210,   20,    4,   44,    4,    5,   52,    5,
   75,   22,  273,   64,   65,  277,   16,   45,   23,  285,
   24,   19,    3,   25,   20,    4,   21,   57,    5,  128,
  128,   95,   22,  181,  198,   95,  199,   19,   59,   23,
   20,   24,   21,  241,   25,  126,  126,   19,   62,  105,
   20,  119,  120,  121,   52,   23,   67,   24,   62,  248,
   25,   64,   65,   19,   45,   23,   20,   24,   21,  259,
   25,  146,  125,   19,   62,  251,   20,  213,   21,  260,
  112,   23,  135,   24,   62,  179,   25,    4,   67,   19,
    5,   23,   20,   24,   21,  263,   25,   64,   65,  245,
   62,  246,   19,    4,   40,   20,    5,   23,   18,   24,
   64,   65,   25,   62,  305,  271,   19,  264,  265,   20,
   23,   21,   24,   64,   65,   25,   19,   62,  269,   20,
   19,   64,   65,   20,   23,   66,   24,   62,   41,   25,
  270,   62,  278,   66,   23,   73,   24,   45,   23,   25,
   24,   19,   19,   25,   20,   20,   21,  194,  279,  281,
  282,  290,   62,   62,  296,   19,  291,  284,   20,   23,
   23,   24,   24,  292,   25,   25,   62,  298,   19,    4,
  293,   20,    5,   23,   19,   24,   98,   20,   25,   62,
    4,  299,  288,    5,  287,   62,   23,  114,   24,  301,
   67,   25,   23,  304,   24,  297,  307,   25,   64,   65,
   79,   80,   81,   82,  289,   87,   85,   86,   83,   84,
   60,   60,   60,  302,   60,    4,   60,  308,    5,  309,
  310,   56,  114,   56,   20,   56,  124,   30,   60,   60,
   60,   60,   54,   46,   54,  109,   54,  133,  152,   56,
   56,   56,   56,   55,    0,   55,    0,   55,    0,    0,
   54,   54,   54,   54,    0,    0,    0,    0,    0,   67,
    0,   55,   55,   55,   55,    4,    0,    0,    5,  138,
    0,  139,   64,   65,   87,   85,   86,  138,    0,  139,
    0,    0,    0,    0,    0,  185,   87,   85,   86,  206,
    0,    0,   67,    0,   87,   85,   86,    0,    0,    0,
    0,    0,    0,    0,   64,   65,    0,   87,   85,   86,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   56,    0,    0,    0,    0,    0,    0,    0,
    0,   88,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  100,    0,    0,    0,    0,    0,
    0,   69,    0,    0,   77,    0,    0,    0,    0,    0,
  115,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  131,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   69,    0,    0,  124,  132,  126,  136,    0,    0,
    0,    0,    0,  115,  161,    0,  115,    0,    0,    4,
  145,  147,    5,    0,    0,   78,   64,   65,   79,   80,
   81,   82,  155,    0,    0,   60,   83,   84,    0,    0,
    0,  156,  157,    0,    0,   60,   56,    0,    0,    0,
    0,    0,   60,   60,   60,   60,   56,   54,    0,    0,
   60,   60,    0,   56,   56,   56,   56,   54,   55,    0,
  180,   56,   56,  115,   54,   54,   54,   54,   55,  217,
  217,    0,   54,   54,  217,   55,   55,   55,   55,    0,
  207,  200,  184,   55,   55,   64,   65,   79,   80,   81,
   82,    0,    0,  218,  218,   83,   84,    0,  218,   79,
   80,   81,   82,  130,    0,    0,    0,   79,   80,   81,
   82,    4,  233,    0,    5,    0,    0,    0,   64,   65,
   79,   80,   81,   82,  217,    0,    0,    0,    0,    0,
    0,  238,    0,  240,    0,    0,    0,  247,    0,    0,
    0,    0,    0,    0,  250,    0,    0,    0,  218,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  272,  280,    0,
  276,    0,    0,    0,  283,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         21,
   40,   23,   40,   44,   14,   40,   59,   45,   59,   41,
   32,   92,   40,   59,   41,   59,   59,   40,   59,   98,
   20,   40,  101,   41,   24,   41,   89,   59,    7,   40,
   44,  165,  281,  282,   44,  169,   46,   40,   40,   49,
  271,  256,   41,   59,   41,   59,   45,   40,  256,   41,
  265,  266,   41,   44,   41,   45,   58,   40,   43,   43,
   45,   45,   59,   40,  279,  258,  266,   46,   59,  132,
   59,  152,   59,   40,   59,   59,  269,  138,  139,  158,
   41,  256,   41,   44,   41,  219,  261,  265,   44,   40,
   43,   45,   45,   43,   42,   45,  271,   40,  271,   47,
   41,  279,   40,   59,   45,  256,   59,  107,  108,   59,
  262,  264,   44,  256,  267,  266,   40,   45,  271,   44,
  142,  143,  144,  266,   49,   45,   40,   59,  261,  264,
   40,  264,  267,   61,  267,   41,  146,   59,  271,   41,
   41,   61,   43,   40,   45,   41,   44,   43,  257,   45,
  259,   40,   40,   12,  272,  257,   15,  259,   43,  265,
   45,   59,  140,  141,   41,   40,   40,  167,   41,   41,
   41,  262,  194,   40,   40,   40,   40,  271,   40,  201,
   41,   59,  272,   41,   40,   40,   40,  271,   41,   59,
   59,   59,  257,   41,   41,   41,   59,   40,   41,   59,
   41,   41,   45,  283,  257,   41,  259,  266,  259,  266,
  256,  266,  256,   40,   59,  256,   41,   60,   61,   62,
  260,  261,   41,  263,  264,  265,  264,  267,  271,  267,
  258,  271,  242,  271,  272,  245,  271,  277,  278,  266,
  280,  260,  261,  283,  263,  264,  265,  270,  267,  281,
  282,  273,  271,  271,  257,  277,  259,  260,  269,  278,
  263,  280,  265,   59,  283,  281,  282,  260,  271,  262,
  263,  270,  271,  272,  266,  278,   45,  280,  271,   59,
  283,  271,  272,  260,  277,  278,  263,  280,  265,   59,
  283,  258,  269,  260,  271,  256,  263,  256,  265,   59,
  256,  278,  256,  280,  271,  256,  283,  264,   45,  260,
  267,  278,  263,  280,  265,   59,  283,  271,  272,  257,
  271,  259,  260,  264,  256,  263,  267,  278,  256,  280,
  271,  272,  283,  271,  256,  259,  260,   41,   59,  263,
  278,  265,  280,  271,  272,  283,  260,  271,  259,  263,
  260,  271,  272,  263,  278,  283,  280,  271,  256,  283,
  259,  271,   59,  283,  278,  279,  280,  277,  278,  283,
  280,  260,  260,  283,  263,  263,  265,  265,  259,  259,
   59,   59,  271,  271,  259,  260,   59,  266,  263,  278,
  278,  280,  280,   59,  283,  283,  271,  259,  260,  264,
   59,  263,  267,  278,  260,  280,  271,  263,  283,  271,
  264,   59,  256,  267,  266,  271,  278,  271,  280,   59,
   45,  283,  278,   59,  280,  259,   59,  283,  271,  272,
  273,  274,  275,  276,  266,   60,   61,   62,  281,  282,
   41,   42,   43,  266,   45,  264,   47,   59,  267,   59,
  266,   41,  271,   43,  262,   45,   41,  266,   59,   60,
   61,   62,   41,   14,   43,   46,   45,   66,   90,   59,
   60,   61,   62,   41,   -1,   43,   -1,   45,   -1,   -1,
   59,   60,   61,   62,   -1,   -1,   -1,   -1,   -1,   45,
   -1,   59,   60,   61,   62,  264,   -1,   -1,  267,   43,
   -1,   45,  271,  272,   60,   61,   62,   43,   -1,   45,
   -1,   -1,   -1,   -1,   -1,   59,   60,   61,   62,  256,
   -1,   -1,   45,   -1,   60,   61,   62,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  271,  272,   -1,   60,   61,   62,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   17,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   26,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   39,   -1,   -1,   -1,   -1,   -1,
   -1,   22,   -1,   -1,   25,   -1,   -1,   -1,   -1,   -1,
   55,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   66,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   62,   -1,   -1,   59,   66,   61,   68,   -1,   -1,
   -1,   -1,   -1,   98,   99,   -1,  101,   -1,   -1,  264,
   75,   76,  267,   -1,   -1,  270,  271,  272,  273,  274,
  275,  276,   93,   -1,   -1,  256,  281,  282,   -1,   -1,
   -1,   96,   97,   -1,   -1,  266,  256,   -1,   -1,   -1,
   -1,   -1,  273,  274,  275,  276,  266,  256,   -1,   -1,
  281,  282,   -1,  273,  274,  275,  276,  266,  256,   -1,
  125,  281,  282,  158,  273,  274,  275,  276,  266,  164,
  165,   -1,  281,  282,  169,  273,  274,  275,  276,   -1,
  151,  146,  256,  281,  282,  271,  272,  273,  274,  275,
  276,   -1,   -1,  164,  165,  281,  282,   -1,  169,  273,
  274,  275,  276,  256,   -1,   -1,   -1,  273,  274,  275,
  276,  264,  183,   -1,  267,   -1,   -1,   -1,  271,  272,
  273,  274,  275,  276,  219,   -1,   -1,   -1,   -1,   -1,
   -1,  196,   -1,  198,   -1,   -1,   -1,  202,   -1,   -1,
   -1,   -1,   -1,   -1,  209,   -1,   -1,   -1,  219,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  242,  249,   -1,
  245,   -1,   -1,   -1,  255,
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
"bloqueTRYCATCH : TRY sentSoloEjecutables CATCH BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : TRY CATCH BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : TRY sentSoloEjecutables BEGIN sentSoloEjecutables END",
"bloqueTRYCATCH : TRY sentSoloEjecutables CATCH sentSoloEjecutables END",
"bloqueTRYCATCH : TRY sentSoloEjecutables CATCH BEGIN sentSoloEjecutables error",
"bloqueEjecutableNormal : BEGIN sentSoloEjecutables END",
"bloqueEjecutableNormal : sentSoloEjecutables END",
"bloqueEjecutableNormal : BEGIN sentSoloEjecutables error",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' retorno ')' ';' END",
"bloqueEjecutableFunc : BEGIN RETURN '(' retorno ')' ';' END",
"bloqueEjecutableFunc : sentEjecutableFunc RETURN '(' retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN retorno ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' ')' END",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' retorno END",
"sentEjecutableFunc : sentEjecutablesFunc sentEjecutableFunc",
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
"declaracionFunc : tipo FUNC ID '(' parametro ','",
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
"sentEjecutables : bloqueTRYCATCH",
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
"sentenciaIF : IF condicion THEN sentEjecutables ELSE bloqueEjecutable ENDIF ';'",
"sentenciaIF : IF condicion THEN bloqueEjecutable ELSE sentEjecutables ENDIF ';'",
"sentenciaIF : IF condicion THEN sentEjecutables ELSE sentEjecutables ENDIF ';'",
"sentenciaIF : IF condicion THEN sentEjecutables ENDIF ';'",
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
"comparaciones : comparacion opLogico comparaciones",
"comparaciones : comparacion",
"comparaciones : opLogico comparaciones",
"comparacion : expAritmetica comparador expAritmetica",
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

//#line 782 "gramatica.y"


public int yylex() {
    int value = AnalizadorLexico.yylex();
    yylval = new ParserVal(AnalizadorLexico.refTDS); 
    return value;
}

public void yyerror(String string) {
	//AnalizadorSintactico.agregarError("Parser token error: " + string);
	//System.out.println("token error en linea  "+ AnalizadorLexico.numLinea + ": " +string);
}
//#line 628 "Parser.java"
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
{
                                    			    if(((Nodo)val_peek(4).obj).checkContract()){
                                        		        /*((Nodo)$$.obj).setFuncContract(((Nodo)$2.obj).checkFuncContract());*/
                                        		        /*((Nodo)$$.obj).setContract(true);*/
                                        		         yyval= new ParserVal (new Nodo("TC", (Nodo)val_peek(4).obj, (Nodo)val_peek(1).obj));

                                        		    }else{
                                        		    if(AnalizadorSintactico.listaErroresSintacticos.size() == 0){
                                        		                                            		    AnalizadorSintactico.agregarError("error: No se permite TRY Catch sin contract (Linea " + AnalizadorLexico.numLinea + ")");

                                        		    }
                                        		    }
            /*$$= new ParserVal (new Nodo("TC", (Nodo)$2.obj, (Nodo)$5.obj));*/

            }
break;
case 6:
//#line 66 "gramatica.y"
{AnalizadorSintactico.agregarError("error TRY vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 7:
//#line 67 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta CATCH (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 8:
//#line 68 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 9:
//#line 69 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 10:
//#line 75 "gramatica.y"
{
			    yyval=val_peek(1);
			}
break;
case 11:
//#line 78 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 12:
//#line 79 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 13:
//#line 84 "gramatica.y"
{
                yyval = new ParserVal(new Nodo("BF",(Nodo)val_peek(6).obj,(Nodo)val_peek(3).obj));
                                        if(((Nodo)val_peek(6).obj).checkContract()){
                                            ((Nodo)yyval.obj).setContract(true);
                                        }
            }
break;
case 14:
//#line 90 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta bloque ejecutable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 15:
//#line 91 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 16:
//#line 93 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 17:
//#line 94 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta retorno (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 18:
//#line 95 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 19:
//#line 101 "gramatica.y"
{ if ((val_peek(1).obj != null) && (val_peek(0).obj != null))
                     {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, (Nodo)val_peek(0).obj));
                                             if(((Nodo)val_peek(1).obj).checkContract() || ((Nodo)val_peek(0).obj).checkContract()  ){
                                                 ((Nodo)yyval.obj).setContract(true);

                                             }
                     }
                  else if((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));
                                          if(((Nodo)val_peek(1).obj).checkContract()){
                                              ((Nodo)yyval.obj).setContract(true);
                                          }
                  }
                       else if ((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", null, (Nodo)val_peek(0).obj));
}
                }
break;
case 20:
//#line 117 "gramatica.y"
{
                    if(val_peek(0).obj == null){
                       yyval= new ParserVal(new Nodo("S",null, null));
                    }else{
                        yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
                        if(((Nodo)val_peek(0).obj).checkContract()){
                            ((Nodo)yyval.obj).setContract(true);
                        }
                    }
                  }
break;
case 21:
//#line 129 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 22:
//#line 130 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 23:
//#line 133 "gramatica.y"
{

            AnalizadorSintactico.agregarAnalisis("sent contract (Linea " + AnalizadorLexico.numLinea + ")");
            if(val_peek(1).obj == null)
                break;
            yyval =  new ParserVal(new Nodo("CONTRACT",(Nodo)val_peek(0).obj,null));
            ((Nodo)yyval.obj).setContract(true);
            }
break;
case 24:
//#line 141 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 25:
//#line 142 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 26:
//#line 143 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 27:
//#line 144 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 28:
//#line 145 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 29:
//#line 151 "gramatica.y"
{ if ((val_peek(0).obj != null) && (val_peek(1).obj != null))
                     {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, (Nodo)val_peek(0).obj));}
                                             			    if(((Nodo)val_peek(1).obj).checkContract() || ((Nodo)val_peek(0).obj).checkContract() ){

                                                 		        ((Nodo)yyval.obj).setFuncContract(((Nodo)val_peek(1).obj).checkFuncContract());
                                                 		        ((Nodo)yyval.obj).setContract(true);
                                                 		    }
                  else if((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));
                                          			    if(((Nodo)val_peek(1).obj).checkContract()){
                                              		        ((Nodo)yyval.obj).setFuncContract(((Nodo)val_peek(1).obj).checkFuncContract());
                                              		        ((Nodo)yyval.obj).setContract(true);
                                              		    }
                  }
                       else if ((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", null, (Nodo)val_peek(0).obj));}
                }
break;
case 30:
//#line 167 "gramatica.y"
{
                    if(val_peek(0).obj == null){
                       yyval= new ParserVal(new Nodo("S",null, null));
                    }else{
                       yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
                                               			    if(((Nodo)val_peek(0).obj).checkContract()){
                                                   		        ((Nodo)yyval.obj).setFuncContract(((Nodo)val_peek(0).obj).checkFuncContract());
                                                   		        ((Nodo)yyval.obj).setContract(true);
                                                   		    }
                    }
                }
break;
case 32:
//#line 182 "gramatica.y"
{yyval=val_peek(0);}
break;
case 34:
//#line 186 "gramatica.y"
{
                    AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 35:
//#line 189 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 36:
//#line 190 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 37:
//#line 191 "gramatica.y"
{
	       	        AnalizadorLexico.tablaDeSimbolos.remove(val_peek(1).sval);
           	        AnalizadorLexico.listaDeErrores.add("Tipo de variable debe ser en mayuscula (Linea " + (AnalizadorLexico.numLinea-1) + ")");
           	      }
break;
case 38:
//#line 195 "gramatica.y"
{
                      if(AnalizadorSintactico.listaErroresSintacticos.size() == 0)
                       AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");
                       }
break;
case 39:
//#line 201 "gramatica.y"
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
case 40:
//#line 210 "gramatica.y"
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
case 41:
//#line 219 "gramatica.y"
{AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 42:
//#line 223 "gramatica.y"
{
        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
        yyval=new ParserVal (new Nodo(val_peek(2).sval+AnalizadorSintactico.ambitoActual, (Nodo)val_peek(0).obj, null));
                                                if(((Nodo)val_peek(0).obj).checkContract()){
                                                    ((Nodo)yyval.obj).setContract(true);
                                                     TDSObject value = AnalizadorLexico.getLexemaObject(val_peek(2).sval+AnalizadorSintactico.ambitoActual);
                                                     value.setContract(true);
                                                }
        
        }
break;
case 43:
//#line 234 "gramatica.y"
{
	        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
	        
	        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
	         
	        yyval=new ParserVal (new Nodo(val_peek(1).sval+AnalizadorSintactico.ambitoActual, (Nodo)val_peek(0).obj, null));
	   }
break;
case 44:
//#line 244 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 45:
//#line 246 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 46:
//#line 247 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 47:
//#line 250 "gramatica.y"
{
            AnalizadorSintactico.tipoActual = val_peek(4).sval;
           }
break;
case 48:
//#line 253 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 49:
//#line 254 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 50:
//#line 255 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 51:
//#line 256 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 52:
//#line 260 "gramatica.y"
{yyval=val_peek(0);}
break;
case 54:
//#line 266 "gramatica.y"
{
                       if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                       	break;
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("+", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                        	                        			    if(((Nodo)val_peek(0).obj).checkContract() || ((Nodo)val_peek(2).obj).checkContract()){
                                                        		        ((Nodo)yyval.obj).setFuncContract(((Nodo)val_peek(0).obj).checkFuncContract());
                                                        		        ((Nodo)yyval.obj).setContract(true);
                                                        		    }
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos + (Linea " + AnalizadorLexico.numLinea + ")");
                      }
                      }
break;
case 55:
//#line 280 "gramatica.y"
{
	                   if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                       	break;
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("-", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                        	                        			    if(((Nodo)val_peek(0).obj).checkContract() || ((Nodo)val_peek(2).obj).checkContract()){
                                                        		        ((Nodo)yyval.obj).setFuncContract(((Nodo)val_peek(0).obj).checkFuncContract());
                                                        		        ((Nodo)yyval.obj).setContract(true);
                                                        		    }
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos - (Linea " + AnalizadorLexico.numLinea + ")");
                      }
					  }
break;
case 56:
//#line 294 "gramatica.y"
{
	                 yyval=val_peek(0);
	                 }
break;
case 57:
//#line 303 "gramatica.y"
{
                        if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
                        	break;
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("*", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                        			    if(((Nodo)val_peek(0).obj).checkContract() || ((Nodo)val_peek(2).obj).checkContract()){

                            		        ((Nodo)yyval.obj).setFuncContract(((Nodo)val_peek(0).obj).checkFuncContract());
                            		        ((Nodo)yyval.obj).setContract(true);
                            		    }
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos * (Linea " + AnalizadorLexico.numLinea + ")");
                      }
				                }
break;
case 58:
//#line 318 "gramatica.y"
{

	            if (AnalizadorSintactico.listaErroresSintacticos.size() != 0 || AnalizadorLexico.listaDeErrores.size() != 0)
    	            break;
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("/", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                        	                        			    if(((Nodo)val_peek(0).obj).checkContract() || ((Nodo)val_peek(2).obj).checkContract()){
                                                        		        ((Nodo)yyval.obj).setFuncContract(((Nodo)val_peek(0).obj).checkFuncContract());
                                                        		        ((Nodo)yyval.obj).setContract(true);
                                                        		    }
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos / (Linea " + AnalizadorLexico.numLinea + ")");
                      }
				            }
break;
case 59:
//#line 333 "gramatica.y"
{
	                        yyval = val_peek(0);
	                        }
break;
case 60:
//#line 342 "gramatica.y"
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
case 61:
//#line 357 "gramatica.y"
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
case 62:
//#line 373 "gramatica.y"
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
case 63:
//#line 397 "gramatica.y"
{yyval=val_peek(0);}
break;
case 64:
//#line 401 "gramatica.y"
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
		    if(value.checkContract()){

		        ((Nodo)yyval.obj).setFuncContract(variable);
		        ((Nodo)yyval.obj).setContract(true);
		    }
		  }else{
		    if(variable == null){
		    AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
		    }else{
		        AnalizadorSintactico.agregarError("ID de parametro no existe (Linea " + AnalizadorLexico.numLinea + ")");
		    }

             /*error*/
		  }
		  }
break;
case 65:
//#line 434 "gramatica.y"
{
            AnalizadorSintactico.agregarError("La funcion debe tener un parametro (Linea " + AnalizadorLexico.numLinea + ")");
            AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
       }
break;
case 66:
//#line 442 "gramatica.y"
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
case 67:
//#line 457 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta parametro (Linea " + AnalizadorLexico.numLinea + ")");
		AnalizadorSintactico.ambitoActual += "@"+ val_peek(2).sval;
		    AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
		}
break;
case 68:
//#line 461 "gramatica.y"
{AnalizadorSintactico.agregarError("Error: No se permite mas de un parametro (Linea " + AnalizadorLexico.numLinea + ")");
                		AnalizadorSintactico.ambitoActual += "@"+ val_peek(3).sval;
                		    AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
                		}
break;
case 69:
//#line 465 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 70:
//#line 466 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 71:
//#line 467 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 72:
//#line 468 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 73:
//#line 472 "gramatica.y"
{
                    Object[] obj = new Object[2] ;
                    obj[0] = val_peek(0).sval;
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                    aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    obj[1] = aux;
                    yyval= new ParserVal(obj);
		     }
break;
case 74:
//#line 480 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 75:
//#line 485 "gramatica.y"
{yyval=val_peek(0);}
break;
case 76:
//#line 486 "gramatica.y"
{yyval=val_peek(0);}
break;
case 77:
//#line 487 "gramatica.y"
{yyval=val_peek(0);}
break;
case 78:
//#line 488 "gramatica.y"
{yyval=val_peek(0);}
break;
case 79:
//#line 489 "gramatica.y"
{yyval=val_peek(0);}
break;
case 80:
//#line 492 "gramatica.y"
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
					                            			    if(((Nodo)val_peek(1).obj).checkContract()){
                                                    		        ((Nodo)yyval.obj).setFuncContract(((Nodo)val_peek(1).obj).checkFuncContract());
                                                    		        ((Nodo)yyval.obj).setContract(true);
                                                    		    }
                        if(value.getTipoContenido().equals( ((Nodo)val_peek(1).obj).getTipo())){
                            ((Nodo)yyval.obj).setTipo(((Nodo)aux.obj).getTipo());
                        }else{
                        	 AnalizadorSintactico.agregarError("Tipo Incompatible (" + value.getTipoContenido() + "," +  ((Nodo)val_peek(1).obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
                        }
                     }
					}
break;
case 81:
//#line 520 "gramatica.y"
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
									                        			    if(((Nodo)val_peek(1).obj).checkContract()){
                                                                		        ((Nodo)yyval.obj).setFuncContract(((Nodo)val_peek(1).obj).checkFuncContract());
                                                                		        ((Nodo)yyval.obj).setContract(true);
                                                                		    }
									if( (((Nodo)val_peek(1).obj).getTipo()).equals("SINGLE")){
										((Nodo)yyval.obj).setTipo(((Nodo)aux.obj).getTipo());
									}else{
										AnalizadorSintactico.agregarError("Tipo Incompatible (SINGLE, " + ((Nodo)val_peek(1).obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
									}
								}
                     
							}
					}
break;
case 82:
//#line 555 "gramatica.y"
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
        					                            			    if(((Nodo)val_peek(1).obj).checkContract()){
                                                            		        ((Nodo)yyval.obj).setFuncContract(((Nodo)val_peek(1).obj).checkFuncContract());
                                                            		        ((Nodo)yyval.obj).setContract(true);
                                                            		    }
                                if(value.getTipoContenido().equals( ((Nodo)val_peek(1).obj).getTipo())){
                                    ((Nodo)yyval.obj).setTipo(((Nodo)aux.obj).getTipo());
                                }else{
                                	 AnalizadorSintactico.agregarError("Tipo Incompatible (" + value.getTipoContenido() + "," +  ((Nodo)val_peek(1).obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
                                }
                             }
        					}
break;
case 83:
//#line 582 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 84:
//#line 583 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 85:
//#line 584 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 86:
//#line 585 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 87:
//#line 586 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 88:
//#line 587 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ':'(Linea " + (AnalizadorLexico.numLinea -1) + ")");}
break;
case 89:
//#line 588 "gramatica.y"
{AnalizadorSintactico.agregarError("Error, no puede asignarse un comparador(Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 90:
//#line 592 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if(val_peek(6).obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 91:
//#line 600 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");
	                                                    if(val_peek(4).obj == null)
                                                           break;
                                                        ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(2).obj, null));
                                                        ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,null));
                                                        yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(4).obj, (Nodo)auxCuerpo.obj));}
break;
case 92:
//#line 606 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if(val_peek(6).obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 93:
//#line 614 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if(val_peek(6).obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 94:
//#line 622 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if(val_peek(6).obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 95:
//#line 630 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");
	                                                    if(val_peek(4).obj == null)
                                                           break;
                                                        ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(2).obj, null));
                                                        ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,null));
                                                        yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(4).obj, (Nodo)auxCuerpo.obj));}
break;
case 96:
//#line 636 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 97:
//#line 637 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 98:
//#line 638 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 99:
//#line 639 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 100:
//#line 640 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 641 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 102:
//#line 642 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 103:
//#line 643 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 104:
//#line 644 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 105:
//#line 645 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 106:
//#line 646 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 107:
//#line 647 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 108:
//#line 651 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		((Nodo)aux.obj).setTipo("CADENA");
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 109:
//#line 655 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 110:
//#line 656 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 111:
//#line 657 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 112:
//#line 658 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 113:
//#line 659 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 114:
//#line 660 "gramatica.y"
{
	       	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
	                           String lexema = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(2).sval);
                               if(lexema != null){
                                  AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                  ParserVal aux = new ParserVal(new Nodo(lexema));
                                  TDSObject value = AnalizadorLexico.getLexemaObject(lexema);
                                  ((Nodo)aux.obj).setTipo("ID-"+value.getTipoContenido());
                                  yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                               }else{
                                   AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                   AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                   /*stop generacion de arbol*/

                               }
	      }
break;
case 115:
//#line 676 "gramatica.y"
{
                	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
                               	                           String lexema = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(2).sval);

                               if(lexema != null){
                                  AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                  ParserVal aux = new ParserVal(new Nodo(lexema));
                                  TDSObject value = AnalizadorLexico.getLexemaObject(lexema);
                                  ((Nodo)aux.obj).setTipo("CTE-"+value.getTipoVariable());
                                  
                                  yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                               }else{
                                  AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                  AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                  /*stop generacion de arbol*/
                               }
                               }
break;
case 116:
//#line 693 "gramatica.y"
{
                          	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
                                        String lexema = AnalizadorSintactico.getReferenciaPorAmbito("-"+val_peek(2).sval);

                                         if(lexema != null){
                                            AnalizadorLexico.tablaDeSimbolos.remove("-"+val_peek(2).sval);                                          
                                            ParserVal aux = new ParserVal(new Nodo(lexema));
                                            TDSObject value = AnalizadorLexico.getLexemaObject(lexema);
                                            ((Nodo)aux.obj).setTipo("CTE-"+value.getTipoVariable());
                                            yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                                         }else{
                                            AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                            AnalizadorLexico.tablaDeSimbolos.remove("-"+val_peek(2).sval);
                                            /*stop generacion de arbol*/
                                         }
	      }
break;
case 117:
//#line 711 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		 if(val_peek(2).obj == null)
             break;
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 118:
//#line 715 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 119:
//#line 716 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 120:
//#line 717 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 121:
//#line 718 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 122:
//#line 722 "gramatica.y"
{
				if(val_peek(1).obj == null)
					break;
				yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(1).obj, null));}
break;
case 123:
//#line 728 "gramatica.y"
{
				if (val_peek(2).obj != null || val_peek(0).obj != null){
					yyval = new ParserVal (new Nodo(val_peek(1).sval,(Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj ));}
			}
break;
case 124:
//#line 732 "gramatica.y"
{yyval=val_peek(0);}
break;
case 125:
//#line 733 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 126:
//#line 737 "gramatica.y"
{
		  if(val_peek(2).obj == null || val_peek(0).obj == null){
             break;
          }else{
          if(((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
          		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
          }else{
          AnalizadorSintactico.agregarError("No pueden compararse distintos tipos (Linea " + AnalizadorLexico.numLinea + ")");
          }
		  }
		}
break;
case 127:
//#line 748 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 128:
//#line 749 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 129:
//#line 752 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 130:
//#line 753 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 131:
//#line 754 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 132:
//#line 755 "gramatica.y"
{yyval = new ParserVal("<>");}
break;
case 133:
//#line 756 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 134:
//#line 757 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 135:
//#line 758 "gramatica.y"
{
	           AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea +": se esperaba comparacion ==.");
	           yyval = new ParserVal("==");
	        }
break;
case 136:
//#line 764 "gramatica.y"
{yyval= new ParserVal("&&");}
break;
case 137:
//#line 765 "gramatica.y"
{yyval= new ParserVal("||");}
break;
case 138:
//#line 769 "gramatica.y"
{AnalizadorSintactico.tipoActual = "LONG";
             yyval = new ParserVal("LONG");
            }
break;
case 139:
//#line 772 "gramatica.y"
{AnalizadorSintactico.tipoActual = "SINGLE";
             yyval = new ParserVal("SINGLE");
             }
break;
//#line 1821 "Parser.java"
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
