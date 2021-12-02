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
   15,   15,   15,   15,   15,   19,   19,   19,   16,   16,
   17,   17,   17,   21,   21,   21,   21,   21,    9,    9,
   22,   22,   22,   23,   23,   23,   24,   24,   24,   24,
   25,   25,   20,   20,   20,   20,   20,   20,   26,   26,
    6,    6,    6,    6,   27,   27,   27,   27,   27,   27,
   27,   27,   27,   27,   28,   28,   28,   28,   28,   28,
   28,   28,   28,   28,   28,   28,   28,   28,   29,   29,
   29,   29,   29,   29,   29,   29,   30,   30,   30,   30,
   30,   14,   32,   32,   32,   31,   31,   31,   31,   31,
   31,   34,   34,   34,   34,   34,   34,   34,   33,   33,
   18,   18,
};
final static short yylen[] = {                            2,
    3,    2,    1,    1,    1,    6,    5,    5,    5,    5,
    6,    8,    9,   10,    9,    3,    2,    3,    8,    1,
    6,    6,    6,    6,    6,    2,    1,    1,    1,    4,
    5,    5,    5,    5,    6,    2,    1,    1,    1,    1,
    3,    2,    3,    2,    2,    3,    1,    2,    3,    2,
    3,    2,    3,    5,    4,    4,    4,    5,    1,    4,
    3,    3,    1,    3,    3,    1,    1,    1,    2,    1,
    4,    4,    6,    5,    5,    5,    5,    6,    2,    1,
    1,    1,    1,    1,    4,    4,    7,    3,    3,    4,
    3,    4,    3,    4,    8,    6,    7,    7,    7,    7,
    7,    7,    8,    5,    5,    5,    5,    6,    5,    4,
    4,    4,    4,    5,    5,    5,    4,    3,    3,    3,
    4,    3,    3,    1,    3,    3,    6,    6,    9,    3,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,  141,  142,    0,    0,    3,   38,   39,
   40,    0,    0,    0,    0,    0,    0,   44,    0,    0,
    0,    0,    0,    0,    0,    0,    1,    2,    4,    5,
    0,    0,    0,   81,   82,   83,   84,   45,    0,    0,
    0,   42,    0,    0,    0,    0,   28,   20,    0,   50,
   27,   29,   47,   52,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   68,    0,    0,    0,
    0,    0,   66,   70,    0,    0,    0,    0,    0,    0,
  134,  135,  136,  137,  138,  132,  133,    0,    0,  124,
    0,    0,    0,   36,   17,    0,    0,    0,    0,    0,
    0,   43,   41,   46,    0,    0,    0,    0,    0,   49,
    0,   26,   53,   51,   80,    0,    0,   55,    0,    0,
    0,    0,    0,  119,    0,  120,   18,   16,    0,   91,
    0,    0,    0,   69,   93,    0,   89,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   88,    0,    0,
    0,  139,  140,  122,    0,    0,    0,    0,  118,    0,
    0,   57,    0,   56,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   79,   75,  111,    0,  113,    0,
    0,  112,  121,  117,    0,    0,    0,   90,   85,   94,
   92,   86,    0,    0,   64,   65,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  110,    0,  130,
    0,    0,  125,  123,    0,    0,   74,    0,   77,   58,
   54,   76,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   30,    0,  114,
  109,  115,  116,   71,   72,    0,    8,    9,    0,   10,
    0,  104,    0,  106,    0,    0,  107,    0,  105,    0,
    0,    7,    0,   78,   73,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   31,   33,    0,   34,   32,
    0,    0,   11,    6,    0,    0,    0,    0,  108,   96,
    0,    0,    0,    0,    0,    0,   24,   25,    0,   23,
    0,   22,    0,    0,    0,   35,   21,   87,   98,  100,
  101,    0,  102,   99,    0,    0,  128,   97,    0,    0,
    0,   60,    0,    0,    0,  103,   95,    0,   12,    0,
   19,    0,    0,    0,   15,   13,    0,  129,   14,
};
final static short yydgoto[] = {                          2,
    7,   27,    8,   29,   30,   31,   32,   48,  226,   49,
   50,   51,   52,   33,    9,   10,   11,  227,   13,   14,
   15,  228,   72,   73,   74,  117,   34,   35,   36,   37,
   90,   91,  155,   92,
};
final static short yysindex[] = {                      -236,
 -125,    0,  -24,    0,    0, -189,  -35,    0,    0,    0,
    0, -184,   51,   -8,  -49,   71, -179,    0,  -12,  -34,
   28,  258,  132,  -31,   38,  490,    0,    0,    0,    0,
  171, -182, -172,    0,    0,    0,    0,    0,  -33,   75,
  -23,    0, -133,   92,   -9,   -8,    0,    0,  111,    0,
    0,    0,    0,    0,   61, -141,  102,  103,  114,  118,
  537,   18,   96,   28, -165,  108,    0,  511, -123,  176,
   21,   67,    0,    0, -112, -200,  118,   42,   53,  122,
    0,    0,    0,    0,    0,    0,    0,  127,  476,    0,
  -15,   38,  -97,    0,    0,  118,  118,  -27,  249,  130,
 -141,    0,    0,    0,  137, -105,   52,  135,  145,    0,
  161,    0,    0,    0,    0,  -69,  162,    0,  150,   -7,
  163,  169,  156,    0,   68,    0,    0,    0,    9,    0,
  178,  472,  157,    0,    0,   30,    0,   38,   38,   38,
   38,   28,   28,  173, -122,   14,  -79,    0,  164,   38,
  345,    0,    0,    0,  524,  136,   28,   -3,    0,   83,
  183,    0,   12,    0,  241,  244,  142,  276,  490,  147,
  254,  462,    4,  282,    0,    0,    0,  -48,    0,  245,
  267,    0,    0,    0,  293,  305,   38,    0,    0,    0,
    0,    0,   67,   67,    0,    0,  100,  110,   28,  140,
  118,  321,  118,  334,  -45,  118,  357,    0,  121,    0,
  347,  136,    0,    0,  179,  118,    0,   20,    0,    0,
    0,    0,  282,  381,  295,  382,  412,  136,  414,  420,
  476,  422,  166,  410,  415,   15,  421,    0,  430,    0,
    0,    0,    0,    0,    0,  143,    0,    0, -153,    0,
  214,    0,  223,    0,   78,  -37,    0,  225,    0,  379,
   38,    0,  226,    0,    0,  450,  282,  227,  -16,  230,
   38,  235,   38,  282,  464,    0,    0,  250,    0,    0,
  239,  405,    0,    0,  453,  459,  461,  -42,    0,    0,
  466,  282,  148,  467,  468,  487,    0,    0,  480,    0,
  149,    0,  154,  501,  282,    0,    0,    0,    0,    0,
    0,  -36,    0,    0,  504,  136,    0,    0,  279,  496,
  291,    0,  379,  499,  519,    0,    0,   38,    0,  297,
    0,  298,  506,  155,    0,    0,  302,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,   60,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   60,    0,    0,    0,    0,    0,    0,    0,    0,
  304,    0,    0,    0,    0,    0,    0,    0,    0,   62,
    0,    0,  101,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -149,    0,  416,    0,    0,    0,    0,
    0,  427,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   82,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -40,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  438,  449,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -21,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   -4,    0,    0,
  533,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -17,    0,    0,    0,    0,
    0,    0,  535,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  563,  755,   44,  -14,    0,  793,  771,    0,  -98,  -11,
  532,  328,    0,  555,    0,    0,    0,    1,  119,    0,
    0,  -22,  -95,  -81,    0,  -20,    0,    0,    0,    0,
  -39,    0,    0,  -77,
};
final static int YYTABLESIZE=1026;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         71,
  131,   12,   79,   89,   26,   61,   99,   12,   61,   54,
  241,  151,  160,  257,   12,   17,  313,   57,  131,  126,
   43,  290,  327,  127,  299,  154,   88,   59,  133,  105,
  108,   26,  107,  178,    1,  103,   59,  126,   89,  100,
   71,  127,  193,  194,  237,  132,   12,  136,  109,  186,
   28,  179,  221,   26,  151,  278,  116,   26,  195,  196,
  265,   88,  238,  138,  143,  139,   18,   26,  131,  156,
  229,   38,  138,  279,  139,  239,   39,  161,  144,  137,
  165,   26,   69,   95,    4,   96,   40,    5,  192,   28,
  127,  169,  170,  167,   43,  138,   97,  139,  116,  163,
  128,  116,  283,   47,   43,   47,   37,   26,  140,   42,
   56,  148,  284,  141,  101,  214,   37,   26,   47,  114,
   47,   28,    4,  217,  266,    5,  269,  209,  212,  115,
   41,   26,   89,   55,  201,    3,  202,  104,    4,  218,
   69,    5,  118,  119,   48,    6,  231,  129,  134,   89,
   26,  211,  142,  151,  123,   88,   70,   26,  233,   48,
  116,  260,  149,  138,  246,  139,  150,  157,  296,  230,
  164,   26,   88,   93,   61,  304,   26,  206,  138,  207,
  139,   26,  292,  282,  172,  138,   26,  139,  317,  322,
  138,  138,  139,  139,  323,  338,  138,  138,  139,  139,
  174,  175,  176,  180,  292,   26,  325,  240,  177,  181,
   26,  255,   26,  256,  182,  190,  312,  187,  289,  326,
   69,   53,  208,  219,   19,    3,   77,   20,    4,   21,
    4,    5,  102,    5,   60,   22,    4,   98,  293,    5,
  131,  131,   23,  115,   24,  292,   16,   25,  301,  298,
  303,   19,    3,  216,   20,    4,   44,   58,    5,  126,
  126,   59,   22,  127,  127,  152,  153,  220,   45,  316,
  203,   24,  204,   19,   25,  264,   20,   19,   21,  185,
   20,  222,   21,  223,   63,  191,  125,   19,   63,  162,
   20,   23,  315,   24,  234,   23,   25,   24,   63,  146,
   25,   19,   69,  242,   20,  334,   21,   24,   66,   67,
   25,   19,   63,  168,   20,  225,  113,   47,   70,   23,
   69,   24,   63,  183,   25,  243,   69,   19,   45,   23,
   20,   24,   21,  244,   25,  268,  287,   19,   63,   69,
   20,   28,   21,   28,   28,   23,    4,   24,   63,    5,
   25,   19,   28,  115,   20,   23,   48,   24,   28,   28,
   25,   28,   63,  245,   28,  247,   66,   67,   45,   23,
   19,   24,  111,   20,   25,  248,  112,   19,   68,  252,
   20,   63,   21,  120,  121,  122,  261,   45,   63,   69,
   24,   19,  254,   25,   20,   23,   19,   24,  166,   20,
   25,   19,   63,  224,   20,  250,   19,   63,  232,   20,
   75,   24,   63,   45,   25,  259,   24,   63,   45,   25,
  267,   24,  270,   45,   25,   19,   24,  275,   20,   25,
   19,  135,   19,   20,  112,   20,   63,  199,   87,   85,
   86,   63,   45,   63,  262,   24,   66,   67,   25,   93,
   24,  271,   24,   25,  272,   25,   67,   67,   67,  273,
   67,  274,   67,  308,   87,   85,   86,   63,  276,   63,
  281,   63,  285,  277,   67,   67,   67,   67,   61,  280,
   61,  286,   61,  291,  294,   63,   63,   63,   63,   62,
  295,   62,  297,   62,  112,  300,   61,   61,   61,   61,
  302,   61,  235,  305,  307,  306,   69,   62,   62,   62,
   62,  309,    4,   18,  138,    5,  139,  310,  138,  311,
  139,   87,   85,   86,  314,  318,  319,  320,   66,   67,
  189,   87,   85,   86,   69,   87,   85,   86,  321,    4,
   68,  324,    5,  328,  329,    4,   66,   67,    5,   87,
   85,   86,   66,   67,  330,   69,  331,  332,    4,  333,
  112,    5,  335,  336,  337,   66,   67,  339,   69,   37,
   87,   85,   86,   59,   62,   60,   46,  110,   78,    0,
    0,   69,    0,   87,   85,   86,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   87,   85,   86,    0,
  210,    0,    0,    0,    0,    0,    0,    0,    4,    0,
    0,    5,    0,    0,    0,   66,   67,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   81,   82,   83,   84,    0,    0,    0,    0,    0,
    0,    0,  171,  173,    0,    0,    0,    0,    0,    0,
    0,   67,    0,    0,    0,    0,    0,   81,   82,   83,
   84,   67,   63,    0,    0,    0,    0,    0,   67,   67,
   67,   67,   63,   61,    0,    0,   67,   67,    0,   63,
   63,   63,   63,   61,   62,    0,    0,   63,   63,    0,
   61,   61,   61,   61,   62,    0,    0,    0,   61,   61,
    0,   62,   62,   62,   62,    4,  236,  188,    5,   62,
   62,    0,   66,   67,   81,   82,   83,   84,    0,    0,
    0,    0,    0,    0,   81,   82,   83,   84,   81,   82,
   83,   84,    0,    4,    0,    0,    5,    0,    0,   80,
   66,   67,   81,   82,   83,   84,  130,    0,    0,    0,
    0,    0,    0,    0,    4,    0,    0,    5,    0,  213,
    0,   66,   67,   81,   82,   83,   84,    4,    0,    0,
    5,   65,    0,    0,   66,   67,   81,   82,   83,   84,
    4,   94,    0,    5,    0,    0,   47,   66,   67,   81,
   82,   83,   84,   64,  124,   76,  126,    0,    0,    0,
    0,    0,    0,   64,    0,    0,    0,    0,    0,    0,
    0,  145,  147,    0,   94,    0,  106,    0,   47,    0,
    0,   47,    0,    0,    0,    0,    0,    0,    0,    0,
  158,  159,    0,    0,    0,    0,   64,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  184,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   47,    0,  106,
  205,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  197,  198,  200,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  215,    0,    0,
    0,    0,    0,    0,   64,   64,   64,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   64,
    0,    0,    0,    0,    0,  251,    0,  253,    0,   47,
  258,    0,   47,    0,    0,    0,    0,    0,    0,  249,
  263,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   64,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  288,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   47,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         22,
   41,    1,   25,   26,   40,   40,   40,    7,   40,   59,
   59,   89,   40,   59,   14,   40,   59,   17,   59,   41,
   44,   59,   59,   41,   41,   41,   26,   40,   68,   44,
   40,   40,   44,   41,  271,   59,   41,   59,   61,   39,
   63,   59,  138,  139,   41,   68,   46,   70,   58,   41,
    7,   59,   41,   40,  132,   41,   56,   40,  140,  141,
   41,   61,   59,   43,  265,   45,  256,   40,   68,   92,
  169,  256,   43,   59,   45,  174,  261,   98,  279,   59,
  101,   40,   45,  266,  264,  258,  271,  267,   59,   46,
  256,   40,  107,  105,   44,   43,  269,   45,   98,   99,
  266,  101,  256,   44,   44,   44,  256,   40,   42,   59,
   40,   59,  266,   47,   40,  155,  266,   40,   59,   59,
   59,   40,  264,   41,  223,  267,  225,  150,  151,  271,
   12,   40,  155,   15,  257,  261,  259,  271,  264,  160,
   45,  267,   41,   41,   44,  271,  169,   40,  272,  172,
   40,  151,  265,  231,   41,  155,   61,   40,  170,   59,
  160,   41,   41,   43,  187,   45,   40,  265,  267,  169,
   41,   40,  172,  279,   40,  274,   40,  257,   43,  259,
   45,   40,  260,   41,   40,   43,   40,   45,   41,   41,
   43,   43,   45,   45,   41,   41,   43,   43,   45,   45,
   40,  271,   41,   41,  282,   40,  305,  256,   59,   41,
   40,  257,   40,  259,   59,   59,  259,   40,  256,  256,
   45,  271,   59,   41,  260,  261,  258,  263,  264,  265,
  264,  267,  256,  267,  269,  271,  264,  271,  261,  267,
  281,  282,  278,  271,  280,  323,  271,  283,  271,  266,
  273,  260,  261,  257,  263,  264,  265,  270,  267,  281,
  282,  266,  271,  281,  282,  281,  282,  256,  277,  292,
  257,  280,  259,  260,  283,  256,  263,  260,  265,  271,
  263,   41,  265,   40,  271,  256,  269,  260,  271,   41,
  263,  278,  292,  280,   41,  278,  283,  280,  271,  258,
  283,  260,   45,   59,  263,  328,  265,  280,  271,  272,
  283,  260,  271,  262,  263,   40,  256,  256,   61,  278,
   45,  280,  271,  256,  283,   59,   45,  260,  277,  278,
  263,  280,  265,   41,  283,   41,  259,  260,  271,   45,
  263,  260,  265,  262,  263,  278,  264,  280,  271,  267,
  283,  260,  271,  271,  263,  278,  256,  280,  277,  278,
  283,  280,  271,   59,  283,  266,  271,  272,  277,  278,
  260,  280,  262,  263,  283,  266,   49,  260,  283,   59,
  263,  271,  265,  270,  271,  272,   40,  277,  271,   45,
  280,  260,   59,  283,  263,  278,  260,  280,  262,  263,
  283,  260,  271,  262,  263,  266,  260,  271,  262,  263,
  279,  280,  271,  277,  283,   59,  280,  271,  277,  283,
   40,  280,   41,  277,  283,  260,  280,  262,  263,  283,
  260,  256,  260,  263,  107,  263,  271,  265,   60,   61,
   62,  271,  277,  271,  266,  280,  271,  272,  283,  279,
  280,   40,  280,  283,   41,  283,   41,   42,   43,   40,
   45,   40,   47,   59,   60,   61,   62,   41,   59,   43,
   41,   45,  259,   59,   59,   60,   61,   62,   41,   59,
   43,  259,   45,  259,  259,   59,   60,   61,   62,   41,
   41,   43,  266,   45,  167,  266,   59,   60,   61,   62,
  266,   40,   41,   40,  266,  256,   45,   59,   60,   61,
   62,   59,  264,  256,   43,  267,   45,   59,   43,   59,
   45,   60,   61,   62,   59,   59,   59,   41,  271,  272,
   59,   60,   61,   62,   45,   60,   61,   62,   59,  264,
  283,   41,  267,   40,  266,  264,  271,  272,  267,   60,
   61,   62,  271,  272,   59,   45,  266,   59,  264,   41,
  233,  267,  266,  266,   59,  271,  272,  266,   45,  266,
   60,   61,   62,   41,   20,   41,   14,   46,   24,   -1,
   -1,   45,   -1,   60,   61,   62,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   60,   61,   62,   -1,
  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  264,   -1,
   -1,  267,   -1,   -1,   -1,  271,  272,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  273,  274,  275,  276,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  108,  109,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,   -1,   -1,   -1,   -1,   -1,  273,  274,  275,
  276,  266,  256,   -1,   -1,   -1,   -1,   -1,  273,  274,
  275,  276,  266,  256,   -1,   -1,  281,  282,   -1,  273,
  274,  275,  276,  266,  256,   -1,   -1,  281,  282,   -1,
  273,  274,  275,  276,  266,   -1,   -1,   -1,  281,  282,
   -1,  273,  274,  275,  276,  264,  172,  256,  267,  281,
  282,   -1,  271,  272,  273,  274,  275,  276,   -1,   -1,
   -1,   -1,   -1,   -1,  273,  274,  275,  276,  273,  274,
  275,  276,   -1,  264,   -1,   -1,  267,   -1,   -1,  270,
  271,  272,  273,  274,  275,  276,  256,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  264,   -1,   -1,  267,   -1,  256,
   -1,  271,  272,  273,  274,  275,  276,  264,   -1,   -1,
  267,   21,   -1,   -1,  271,  272,  273,  274,  275,  276,
  264,   31,   -1,  267,   -1,   -1,   14,  271,  272,  273,
  274,  275,  276,   21,   60,   23,   62,   -1,   -1,   -1,
   -1,   -1,   -1,   31,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   77,   78,   -1,   64,   -1,   44,   -1,   46,   -1,
   -1,   49,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   96,   97,   -1,   -1,   -1,   -1,   64,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  125,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  105,   -1,  107,
  146,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  142,  143,  144,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  157,   -1,   -1,
   -1,   -1,   -1,   -1,  142,  143,  144,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  157,
   -1,   -1,   -1,   -1,   -1,  201,   -1,  203,   -1,  167,
  206,   -1,  170,   -1,   -1,   -1,   -1,   -1,   -1,  199,
  216,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  199,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  255,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  233,
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

