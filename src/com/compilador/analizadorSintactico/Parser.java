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
    4,    3,    6,    5,    5,    5,    5,    6,    2,    1,
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
    0,  112,  121,  117,    0,   72,    0,   90,   85,   94,
   92,   86,    0,    0,   64,   65,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  110,    0,  130,
    0,    0,  125,  123,    0,    0,   74,    0,   77,   58,
   54,   76,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   30,    0,  114,
  109,  115,  116,   71,    0,    8,    9,    0,   10,    0,
  104,    0,  106,    0,    0,  107,    0,  105,    0,    0,
    7,    0,   78,   73,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   31,   33,    0,   34,   32,    0,
    0,   11,    6,    0,    0,    0,    0,  108,   96,    0,
    0,    0,    0,    0,    0,   24,   25,    0,   23,    0,
   22,    0,    0,    0,   35,   21,   87,   98,  100,  101,
    0,  102,   99,    0,    0,  128,   97,    0,    0,    0,
   60,    0,    0,    0,  103,   95,    0,   12,    0,   19,
    0,    0,    0,   15,   13,    0,  129,   14,
};
final static short yydgoto[] = {                          2,
    7,   27,    8,   29,   30,   31,   32,   48,  226,   49,
   50,   51,   52,   33,    9,   10,   11,  227,   13,   14,
   15,  228,   72,   73,   74,  117,   34,   35,   36,   37,
   90,   91,  155,   92,
};
final static short yysindex[] = {                      -235,
 -141,    0,  -34,    0,    0, -197,  -39,    0,    0,    0,
    0, -188,  -15,  -18,   -4,   31, -234,    0,  -35,   10,
  183,  262,  159,  -27,   37,  494,    0,    0,    0,    0,
  169, -174, -180,    0,    0,    0,    0,    0,  -31,   73,
   65,    0, -152,   99,  -26,  -18,    0,    0,  103,    0,
    0,    0,    0,    0,   72, -123,  101,  113,  122,  124,
  533,   11,   60,  183, -160,  119,    0,  507, -106,   32,
   48,   68,    0,    0,  -78, -205,  124,   35,   52,  150,
    0,    0,    0,    0,    0,    0,    0,  152,  481,    0,
   15,   37,  -72,    0,    0,  124,  124,  286,   23,  155,
 -123,    0,    0,    0,  128,  -84,   39,  157,  158,    0,
  160,    0,    0,    0,    0,  -69,  162,    0,  146,  -17,
  165,  171,  156,    0,   64,    0,    0,    0,  -20,    0,
  173,  470,  175,    0,    0,   58,    0,   37,   37,   37,
   37,  183,  183,  190, -132,   -5,  -82,    0,  204,   37,
  352,    0,    0,    0,  520,  143,  183,  -38,    0,   -3,
  176,    0,  -29,    0,  177,  232,  138,  100,  494,  154,
  236,  461,   13,  358,    0,    0,    0,  -42,    0,  266,
  271,    0,    0,    0,  295,    0,   37,    0,    0,    0,
    0,    0,   68,   68,    0,    0,   84,   85,  183,   87,
  124,  301,  124,  309,  -49,  124,  310,    0,  112,    0,
  333,  143,    0,    0,  109,  124,    0,   25,    0,    0,
    0,    0,  358,  338,  340,  355,  366,  143,  369,  372,
  481,  373,  164,  361,  364,   17,  374,    0,  387,    0,
    0,    0,    0,    0,  117,    0,    0, -158,    0,  186,
    0,  192,    0,   74,  -40,    0,  205,    0,  540,   37,
    0,  206,    0,    0,  395,  358,  202,   45,  213,   37,
  215,   37,  358,  432,    0,    0,  227,    0,    0,  218,
  476,    0,    0,  431,  433,  435,  -48,    0,    0,  436,
  358,  126,  441,  444,  463,    0,    0,  446,    0,  131,
    0,  139,  471,  358,    0,    0,    0,    0,    0,    0,
  -36,    0,    0,  474,  143,    0,    0,  245,  457,  253,
    0,  540,  466,  479,    0,    0,   37,    0,  261,    0,
  274,  469,  140,    0,    0,  278,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    8,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    8,    0,    0,    0,    0,    0,    0,    0,    0,
  280,    0,    0,    0,    0,    0,    0,    0,    0,   90,
    0,    0,   92,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -144,    0,  415,    0,    0,    0,    0,
    0,  426,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   78,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -25,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  437,  448,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    2,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   46,    0,    0,
  506,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    4,    0,    0,    0,    0,    0,
    0,  508,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  534,  759,   19,  -19,    0,  789,  541,    0,  390,  -24,
  505,  410,    0,   29,    0,    0,    0,    1,  120,    0,
    0,  -22,  -54,   49,    0,  -70,    0,    0,    0,    0,
  -61,    0,    0,  -52,
};
final static int YYTABLESIZE=1022;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         71,
   26,   12,   79,   89,   59,   17,  133,   12,   99,  256,
  312,  221,   61,  108,   12,  131,  241,   57,  289,  107,
  186,   26,  326,  178,  105,   28,   88,  161,   43,    4,
  165,  109,    5,  131,   26,    1,  151,  217,   89,  100,
   71,  179,  126,   42,  127,  132,   12,  136,   62,   61,
   26,   47,   78,  237,   54,  154,  116,  277,   18,  143,
  126,   88,  127,  162,   28,  264,   47,   38,  131,  156,
   56,  238,   39,  144,   26,  278,   69,   96,  169,  151,
  167,   69,   40,  193,  194,  298,   59,  170,   97,  218,
  138,   95,  139,  214,  138,  127,  139,  282,  116,  163,
  138,  116,  139,   26,   69,  128,  137,  283,   43,  140,
  148,   37,  101,   26,  141,   43,  192,   28,  104,    3,
   70,   37,    4,  103,  201,    5,  202,  209,  212,    6,
  114,   41,   89,   47,   55,   48,  171,  173,   26,  225,
    4,  118,   26,    5,   69,  233,  231,  115,   47,   89,
   48,  211,  259,  119,  138,   88,  139,  281,  129,  138,
  116,  139,  123,   26,  245,  134,  316,   26,  138,  230,
  139,  321,   88,  138,  206,  139,  207,   26,  151,  322,
  337,  138,  138,  139,  139,  138,  142,  139,  195,  196,
  149,  150,  157,   26,   93,  164,   61,  172,   26,  174,
  236,  175,  176,   26,  177,  180,  291,  254,   26,  255,
  311,  181,  187,  240,  182,  288,  219,  222,  216,  325,
   19,    3,   26,   20,    4,   21,  220,    5,  291,   26,
   77,   22,    4,  190,   58,    5,   16,  292,   23,   98,
   24,   19,    3,   25,   20,    4,   44,  300,    5,  302,
  185,  203,   22,  204,   19,  131,  131,   20,   45,   21,
    4,   24,  208,    5,   25,   63,   53,  115,  315,  291,
   19,  223,   23,   20,   24,   21,  234,   25,   60,  125,
  263,   63,  126,  126,  127,  127,    4,  135,   23,    5,
   24,  314,  146,   25,   19,  152,  153,   20,   19,   21,
  168,   20,   66,   67,  333,   63,   69,   66,   67,   63,
  297,   59,   23,  191,   24,   45,   23,   25,   24,  183,
  102,   25,   70,   19,  242,  160,   20,  113,   21,  243,
   66,   67,  286,   19,   63,  244,   20,   28,   21,   28,
   28,   23,   68,   24,   63,   47,   25,   48,   28,  246,
  247,   23,  249,   24,   28,   28,   25,   28,   19,  251,
   28,   20,   19,    4,  111,   20,    5,  253,  258,   63,
   66,   67,  260,   63,  261,   45,   23,  266,   24,   45,
  267,   25,   24,   19,   69,   25,   20,   19,   21,  166,
   20,  120,  121,  122,   63,  269,   69,   19,   63,  224,
   20,   23,   69,   24,   45,  270,   25,   24,   63,  271,
   25,  272,  273,   19,   45,  232,   20,   24,   19,  275,
   25,   20,  276,   19,   63,  274,   20,  280,   19,   63,
   45,   20,  279,   24,   63,  294,   25,   75,   24,   63,
   45,   25,   19,   24,  284,   20,   25,   93,   24,   19,
  285,   25,   20,   63,  199,   67,   67,   67,  112,   67,
   63,   67,   24,  290,  293,   25,   63,  296,   63,   24,
   63,  304,   25,   67,   67,   67,   67,   61,  299,   61,
  301,   61,  305,  306,   63,   63,   63,   63,   62,  308,
   62,  309,   62,  310,  313,   61,   61,   61,   61,  317,
   61,  235,  318,  319,  320,   69,   62,   62,   62,   62,
  328,  323,  138,  327,  139,  329,  112,   18,  330,  332,
   87,   85,   86,  138,  331,  139,  334,  336,  189,   87,
   85,   86,   66,   67,  307,   87,   85,   86,   69,  335,
   87,   85,   86,  338,   68,   37,   59,   46,   60,    4,
  110,   69,    5,   87,   85,   86,  115,    0,  229,    0,
    0,   65,    0,  239,   69,    0,   87,   85,   86,    0,
    0,   94,    0,    0,    0,    0,  112,   69,    0,   87,
   85,   86,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   87,   85,   86,    0,    0,    0,    0,   87,
   85,   86,    0,    4,   94,    0,    5,  210,    0,    0,
   66,   67,  265,    0,  268,    4,    0,    0,    5,    0,
    0,    4,   66,   67,    5,    0,    0,    0,   66,   67,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  112,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  295,    0,    0,    0,    0,
    0,    0,  303,    0,    0,    0,    0,    0,    0,    0,
   67,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   67,   63,  197,  198,  200,    0,    0,   67,   67,   67,
   67,   63,   61,  324,    0,   67,   67,  215,   63,   63,
   63,   63,   61,   62,    0,    0,   63,   63,    0,   61,
   61,   61,   61,   62,    0,    0,    0,   61,   61,    0,
   62,   62,   62,   62,    4,  188,    0,    5,   62,   62,
    0,   66,   67,   81,   82,   83,   84,    0,    0,  248,
    0,    0,   81,   82,   83,   84,    0,    0,   81,   82,
   83,   84,    0,   81,   82,   83,   84,    4,    0,    0,
    5,    0,  130,   80,   66,   67,   81,   82,   83,   84,
    4,    0,    0,    5,    0,  213,    0,   66,   67,   81,
   82,   83,   84,    4,    0,    0,    5,    0,    0,    0,
   66,   67,   81,   82,   83,   84,    4,    0,    0,    5,
    0,    0,   47,   66,   67,   81,   82,   83,   84,   64,
    0,   76,   81,   82,   83,   84,    0,    0,  124,   64,
  126,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  106,    0,   47,  145,  147,   47,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   64,    0,  158,  159,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  184,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   47,    0,  106,    0,    0,    0,    0,
    0,    0,    0,    0,  205,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   64,   64,   64,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   64,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   47,    0,    0,   47,  250,
    0,  252,    0,    0,  257,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  262,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   64,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  287,    0,    0,    0,    0,    0,    0,    0,
    0,   47,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         22,
   40,    1,   25,   26,   40,   40,   68,    7,   40,   59,
   59,   41,   40,   40,   14,   41,   59,   17,   59,   44,
   41,   40,   59,   41,   44,    7,   26,   98,   44,  264,
  101,   58,  267,   59,   40,  271,   89,   41,   61,   39,
   63,   59,   41,   59,   41,   68,   46,   70,   20,   40,
   40,   44,   24,   41,   59,   41,   56,   41,  256,  265,
   59,   61,   59,   41,   46,   41,   59,  256,   68,   92,
   40,   59,  261,  279,   40,   59,   45,  258,   40,  132,
  105,   45,  271,  138,  139,   41,   41,  107,  269,  160,
   43,  266,   45,  155,   43,  256,   45,  256,   98,   99,
   43,  101,   45,   40,   45,  266,   59,  266,   44,   42,
   59,  256,   40,   40,   47,   44,   59,   40,  271,  261,
   61,  266,  264,   59,  257,  267,  259,  150,  151,  271,
   59,   12,  155,   44,   15,   44,  108,  109,   40,   40,
  264,   41,   40,  267,   45,  170,  169,  271,   59,  172,
   59,  151,   41,   41,   43,  155,   45,   41,   40,   43,
  160,   45,   41,   40,  187,  272,   41,   40,   43,  169,
   45,   41,  172,   43,  257,   45,  259,   40,  231,   41,
   41,   43,   43,   45,   45,   43,  265,   45,  140,  141,
   41,   40,  265,   40,  279,   41,   40,   40,   40,   40,
  172,  271,   41,   40,   59,   41,  259,  257,   40,  259,
  259,   41,   40,  256,   59,  256,   41,   41,  257,  256,
  260,  261,   40,  263,  264,  265,  256,  267,  281,   40,
  258,  271,  264,   59,  270,  267,  271,  260,  278,  271,
  280,  260,  261,  283,  263,  264,  265,  270,  267,  272,
  271,  257,  271,  259,  260,  281,  282,  263,  277,  265,
  264,  280,   59,  267,  283,  271,  271,  271,  291,  322,
  260,   40,  278,  263,  280,  265,   41,  283,  269,  269,
  256,  271,  281,  282,  281,  282,  264,  256,  278,  267,
  280,  291,  258,  283,  260,  281,  282,  263,  260,  265,
  262,  263,  271,  272,  327,  271,   45,  271,  272,  271,
  266,  266,  278,  256,  280,  277,  278,  283,  280,  256,
  256,  283,   61,  260,   59,   40,  263,  256,  265,   59,
  271,  272,  259,  260,  271,   41,  263,  260,  265,  262,
  263,  278,  283,  280,  271,  256,  283,  256,  271,  266,
  266,  278,  266,  280,  277,  278,  283,  280,  260,   59,
  283,  263,  260,  264,  262,  263,  267,   59,   59,  271,
  271,  272,   40,  271,  266,  277,  278,   40,  280,  277,
   41,  283,  280,  260,   45,  283,  263,  260,  265,  262,
  263,  270,  271,  272,  271,   41,   45,  260,  271,  262,
  263,  278,   45,  280,  277,   40,  283,  280,  271,   41,
  283,   40,   40,  260,  277,  262,  263,  280,  260,   59,
  283,  263,   59,  260,  271,  262,  263,   41,  260,  271,
  277,  263,   59,  280,  271,   41,  283,  279,  280,  271,
  277,  283,  260,  280,  259,  263,  283,  279,  280,  260,
  259,  283,  263,  271,  265,   41,   42,   43,   49,   45,
  271,   47,  280,  259,  259,  283,   41,  266,   43,  280,
   45,   40,  283,   59,   60,   61,   62,   41,  266,   43,
  266,   45,  256,  266,   59,   60,   61,   62,   41,   59,
   43,   59,   45,   59,   59,   59,   60,   61,   62,   59,
   40,   41,   59,   41,   59,   45,   59,   60,   61,   62,
  266,   41,   43,   40,   45,   59,  107,  256,  266,   41,
   60,   61,   62,   43,   59,   45,  266,   59,   59,   60,
   61,   62,  271,  272,   59,   60,   61,   62,   45,  266,
   60,   61,   62,  266,  283,  266,   41,   14,   41,  264,
   46,   45,  267,   60,   61,   62,  271,   -1,  169,   -1,
   -1,   21,   -1,  174,   45,   -1,   60,   61,   62,   -1,
   -1,   31,   -1,   -1,   -1,   -1,  167,   45,   -1,   60,
   61,   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   60,   61,   62,   -1,   -1,   -1,   -1,   60,
   61,   62,   -1,  264,   64,   -1,  267,  256,   -1,   -1,
  271,  272,  223,   -1,  225,  264,   -1,   -1,  267,   -1,
   -1,  264,  271,  272,  267,   -1,   -1,   -1,  271,  272,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  233,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  266,   -1,   -1,   -1,   -1,
   -1,   -1,  273,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  266,  256,  142,  143,  144,   -1,   -1,  273,  274,  275,
  276,  266,  256,  304,   -1,  281,  282,  157,  273,  274,
  275,  276,  266,  256,   -1,   -1,  281,  282,   -1,  273,
  274,  275,  276,  266,   -1,   -1,   -1,  281,  282,   -1,
  273,  274,  275,  276,  264,  256,   -1,  267,  281,  282,
   -1,  271,  272,  273,  274,  275,  276,   -1,   -1,  199,
   -1,   -1,  273,  274,  275,  276,   -1,   -1,  273,  274,
  275,  276,   -1,  273,  274,  275,  276,  264,   -1,   -1,
  267,   -1,  256,  270,  271,  272,  273,  274,  275,  276,
  264,   -1,   -1,  267,   -1,  256,   -1,  271,  272,  273,
  274,  275,  276,  264,   -1,   -1,  267,   -1,   -1,   -1,
  271,  272,  273,  274,  275,  276,  264,   -1,   -1,  267,
   -1,   -1,   14,  271,  272,  273,  274,  275,  276,   21,
   -1,   23,  273,  274,  275,  276,   -1,   -1,   60,   31,
   62,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   44,   -1,   46,   77,   78,   49,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   64,   -1,   96,   97,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  105,   -1,  107,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  146,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  142,  143,  144,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  157,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  167,   -1,   -1,  170,  201,
   -1,  203,   -1,   -1,  206,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  216,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  199,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  254,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  233,
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

