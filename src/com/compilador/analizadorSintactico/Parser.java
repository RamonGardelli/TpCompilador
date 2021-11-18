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
   11,   11,   11,   11,   11,   10,   10,   12,   12,   13,
   13,   13,   13,   13,   13,    7,    7,    3,    3,    3,
   15,   15,   15,   19,   19,   19,   16,   16,   17,   17,
   17,   21,   21,   21,   21,   21,    9,    9,   22,   22,
   22,   23,   23,   23,   24,   24,   24,   24,   25,   25,
   20,   20,   20,   20,   20,   20,   26,   26,    6,    6,
    6,    6,   27,   27,   27,   27,   27,   28,   28,   28,
   28,   28,   28,   28,   28,   28,   28,   28,   28,   28,
   28,   29,   29,   29,   29,   29,   29,   29,   29,   30,
   30,   30,   30,   30,   14,   31,   31,   31,   33,   33,
   33,   33,   33,   33,   34,   34,   34,   34,   34,   34,
   32,   32,   18,   18,
};
final static short yylen[] = {                            2,
    3,    2,    1,    1,    1,    6,    5,    5,    5,    5,
    6,    8,    9,   10,    9,    3,    2,    3,    8,    1,
    6,    6,    6,    6,    6,    2,    1,    1,    1,    4,
    5,    5,    5,    5,    6,    2,    1,    1,    1,    1,
    3,    2,    3,    3,    1,    2,    3,    2,    3,    2,
    3,    5,    4,    4,    4,    5,    1,    4,    3,    3,
    1,    3,    3,    1,    1,    1,    2,    1,    4,    4,
    6,    5,    5,    5,    5,    6,    2,    1,    1,    1,
    1,    1,    4,    7,    3,    3,    4,    8,    6,    7,
    7,    7,    7,    7,    7,    8,    5,    5,    5,    5,
    6,    5,    4,    4,    4,    4,    5,    5,    5,    4,
    3,    3,    3,    4,    3,    3,    1,    3,    3,    6,
    6,    9,    3,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,  133,  134,   45,    0,    3,   38,   39,
   40,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    1,    2,    4,    5,    0,
    0,    0,   79,   80,   81,   82,    0,    0,    0,   42,
    0,    0,    0,    0,   28,   20,    0,   48,   27,   29,
   50,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   37,    0,    0,   66,    0,    0,    0,    0,   64,   68,
    0,    0,    0,    0,    0,    0,  127,  128,  129,  130,
  125,  126,    0,    0,    0,  117,    0,    0,   17,   36,
    0,    0,    0,    0,    0,    0,   43,   41,   44,    0,
    0,    0,    0,    0,   47,    0,   26,   51,   49,   78,
    0,    0,   53,    0,    0,    0,    0,    0,  112,    0,
  113,   18,   16,    0,    0,    0,   67,   86,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   85,    0,
    0,    0,  131,  132,  115,    0,    0,    0,    0,  111,
    0,    0,   55,    0,   54,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   77,   73,  104,    0,  106,
    0,    0,  105,  114,  110,    0,    0,    0,   87,   83,
    0,    0,   62,   63,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  103,    0,  123,    0,    0,
  118,  116,    0,    0,   72,    0,   75,   56,   52,   74,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   30,    0,  107,  102,  108,
  109,   69,   70,    0,    8,    9,    0,   10,    0,   97,
    0,   99,    0,    0,  100,    0,   98,    0,    0,    7,
    0,   76,   71,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   31,   33,    0,   34,   32,    0,    0,
   11,    6,    0,    0,    0,    0,  101,   89,    0,    0,
    0,    0,    0,    0,   24,   25,    0,   23,    0,   22,
    0,    0,    0,   35,   21,   84,   91,   93,   94,    0,
   95,   92,    0,    0,  121,   90,    0,    0,    0,   58,
    0,    0,    0,   96,   88,    0,   12,    0,   19,    0,
    0,    0,   15,   13,    0,  122,   14,
};
final static short yydgoto[] = {                          2,
    7,   26,    8,   28,   29,   30,   31,   46,  214,   47,
   48,   49,   50,   32,    9,   10,   11,  215,   13,   14,
   15,  216,   68,   69,   70,  112,   33,   34,   35,   36,
   85,  146,   86,   87,
};
final static short yysindex[] = {                      -238,
 -172,    0,  -28,    0,    0,    0,  -34,    0,    0,    0,
    0, -190,   -9,  -13,   82,   36, -174,  -17,   -8,  271,
   42,  128,  -38,  111,  357,    0,    0,    0,    0, -234,
  194, -184,    0,    0,    0,    0,  -32,   54,  -35,    0,
 -164,   88,   -6,  -13,    0,    0,  107,    0,    0,    0,
    0,  116, -162,   75,   76,   60,  114,  548,   25,   42,
    0,    9,   89,    0,  405, -142,   11,   49,    0,    0,
 -131, -212,  114,   39,   16,   99,    0,    0,    0,    0,
    0,    0,  119,  510,  -40,    0,  111, -117,    0,    0,
  114,  114,   79,  122,  121, -162,    0,    0,    0,  133,
 -234,   38,  126,  131,    0,  146,    0,    0,    0,    0,
 -119,  124,    0,  134,   -1,  147,  154,  137,    0,   64,
    0,    0,    0,  -26,  158,   67,    0,    0,  111,  111,
  111,  111,  271,  271,  196, -151,    3,  -77,    0,  141,
  111,  318,    0,    0,    0,  526,  149,  271,  -56,    0,
  295,  163,    0,  -31,    0,  165,  168,  138,  335,  357,
  143,  169,  389,    1,  405,    0,    0,    0,  -45,    0,
  150,  156,    0,    0,    0,  175,  159,  111,    0,    0,
   49,   49,    0,    0,  215,  231,  271,  233,  114,  164,
  114,  232,  -52,  114,  253,    0,   70,    0,  182,  149,
    0,    0,  266,  114,    0,   31,    0,    0,    0,    0,
  405,  184,  403,  285,  320,  149,  371,  336,  510,  387,
  162,  372,  377,    5,  378,    0,  399,    0,    0,    0,
    0,    0,    0,   92,    0,    0,  172,    0,  187,    0,
  188,    0,   74,  -42,    0,  199,    0,  302,  111,    0,
  203,    0,    0,  400,  405,  -63,   27,  197,  111,  204,
  111,  405,  413,    0,    0,  213,    0,    0,  205,  414,
    0,    0,  421,  425,  428,  -46,    0,    0,  430,  405,
  108,  441,  449,  431,    0,    0,  453,    0,  129,    0,
  136,  476,  405,    0,    0,    0,    0,    0,    0,  -39,
    0,    0,  478,  149,    0,    0,  255,  464,  258,    0,
  302,  466,  487,    0,    0,  111,    0,  269,    0,  270,
  485,  144,    0,    0,  282,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   29,    0,    0,    0,    0,    0,    0,    0,    0,  267,
    0,    0,    0,    0,    0,    0,    0,  117,    0,    0,
  125,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  498,    0,    0,    0,    0,  423,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   78,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -25,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  447,  460,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   -5,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   62,    0,    0,  511,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -3,    0,    0,    0,    0,    0,    0,
  515,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  550,  752,   19,  -20,    0,  745,   10,    0,  350,  -23,
  521,  -22,    0,   28,    0,    0,    0,    4,  112,    0,
    0,  -21,   -7,  -48,    0,  -65,    0,    0,    0,    0,
    0,    0,  422,  -29,
};
final static int YYTABLESIZE=995;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         67,
  145,   58,   75,   84,   12,   25,  245,   94,   41,  209,
   12,   17,  301,  229,  177,  124,  278,   12,  102,  315,
   54,  100,   56,   98,  107,   27,   25,  152,   83,   62,
  156,   58,    1,  103,   41,  119,   84,  120,   67,  169,
   95,  225,   25,  126,   88,  266,   59,   12,   25,   40,
   74,  104,  134,  129,  142,  130,  111,  170,  129,  226,
  130,   83,   27,  267,   25,  147,  135,  287,  125,  128,
   37,  253,   45,   91,  139,   53,  158,  160,   25,  107,
   38,  161,  183,  184,   92,  206,   66,   45,    3,    4,
  131,    4,    5,   96,    5,  132,  111,  154,    6,  111,
  118,    4,   57,   25,    5,  189,   99,  190,  110,  129,
  248,  130,  129,   25,  130,  113,  114,   28,  151,  197,
  200,  181,  182,   39,   84,  180,   52,   25,  124,  127,
  162,  164,  270,  133,  129,  107,  130,  221,  219,  140,
   51,   84,  185,  186,  188,  199,   25,  148,  305,   83,
  129,  166,  130,   25,  111,   66,  234,  203,  141,   41,
   45,  155,  153,  218,  167,   58,   83,   25,   46,  310,
  163,  129,   25,  130,  109,   45,  311,   25,  129,  194,
  130,  195,   25,   46,  326,  165,  129,  171,  130,  142,
  224,  129,  168,  130,  172,  173,  237,  178,  107,  196,
  204,   25,  285,  207,  243,  210,  244,  211,  230,  222,
  228,   25,  300,  277,  231,  232,  314,  233,  280,   73,
   97,  249,  240,  255,  208,   18,    3,  281,   19,    4,
   20,    4,    5,   25,    5,   25,   21,  289,   93,  291,
  143,  144,   16,   22,  176,   23,   18,    3,   24,   19,
    4,   42,   55,    5,   25,  124,  124,   21,  304,  191,
   57,  192,   18,   43,  122,   19,   23,   20,   18,   24,
   25,   19,   25,   60,  123,  119,  119,  120,  120,   60,
   22,  280,   23,  303,   18,   24,  252,   19,   23,   20,
  242,   24,  286,  120,  322,   60,  137,   18,   18,  159,
   19,   19,   22,   20,   23,   25,   37,   24,   60,   60,
   25,  247,   63,   64,   43,   22,   22,   23,   23,  174,
   24,   24,  179,   18,   65,  258,   19,   57,   20,  115,
  116,  117,  275,   18,   60,  205,   19,   28,   20,   28,
   28,   22,    4,   23,   60,    5,   24,   18,   28,  110,
   19,   22,    6,   23,   28,   28,   24,   28,   60,  259,
   28,   82,   66,   81,   43,   22,   18,   23,  106,   19,
   24,  108,   45,   18,  213,  261,   19,   60,   20,   66,
   46,   63,   64,   43,   60,    4,   23,   18,    5,   24,
   19,   22,   18,   23,  157,   19,   24,   18,   60,  212,
   19,   66,   18,   60,  220,   19,   71,   23,   60,   43,
   24,  260,   23,   60,   43,   24,   82,   23,   81,   43,
   24,   18,   23,  263,   19,   24,  262,  271,   58,  223,
  264,   18,   60,   66,   19,  265,  268,  272,   43,  269,
  283,   23,   60,  256,   24,  273,  274,   66,   82,   66,
   81,   23,  293,   18,   24,   18,   19,  279,   19,   89,
  187,  282,  288,   61,   60,   61,   60,   61,  294,  290,
  295,  308,  296,   23,   18,   23,   24,   19,   24,  297,
  235,   61,   61,  298,   61,   60,  299,   59,  302,   59,
   18,   59,   18,   19,   23,   19,  236,   24,  238,  306,
   60,   60,   60,   60,   60,   59,   59,  307,   59,  217,
   23,  309,   23,   24,  227,   24,  312,  316,   60,   60,
  317,   60,  318,  319,  320,   18,   37,  321,   19,   37,
   18,  250,   37,   19,  323,  324,   60,   37,   65,   65,
   65,   60,   65,  325,   65,   23,   37,  327,   24,   37,
   23,   57,  129,   24,  130,   58,   65,   65,    4,   65,
  254,    5,  257,   44,  105,  110,    0,  202,    0,   82,
   66,   81,    0,  198,   77,   78,   79,   80,    0,    0,
    0,    4,    0,    0,    5,   82,    0,   81,   63,   64,
    0,    0,   66,    0,    0,    0,    0,    0,    4,    0,
    0,    5,    0,    0,  284,   63,   64,   82,    0,   81,
    0,  292,    0,    0,    0,    0,    0,    0,    0,    0,
    4,    0,    0,    5,    0,    0,   76,   63,   64,   77,
   78,   79,   80,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  313,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    4,    0,    0,    5,    0,    0,    0,   63,
   64,   77,   78,   79,   80,    0,    4,    0,    4,    5,
    0,    5,    0,   63,   64,   63,   64,    0,   61,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   61,    0,
    0,    0,    0,    0,    0,   61,   61,   61,   61,    0,
    0,    0,   59,   61,   61,    0,    0,    0,    0,    0,
    0,    0,   59,    0,    0,   60,    0,    0,    0,   59,
   59,   59,   59,    0,    0,   60,    0,   59,   59,    0,
    0,    0,   60,   60,   60,   60,    0,    0,    0,    0,
   60,   60,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   65,    0,    0,    0,    0,   45,    0,
    0,    0,    0,   65,   61,    0,   72,    0,    0,    0,
   65,   65,   65,   65,    0,   90,    0,    0,   65,   65,
    0,  201,   77,   78,   79,   80,  101,    0,   45,    4,
    0,   45,    5,    0,    0,    0,   63,   64,   77,   78,
   79,   80,    0,    0,    0,    0,   90,    0,  119,    0,
  121,    4,    0,    0,    5,    0,    0,    0,   63,   64,
   77,   78,   79,   80,  136,  138,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  149,  150,   45,    0,  101,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  175,    0,    0,    0,    0,    0,   61,   61,   61,
    0,    0,    0,    0,    0,    0,    0,    0,  193,    0,
    0,    0,   61,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   45,    0,    0,   45,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   90,
   90,   61,   90,    0,    0,    0,    0,    0,    0,    0,
  239,    0,  241,    0,    0,  246,    0,   90,    0,    0,
    0,    0,    0,    0,    0,  251,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   45,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   90,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  276,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         21,
   41,   40,   24,   25,    1,   40,   59,   40,   44,   41,
    7,   40,   59,   59,   41,   41,   59,   14,   42,   59,
   17,   42,   40,   59,   47,    7,   40,   93,   25,   20,
   96,   40,  271,   40,   44,   41,   58,   41,   60,   41,
   37,   41,   40,   65,  279,   41,   19,   44,   40,   59,
   23,   58,  265,   43,   84,   45,   53,   59,   43,   59,
   45,   58,   44,   59,   40,   87,  279,   41,   65,   59,
  261,   41,   44,  258,   59,   40,  100,   40,   40,  102,
  271,  102,  131,  132,  269,  151,   45,   59,  261,  264,
   42,  264,  267,   40,  267,   47,   93,   94,  271,   96,
   41,  264,   41,   40,  267,  257,  271,  259,  271,   43,
   41,   45,   43,   40,   45,   41,   41,   40,   40,  141,
  142,  129,  130,   12,  146,   59,   15,   40,   40,  272,
  103,  104,   41,  265,   43,  158,   45,  161,  160,   41,
   59,  163,  133,  134,  135,  142,   40,  265,   41,  146,
   43,  271,   45,   40,  151,   45,  178,  148,   40,   44,
   44,   41,   41,  160,   41,   40,  163,   40,   44,   41,
   40,   43,   40,   45,   59,   59,   41,   40,   43,  257,
   45,  259,   40,   59,   41,   40,   43,   41,   45,  219,
  163,   43,   59,   45,   41,   59,  187,   40,  221,   59,
  257,   40,  266,   41,  257,   41,  259,   40,   59,   41,
  256,   40,  259,  256,   59,   41,  256,   59,  248,  258,
  256,   40,   59,   40,  256,  260,  261,  249,  263,  264,
  265,  264,  267,   40,  267,   40,  271,  259,  271,  261,
  281,  282,  271,  278,  271,  280,  260,  261,  283,  263,
  264,  265,  270,  267,   40,  281,  282,  271,  280,  257,
  269,  259,  260,  277,  256,  263,  280,  265,  260,  283,
   40,  263,   40,  271,  266,  281,  282,  281,  282,  271,
  278,  311,  280,  280,  260,  283,  256,  263,  280,  265,
   59,  283,  266,  269,  316,  271,  258,  260,  260,  262,
  263,  263,  278,  265,  280,   40,   40,  283,  271,  271,
   40,   59,  271,  272,  277,  278,  278,  280,  280,  256,
  283,  283,  256,  260,  283,   41,  263,  266,  265,  270,
  271,  272,  259,  260,  271,   41,  263,  260,  265,  262,
  263,  278,  264,  280,  271,  267,  283,  260,  271,  271,
  263,  278,  271,  280,  277,  278,  283,  280,  271,   40,
  283,   60,   45,   62,  277,  278,  260,  280,  262,  263,
  283,  256,  256,  260,   40,   40,  263,  271,  265,   45,
  256,  271,  272,  277,  271,  264,  280,  260,  267,  283,
  263,  278,  260,  280,  262,  263,  283,  260,  271,  262,
  263,   45,  260,  271,  262,  263,  279,  280,  271,  277,
  283,   41,  280,  271,  277,  283,   60,  280,   62,  277,
  283,  260,  280,  262,  263,  283,   40,  256,   40,   41,
   59,  260,  271,   45,  263,   59,   59,  266,  277,   41,
   41,  280,  271,   41,  283,  259,  259,   45,   60,   45,
   62,  280,   40,  260,  283,  260,  263,  259,  263,  266,
  265,  259,  266,   41,  271,   43,  271,   45,  256,  266,
  266,   41,   59,  280,  260,  280,  283,  263,  283,   59,
  266,   59,   60,   59,   62,  271,   59,   41,   59,   43,
  260,   45,  260,  263,  280,  263,  266,  283,  266,   59,
   41,  271,   43,  271,   45,   59,   60,   59,   62,  160,
  280,   59,  280,  283,  165,  283,   41,   40,   59,   60,
  266,   62,   59,  266,   59,  260,  260,   41,  263,  263,
  260,  266,  266,  263,  266,  266,  271,  271,   41,   42,
   43,  271,   45,   59,   47,  280,  280,  266,  283,  283,
  280,   41,   43,  283,   45,   41,   59,   60,  264,   62,
  211,  267,  213,   14,   44,  271,   -1,  146,   -1,   60,
   45,   62,   -1,  256,  273,  274,  275,  276,   -1,   -1,
   -1,  264,   -1,   -1,  267,   60,   -1,   62,  271,  272,
   -1,   -1,   45,   -1,   -1,   -1,   -1,   -1,  264,   -1,
   -1,  267,   -1,   -1,  255,  271,  272,   60,   -1,   62,
   -1,  262,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  264,   -1,   -1,  267,   -1,   -1,  270,  271,  272,  273,
  274,  275,  276,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  293,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  264,   -1,   -1,  267,   -1,   -1,   -1,  271,
  272,  273,  274,  275,  276,   -1,  264,   -1,  264,  267,
   -1,  267,   -1,  271,  272,  271,  272,   -1,  256,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  266,   -1,
   -1,   -1,   -1,   -1,   -1,  273,  274,  275,  276,   -1,
   -1,   -1,  256,  281,  282,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  266,   -1,   -1,  256,   -1,   -1,   -1,  273,
  274,  275,  276,   -1,   -1,  266,   -1,  281,  282,   -1,
   -1,   -1,  273,  274,  275,  276,   -1,   -1,   -1,   -1,
  281,  282,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,   14,   -1,
   -1,   -1,   -1,  266,   20,   -1,   22,   -1,   -1,   -1,
  273,  274,  275,  276,   -1,   31,   -1,   -1,  281,  282,
   -1,  256,  273,  274,  275,  276,   42,   -1,   44,  264,
   -1,   47,  267,   -1,   -1,   -1,  271,  272,  273,  274,
  275,  276,   -1,   -1,   -1,   -1,   62,   -1,   57,   -1,
   59,  264,   -1,   -1,  267,   -1,   -1,   -1,  271,  272,
  273,  274,  275,  276,   73,   74,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   91,   92,  100,   -1,  102,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  120,   -1,   -1,   -1,   -1,   -1,  133,  134,  135,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  137,   -1,
   -1,   -1,  148,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  158,   -1,   -1,  161,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  185,
  186,  187,  188,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  189,   -1,  191,   -1,   -1,  194,   -1,  203,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  204,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  221,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  237,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  243,
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
"sentSoloEjecutables : sentSoloEjecutables sentEjecutables",
"sentSoloEjecutables : sentEjecutables",
"sentenciasDeclarativas : declaraVariable",
"sentenciasDeclarativas : declaraFunc",
"sentenciasDeclarativas : declaraVarFunc",
"declaraVariable : tipo listaVariables ';'",
"declaraVariable : listaVariables ';'",
"declaraVariable : tipo listaVariables error",
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
};

