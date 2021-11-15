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
   14,   14,   18,   18,   18,   15,   15,   16,   16,   16,
   20,   20,   20,   20,   20,    9,    9,   21,   21,   21,
   22,   22,   22,   23,   23,   23,   23,   24,   24,   19,
   19,   19,   19,   19,   19,   25,   25,    6,    6,    6,
    6,   26,   26,   26,   26,   26,   27,   27,   27,   27,
   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,
   28,   28,   28,   28,   28,   28,   28,   28,   29,   29,
   29,   29,   29,   13,   30,   30,   30,   32,   32,   32,
   32,   32,   32,   33,   33,   33,   33,   33,   33,   31,
   31,   17,   17,
};
final static short yylen[] = {                            2,
    3,    2,    1,    1,    1,    6,    5,    5,    5,    5,
    6,    8,    9,   10,    9,    3,    2,    3,    8,    1,
    6,    6,    6,    6,    6,    2,    2,    1,    6,    5,
    5,    5,    5,    6,    2,    1,    1,    1,    1,    3,
    2,    3,    3,    1,    2,    3,    2,    3,    2,    3,
    5,    4,    4,    4,    5,    1,    4,    3,    3,    1,
    3,    3,    1,    1,    1,    2,    1,    4,    4,    6,
    5,    5,    5,    5,    6,    2,    1,    1,    1,    1,
    1,    4,    7,    3,    3,    4,    8,    6,    7,    7,
    7,    7,    7,    7,    8,    5,    5,    5,    5,    6,
    5,    4,    4,    4,    4,    5,    5,    5,    4,    3,
    3,    3,    4,    3,    3,    1,    3,    3,    6,    6,
    9,    3,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,  132,  133,   44,    0,    3,   37,   38,
   39,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    1,    2,    4,    5,    0,
    0,    0,   78,   79,   80,   81,    0,    0,    0,   41,
    0,    0,    0,   28,   20,    0,   47,   49,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   36,    0,    0,
   65,    0,    0,    0,    0,   63,   67,    0,    0,    0,
    0,    0,    0,  126,  127,  128,  129,  124,  125,    0,
    0,    0,  116,    0,    0,   17,   35,    0,    0,    0,
    0,    0,    0,   42,   40,   43,    0,    0,    0,   46,
    0,    0,   26,   27,   50,   48,   77,    0,    0,   52,
    0,    0,    0,    0,    0,  111,    0,  112,   18,   16,
    0,    0,    0,   66,   85,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   84,    0,    0,    0,  130,
  131,  114,    0,    0,    0,    0,  110,    0,    0,   54,
    0,   53,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   76,   72,  103,    0,  105,    0,    0,  104,
  113,  109,    0,    0,    0,   86,   82,    0,    0,   61,
   62,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  102,    0,  122,    0,    0,  117,  115,    0,
    0,   71,    0,   74,   55,   51,   73,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  106,  101,  107,  108,   68,   69,    0,    8,
    9,    0,   10,    0,   96,    0,   98,    0,    0,   99,
    0,   97,    0,    0,    7,    0,   75,   70,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   11,    6,    0,    0,    0,    0,
  100,   88,    0,    0,    0,    0,    0,    0,   24,   25,
    0,   23,    0,   22,    0,    0,    0,   21,   30,   32,
    0,   33,   31,   83,   90,   92,   93,    0,   94,   91,
    0,    0,  120,   89,    0,    0,    0,   57,    0,    0,
    0,   34,   29,   95,   87,    0,   12,    0,   19,    0,
    0,    0,   15,   13,    0,  121,   14,
};
final static short yydgoto[] = {                          2,
    7,   26,    8,   28,   29,   30,   31,   45,  211,   46,
   47,  104,   32,    9,   10,   11,  212,   13,   14,   15,
  213,   65,   66,   67,  109,   33,   34,   35,   36,   82,
  143,   83,   84,
};
final static short yysindex[] = {                      -224,
 -180,    0,  -35,    0,    0,    0,  -33,    0,    0,    0,
    0, -222,   13,   -9,  -49,   50, -232,  -31,  -29,  246,
   88,   -3,  -38,   97,  546,    0,    0,    0,    0, -228,
  157, -213,    0,    0,    0,    0,   57,   79,   76,    0,
 -212,  170,   -9,    0,    0,  103,    0,    0,   86, -179,
   67,   75,  298,  110,  572,   25,   88,    0,  116,   83,
    0,  371, -138,   -5,   47,    0,    0, -126, -236,  110,
   39,   21,  105,    0,    0,    0,    0,    0,    0,  101,
  413,  -40,    0,   97, -110,    0,    0,  110,  110,  143,
  -18,  120, -179,    0,    0,    0,  184, -228,   29,    0,
  130,  -12,    0,    0,    0,    0,    0, -108,  135,    0,
  119,    9,  148,  149,  132,    0,   60,    0,    0,    0,
  -27,  152,   62,    0,    0,   97,   97,   97,   97,  246,
  246,  171, -131,    4, -128,    0,  137,   97,  358,    0,
    0,    0,  559,   95,  246,  -64,    0,  317,  153,    0,
   -8,    0,  158,  161,  129,  233,  546,  197, -228,  371,
  162,  163,    0,    0,    0,  -44,    0,  139,  146,    0,
    0,    0,  166,  150,   97,    0,    0,   47,   47,    0,
    0,  203,  219,  246,  232,  110,  159,  110,  211,  -51,
  110,  256,    0,   58,    0,  174,   95,    0,    0,  253,
  110,    0,   42,    0,    0,    0,    0,  371,  176,  356,
  180,  189,   95,  315,  345,  413,  354,  142,  357,  367,
  533,  386,    0,    0,    0,    0,    0,    0,   61,    0,
    0,  155,    0,  136,    0,  165,    0,   70,  -39,    0,
  173,    0,  383,   97,    0,  190,    0,    0,  388,  371,
  186,   17,  195,   97,  196,   97,  371,  399,  199,  411,
  412,   11,  417,  419,    0,    0,  422,  425,  428,  -46,
    0,    0,  429,  371,   68,  435,  437,  431,    0,    0,
  442,    0,  106,    0,  123,  470,  371,    0,    0,    0,
  122,    0,    0,    0,    0,    0,    0,  128,    0,    0,
  474,   95,    0,    0,  252,  461,  255,    0,  383,  463,
  482,    0,    0,    0,    0,   97,    0,  259,    0,  261,
  476,  134,    0,    0,  272,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   34,    0,    0,    0,    0,    0,    0,    0,    0,  271,
    0,    0,    0,    0,    0,    0,    0,   93,    0,    0,
  121,    0,    0,    0,    0,    0,    0,    0,    0,    0,
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
    0,    0,   35,    0,    0,  503,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   45,    0,    0,    0,    0,    0,    0,  507,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  536,  771,   19,  -24,    0,  835,   41,    0,  -83,  -26,
  509,    0,   -2,    0,    0,    0,    5,  100,    0,    0,
  -21,   31,   56,    0,  -66,    0,    0,    0,    0,    0,
    0,  410,  -28,
};
final static int YYTABLESIZE=1067;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         64,
  142,   55,   72,   81,   17,   12,   25,  240,   53,   48,
   55,   12,  299,  174,  224,   99,   56,   97,   12,  272,
   71,   51,  150,  149,  123,   27,  153,  161,  131,   80,
   25,    4,  206,   81,    5,   64,   25,  126,   37,  127,
  123,   92,  132,   25,   88,  162,    1,   12,   38,  166,
   85,  291,  139,  125,  108,   89,   41,  281,   96,   80,
   59,   27,  144,  126,   25,  127,  122,  167,  157,  292,
  155,   40,  118,  214,  158,   56,  219,   44,   25,  136,
    3,  203,  248,    4,    4,  119,    5,    5,  128,   50,
    6,  107,   44,  129,  108,  151,   91,  108,  243,   25,
  126,  264,  127,  126,  126,  127,  127,  110,  303,   25,
  126,   39,  127,   28,   49,  111,  194,  197,   93,   41,
  177,   81,  121,   26,  249,  186,  252,  187,  191,   41,
  192,  218,   63,  124,   95,  216,   44,  126,  130,  127,
  138,   63,   25,  196,  106,  137,  308,   80,  126,   25,
  127,   44,  108,  229,  145,   25,  178,  179,  220,  222,
  152,  215,  163,  309,   45,  126,  278,  127,   25,  160,
  182,  183,  185,  286,  326,  164,  126,  165,  127,   45,
  313,   25,  148,  180,  181,  200,  315,  139,  168,  169,
  170,  175,  201,  204,   25,  193,   25,  225,  207,   81,
  208,   55,  221,  311,  226,  238,  227,  239,  228,   25,
   25,  223,  298,  244,  274,  250,  271,  235,  262,   70,
  253,    6,  275,   25,  232,   80,   18,    3,  254,   19,
    4,   20,  283,    5,  285,   16,   25,   21,   52,   54,
  140,  141,   25,  173,   22,    4,   23,  205,    5,   24,
   18,    3,  302,   19,    4,   42,   18,    5,   25,   19,
  188,   21,  189,   18,  123,  123,   19,   57,   20,  237,
   23,   25,  210,   24,   57,   68,   23,   63,  301,   24,
  274,   22,  280,   23,   18,   25,   24,   19,   18,   20,
  156,   19,   25,  117,  322,   57,  134,  247,   18,   57,
   56,   19,   22,   20,   23,  102,   22,   24,   23,   57,
   36,   24,  118,  118,  242,  171,   22,  176,   23,   18,
    4,   24,   19,    5,   20,  119,  119,   90,  269,   18,
   57,   94,   19,   28,   20,   28,   28,   22,  115,   23,
   57,  105,   24,   26,   28,   26,   26,   22,   44,   23,
   28,   28,   24,   28,   26,  255,   28,  202,   60,   61,
   26,   26,   18,   26,  101,   19,   26,   60,   61,   18,
   62,  119,   19,   57,   20,   18,   45,  312,   19,  102,
   57,  120,   23,  314,  256,   24,   57,   22,   18,   23,
  209,   19,   24,  257,  267,   23,  251,  259,   24,   57,
   63,   18,   63,  258,   19,  102,    4,  260,   23,    5,
  265,   24,   57,  107,   18,   63,   18,   19,  102,   19,
  266,   23,   86,  268,   24,   57,  263,   57,  277,   18,
   18,  273,   19,   19,   23,  184,   23,   24,  287,   24,
   57,   57,   79,   18,   78,  154,   19,   22,  276,   23,
   23,  279,   24,   24,   57,  126,   18,  127,  217,   19,
  282,  284,   18,   23,  288,   19,   24,   57,  230,  289,
  290,  306,   79,   57,   78,  293,   23,  294,   18,   24,
  295,   19,   23,  296,  231,   24,  297,  300,   60,   57,
   60,   18,   60,  304,   19,  305,    4,  233,   23,    5,
  307,   24,   57,   60,   61,   18,   60,   60,   19,   60,
  310,   23,   18,  316,   24,   19,   57,  317,  245,  318,
  319,  320,  321,   57,  323,   23,  324,   58,   24,   58,
   36,   58,   23,   36,  325,   24,   36,  327,   64,   64,
   64,   36,   64,   56,   64,   58,   58,   57,   58,   43,
   36,  100,  199,   36,    0,    0,   64,   64,    0,   64,
   59,    0,   59,    0,   59,    0,    0,  112,  113,  114,
    0,    0,   55,  261,    0,    0,    0,   63,   59,   59,
    4,   59,    0,    5,    0,    0,    0,  107,    0,    0,
   63,    0,   79,    0,   78,    0,    0,    0,    0,    0,
    0,    0,    0,   63,    0,   79,    0,   78,    0,    0,
    0,    0,    0,  195,    0,    0,   63,    0,   79,    4,
   78,    4,    5,    0,    5,    0,   60,   61,   60,   61,
    0,   79,    0,   78,    4,    0,    0,    5,    0,    0,
    0,   60,   61,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   74,   75,   76,   77,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   74,   75,   76,   77,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   60,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   60,    0,    0,    0,    0,    0,    0,
   60,   60,   60,   60,    0,    0,    0,    0,   60,   60,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   58,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   58,   64,    0,    0,    0,    0,    0,   58,
   58,   58,   58,   64,    0,    0,    0,   58,   58,    0,
   64,   64,   64,   64,    0,   59,    0,    0,   64,   64,
    0,    0,    0,    0,    0,   59,    0,    0,    0,    0,
    0,    0,   59,   59,   59,   59,    4,    0,    0,    5,
   59,   59,    0,   60,   61,   74,   75,   76,   77,    4,
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
   40,    7,   59,   41,   59,   42,   19,   42,   14,   59,
   23,   17,   41,   90,   41,    7,   93,   40,  265,   25,
   40,  264,   41,   55,  267,   57,   40,   43,  261,   45,
   62,   37,  279,   40,  258,   58,  271,   43,  271,   41,
  279,   41,   81,   59,   50,  269,   44,   41,  271,   55,
   20,   43,   84,   43,   40,   45,   62,   59,   40,   59,
   97,   59,   41,  157,   99,   41,  160,   44,   40,   59,
  261,  148,   41,  264,  264,   41,  267,  267,   42,   40,
  271,  271,   59,   47,   90,   91,   40,   93,   41,   40,
   43,   41,   45,   43,   43,   45,   45,   41,   41,   40,
   43,   12,   45,   40,   15,   41,  138,  139,   40,   44,
   59,  143,   40,   40,  208,  257,  210,  259,  257,   44,
  259,  158,   45,  272,   59,  157,   44,   43,  265,   45,
   40,   45,   40,  139,   59,   41,   41,  143,   43,   40,
   45,   59,  148,  175,  265,   40,  126,  127,  161,  162,
   41,  157,  271,   41,   44,   43,  250,   45,   40,   40,
  130,  131,  132,  257,   41,   41,   43,   59,   45,   59,
   59,   40,   40,  128,  129,  145,   59,  216,   41,   41,
   59,   40,  257,   41,   40,   59,   40,   59,   41,  221,
   40,   40,   40,  287,   59,  257,   41,  259,   59,   40,
   40,  256,  259,   40,  243,   40,  256,   59,  221,  258,
   41,  271,  244,   40,  184,  221,  260,  261,   40,  263,
  264,  265,  254,  267,  256,  271,   40,  271,  270,  269,
  281,  282,   40,  271,  278,  264,  280,  256,  267,  283,
  260,  261,  274,  263,  264,  265,  260,  267,   40,  263,
  257,  271,  259,  260,  281,  282,  263,  271,  265,   59,
  280,   40,   40,  283,  271,  279,  280,   45,  274,  283,
  309,  278,  266,  280,  260,   40,  283,  263,  260,  265,
  262,  263,   40,  269,  316,  271,  258,  256,  260,  271,
  266,  263,  278,  265,  280,  277,  278,  283,  280,  271,
   40,  283,  281,  282,   59,  256,  278,  256,  280,  260,
  264,  283,  263,  267,  265,  281,  282,  271,  259,  260,
  271,  256,  263,  260,  265,  262,  263,  278,   41,  280,
  271,  256,  283,  260,  271,  262,  263,  278,  256,  280,
  277,  278,  283,  280,  271,   41,  283,   41,  271,  272,
  277,  278,  260,  280,  262,  263,  283,  271,  272,  260,
  283,  256,  263,  271,  265,  260,  256,  256,  263,  277,
  271,  266,  280,  256,   40,  283,  271,  278,  260,  280,
  262,  263,  283,   40,  259,  280,   41,   41,  283,  271,
   45,  260,   45,  262,  263,  277,  264,   41,  280,  267,
  256,  283,  271,  271,  260,   45,  260,  263,  277,  263,
  266,  280,  266,  259,  283,  271,   41,  271,   41,  260,
  260,  259,  263,  263,  280,  265,  280,  283,   40,  283,
  271,  271,   60,  260,   62,  262,  263,  278,  259,  280,
  280,  266,  283,  283,  271,   43,  260,   45,  262,  263,
  266,  266,  260,  280,  266,  263,  283,  271,  266,   59,
   59,   41,   60,  271,   62,   59,  280,   59,  260,  283,
   59,  263,  280,   59,  266,  283,   59,   59,   41,  271,
   43,  260,   45,   59,  263,   59,  264,  266,  280,  267,
   59,  283,  271,  271,  272,  260,   59,   60,  263,   62,
   41,  280,  260,   40,  283,  263,  271,  266,  266,   59,
  266,   59,   41,  271,  266,  280,  266,   41,  283,   43,
  260,   45,  280,  263,   59,  283,  266,  266,   41,   42,
   43,  271,   45,   41,   47,   59,   60,   41,   62,   14,
  280,   43,  143,  283,   -1,   -1,   59,   60,   -1,   62,
   41,   -1,   43,   -1,   45,   -1,   -1,  270,  271,  272,
   -1,   -1,   40,   41,   -1,   -1,   -1,   45,   59,   60,
  264,   62,   -1,  267,   -1,   -1,   -1,  271,   -1,   -1,
   45,   -1,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   45,   -1,   60,   -1,   62,   -1,   -1,
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

