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
import com.compilador.arbolSintactico.Nodo;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;



//#line 31 "Parser.java"




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
    6,    3,    2,    3,    7,    6,    6,    6,    6,    6,
    2,    2,    1,    6,    5,    5,    5,    5,    6,    2,
    1,    1,    1,    1,    3,    2,    3,    3,    2,    3,
    2,    3,    5,    4,    4,    4,    5,    1,    4,    3,
    3,    1,    3,    3,    1,    1,    1,    2,    1,    4,
    3,    6,    5,    5,    5,    5,    6,    2,    1,    1,
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
    0,    0,   96,  105,  101,   61,    0,    0,   78,   74,
    0,    0,   53,   54,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   94,    0,  114,    0,    0,
  109,  107,    0,    0,   63,    0,   66,   47,   43,   65,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   98,   93,   99,  100,   60,    0,    8,    9,    0,
   10,    0,   88,    0,   90,    0,    0,   91,    0,   89,
    0,    0,    7,    0,   67,   62,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   11,    6,
    0,    0,    0,    0,   92,   80,    0,    0,    0,    0,
   19,   20,    0,   18,    0,   17,    0,   16,   25,   27,
    0,   28,   26,   75,   82,   84,   85,    0,   86,   83,
    0,    0,  112,   81,   15,   49,    0,   29,   24,   87,
   79,    0,    0,  113,
};
final static short yydgoto[] = {                          2,
    7,   26,    8,   28,   29,   30,   31,   45,   46,  202,
  101,   32,    9,   10,   11,  105,   13,   14,   15,   80,
   64,   65,   66,  106,   33,   34,   35,   36,   81,  140,
   82,   83,
};
final static short yysindex[] = {                      -240,
 -172,    0,  -36,    0,    0,    0,  -38,    0,    0,    0,
    0, -237,  -11,  -17,  -51,   35, -221,  -34,  -39,  191,
  170,  120,  -30,  -33,  280,    0,    0,    0,    0, -235,
  126, -195,    0,    0,    0,    0,  153,   72,   -5,    0,
 -207,  191,  -17,    0,    0,   66,    0,   24, -166,   67,
   92,  252,   70,  325,   12,  170,    0,   95,  102,    0,
  365, -118,   34,   60,    0,    0, -107, -212,   70,   26,
   45,  118,    0,    0,    0,    0,    0,    0,  121,  302,
  -24,    0,  -33, -103,    0,    0,   70,   70,  298,  -15,
  122, -166,    0,    0,    0,   94,    0,  124,  -26,    0,
    0,    0,    0,    0, -106,  127,    0,  108,  -14,  128,
  129,  112,    0,   40,    0,    0,    0,  232,  132,   14,
    0,    0,  -33,  -33,  -33,  -33,  191,  191,  142, -188,
   -4, -114,    0,  114,  -33,  359,    0,    0,    0,  262,
  106,  191,  -83,    0,  303,  134,    0,    8,    0,  135,
   96,  280,  365,  137,  138,    0,    0,    0,  -52,    0,
  125,  130,    0,    0,    0,    0,  144,  -33,    0,    0,
   60,   60,    0,    0,  148,  152,  191,  167,   70,  131,
   70,  136,   28,   70,  139,    0,   68,    0,  143,  106,
    0,    0,  177,   70,    0,    9,    0,    0,    0,    0,
  349,  145,  151,  106,  155,  154,  302,  156,  158,  470,
  159,    0,    0,    0,    0,    0,   75,    0,    0,  113,
    0,  -80,    0,  -58,    0,   56,  -44,    0,  -57,    0,
  374,  -33,    0,  -56,    0,    0,  -60,  -32,  -48,  -33,
  -42,  -33,   -6,  149,  150,    1,  223,  239,    0,    0,
  240,  258,  272,  -46,    0,    0,  293,  365,   76,  300,
    0,    0,  115,    0,   81,    0,   82,    0,    0,    0,
  -40,    0,    0,    0,    0,    0,    0,   22,    0,    0,
  332,  106,    0,    0,    0,    0,  374,    0,    0,    0,
    0,  -33,  105,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   11,    0,    0,    0,    0,    0,    0,    0,    0,  192,
    0,    0,    0,    0,    0,    0,    0,   32,    0,    0,
   38,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  423,    0,
    0,    0,    0,  435,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -13,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  446,  457,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   20,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -25,    0,    0,  169,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   31,    0,    0,    0,    0,  341,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  381,  637,   13,    0,    0,  733,   10,  355,  379, -115,
    0,  291,    0,    0,    0,    4,   88,    0,    0,  -21,
    5,  -41,    0,  -67,    0,    0,    0,    0,    0,    0,
  261,  101,
};
final static int YYTABLESIZE=953;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         63,
   54,   25,   71,   17,   12,   52,  213,   47,  263,   54,
   12,   62,  279,  154,  256,   48,  139,   12,  289,   27,
   50,  146,   25,   37,  150,  147,  159,  115,   79,   58,
    1,  155,   41,   38,   63,   25,  205,  208,   41,  120,
   91,  271,    4,   84,  160,    5,   12,   40,  199,  236,
  167,   25,  128,   94,  127,   27,  123,   79,  124,  272,
  110,  141,   87,   95,  119,   25,  129,   41,  179,  127,
  180,  111,  170,   88,   49,  127,  123,  196,  124,   25,
  291,  128,  103,  173,  174,  238,  228,  123,    3,  124,
  127,    4,  122,  148,    5,   25,  128,    4,    6,   39,
    5,  125,   48,  133,  104,   25,  126,  107,  231,   25,
  123,   92,  124,  187,  190,  248,  283,  123,  123,  124,
  124,  286,  287,  123,  123,  124,  124,  171,  172,  204,
  207,  204,  108,  152,   25,  201,  175,  176,  178,  189,
   62,  118,  184,   79,  185,  294,  217,  123,  123,  124,
  124,  193,   25,  121,  203,  206,  203,  127,  134,   25,
  135,  142,  149,  153,  156,   25,  158,  157,  161,  162,
  163,  168,  186,  194,  197,  200,   54,  210,  251,  204,
  136,   25,  232,  214,  216,  239,  220,   25,  215,  223,
  240,   25,   90,  242,  225,  241,  243,  230,  244,  247,
  252,  257,  260,  212,  203,  261,   25,  269,  270,   48,
  259,  255,  278,   79,   62,  288,   25,  264,  265,    6,
  267,   18,    3,  266,   19,    4,   20,   69,    5,   53,
   25,   31,   21,  262,   16,   51,  282,   59,   60,   22,
   48,   23,   18,    3,   24,   19,    4,   42,    4,    5,
   93,    5,  181,   21,  182,   18,  137,  138,   19,  268,
   20,  281,   23,  198,  235,   24,   56,  115,  115,  169,
  293,   18,  166,   22,   19,   23,   20,  290,   24,  102,
  114,  273,   56,  131,  226,   18,  227,  127,   19,   22,
   20,   23,  112,  128,   24,  164,   56,  274,  275,   18,
  110,  110,   19,   22,   20,   23,   62,  136,   24,   55,
   56,  111,  111,   70,  253,   18,  276,   22,   19,   23,
   20,   78,   24,   77,   62,   18,   56,   98,   19,   18,
  277,  258,   19,   22,   20,   23,   56,  145,   24,   78,
   56,   77,   99,  195,  123,   23,  124,   22,   24,   23,
  116,  280,   24,   18,   18,  151,   19,   19,  284,    4,
  117,   78,    5,   77,   56,   56,   59,   60,  249,   62,
   99,  292,   18,   23,   23,   19,   24,   24,  250,   18,
  285,   49,   19,   56,   78,   18,   77,  258,   19,  237,
   56,   85,   23,   62,   43,   24,   56,   97,   67,   23,
  192,   18,   24,   62,   19,   23,  177,   18,   24,   62,
   19,   18,   56,  218,   19,    0,    4,  219,   56,    5,
   96,   23,   56,   89,   24,    0,   18,   23,    0,   19,
   24,   23,  221,   78,   24,   77,   18,   56,    0,   19,
   59,   60,  233,    0,  209,  211,   23,   56,    0,   24,
   18,   31,   61,   19,   31,    0,   23,   31,    0,   24,
    0,   56,   31,   56,   56,   56,    0,   56,    0,   56,
   23,   31,    0,   24,   31,   52,    0,   52,    0,   52,
    0,   56,   56,    0,   56,    0,   50,    0,   50,    0,
   50,    0,    0,   52,   52,    4,   52,   51,    5,   51,
  246,   51,  104,    0,   50,   50,    0,   50,    0,   54,
  245,    0,    0,    0,   62,   51,   51,  191,   51,    0,
    0,  109,  110,  111,    0,    4,    0,    0,    5,   78,
    0,   77,   59,   60,   73,   74,   75,   76,    0,    0,
    0,    0,    0,    4,    0,    0,    5,    0,    0,   72,
   59,   60,   73,   74,   75,   76,    0,    0,    0,    0,
    0,    4,    0,    0,    5,    0,    4,    0,  104,    5,
    0,    0,    0,  104,   73,   74,   75,   76,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    4,    0,
    0,    5,    0,    0,    0,   59,   60,   73,   74,   75,
   76,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    4,    0,  188,    5,    0,    0,    0,   59,
   60,    0,    4,    0,    0,    5,    0,    0,    4,   59,
   60,    5,    0,    0,    0,   59,   60,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   73,   74,   75,   76,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   56,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   56,  113,
   52,  115,    0,    0,    0,   56,   56,   56,   56,    0,
   52,   50,    0,   56,   56,  130,  132,   52,   52,   52,
   52,   50,   51,    0,    0,   52,   52,    0,   50,   50,
   50,   50,   51,  143,  144,    0,   50,   50,    0,   51,
   51,   51,   51,    4,    0,    0,    5,   51,   51,    0,
   59,   60,   73,   74,   75,   76,   44,    0,    0,    0,
  165,    0,   57,    0,   68,    0,    0,    0,    0,    0,
    0,    0,    0,   86,    0,    0,    0,  183,    0,    0,
    0,    0,    0,    0,   44,   44,    0,    0,  100,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   86,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  222,    0,  224,    0,    0,
  229,    0,    0,    0,    0,    0,    0,    0,  100,    0,
  234,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   57,
   57,   57,  254,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   57,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   86,   86,   57,
   86,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   86,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   86,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         21,
   40,   40,   24,   40,    1,   40,   59,   59,   41,   40,
    7,   45,   59,   40,   59,   41,   41,   14,   59,    7,
   17,   89,   40,  261,   92,   41,   41,   41,   25,   20,
  271,   58,   44,  271,   56,   40,  152,  153,   44,   61,
   37,   41,  264,  279,   59,  267,   43,   59,   41,   41,
  118,   40,  265,   59,   44,   43,   43,   54,   45,   59,
   41,   83,  258,  271,   61,   40,  279,   44,  257,   59,
  259,   41,   59,  269,   40,   44,   43,  145,   45,   40,
   59,   44,   59,  125,  126,  201,   59,   43,  261,   45,
   59,  264,   59,   90,  267,   40,   59,  264,  271,   12,
  267,   42,   15,   59,  271,   40,   47,   41,   41,   40,
   43,   40,   45,  135,  136,   41,   41,   43,   43,   45,
   45,   41,   41,   43,   43,   45,   45,  123,  124,  151,
  152,  153,   41,   40,   40,   40,  127,  128,  129,  136,
   45,   40,  257,  140,  259,   41,  168,   43,   43,   45,
   45,  142,   40,  272,  151,  152,  153,  265,   41,   40,
   40,  265,   41,   40,  271,   40,   59,   41,   41,   41,
   59,   40,   59,  257,   41,   41,   40,   40,  259,  201,
   80,   40,   40,   59,   41,   41,  177,   40,   59,   59,
   40,   40,   40,   40,   59,   41,   41,   59,   41,   41,
  259,  259,  259,  256,  201,  266,   40,   59,   59,   41,
  232,  256,  259,  210,   45,  256,   40,  266,  240,  271,
  242,  260,  261,  266,  263,  264,  265,  258,  267,  269,
   40,   40,  271,  266,  271,  270,  258,  271,  272,  278,
  266,  280,  260,  261,  283,  263,  264,  265,  264,  267,
  256,  267,  257,  271,  259,  260,  281,  282,  263,  266,
  265,  258,  280,  256,  256,  283,  271,  281,  282,  256,
  292,  260,   41,  278,  263,  280,  265,  256,  283,  256,
  269,   59,  271,  258,  257,  260,  259,  256,  263,  278,
  265,  280,   41,  256,  283,  256,  271,   59,   59,  260,
  281,  282,  263,  278,  265,  280,   45,  207,  283,   19,
  271,  281,  282,   23,  259,  260,   59,  278,  263,  280,
  265,   60,  283,   62,   45,  260,  271,  262,  263,  260,
   59,  231,  263,  278,  265,  280,  271,   40,  283,   60,
  271,   62,  277,   41,   43,  280,   45,  278,  283,  280,
  256,   59,  283,  260,  260,  262,  263,  263,   59,  264,
  266,   60,  267,   62,  271,  271,  271,  272,  256,   45,
  277,   40,  260,  280,  280,  263,  283,  283,  266,  260,
  266,   41,  263,  271,   60,  260,   62,  287,  263,   41,
  271,  266,  280,   45,   14,  283,  271,   43,  279,  280,
  140,  260,  283,   45,  263,  280,  265,  260,  283,   45,
  263,  260,  271,  266,  263,   -1,  264,  266,  271,  267,
   42,  280,  271,  271,  283,   -1,  260,  280,   -1,  263,
  283,  280,  266,   60,  283,   62,  260,  271,   -1,  263,
  271,  272,  266,   -1,  154,  155,  280,  271,   -1,  283,
  260,  260,  283,  263,  263,   -1,  280,  266,   -1,  283,
   -1,  271,  271,   41,   42,   43,   -1,   45,   -1,   47,
  280,  280,   -1,  283,  283,   41,   -1,   43,   -1,   45,
   -1,   59,   60,   -1,   62,   -1,   41,   -1,   43,   -1,
   45,   -1,   -1,   59,   60,  264,   62,   41,  267,   43,
  210,   45,  271,   -1,   59,   60,   -1,   62,   -1,   40,
   41,   -1,   -1,   -1,   45,   59,   60,  256,   62,   -1,
   -1,  270,  271,  272,   -1,  264,   -1,   -1,  267,   60,
   -1,   62,  271,  272,  273,  274,  275,  276,   -1,   -1,
   -1,   -1,   -1,  264,   -1,   -1,  267,   -1,   -1,  270,
  271,  272,  273,  274,  275,  276,   -1,   -1,   -1,   -1,
   -1,  264,   -1,   -1,  267,   -1,  264,   -1,  271,  267,
   -1,   -1,   -1,  271,  273,  274,  275,  276,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  264,   -1,
   -1,  267,   -1,   -1,   -1,  271,  272,  273,  274,  275,
  276,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  264,   -1,  256,  267,   -1,   -1,   -1,  271,
  272,   -1,  264,   -1,   -1,  267,   -1,   -1,  264,  271,
  272,  267,   -1,   -1,   -1,  271,  272,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  273,  274,  275,  276,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  266,   53,
  256,   55,   -1,   -1,   -1,  273,  274,  275,  276,   -1,
  266,  256,   -1,  281,  282,   69,   70,  273,  274,  275,
  276,  266,  256,   -1,   -1,  281,  282,   -1,  273,  274,
  275,  276,  266,   87,   88,   -1,  281,  282,   -1,  273,
  274,  275,  276,  264,   -1,   -1,  267,  281,  282,   -1,
  271,  272,  273,  274,  275,  276,   14,   -1,   -1,   -1,
  114,   -1,   20,   -1,   22,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   31,   -1,   -1,   -1,  131,   -1,   -1,
   -1,   -1,   -1,   -1,   42,   43,   -1,   -1,   46,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   58,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  179,   -1,  181,   -1,   -1,
  184,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   96,   -1,
  194,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  127,
  128,  129,  226,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  142,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  175,  176,  177,
  178,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  193,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  220,
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
"bloqueEjecutableFunc : BEGIN sentEjecutableFunc RETURN '(' retorno ')' END",
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