//#line 591 "gramatica.y"


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
		    System.out.println(aux.sval);
		    yyval= new ParserVal(new Nodo("LF",(Nodo)aux.obj, (Nodo)aux2.obj ));
		    ((Nodo)yyval.obj).setTipo(value.getTipoContenido());
		  }else{
             AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
             /*error*/
		  }
		  }
break;
case 72:
//#line 361 "gramatica.y"
{
	   		  String variable = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(2).sval);
       		  if(variable != null){
       		    AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
       		    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
       		    ParserVal aux= new ParserVal(val_peek(2).sval);
       		    yyval= new ParserVal(new Nodo("LF",(Nodo)aux.obj, null ));
       		    ((Nodo)yyval.obj).setTipo(value.getTipoContenido());
       		  }else{
                    AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
                    /*error*/
       		  }
	   }
break;
case 73:
//#line 378 "gramatica.y"
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
//#line 391 "gramatica.y"
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
//#line 402 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 76:
//#line 403 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 77:
//#line 404 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 78:
//#line 405 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 79:
//#line 409 "gramatica.y"
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
//#line 417 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 81:
//#line 422 "gramatica.y"
{yyval=val_peek(0);}
break;
case 82:
//#line 423 "gramatica.y"
{yyval=val_peek(0);}
break;
case 83:
//#line 424 "gramatica.y"
{yyval=val_peek(0);}
break;
case 84:
//#line 425 "gramatica.y"
{yyval=val_peek(0);}
break;
case 85:
//#line 428 "gramatica.y"
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
//#line 450 "gramatica.y"
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
//#line 473 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 88:
//#line 474 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 89:
//#line 475 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 90:
//#line 476 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 91:
//#line 477 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 92:
//#line 478 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' y falta :  (Linea " + (AnalizadorLexico.numLinea -1) + ")");}
break;
case 93:
//#line 479 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' y falta :  (Linea " + (AnalizadorLexico.numLinea -1) + ")");}
break;
case 94:
//#line 480 "gramatica.y"
{AnalizadorSintactico.agregarError("Error, no puede asignarse un comparador(Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 95:
//#line 484 "gramatica.y"
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
//#line 492 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");
	                                                    if(val_peek(4).obj == null)
                                                           break;
                                                        ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(2).obj, null));
                                                        ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,null));
                                                        yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(4).obj, (Nodo)auxCuerpo.obj));}
