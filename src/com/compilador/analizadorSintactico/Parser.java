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
    4,    5,    5,    5,    8,    8,    8,    8,    8,    8,
    9,    9,    9,   11,   11,   11,   11,   11,   11,    7,
    7,    3,    3,    3,   13,   13,   13,   14,   14,   15,
   15,   15,   19,   19,   19,   19,   19,   10,   10,   20,
   20,   20,   21,   21,   21,   22,   22,   22,   22,   23,
   23,   18,   18,   18,   18,   18,   18,   24,   24,    6,
    6,    6,    6,   25,   25,   25,   25,   25,   26,   26,
   26,   26,   26,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   27,   27,   27,   27,   27,   27,   27,   27,
   28,   28,   28,   28,   28,   12,   29,   29,   29,   31,
   31,   31,   31,   31,   31,   32,   32,   32,   32,   32,
   32,   30,   30,   16,   16,   17,   17,   17,
};
final static short yylen[] = {                            2,
    3,    2,    1,    1,    1,    6,    5,    5,    5,    5,
    6,    3,    2,    3,    8,    6,    6,    6,    6,    6,
    2,    2,    1,    6,    5,    5,    5,    5,    6,    2,
    1,    1,    1,    1,    3,    2,    3,    3,    2,    3,
    2,    3,    5,    4,    4,    4,    5,    1,    4,    3,
    3,    1,    3,    3,    1,    1,    1,    2,    1,    4,
    4,    6,    5,    5,    5,    5,    6,    2,    1,    1,
    1,    1,    1,    4,    7,    3,    3,    4,    8,    6,
    7,    7,    7,    7,    7,    7,    8,    5,    5,    5,
    5,    6,    5,    4,    4,    4,    4,    5,    5,    5,
    4,    3,    3,    3,    4,    3,    3,    1,    3,    3,
    6,    6,    9,    3,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    3,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,  124,  125,  127,    0,    3,   32,   33,
   34,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    1,    2,    4,    5,    0,
    0,    0,   70,   71,   72,   73,    0,    0,    0,   36,
    0,    0,    0,   23,   39,    0,   41,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   31,    0,    0,   57,
    0,    0,    0,    0,   55,   59,    0,    0,    0,    0,
    0,    0,  118,  119,  120,  121,  116,  117,    0,    0,
    0,  108,    0,    0,   13,   30,    0,    0,    0,    0,
    0,    0,   37,   35,  126,    0,   38,    0,    0,   21,
   22,   42,   40,   69,    0,    0,   44,    0,    0,    0,
    0,    0,  103,    0,  104,   14,   12,    0,    0,    0,
   58,   77,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   76,    0,    0,    0,  122,  123,  106,    0,
    0,    0,    0,  102,    0,    0,   46,    0,   45,    0,
    0,    0,    0,    0,    0,   68,   64,   95,    0,   97,
    0,    0,   96,  105,  101,    0,    0,    0,   78,   74,
    0,    0,   53,   54,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   94,    0,  114,    0,    0,
  109,  107,    0,    0,   63,    0,   66,   47,   43,   65,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   98,   93,   99,  100,   61,   60,    0,    8,    9,
    0,   10,    0,   88,    0,   90,    0,    0,   91,    0,
   89,    0,    0,    7,    0,   67,   62,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   11,
    6,    0,    0,    0,    0,   92,   80,    0,    0,    0,
    0,   19,   20,    0,   18,    0,   17,    0,   16,   25,
   27,    0,   28,   26,   75,   82,   84,   85,    0,   86,
   83,    0,    0,  112,   81,    0,   49,    0,   29,   24,
   87,   79,    0,   15,    0,  113,
};
final static short yydgoto[] = {                          2,
    7,   26,    8,   28,   29,   30,   31,   45,   46,  202,
  101,   32,    9,   10,   11,  105,   13,   14,   15,   80,
   64,   65,   66,  106,   33,   34,   35,   36,   81,  140,
   82,   83,
};
final static short yysindex[] = {                      -247,
 -168,    0,  -39,    0,    0,    0,  -36,    0,    0,    0,
    0, -197,   12,  -15,  -37,  -12, -169,  -33,  -30,  176,
   15,  121,  -32,   52,  357,    0,    0,    0,    0, -243,
  123, -214,    0,    0,    0,    0,  103,    5,  -13,    0,
 -212,  176,  -15,    0,    0,   66,    0,   82, -184,   47,
   59,   13,   76,  468,   11,   15,    0,   95,   38,    0,
  206, -165,   27,    6,    0,    0, -148, -245,   76,   37,
   46,   83,    0,    0,    0,    0,    0,    0,   88,   30,
  -28,    0,   52, -126,    0,    0,   76,   76,  105,   -3,
   92, -184,    0,    0,    0,  102,    0,  106,   -1,    0,
    0,    0,    0,    0, -123,  125,    0,  113,    2,  132,
  135,  119,    0,   36,    0,    0,    0,  225,  143,   23,
    0,    0,   52,   52,   52,   52,  176,  176,  128, -149,
   10, -132,    0,  126,   52,  303,    0,    0,    0,  455,
   93,  176,  -71,    0,  270,  150,    0,  -35,    0,  151,
  -31,  357,  206,  148,  154,    0,    0,    0,  -47,    0,
  137,  138,    0,    0,    0,  139,  159,   52,    0,    0,
    6,    6,    0,    0,  147,  149,  176,  162,   76,  142,
   76,  144,   75,   76,  145,    0,   68,    0,  155,   93,
    0,    0,  175,   76,    0,  -26,    0,    0,    0,    0,
  268,  165,  167,   93,  170,  168,   30,  182,  221,  290,
  277,    0,    0,    0,    0,    0,    0,   77,    0,    0,
   97,    0,  -41,    0,   85,    0,   62,  -43,    0,  146,
    0,  122,   52,    0,  164,    0,    0,  158,  -11,  178,
   52,  185,   52,  198,  355,  362,    8,  367,  372,    0,
    0,  375,  407,  416,   53,    0,    0,  422,  206,   78,
  425,    0,    0,  427,    0,  117,    0,  124,    0,    0,
    0,  134,    0,    0,    0,    0,    0,    0,  160,    0,
    0,  448,   93,    0,    0,  228,    0,  122,    0,    0,
    0,    0,   52,    0,  136,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   25,    0,    0,    0,    0,    0,    0,    0,    0,  177,
    0,    0,    0,    0,    0,    0,    0,  115,    0,    0,
  131,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  420,    0,
    0,    0,    0,  409,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -24,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  431,  442,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -22,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   44,    0,    0,  454,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   -4,    0,    0,    0,    0,  456,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  484,  695,   20,    0,    0,  731,   22,  460,  457, -120,
    0,  430,    0,    0,    0,    4,   89,    0,    0,  -21,
   29,   45,    0,  -66,    0,    0,    0,    0,    0,    0,
  365,  -78,
};
final static int YYTABLESIZE=952;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         63,
   17,  136,   71,   25,   12,  199,   52,   54,  201,   54,
   12,  213,  139,   62,  237,  257,  115,   12,  110,  128,
   50,   47,  146,    1,   25,  150,   27,   49,   79,  264,
   41,  205,  208,  129,   63,   84,  111,  147,  154,  120,
   91,   58,  159,   87,   92,   94,   12,  125,  272,   25,
   25,  167,  126,  112,   88,   41,  155,   79,   95,   62,
  160,  141,   27,   37,  119,  123,  273,  124,  127,  123,
   40,  124,  123,   38,  124,   25,   25,  118,  196,    4,
  239,  170,    5,  127,   48,  122,  104,  107,  123,   78,
  124,   77,    3,  148,    4,    4,   62,    5,    5,  108,
   39,   25,    6,   48,  133,   25,  121,  179,  232,  180,
  123,  280,  124,  187,  190,   25,  127,  249,  284,  123,
  123,  124,  124,  134,  184,   41,  185,  135,  136,  204,
  207,  204,  149,  229,   25,  123,   25,  124,  142,  189,
  103,  152,   90,   79,  145,  153,  218,  156,  175,  176,
  178,  171,  172,  259,  203,  206,  203,  287,  127,  123,
   25,  124,   25,  193,  288,  157,  123,   25,  124,  173,
  174,  158,  161,  127,  128,  162,  296,  163,  123,  204,
  124,   78,  168,   77,  186,  194,   25,   54,   25,  128,
  197,  200,  290,  210,  233,  214,  215,  216,  221,  217,
  224,   25,  226,  231,  203,  240,  241,  243,  212,  259,
  242,  260,  256,   79,   25,   25,   31,  252,  292,  266,
  198,  268,  244,   18,    3,   69,   19,    4,   20,  236,
    5,   16,    4,    6,   21,    5,   51,  283,   53,   59,
   60,   22,   93,   23,   18,    3,   24,   19,    4,   42,
   62,    5,  137,  138,  263,   21,  115,  115,  110,  110,
    4,  245,  282,    5,   23,  166,  181,   24,  182,   18,
   18,  295,   19,   19,   20,   20,  111,  111,  169,  114,
   56,   56,  109,  110,  111,   59,   60,   22,   22,   23,
   23,  164,   24,   24,  131,   18,   18,   61,   19,   19,
   20,   20,   73,   74,   75,   76,   56,   56,  238,   48,
  195,  279,   62,   22,   22,   23,   23,  248,   24,   24,
  254,   18,   59,   60,   19,   18,   20,   98,   19,   54,
  246,  227,   56,  228,   62,   18,   56,  102,   19,   22,
   20,   23,   99,  253,   24,   23,   56,   62,   24,   78,
  116,   77,  250,   22,   18,   23,   18,   19,   24,   19,
  117,   18,  251,  151,   19,   56,    4,   56,    4,    5,
  127,    5,   56,   89,   23,  104,   23,   24,   99,   24,
   18,   23,   18,   19,   24,   19,  128,   18,   85,  289,
   19,   56,  177,   56,   73,   74,   75,   76,   56,   67,
   23,   62,   23,   24,  258,   24,   18,   23,   18,   19,
   24,   19,  219,  270,  220,  291,   78,   56,   77,   56,
  271,   18,  261,  262,   19,  274,   23,  222,   23,   24,
  275,   24,   56,  276,   18,   18,   31,   19,   19,   31,
  234,   23,   31,  265,   24,   56,   56,   31,   55,   52,
  267,   52,   70,   52,   23,   23,   31,   24,   24,   31,
   56,   56,   56,  269,   56,  277,   56,   52,   52,    4,
   52,   50,    5,   50,  278,   50,   59,   60,   56,   56,
  281,   56,   51,  285,   51,  286,   51,  293,    4,   50,
   50,    5,   50,  294,   48,  104,   49,   43,   96,   62,
   51,   51,   97,   51,  192,    0,    0,    0,    0,    0,
    0,    0,   62,    0,   78,    0,   77,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   78,    0,   77,
    0,    4,    0,    4,    5,    0,    5,    0,   59,   60,
  104,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    4,    0,    0,    5,    0,  188,    0,
   59,   60,   73,   74,   75,   76,    4,    0,    0,    5,
    0,    0,    0,   59,   60,    0,    0,    0,    0,    0,
    0,    0,    0,  209,  211,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    4,    0,    0,    5,    0,    0,   72,   59,   60,   73,
   74,   75,   76,    0,    0,    0,    0,    0,    0,  247,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   52,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   52,   56,    0,    0,    0,    0,
    0,   52,   52,   52,   52,   56,   50,    0,    0,   52,
   52,    0,   56,   56,   56,   56,   50,   51,    0,    0,
   56,   56,    0,   50,   50,   50,   50,   51,    0,    0,
  191,   50,   50,    0,   51,   51,   51,   51,    4,    0,
    0,    5,   51,   51,    0,   59,   60,   73,   74,   75,
   76,    4,    0,    0,    5,    0,    0,    0,   59,   60,
   73,   74,   75,   76,   44,    0,    0,  113,    0,  115,
   57,    0,   68,    0,    0,    0,    0,    0,    0,    0,
    0,   86,    0,  130,  132,    0,    0,    0,    0,    0,
    0,    0,   44,   44,    0,    0,  100,    0,    0,    0,
    0,  143,  144,    0,    0,    0,    0,    0,   86,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  165,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  183,  100,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   57,   57,   57,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   57,  223,    0,  225,    0,    0,  230,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  235,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   86,   86,   57,   86,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  255,    0,   86,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   86,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         21,
   40,   80,   24,   40,    1,   41,   40,   40,   40,   40,
    7,   59,   41,   45,   41,   59,   41,   14,   41,  265,
   17,   59,   89,  271,   40,   92,    7,   40,   25,   41,
   44,  152,  153,  279,   56,  279,   41,   41,   40,   61,
   37,   20,   41,  258,   40,   59,   43,   42,   41,   40,
   40,  118,   47,   41,  269,   44,   58,   54,  271,   45,
   59,   83,   43,  261,   61,   43,   59,   45,   44,   43,
   59,   45,   43,  271,   45,   40,   40,   40,  145,  264,
  201,   59,  267,   59,   41,   59,  271,   41,   43,   60,
   45,   62,  261,   90,  264,  264,   45,  267,  267,   41,
   12,   40,  271,   15,   59,   40,  272,  257,   41,  259,
   43,   59,   45,  135,  136,   40,  265,   41,   41,   43,
   43,   45,   45,   41,  257,   44,  259,   40,  207,  151,
  152,  153,   41,   59,   40,   43,   40,   45,  265,  136,
   59,   40,   40,  140,   40,   40,  168,  271,  127,  128,
  129,  123,  124,  232,  151,  152,  153,   41,   44,   43,
   40,   45,   40,  142,   41,   41,   43,   40,   45,  125,
  126,   59,   41,   59,   44,   41,   41,   59,   43,  201,
   45,   60,   40,   62,   59,  257,   40,   40,   40,   59,
   41,   41,   59,   40,   40,   59,   59,   59,  177,   41,
   59,   40,   59,   59,  201,   41,   40,   40,  256,  288,
   41,  233,  256,  210,   40,   40,   40,  259,   59,  241,
  256,  243,   41,  260,  261,  258,  263,  264,  265,  256,
  267,  271,  264,  271,  271,  267,  270,  259,  269,  271,
  272,  278,  256,  280,  260,  261,  283,  263,  264,  265,
   45,  267,  281,  282,  266,  271,  281,  282,  281,  282,
  264,   41,  259,  267,  280,   41,  257,  283,  259,  260,
  260,  293,  263,  263,  265,  265,  281,  282,  256,  269,
  271,  271,  270,  271,  272,  271,  272,  278,  278,  280,
  280,  256,  283,  283,  258,  260,  260,  283,  263,  263,
  265,  265,  273,  274,  275,  276,  271,  271,   41,  266,
   41,  259,   45,  278,  278,  280,  280,   41,  283,  283,
  259,  260,  271,  272,  263,  260,  265,  262,  263,   40,
   41,  257,  271,  259,   45,  260,  271,  256,  263,  278,
  265,  280,  277,  259,  283,  280,  271,   45,  283,   60,
  256,   62,  256,  278,  260,  280,  260,  263,  283,  263,
  266,  260,  266,  262,  263,  271,  264,  271,  264,  267,
  256,  267,  271,  271,  280,  271,  280,  283,  277,  283,
  260,  280,  260,  263,  283,  263,  256,  260,  266,  256,
  263,  271,  265,  271,  273,  274,  275,  276,  271,  279,
  280,   45,  280,  283,  259,  283,  260,  280,  260,  263,
  283,  263,  266,   59,  266,  256,   60,  271,   62,  271,
   59,  260,  259,  266,  263,   59,  280,  266,  280,  283,
   59,  283,  271,   59,  260,  260,  260,  263,  263,  263,
  266,  280,  266,  266,  283,  271,  271,  271,   19,   41,
  266,   43,   23,   45,  280,  280,  280,  283,  283,  283,
   41,   42,   43,  266,   45,   59,   47,   59,   60,  264,
   62,   41,  267,   43,   59,   45,  271,  272,   59,   60,
   59,   62,   41,   59,   43,   59,   45,   40,  264,   59,
   60,  267,   62,  266,   41,  271,   41,   14,   42,   45,
   59,   60,   43,   62,  140,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   45,   -1,   60,   -1,   62,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   60,   -1,   62,
   -1,  264,   -1,  264,  267,   -1,  267,   -1,  271,  272,
  271,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  264,   -1,   -1,  267,   -1,  256,   -1,
  271,  272,  273,  274,  275,  276,  264,   -1,   -1,  267,
   -1,   -1,   -1,  271,  272,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  154,  155,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  264,   -1,   -1,  267,   -1,   -1,  270,  271,  272,  273,
  274,  275,  276,   -1,   -1,   -1,   -1,   -1,   -1,  210,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  266,  256,   -1,   -1,   -1,   -1,
   -1,  273,  274,  275,  276,  266,  256,   -1,   -1,  281,
  282,   -1,  273,  274,  275,  276,  266,  256,   -1,   -1,
  281,  282,   -1,  273,  274,  275,  276,  266,   -1,   -1,
  256,  281,  282,   -1,  273,  274,  275,  276,  264,   -1,
   -1,  267,  281,  282,   -1,  271,  272,  273,  274,  275,
  276,  264,   -1,   -1,  267,   -1,   -1,   -1,  271,  272,
  273,  274,  275,  276,   14,   -1,   -1,   53,   -1,   55,
   20,   -1,   22,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   31,   -1,   69,   70,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   42,   43,   -1,   -1,   46,   -1,   -1,   -1,
   -1,   87,   88,   -1,   -1,   -1,   -1,   -1,   58,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  114,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  131,   96,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  127,  128,  129,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  142,  179,   -1,  181,   -1,   -1,  184,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  194,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  175,  176,  177,  178,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  227,   -1,  193,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  221,
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
"bloqueEjecutableNormal : BEGIN sentSoloEjecutables END",
"bloqueEjecutableNormal : sentSoloEjecutables END",
"bloqueEjecutableNormal : BEGIN sentSoloEjecutables error",
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' retorno ')' ';' END",
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