//#line 501 "gramatica.y"


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
{
                    AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 41:
//#line 145 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta 'tipo' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 42:
//#line 146 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 43:
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
case 44:
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
case 45:
//#line 167 "gramatica.y"
{AnalizadorSintactico.agregarError("falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 46:
//#line 171 "gramatica.y"
{
        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
        yyval=new ParserVal (new Nodo(val_peek(2).sval, (Nodo)val_peek(0).obj, null));
        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
        }
break;
case 47:
//#line 176 "gramatica.y"
{
	        AnalizadorSintactico.agregarAnalisis("Funcion reconocida en. (Linea " + AnalizadorLexico.numLinea + ")");
	        yyval=new ParserVal (new Nodo(val_peek(1).sval, (Nodo)val_peek(0).obj, null));
	        AnalizadorSintactico.ambitoActual = AnalizadorSintactico.ambitoActual.substring(0,AnalizadorSintactico.ambitoActual.lastIndexOf("@"));
	   }
break;
case 48:
//#line 183 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de variable. (Linea " + AnalizadorLexico.numLinea + ")");
            }
break;
case 49:
//#line 185 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta variable (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 50:
//#line 186 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 51:
//#line 189 "gramatica.y"
{
            AnalizadorSintactico.tipoActual = val_peek(4).sval;
           }
break;
case 52:
//#line 192 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo antes de FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 53:
//#line 193 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 54:
//#line 194 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo entre parentesis (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 55:
//#line 195 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 56:
//#line 199 "gramatica.y"
{yyval=val_peek(0);}
break;
case 58:
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
case 59:
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
case 60:
//#line 221 "gramatica.y"
{
	                 yyval=val_peek(0);
	                 }
break;
case 61:
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
case 62:
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
case 63:
//#line 246 "gramatica.y"
{
	                        yyval = val_peek(0);
	                        }
break;
case 64:
//#line 255 "gramatica.y"
{
                     String lexema = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(0).sval);
                     if(lexema != null){
                        AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                        TDSObject value = AnalizadorLexico.getLexemaObject(lexema);
                        yyval= new ParserVal(new Nodo(val_peek(0).sval));
                        ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
                        /*((Nodo)$$.obj).setTipoContenido("VAR");*/
                     }else{
                         AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                         /*stop generacion de arbol*/
                     }
                }
break;
case 65:
//#line 268 "gramatica.y"
{
                    if (val_peek(0).sval != null){
                        yyval= new ParserVal(new Nodo(val_peek(0).sval));
                    }
	            }
break;
case 66:
//#line 273 "gramatica.y"
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
case 67:
//#line 299 "gramatica.y"
{yyval=val_peek(0);}
break;
case 68:
//#line 303 "gramatica.y"
{
		  String variable = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(3).sval);
		  if(variable != null){
		    AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
		    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
		    if(value.getTipoContenido().equals("FUNC")){
		        ParserVal aux2= new ParserVal(val_peek(1).sval);
		        ParserVal aux= new ParserVal(val_peek(3).sval);
		        yyval= new ParserVal(new Nodo("LF",(Nodo)aux.obj, (Nodo)aux2.obj ));
		        ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
		    }else{
		        AnalizadorSintactico.agregarError("ID pertenece a variable, no a Funcion (Linea " + AnalizadorLexico.numLinea + ")");
		        /*error*/
		    }
		  }else{
             AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
             /*error*/
		  }
		  }