//#line 587 "gramatica.y"


public int yylex() {
    int value = AnalizadorLexico.yylex();
    yylval = new ParserVal(AnalizadorLexico.refTDS); 
    return value;
}

public void yyerror(String string) {
	//AnalizadorSintactico.agregarError("Parser token error: " + string);
	//System.out.println("token error en linea  "+ AnalizadorLexico.numLinea + ": " +string);
}
//#line 684 "Parser.java"
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
                       else if ((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", null, (Nodo)val_peek(1).obj));}
                }
break;
case 27:
//#line 100 "gramatica.y"
{
                    if(val_peek(0).obj == null){
                       yyval= new ParserVal(new Nodo("S",null, null));
                    }else{
                        yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
                    }
                  }
break;
case 28:
//#line 109 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 29:
//#line 110 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 30:
//#line 113 "gramatica.y"
{

            AnalizadorSintactico.agregarAnalisis("sent contract (Linea " + AnalizadorLexico.numLinea + ")");
            if(val_peek(1).obj == null)
                break;
            yyval =  new ParserVal(new Nodo("CONTRACT",(Nodo)val_peek(0).obj,null));
            }
break;
case 31:
//#line 120 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 32:
//#line 121 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 33:
//#line 122 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 34:
//#line 123 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 35:
//#line 124 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 36:
//#line 130 "gramatica.y"
{ if ((val_peek(0).obj != null) && (val_peek(1).obj != null))
                     {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, (Nodo)val_peek(0).obj));}
                  else if((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));}
                       else if ((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", null, (Nodo)val_peek(0).obj));}
                }
break;
case 37:
//#line 136 "gramatica.y"
{
                    if(val_peek(0).obj == null){
                       yyval= new ParserVal(new Nodo("S",null, null));
                    }else{
                       yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
                    }
                }
break;
case 39:
//#line 147 "gramatica.y"
{yyval=val_peek(0);}
break;
case 41:
//#line 151 "gramatica.y"
{
                    AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 42:
//#line 154 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 43:
//#line 155 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 44:
//#line 156 "gramatica.y"
{
	       	        AnalizadorLexico.tablaDeSimbolos.remove(val_peek(1).sval);
           	        AnalizadorLexico.listaDeErrores.add("Tipo de variable debe ser en mayuscula (Linea " + (AnalizadorLexico.numLinea-1) + ")");
           	      }
break;
case 45:
//#line 160 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 46:
//#line 163 "gramatica.y"
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
case 47:
//#line 172 "gramatica.y"
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
//#line 181 "gramatica.y"
{AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 49:
//#line 185 "gramatica.y"
{
        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
        yyval=new ParserVal (new Nodo(val_peek(2).sval, (Nodo)val_peek(0).obj, null));
        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
        }
break;
case 50:
//#line 190 "gramatica.y"
{
	        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
	        yyval=new ParserVal (new Nodo(val_peek(1).sval, (Nodo)val_peek(0).obj, null));
	        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
	   }
break;
case 51:
//#line 197 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 52:
//#line 199 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 53:
//#line 200 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 54:
//#line 203 "gramatica.y"
{
            AnalizadorSintactico.tipoActual = val_peek(4).sval;
           }
break;
case 55:
//#line 206 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 56:
//#line 207 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 57:
//#line 208 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 58:
//#line 209 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 59:
//#line 213 "gramatica.y"
{yyval=val_peek(0);}
break;
case 61:
//#line 219 "gramatica.y"
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
case 62:
//#line 229 "gramatica.y"
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
case 63:
//#line 239 "gramatica.y"
{
	                 yyval=val_peek(0);
	                 }
break;
case 64:
//#line 248 "gramatica.y"
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
case 65:
//#line 258 "gramatica.y"
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
case 66:
//#line 269 "gramatica.y"
{
	                        yyval = val_peek(0);
	                        }
break;
case 67:
//#line 278 "gramatica.y"
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
case 68:
//#line 293 "gramatica.y"
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
case 69:
//#line 309 "gramatica.y"
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
case 70:
//#line 333 "gramatica.y"
{yyval=val_peek(0);}
break;
case 71:
//#line 337 "gramatica.y"
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
case 72:
//#line 357 "gramatica.y"
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
case 73:
//#line 374 "gramatica.y"
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
case 74:
//#line 387 "gramatica.y"
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
case 75:
//#line 398 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 76:
//#line 399 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 77:
//#line 400 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 78:
//#line 401 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 79:
//#line 405 "gramatica.y"
{
                    Object[] obj = new Object[2] ;
                    obj[0] = val_peek(0).sval;
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                    aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    obj[1] = aux;
                    yyval= new ParserVal(obj);
		     }
break;
case 80:
//#line 413 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 81:
//#line 418 "gramatica.y"
{yyval=val_peek(0);}
break;
case 82:
//#line 419 "gramatica.y"
{yyval=val_peek(0);}
break;
case 83:
//#line 420 "gramatica.y"
{yyval=val_peek(0);}
break;
case 84:
//#line 421 "gramatica.y"
{yyval=val_peek(0);}
break;
case 85:
//#line 424 "gramatica.y"
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
case 86:
//#line 446 "gramatica.y"
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
case 87:
//#line 469 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 88:
//#line 470 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 89:
//#line 471 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 90:
//#line 472 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 91:
//#line 473 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 92:
//#line 474 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' y falta :  (Linea " + (AnalizadorLexico.numLinea -1) + ")");}
break;
case 93:
//#line 475 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' y falta :  (Linea " + (AnalizadorLexico.numLinea -1) + ")");}
break;
case 94:
//#line 476 "gramatica.y"
{AnalizadorSintactico.agregarError("Error, no puede asignarse un comparador(Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 95:
//#line 480 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if(val_peek(6).obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 96:
//#line 488 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");
	                                                    if(val_peek(4).obj == null)
                                                           break;
                                                        ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(2).obj, null));
                                                        ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,null));
                                                        yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(4).obj, (Nodo)auxCuerpo.obj));}