//#line 246 "gramatica.y"


public int yylex() {
    return AnalizadorLexico.yylex();
}

public void yyerror(String string) {
	AnalizadorSintactico.agregarError("Parser: " + string);
}
//#line 636 "Parser.java"
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
//#line 24 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Programa reconocido. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 7:
//#line 38 "gramatica.y"
{AnalizadorSintactico.agregarError("error: falta TRY en (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 8:
//#line 39 "gramatica.y"
{AnalizadorSintactico.agregarError("error TRY-CATCH vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 9:
//#line 40 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta CATCH (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 10:
//#line 41 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 11:
//#line 42 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 13:
//#line 46 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 14:
//#line 47 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta END (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 16:
//#line 52 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta BEGIN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 17:
//#line 53 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta RETURN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 18:
//#line 54 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 19:
//#line 55 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta retorno (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 20:
//#line 56 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 24:
//#line 66 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sent control (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 25:
//#line 67 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ':' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 26:
//#line 68 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 27:
//#line 69 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 28:
//#line 70 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 29:
//#line 71 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 35:
//#line 87 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 36:
//#line 88 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 37:
//#line 89 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 38:
//#line 92 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 39:
//#line 93 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 40:
//#line 96 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 41:
//#line 97 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 42:
//#line 98 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 44:
//#line 102 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 45:
//#line 103 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 46:
//#line 104 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 47:
//#line 105 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 50:
//#line 114 "gramatica.y"
{AnalizadorSintactico.arbol= new Nodo('+', val_peek(2), val_peek(0));}
break;
case 51:
//#line 115 "gramatica.y"
{AnalizadorSintactico.arbol= new Nodo('-', val_peek(2), val_peek(0));}
break;
case 52:
//#line 116 "gramatica.y"
{yyval=val_peek(0) ;}
break;
case 53:
//#line 119 "gramatica.y"
{AnalizadorSintactico.arbol= new Nodo('*', val_peek(2), val_peek(0));}
break;
case 54:
//#line 120 "gramatica.y"
{AnalizadorSintactico.arbol= new Nodo('/',val_peek(2), val_peek(0));}
break;
case 55:
//#line 121 "gramatica.y"
{yyval = val_peek(0);}
break;
case 56:
//#line 124 "gramatica.y"
{AnalizadorSintactico.arbol = new Nodo(val_peek(0));}
break;
case 57:
//#line 125 "gramatica.y"
{AnalizadorSintactico.arbol = new Nodo(val_peek(0));}
break;
case 58:
//#line 126 "gramatica.y"
{AnalizadorLexico.agregarNegativoTDS(val_peek(0));
			AnalizadorSintactico.arbol = new Nodo(-val_peek(0));}
break;
case 62:
//#line 137 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 63:
//#line 138 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 64:
//#line 139 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 65:
//#line 140 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 66:
//#line 141 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 67:
//#line 142 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 69:
//#line 147 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 74:
//#line 158 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion (Linea " + AnalizadorLexico.numLinea + ")");
					AnalizadorSintactico.arbol= new Nodo(':=', val_peek(3), val_peek(1));}
break;
case 75:
//#line 161 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 76:
//#line 162 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 77:
//#line 163 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 78:
//#line 164 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 79:
//#line 168 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");}
break;
case 80:
//#line 169 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 81:
//#line 170 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 82:
//#line 171 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 83:
//#line 172 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 84:
//#line 173 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 85:
//#line 174 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 86:
//#line 175 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 87:
//#line 176 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 88:
//#line 177 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 89:
//#line 178 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 90:
//#line 179 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 91:
//#line 180 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 92:
//#line 181 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 93:
//#line 184 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 94:
//#line 185 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 95:
//#line 186 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 96:
//#line 187 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 97:
//#line 188 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 98:
//#line 189 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 194 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 102:
//#line 195 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 103:
//#line 196 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 104:
//#line 197 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 105:
//#line 198 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 109:
//#line 208 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 114:
//#line 216 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 115:
//#line 217 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 128:
//#line 240 "gramatica.y"
{AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
//#line 1111 "Parser.java"
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