//#line 510 "gramatica.y"


public int yylex() {
    int value = AnalizadorLexico.yylex();
    yylval = new ParserVal(AnalizadorLexico.refTDS); 
    return value;
}

public void yyerror(String string) {
	AnalizadorSintactico.agregarError("Parser: " + string);
}
//#line 664 "Parser.java"
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
{
          		  yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
          		}
break;
case 28:
//#line 105 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 29:
//#line 106 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 30:
//#line 109 "gramatica.y"
{

            AnalizadorSintactico.agregarAnalisis("sent control (Linea " + AnalizadorLexico.numLinea + ")");
            yyval =  new ParserVal(new Nodo("CONTRACT",(Nodo)val_peek(0).obj,null));
            }
break;
case 31:
//#line 114 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 32:
//#line 115 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 33:
//#line 116 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 34:
//#line 117 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 35:
//#line 118 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 36:
//#line 124 "gramatica.y"
{ if ((val_peek(1).obj != null) && (val_peek(0).obj != null))
				 	{yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, (Nodo)val_peek(1).obj));}
				  else if((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, null));}
				       else if ((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));}
				}
break;
case 37:
//#line 130 "gramatica.y"
{
				    yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));				    
				}
break;
case 39:
//#line 138 "gramatica.y"
{yyval=val_peek(0);}
break;
case 41:
//#line 142 "gramatica.y"
{
                    AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 42:
//#line 145 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 43:
//#line 146 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 44:
//#line 149 "gramatica.y"
{
                		     if( AnalizadorSintactico.esVariableRedeclarada(val_peek(0).sval + AnalizadorSintactico.ambitoActual)){
                                   /*corto arbol*/
                             }else{
                                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                                aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                                AnalizadorLexico.tablaDeSimbolos.put(val_peek(0).sval + AnalizadorSintactico.ambitoActual,aux);
                             }
                            }
break;
case 45:
//#line 158 "gramatica.y"
{
	                if( AnalizadorSintactico.esVariableRedeclarada(val_peek(0).sval + AnalizadorSintactico.ambitoActual)){
                        /*corto arbol*/
                    }else{
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                     aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    AnalizadorLexico.tablaDeSimbolos.put(val_peek(0).sval + AnalizadorSintactico.ambitoActual,aux);
                }
	      }
break;
case 46:
//#line 167 "gramatica.y"
{AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 47:
//#line 171 "gramatica.y"
{
        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
        yyval=new ParserVal (new Nodo(val_peek(2).sval, (Nodo)val_peek(0).obj, null));
        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
        }
break;
case 48:
//#line 176 "gramatica.y"
{
	        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
	        yyval=new ParserVal (new Nodo(val_peek(1).sval, (Nodo)val_peek(0).obj, null));
	        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
	   }
break;
case 49:
//#line 183 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 50:
//#line 185 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 51:
//#line 186 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 52:
//#line 189 "gramatica.y"
{
            AnalizadorSintactico.tipoActual = val_peek(4).sval;
           }
break;
case 53:
//#line 192 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 54:
//#line 193 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 55:
//#line 194 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 56:
//#line 195 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 57:
//#line 199 "gramatica.y"
{yyval=val_peek(0);}
break;
case 59:
//#line 205 "gramatica.y"
{
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("+", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos + (Linea " + AnalizadorLexico.numLinea + ")");
                      }
                      }
break;
case 60:
//#line 213 "gramatica.y"
{
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("-", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos - (Linea " + AnalizadorLexico.numLinea + ")");
                      }
					  }
break;
case 61:
//#line 221 "gramatica.y"
{
	                 yyval=val_peek(0);
	                 }
break;
case 62:
//#line 230 "gramatica.y"
{
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("*", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos * (Linea " + AnalizadorLexico.numLinea + ")");
                      }
				                }
break;
case 63:
//#line 238 "gramatica.y"
{
                      if (((Nodo)val_peek(2).obj).getTipo().equals(((Nodo)val_peek(0).obj).getTipo())){
                        	yyval= new ParserVal(new Nodo("/", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                        	((Nodo)yyval.obj).setTipo(((Nodo)val_peek(0).obj).getTipo());
                      }else{
                            AnalizadorSintactico.agregarError("Incompatibilidad de tipos / (Linea " + AnalizadorLexico.numLinea + ")");
                      }
				            }
break;
case 64:
//#line 246 "gramatica.y"
{
	                        yyval = val_peek(0);
	                        }
break;
case 65:
//#line 255 "gramatica.y"
{
                     String lexema = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(0).sval);
                     if(lexema != null){
                        AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                        TDSObject value = AnalizadorLexico.getLexemaObject(lexema);
                        yyval= new ParserVal(new Nodo(val_peek(0).sval));
                        ((Nodo)yyval.obj).setTipo(value.getTipoContenido());
                        /*((Nodo)$$.obj).setTipoContenido("VAR");*/
                     }else{
                         AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                         /*stop generacion de arbol*/
                     }
                }
break;
case 66:
//#line 268 "gramatica.y"
{
                    if (val_peek(0).sval != null){
                        yyval= new ParserVal(new Nodo(val_peek(0).sval));
                        TDSObject value = AnalizadorLexico.getLexemaObject(val_peek(0).sval);
                        if( value != null){
                            ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                        }
                    }
	            }
break;
case 67:
//#line 277 "gramatica.y"
{
	                AnalizadorLexico.agregarNegativoTDS(val_peek(0).sval);
			        yyval= new ParserVal(new Nodo("-"+val_peek(0).sval));
			        TDSObject value = AnalizadorLexico.getLexemaObject("-"+val_peek(0).sval);
                    if( value != null){
                        if ( value.getTipoVariable() == "LONG" ) {
                                long l = Long.parseLong("-"+val_peek(0).sval);
                                if( !((l >= -2147483648) && (l <= 2147483647))){
                                   AnalizadorSintactico.agregarError("CTE LONG fuera de Rango (Linea " + AnalizadorLexico.numLinea + ")");
                                   /*break*/
                                }else{
                                      ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                                }
                        }else{
                                float f = Float.parseFloat(("-"+val_peek(0).sval).replace('S','E'));
                                if( f != 0.0f  ){
                                    if( !((f > -3.40282347E+38) && (f < -1.17549435E-38 ))){
                                       AnalizadorSintactico.agregarError("CTE FLOAT fuera de Rango (Linea " + AnalizadorLexico.numLinea + ")");
                                    /*break*/
                                     }else{
                                        ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                                     }
                                }else{((Nodo)yyval.obj).setTipo(value.getTipoVariable());}
                        }
                    }
			    }
break;
case 68:
//#line 303 "gramatica.y"
{yyval=val_peek(0);}
break;
case 69:
//#line 307 "gramatica.y"
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
		    ParserVal aux2= new ParserVal(val_peek(1).sval);
		    ParserVal aux= new ParserVal(val_peek(3).sval);
		    yyval= new ParserVal(new Nodo("LF",(Nodo)aux.obj, (Nodo)aux2.obj ));
		    ((Nodo)yyval.obj).setTipo(value.getTipoContenido());
		  }else{
             AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
             /*error*/
		  }
		  }
break;
case 70:
//#line 327 "gramatica.y"
{
	   		  String variable = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(3).sval);
       		  if(variable != null){
       		    AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
       		    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
       		    ParserVal aux= new ParserVal(val_peek(3).sval);
       		    yyval= new ParserVal(new Nodo("LF",(Nodo)aux.obj, null ));
       		    ((Nodo)yyval.obj).setTipo(value.getTipoContenido());
       		  }else{
                    AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
                    /*error*/
       		  }
	   }