break;
case 69:
//#line 322 "gramatica.y"
{
	   		  String variable = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(3).sval);
       		  if(variable != null){
       		    AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
       		    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
       		    if(value.getTipoContenido().equals("FUNC")){
       		        ParserVal aux= new ParserVal(val_peek(3).sval);
       		        yyval= new ParserVal(new Nodo("LF",(Nodo)aux.obj, null ));
       		        ((Nodo)yyval.obj).setTipo(value.getTipoVariable());
       		    }else{
       		        AnalizadorSintactico.agregarError("ID pertenece a variable, no a Funcion (Linea " + AnalizadorLexico.numLinea + ")");
       		        /*error*/
       		    }
       		  }else{
                    AnalizadorSintactico.agregarError("ID de Funcion no declarada (Linea " + AnalizadorLexico.numLinea + ")");
                    /*error*/
       		  }
	   }
break;
case 70:
//#line 344 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
            if( AnalizadorSintactico.esVariableRedeclarada(val_peek(3).sval + AnalizadorSintactico.ambitoActual)){
                /*corto arbol*/
            }else{
                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(3).sval);
                aux.setTipoContenido("FUNC");
                AnalizadorLexico.tablaDeSimbolos.put(val_peek(3).sval + AnalizadorSintactico.ambitoActual,aux);
                yyval=val_peek(3);
            }
            AnalizadorSintactico.ambitoActual += "@"+ val_peek(3).sval;
			}