//#line 393 "gramatica.y"


public int yylex() {
    int value = AnalizadorLexico.yylex();
    yylval = new ParserVal(AnalizadorLexico.refTDS); 
    return value;
}

public void yyerror(String string) {
	AnalizadorSintactico.agregarError("Parser: " + string);
}
//#line 639 "Parser.java"
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
case 5:
//#line 48 "gramatica.y"
{yyval=val_peek(0);}
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
//#line 60 "gramatica.y"
{
			    yyval=val_peek(1);
			}
break;
case 13:
//#line 63 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 14:
//#line 64 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 15:
//#line 69 "gramatica.y"
{
                yyval = new ParserVal(new Nodo("BF",(Nodo)val_peek(6).obj,null));
            }
break;
case 16:
//#line 72 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 17:
//#line 73 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta RETURN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 18:
//#line 74 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 19:
//#line 75 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta retorno (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 20:
//#line 76 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 21:
//#line 81 "gramatica.y"
{ if ((val_peek(1).obj != null) && (val_peek(0).obj != null))
				 	{yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, (Nodo)val_peek(1).obj));}
				  else if((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, null));}
				       else if ((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));}
				}
break;
case 22:
//#line 87 "gramatica.y"
{ if ((val_peek(1).obj != null) && (val_peek(0).obj != null))
				 	{yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, (Nodo)val_peek(1).obj));}
				  else if((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, null));}
				       else if ((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));}
				}