break;
case 71:
//#line 344 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
            if( AnalizadorSintactico.esVariableRedeclarada(val_peek(3).sval + AnalizadorSintactico.ambitoActual)){
                /*corto arbol*/
            }else{
                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
                aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                aux.setTipoParametro(((TDSObject)((Object[])(val_peek(1).obj))[1]).getTipoContenido());
                AnalizadorLexico.tablaDeSimbolos.put(val_peek(3).sval + AnalizadorSintactico.ambitoActual,aux);
                yyval=val_peek(3);
            }
            AnalizadorSintactico.ambitoActual += "@"+ val_peek(3).sval;
            AnalizadorLexico.tablaDeSimbolos.put(((Object[])(val_peek(1).obj))[0] + AnalizadorSintactico.ambitoActual,(TDSObject)((Object[])(val_peek(1).obj))[1]);
			}
break;
case 72:
//#line 357 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
		     if( AnalizadorSintactico.esVariableRedeclarada(val_peek(2).sval + AnalizadorSintactico.ambitoActual)){
                   /*corto arbol*/
             }else{
                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                AnalizadorLexico.tablaDeSimbolos.put(val_peek(2).sval + AnalizadorSintactico.ambitoActual,aux);
                yyval=val_peek(2);
             }
             AnalizadorSintactico.ambitoActual += "@"+ val_peek(2).sval;
            }