break;
case 71:
//#line 355 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Declaracion de funcion en (Linea " + AnalizadorLexico.numLinea + ")");
		     if( AnalizadorSintactico.esVariableRedeclarada(val_peek(2).sval + AnalizadorSintactico.ambitoActual)){
                   /*corto arbol*/
             }else{
                TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                aux.setTipoContenido("FUNC");
                AnalizadorLexico.tablaDeSimbolos.put(val_peek(2).sval + AnalizadorSintactico.ambitoActual,aux);
                yyval=val_peek(2);
             }
             AnalizadorSintactico.ambitoActual += "@"+ val_peek(2).sval;
            }
break;
case 72:
//#line 366 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta tipo (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 73:
//#line 367 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 74:
//#line 368 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 75:
//#line 369 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 76:
//#line 373 "gramatica.y"
{yyval= new ParserVal(new Nodo(val_peek(0).sval));
                    TDSObject aux = AnalizadorLexico.tablaDeSimbolos.remove(val_peek(0).sval);
                     aux.setTipoContenido(AnalizadorSintactico.tipoActual);
                    AnalizadorLexico.tablaDeSimbolos.put(val_peek(0).sval + AnalizadorSintactico.ambitoActual,aux);
		     }
break;
case 77:
//#line 378 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta FUNC (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 78:
//#line 383 "gramatica.y"
{yyval=val_peek(0);}
break;
case 79:
//#line 384 "gramatica.y"
{yyval=val_peek(0);}
break;
case 80:
//#line 385 "gramatica.y"
{yyval=val_peek(0);}
break;
case 81:
//#line 386 "gramatica.y"
{yyval=val_peek(0);}
break;
case 82:
//#line 389 "gramatica.y"
{
                    String variable = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(3).sval);
                    if(variable == null){
                       AnalizadorSintactico.agregarError("Variable no definida (Linea " + AnalizadorLexico.numLinea + ")");
                       /*corta arbol*/
                    }else{
                        AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion (Linea " + AnalizadorLexico.numLinea + ")");
					    ParserVal aux = new ParserVal(new Nodo(val_peek(3).sval));
					    TDSObject value = AnalizadorLexico.getLexemaObject(variable);
					    yyval= new ParserVal(new Nodo(":=", (Nodo)aux.obj, (Nodo)val_peek(1).obj));
                        if(((Nodo)aux.obj).getTipo() == ((Nodo)val_peek(1).obj).getTipo()){
                            ((Nodo)yyval.obj).setTipo(((Nodo)aux.obj).getTipo());
                        }else{
                        	 AnalizadorSintactico.agregarError("Tipo Incompatible (" + ((Nodo)aux.obj).getTipo() + "," +  ((Nodo)val_peek(1).obj).getTipo()  + ") (Linea " + AnalizadorLexico.numLinea + ")");
                        }
                     }
					}