break;
case 97:
//#line 494 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 98:
//#line 495 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 99:
//#line 496 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 100:
//#line 497 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 498 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 102:
//#line 499 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 103:
//#line 500 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 104:
//#line 501 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 105:
//#line 502 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 106:
//#line 503 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 107:
//#line 504 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 108:
//#line 505 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 109:
//#line 508 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 110:
//#line 511 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 111:
//#line 512 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 112:
//#line 513 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 113:
//#line 514 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 114:
//#line 515 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 117:
//#line 520 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		 if(val_peek(2).obj == null)
             break;
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 118:
//#line 524 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 119:
//#line 525 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 120:
//#line 526 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 121:
//#line 527 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 122:
//#line 531 "gramatica.y"
{yyval=val_peek(1);}
break;
case 124:
//#line 535 "gramatica.y"
{
	     if(val_peek(0).obj == null)
             break;
	     yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(0).obj, null));}
break;
case 125:
//#line 539 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 126:
//#line 543 "gramatica.y"
{
		  if(val_peek(2).obj == null || val_peek(0).obj == null){
             break;
          }else{
		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
		  }
		}
break;
case 130:
//#line 553 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 131:
//#line 554 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 132:
//#line 557 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 133:
//#line 558 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 134:
//#line 559 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 135:
//#line 560 "gramatica.y"
{yyval = new ParserVal("<>");}
break;
case 136:
//#line 561 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 137:
//#line 562 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 138:
//#line 563 "gramatica.y"
{
	           AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea +": se esperaba comparacion ==.");
	           yyval = new ParserVal("==");
	        }
break;
case 139:
//#line 569 "gramatica.y"
{yyval= new ParserVal("&&");}
break;
case 140:
//#line 570 "gramatica.y"
{yyval= new ParserVal("||");}
break;
case 141:
//#line 574 "gramatica.y"
{AnalizadorSintactico.tipoActual = "LONG";
             yyval = new ParserVal("LONG");
            }
break;
case 142:
//#line 577 "gramatica.y"
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