break;
case 73:
//#line 368 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 74:
//#line 369 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 75:
//#line 370 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 76:
//#line 371 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 77:
//#line 375 "gramatica.y"
{
                    Object[] obj = new Object[2] ;
                    obj[0] = val_peek(0).sval;
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                    aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    obj[1] = aux;
                    yyval= new ParserVal(obj);
		     }
break;
case 78:
//#line 383 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 79:
//#line 388 "gramatica.y"
{yyval=val_peek(0);}
break;
case 80:
//#line 389 "gramatica.y"
{yyval=val_peek(0);}
break;
case 81:
//#line 390 "gramatica.y"
{yyval=val_peek(0);}
break;
case 82:
//#line 391 "gramatica.y"
{yyval=val_peek(0);}
break;
case 83:
//#line 394 "gramatica.y"
{
                    String variable = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(3).sval);
                    if(variable == null){
                       AnalizadorSintactico.agregarError("Variable no definida (Linea " + AnalizadorLexico.numLinea + ")");
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
case 84:
//#line 412 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 85:
//#line 413 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 86:
//#line 414 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 87:
//#line 415 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 88:
//#line 419 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 89:
//#line 425 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 90:
//#line 426 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 91:
//#line 427 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 92:
//#line 428 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 93:
//#line 429 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 94:
//#line 430 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 95:
//#line 431 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 96:
//#line 432 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 97:
//#line 433 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 98:
//#line 434 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 99:
//#line 435 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 100:
//#line 436 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 437 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 102:
//#line 440 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 103:
//#line 444 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 104:
//#line 445 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 105:
//#line 446 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 106:
//#line 447 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 107:
//#line 448 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 110:
//#line 453 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 111:
//#line 455 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 112:
//#line 456 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 113:
//#line 457 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 114:
//#line 458 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 115:
//#line 462 "gramatica.y"
{yyval=val_peek(1);}
break;
case 117:
//#line 466 "gramatica.y"
{
	       AnalizadorSintactico.agregarError("EAAAAAAAAAAAAAAAAAAA");
	     yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(0).obj, null));}
break;
case 118:
//#line 469 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 119:
//#line 473 "gramatica.y"
{
		  AnalizadorSintactico.agregarError("EAAAAAAAAAAAAAAAAAAA");
		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
		}
break;
case 123:
//#line 480 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 124:
//#line 481 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 125:
//#line 484 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 126:
//#line 485 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 127:
//#line 486 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 128:
//#line 487 "gramatica.y"
{yyval = new ParserVal("!=");}
break;
case 129:
//#line 488 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 130:
//#line 489 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 133:
//#line 497 "gramatica.y"
{AnalizadorSintactico.tipoActual = "LONG";
             yyval = new ParserVal("LONG");
            }
break;
case 134:
//#line 500 "gramatica.y"
{AnalizadorSintactico.tipoActual = "SINGLE";
             yyval = new ParserVal("SINGLE");
             }
break;
//#line 1542 "Parser.java"
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