break;
case 23:
//#line 93 "gramatica.y"
{
          		  yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));
          		}
break;
case 24:
//#line 98 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sent control (Linea " + AnalizadorLexico.numLinea + ")");
            yyval =  new ParserVal(new Nodo("CONTRACT",(Nodo)val_peek(2).obj,null));
            }
break;
case 25:
//#line 101 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 26:
//#line 102 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 27:
//#line 103 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 28:
//#line 104 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 29:
//#line 105 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 30:
//#line 111 "gramatica.y"
{ if ((val_peek(1).obj != null) && (val_peek(0).obj != null))
				 	{yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, (Nodo)val_peek(1).obj));}
				  else if((val_peek(1).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(0).obj, null));}
				       else if ((val_peek(0).obj == null)) {yyval= new ParserVal(new Nodo("S", (Nodo)val_peek(1).obj, null));}
				}
break;
case 31:
//#line 117 "gramatica.y"
{
				    yyval= new ParserVal(new Nodo("S",((Nodo)val_peek(0).obj), null));				    
				}
break;
case 33:
//#line 125 "gramatica.y"
{yyval=val_peek(0);}
break;
case 35:
//#line 129 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 36:
//#line 130 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 37:
//#line 131 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 38:
//#line 134 "gramatica.y"
{
        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
        yyval=new ParserVal (new Nodo(val_peek(2).sval, (Nodo)val_peek(0).obj, null));}