break;
case 83:
//#line 406 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("Sentencia ejecutable asignacion casteada (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 84:
//#line 407 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ID (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 85:
//#line 408 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ASIGN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 86:
//#line 409 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 87:
//#line 413 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 88:
//#line 419 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 89:
//#line 420 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 90:
//#line 421 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 91:
//#line 422 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 92:
//#line 423 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 93:
//#line 424 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 94:
//#line 425 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 95:
//#line 426 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 96:
//#line 427 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 97:
//#line 428 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 98:
//#line 429 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 99:
//#line 430 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 100:
//#line 431 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 101:
//#line 434 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 102:
//#line 438 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 103:
//#line 439 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 104:
//#line 440 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 105:
//#line 441 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 106:
//#line 442 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 109:
//#line 447 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 110:
//#line 449 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 111:
//#line 450 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 112:
//#line 451 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 113:
//#line 452 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 114:
//#line 456 "gramatica.y"
{yyval=val_peek(1);}
break;
case 116:
//#line 460 "gramatica.y"
{yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(0).obj, null));}
break;
case 117:
//#line 461 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 118:
//#line 465 "gramatica.y"
{ 
		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
		}
break;
case 122:
//#line 471 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 123:
//#line 472 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 124:
//#line 475 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 125:
//#line 476 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 126:
//#line 477 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 127:
//#line 478 "gramatica.y"
{yyval = new ParserVal("!=");}
break;
case 128:
//#line 479 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 129:
//#line 480 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 132:
//#line 488 "gramatica.y"
{AnalizadorSintactico.tipoActual = "LONG";
             yyval = new ParserVal("LONG");
            }
break;
case 133:
//#line 491 "gramatica.y"
{AnalizadorSintactico.tipoActual = "SINGLE";
             yyval = new ParserVal("SINGLE");
             }
break;
//#line 1544 "Parser.java"
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