break;
case 97:
//#line 498 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 98:
//#line 499 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 99:
//#line 500 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 100:
//#line 501 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 502 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 102:
//#line 503 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 103:
//#line 504 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 104:
//#line 505 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 105:
//#line 506 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 106:
//#line 507 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 107:
//#line 508 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 108:
//#line 509 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 109:
//#line 512 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 110:
//#line 515 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 111:
//#line 516 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 112:
//#line 517 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 113:
//#line 518 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 114:
//#line 519 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 117:
//#line 524 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		 if(val_peek(2).obj == null)
             break;
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 118:
//#line 528 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 119:
//#line 529 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 120:
//#line 530 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 121:
//#line 531 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 122:
//#line 535 "gramatica.y"
{yyval=val_peek(1);}
break;
case 124:
//#line 539 "gramatica.y"
{
	     if(val_peek(0).obj == null)
             break;
	     yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(0).obj, null));}
break;
case 125:
//#line 543 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 126:
//#line 547 "gramatica.y"
{
		  if(val_peek(2).obj == null || val_peek(0).obj == null){
             break;
          }else{
		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
		  }
		}
break;
case 130:
//#line 557 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 131:
//#line 558 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 132:
//#line 561 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 133:
//#line 562 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 134:
//#line 563 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 135:
//#line 564 "gramatica.y"
{yyval = new ParserVal("<>");}
break;
case 136:
//#line 565 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 137:
//#line 566 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 138:
//#line 567 "gramatica.y"
{
	           AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea +": se esperaba comparacion ==.");
	           yyval = new ParserVal("==");
	        }
break;
case 139:
//#line 573 "gramatica.y"
{yyval= new ParserVal("&&");}
break;
case 140:
//#line 574 "gramatica.y"
{yyval= new ParserVal("||");}
break;
case 141:
//#line 578 "gramatica.y"
{AnalizadorSintactico.tipoActual = "LONG";
             yyval = new ParserVal("LONG");
            }
break;
case 142:
//#line 581 "gramatica.y"
{AnalizadorSintactico.tipoActual = "SINGLE";
             yyval = new ParserVal("SINGLE");
             }
break;
//#line 1677 "Parser.java"
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