break;
case 39:
//#line 137 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 40:
//#line 140 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 41:
//#line 141 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 42:
//#line 142 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 44:
//#line 146 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 45:
//#line 147 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 46:
//#line 148 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 47:
//#line 149 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 50:
//#line 162 "gramatica.y"
{
					  yyval= new ParserVal(new Nodo("+", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                                          String tipo = AnalizadorSintactico.calculadorTipo("+",((Nodo)val_peek(2).obj).getTipo(),((Nodo)val_peek(0).obj).getTipo());
                                          ((Nodo)yyval.obj).setTipo(tipo);
                                          }
break;
case 51:
//#line 167 "gramatica.y"
{
					                  yyval= new ParserVal(new Nodo("-", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
					                  String tipo = AnalizadorSintactico.calculadorTipo("-",((Nodo)val_peek(2).obj).getTipo(),((Nodo)val_peek(0).obj).getTipo());
                                      ((Nodo)yyval.obj).setTipo(tipo);
					                  }
break;
case 52:
//#line 172 "gramatica.y"
{
	                 yyval=val_peek(0);
	                 }
break;
case 53:
//#line 181 "gramatica.y"
{
				                yyval= new ParserVal(new Nodo("*", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                                String tipo = AnalizadorSintactico.calculadorTipo("*",((Nodo)val_peek(2).obj).getTipo(),((Nodo)val_peek(0).obj).getTipo());
                                ((Nodo)yyval.obj).setTipo(tipo);
				                }
break;
case 54:
//#line 186 "gramatica.y"
{
				            yyval= new ParserVal(new Nodo("/", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));
                            String tipo = AnalizadorSintactico.calculadorTipo("/",((Nodo)val_peek(2).obj).getTipo(),((Nodo)val_peek(0).obj).getTipo());
                            ((Nodo)yyval.obj).setTipo(tipo);
				            }
break;
case 55:
//#line 191 "gramatica.y"
{
	                        yyval = val_peek(0);
	                        }
break;
case 56:
//#line 200 "gramatica.y"
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
case 57:
//#line 210 "gramatica.y"
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
case 58:
//#line 219 "gramatica.y"
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
case 59:
//#line 229 "gramatica.y"
{yyval=val_peek(0);}
break;
case 60:
//#line 233 "gramatica.y"
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
case 62:
//#line 247 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
			yyval=val_peek(3);}
break;
case 63:
//#line 249 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 64:
//#line 250 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 65:
//#line 251 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 66:
//#line 252 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 67:
//#line 253 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 68:
//#line 257 "gramatica.y"
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
case 69:
//#line 266 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 70:
//#line 271 "gramatica.y"
{yyval=val_peek(0);}
break;
case 71:
//#line 272 "gramatica.y"
{yyval=val_peek(0);}
break;
case 72:
//#line 273 "gramatica.y"
{yyval=val_peek(0);}
break;
case 73:
//#line 274 "gramatica.y"
{yyval=val_peek(0);}
break;
case 74:
//#line 277 "gramatica.y"
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
case 75:
//#line 298 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 76:
//#line 299 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 77:
//#line 300 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 78:
//#line 301 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 79:
//#line 305 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 80:
//#line 311 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 81:
//#line 312 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 82:
//#line 313 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 83:
//#line 314 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 84:
//#line 315 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 85:
//#line 316 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 86:
//#line 317 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 87:
//#line 318 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 88:
//#line 319 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 89:
//#line 320 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 90:
//#line 321 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 91:
//#line 322 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 92:
//#line 323 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 93:
//#line 326 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 94:
//#line 330 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 95:
//#line 331 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 96:
//#line 332 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 97:
//#line 333 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 98:
//#line 334 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 339 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 102:
//#line 341 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 103:
//#line 342 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 104:
//#line 343 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 105:
//#line 344 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 106:
//#line 348 "gramatica.y"
{yyval=val_peek(1);}
break;
case 108:
//#line 352 "gramatica.y"
{yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(0).obj, null));}
break;
case 109:
//#line 353 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 110:
//#line 357 "gramatica.y"
{ 
		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
		}
break;
case 114:
//#line 363 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 115:
//#line 364 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 116:
//#line 367 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 117:
//#line 368 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 118:
//#line 369 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 119:
//#line 370 "gramatica.y"
{yyval = new ParserVal("!=");}
break;
case 120:
//#line 371 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 121:
//#line 372 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 128:
//#line 387 "gramatica.y"
{AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
//#line 1349 "Parser.java"
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
