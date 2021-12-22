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
   27,   27,   27,   27,   27,   28,   28,   28,   28,   28,
   28,   28,   28,   28,   29,   29,   29,   29,   29,   13,
   31,   31,   31,   30,   30,   30,   33,   33,   33,   33,
   33,   33,   33,   32,   32,   17,   17,
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
    7,    3,    3,    4,    3,    3,    4,    8,    6,    8,
    8,    8,    6,    7,    7,    7,    7,    7,    7,    8,
    5,    5,    5,    5,    6,    5,    4,    4,    4,    4,
    5,    5,    5,    6,    4,    3,    3,    3,    4,    3,
    3,    1,    3,    3,    3,    2,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,  136,  137,    0,    0,    3,   33,   34,
   35,    0,    0,    0,    0,    0,    0,   39,    0,    0,
    0,    0,    0,    0,    0,    1,    2,    4,    0,    0,
    0,   74,   75,   76,   77,   40,    0,    0,    0,   37,
    0,    0,    0,    0,    0,   24,    0,   45,    0,   22,
   23,   42,   47,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   63,    0,    0,    0,    0,    0,
   61,   65,    0,    0,    0,    0,  129,  130,  131,  132,
  133,  127,  128,    0,    0,  122,    0,    0,   12,   31,
    0,    0,    0,    0,    0,    0,   38,   36,   41,    0,
    0,    0,    0,    0,    0,   44,    0,    0,   21,   48,
   46,   73,    0,    0,   50,    0,    0,    0,    0,    0,
    0,  117,    0,  118,   13,   11,    0,   85,    0,    0,
    0,   64,   86,    0,   83,    0,    0,    0,    0,    0,
    0,    0,   82,    0,    0,    0,  134,  135,  120,    0,
    0,    0,  116,    0,    0,   52,    0,   51,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   72,   68,  108,    0,  110,    0,    0,  109,    0,  119,
  115,    0,    0,   84,   78,   87,   80,    0,    0,   59,
   60,    0,    0,    0,    0,    0,    0,    0,    0,  107,
    0,  125,    0,  123,  121,    0,    0,   70,   53,   49,
   69,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   25,    0,    0,    0,    0,    0,    0,
  111,  106,  112,  113,    0,   66,    0,    0,  101,    0,
  103,    0,    0,  104,    0,    0,    0,  102,    0,    0,
   71,   67,    0,    0,    0,    0,    0,    0,   26,   28,
    0,   29,   27,    7,    8,    0,    9,    6,    0,  114,
    0,    0,    0,    0,    0,    0,  105,   89,    0,    0,
   93,    0,    0,    0,    0,    0,   19,   20,    0,   18,
   17,   30,   10,    5,   16,   81,   95,   97,   98,    0,
   99,    0,    0,    0,   96,   79,   94,   15,   55,    0,
  100,   88,   91,   90,   92,   14,
};
final static short yydgoto[] = {                          2,
    7,   26,    8,   28,   46,   47,   29,   48,   49,  212,
   50,   30,   31,    9,   10,   11,   12,   13,   14,   15,
   85,   70,   71,   72,  114,   32,   33,   34,   35,   86,
   87,  150,   88,
};
final static short yysindex[] = {                      -251,
 -138,    0,  -34,    0,    0, -174,  -18,    0,    0,    0,
    0, -197,  -20,  -39,   22,   29, -173,    0,  -22,  -38,
   32,   82,   16,   -8,  445,    0,    0,    0, -166,   32,
 -172,    0,    0,    0,    0,    0,  200,   70,  -40,    0,
 -140,   -2,  -17, -209,  -39,    0, -144,    0,    8,    0,
    0,    0,    0,   45, -143,  101,  115,  182,  118,  465,
   53,    1, -190,  124,    0,  458, -106,  -42,   -1,   60,
    0,    0,  118,   63,    2,  126,    0,    0,    0,    0,
    0,    0,    0,  127,   56,    0,  -15,   -8,    0,    0,
  118,  118,  266,   47,  128, -143,    0,    0,    0,  130,
   27,  131,  132,  -92, -187,    0,  -90,  137,    0,    0,
    0,    0,  -93,  141,    0,  122,  -16,  142,  143,  136,
  -87,    0,   79,    0,    0,    0,  -85,    0,  147,  376,
  138,    0,    0,   12,    0,   -8,   -8,   -8,   -8, -148,
   37, -137,    0,  144,  -83,   69,    0,    0,    0,  432,
  109,  -68,    0, -143,  149,    0,  -36,    0,  150,  264,
  237,  445,  158,  352,   -6,   32,   32,  125,   32,  264,
    0,    0,    0,  -47,    0,  145,  159,    0,  160,    0,
    0,  161,   -8,    0,    0,    0,    0,   60,   60,    0,
    0,  118,  260,  118,  286,  -52,   92,  118,  289,    0,
   78,    0,  109,    0,    0,  118,   43,    0,    0,    0,
    0,  323,  326,  109,  328,  338,  343,    9,   56,  308,
  327,   -5,  336,    0,  -66,  121,   32,  152,  156,  361,
    0,    0,    0,    0,  345,    0,   93,  157,    0,  169,
    0,  111,   91,    0,  118,  347,  175,    0,   -8,  183,
    0,    0,  350,   -8,  178,  -14,  179,  180,    0,    0,
  154,    0,    0,    0,    0, -160,    0,    0,  185,    0,
  356,  403,  409,  411,  -51,   97,    0,    0,  207,  140,
    0,  413,   20,  415,  210,  112,    0,    0,  419,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  102,
    0,  424,  425,  426,    0,    0,    0,    0,    0,  220,
    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,   46,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   46,    0,    0,    0,    0,    0,    0,    0, -158,
    0,    0,    0,    0,    0,    0,    0,   71,    0,    0,
   81,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  151,    0,    0,    0,    0,    0,  174,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -31,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  398,  420,    0,
    0,    0,    0,    0,    0,    0,  221,    0,    0,    0,
    0,    0,  -25,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -12,    0,    0,    0,    0,  447,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  221,    0,    0,    0,  221,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  475,  686,    6,    0,    0,  452,  -21,  446,  455, -130,
  -28,   38,   10,    0,    0,    0,  604,  129,    0,    0,
  407,   23,   24,    0,  -79,    0,    0,    0,    0,  -55,
    0,    0,  -70,
};
final static int YYTABLESIZE=931;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         63,
   25,   60,   67,   41,  210,   17,  244,  301,   90,  126,
  131,  232,   27,  155,  146,  124,  159,   58,   98,    1,
  109,   25,  102,   41,  174,  149,  289,  126,   54,   61,
  216,  217,   74,  124,  223,  261,   67,   25,   40,  230,
  103,  136,  175,  137,  136,   67,  137,   25,  254,  145,
   27,   51,  224,  262,  136,   60,  137,  135,   36,  146,
  143,   68,  136,   37,  137,  125,  162,   43,   55,  104,
  187,   25,  109,   38,  207,  126,   25,  167,  306,   51,
   53,   18,   51,  252,  256,   91,   51,  156,   41,   42,
    4,  168,   25,    5,  205,  293,   92,   32,  136,   89,
  137,  138,   25,  111,   42,  294,  139,   32,  192,   96,
  193,  163,  165,   67,   42,   83,   81,   82,   25,  198,
    4,  199,    3,    5,   43,    4,   67,  112,    5,   42,
   99,   25,    6,  271,  107,  136,   25,  137,   51,   43,
   39,  115,   68,   54,  225,  226,  228,  229,  146,  278,
   25,  136,  309,  137,  136,  116,  137,   25,  188,  189,
  312,  190,  191,  127,   25,  132,  144,  145,  158,  160,
   60,  164,  166,  222,  169,   90,  170,  171,  197,   25,
  173,  172,  176,  177,  179,  182,  183,  201,  206,  208,
  211,   62,   62,   62,  178,   62,  186,   62,  220,  264,
  235,  236,  200,  233,  242,  266,  243,  300,  231,   62,
   62,   62,   62,  133,   58,   97,   58,  234,   58,  209,
   19,    3,  120,   20,    4,   42,  121,    5,   64,   65,
   59,   22,   58,   58,   58,   58,   16,   43,   44,   94,
   23,   19,    3,   24,   20,    4,   21,   57,    5,  126,
  126,  288,   22,   54,   90,  124,  124,   19,   90,  100,
   20,   23,   64,   65,   24,  147,  148,   19,   62,  108,
   20,   64,   65,   73,   43,   44,  215,   23,   62,  276,
   24,   67,  280,   66,   43,   44,   19,   23,  161,   20,
   24,   19,   52,  194,   20,  195,   19,   62,  251,   20,
  110,   21,   62,   43,   44,  154,   23,   62,   67,   24,
    4,   23,   19,    5,   24,   20,   23,   21,  239,   24,
  141,  123,   19,   62,  202,   20,   42,   21,   77,   78,
   79,   80,   23,   62,  180,   24,   43,   18,   19,   64,
   65,   20,   23,   21,  241,   24,  277,  248,  245,   62,
  246,   19,   64,   65,   20,  302,   19,  311,   23,   20,
  249,   24,   62,  253,   66,  254,  259,   62,  255,  274,
   19,   23,   67,   20,   24,   21,   23,   19,  257,   24,
   20,   62,   21,  258,   19,  260,  265,   20,   62,  227,
   23,   60,  221,   24,  263,   62,   67,   23,  304,   19,
   24,  269,   20,  270,   23,  281,   62,   24,  285,  292,
   62,   83,   81,   82,  296,  272,   62,  267,  136,   23,
  137,  268,   24,   62,   62,   62,   62,  273,   69,   58,
   75,   62,   62,  282,  185,   83,   81,   82,   56,   58,
   56,  284,   56,  287,  290,  291,   58,   58,   58,   58,
  295,  117,  118,  119,   58,   58,   56,   56,   56,   56,
   57,  297,   57,    4,   57,  303,    5,  298,   69,  299,
   93,  305,  130,  307,  134,  308,   67,  310,   57,   57,
   57,   57,  313,  314,  315,  316,   32,   54,   45,   67,
  106,   83,   81,   82,  151,  105,  101,    0,    0,    0,
    4,    0,   67,    5,   83,   81,   82,   64,   65,   67,
    0,    0,    0,    0,    0,    0,    0,   83,   81,   82,
    0,    0,    0,    0,   83,   81,   82,    4,    0,    4,
    5,    0,    5,    0,   64,   65,  112,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  203,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  214,  214,  219,    0,
    0,    0,    0,    0,    0,    0,  214,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  237,
    0,    4,    0,    0,    5,    0,    0,    0,   64,   65,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   56,  214,   64,   65,   77,   78,   79,   80,   84,    0,
    0,  184,    0,    0,    0,    0,    0,    0,    0,    0,
   95,    0,    0,    0,    0,    0,    0,    0,   77,   78,
   79,   80,    0,   56,    0,  283,    0,    0,  113,    0,
  286,    0,    0,   56,    0,    0,    0,    0,    0,  129,
   56,   56,   56,   56,    0,   57,    0,    0,   56,   56,
    0,    0,    0,    0,    0,   57,    0,  204,    0,    0,
    0,    0,   57,   57,   57,   57,  113,  157,    0,  113,
   57,   57,   64,   65,   77,   78,   79,   80,    4,    0,
    0,    5,    0,  128,   76,   64,   65,   77,   78,   79,
   80,    4,    0,    0,    5,    0,    0,    0,   64,   65,
   77,   78,   79,   80,    0,   64,   65,   77,   78,   79,
   80,    0,    0,    0,  122,    0,  124,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  113,  140,  142,
    0,    0,    0,  213,  213,  218,    0,    0,    0,    0,
    0,    0,    0,  213,    0,    0,  152,  153,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  181,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  213,    0,
    0,    0,    0,    0,    0,    0,  196,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  238,    0,  240,
    0,    0,    0,  247,    0,    0,    0,    0,    0,    0,
    0,  250,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  275,    0,    0,
  279,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         21,
   40,   40,   45,   44,   41,   40,   59,   59,   30,   41,
   66,   59,    7,   93,   85,   41,   96,   40,   59,  271,
   49,   40,   40,   44,   41,   41,   41,   59,   41,   20,
  161,  162,   23,   59,   41,   41,   45,   40,   59,  170,
   58,   43,   59,   45,   43,   45,   45,   40,   40,   41,
   45,   14,   59,   59,   43,   40,   45,   59,  256,  130,
   59,   61,   43,  261,   45,  256,   40,  277,   40,  279,
   59,   40,  101,  271,  154,  266,   40,  265,   59,   42,
   59,  256,   45,   41,  215,  258,   49,   41,   44,   44,
  264,  279,   40,  267,  150,  256,  269,  256,   43,  266,
   45,   42,   40,   59,   59,  266,   47,  266,  257,   40,
  259,  102,  103,   45,   44,   60,   61,   62,   40,  257,
  264,  259,  261,  267,   44,  264,   45,  271,  267,   59,
  271,   40,  271,   41,  279,   43,   40,   45,  101,   59,
   12,   41,   61,   15,  166,  167,  168,  169,  219,   59,
   40,   43,   41,   45,   43,   41,   45,   40,  136,  137,
   59,  138,  139,   40,   40,  272,   41,   41,   41,   40,
   40,   40,  265,  164,  265,  197,   40,  271,  141,   40,
   59,   41,   41,   41,  272,  271,   40,  271,  257,   41,
   41,   41,   42,   43,   59,   45,   59,   47,   41,  266,
   41,   41,   59,   59,  257,  227,  259,  259,  256,   59,
   60,   61,   62,  256,   41,  256,   43,   59,   45,  256,
  260,  261,   41,  263,  264,  265,   45,  267,  271,  272,
  269,  271,   59,   60,   61,   62,  271,  277,  278,   40,
  280,  260,  261,  283,  263,  264,  265,  270,  267,  281,
  282,  266,  271,  266,  276,  281,  282,  260,  280,  262,
  263,  280,  271,  272,  283,  281,  282,  260,  271,  262,
  263,  271,  272,  258,  277,  278,   40,  280,  271,  242,
  283,   45,  245,  283,  277,  278,  260,  280,  262,  263,
  283,  260,  271,  257,  263,  259,  260,  271,  256,  263,
  256,  265,  271,  277,  278,   40,  280,  271,   45,  283,
  264,  280,  260,  267,  283,  263,  280,  265,   59,  283,
  258,  269,  260,  271,  256,  263,  256,  265,  273,  274,
  275,  276,  280,  271,  256,  283,  256,  256,  260,  271,
  272,  263,  280,  265,   59,  283,  256,   59,  257,  271,
  259,  260,  271,  272,  263,  259,  260,  256,  280,  263,
  283,  283,  271,   41,  283,   40,   59,  271,   41,  259,
  260,  280,   45,  263,  283,  265,  280,  260,   41,  283,
  263,  271,  265,   41,  260,   59,  266,  263,  271,  265,
  280,   40,   41,  283,   59,  271,   45,  280,  259,  260,
  283,   41,  263,   59,  280,   59,  256,  283,   59,  256,
  271,   60,   61,   62,   59,  259,  266,  266,   43,  280,
   45,  266,  283,  273,  274,  275,  276,  259,   22,  256,
   24,  281,  282,  259,   59,   60,   61,   62,   41,  266,
   43,  259,   45,  266,  266,  266,  273,  274,  275,  276,
  266,  270,  271,  272,  281,  282,   59,   60,   61,   62,
   41,   59,   43,  264,   45,  259,  267,   59,   62,   59,
  271,   59,   66,   59,   68,  266,   45,   59,   59,   60,
   61,   62,   59,   59,   59,  266,  266,   41,   14,   45,
   45,   60,   61,   62,   88,   44,   42,   -1,   -1,   -1,
  264,   -1,   45,  267,   60,   61,   62,  271,  272,   45,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   60,   61,   62,
   -1,   -1,   -1,   -1,   60,   61,   62,  264,   -1,  264,
  267,   -1,  267,   -1,  271,  272,  271,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  146,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  160,  161,  162,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  170,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  183,
   -1,  264,   -1,   -1,  267,   -1,   -1,   -1,  271,  272,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   17,  215,  271,  272,  273,  274,  275,  276,   25,   -1,
   -1,  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   37,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  273,  274,
  275,  276,   -1,  256,   -1,  249,   -1,   -1,   55,   -1,
  254,   -1,   -1,  266,   -1,   -1,   -1,   -1,   -1,   66,
  273,  274,  275,  276,   -1,  256,   -1,   -1,  281,  282,
   -1,   -1,   -1,   -1,   -1,  266,   -1,  256,   -1,   -1,
   -1,   -1,  273,  274,  275,  276,   93,   94,   -1,   96,
  281,  282,  271,  272,  273,  274,  275,  276,  264,   -1,
   -1,  267,   -1,  256,  270,  271,  272,  273,  274,  275,
  276,  264,   -1,   -1,  267,   -1,   -1,   -1,  271,  272,
  273,  274,  275,  276,   -1,  271,  272,  273,  274,  275,
  276,   -1,   -1,   -1,   59,   -1,   61,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  154,   73,   74,
   -1,   -1,   -1,  160,  161,  162,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  170,   -1,   -1,   91,   92,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  123,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  215,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  141,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  192,   -1,  194,
   -1,   -1,   -1,  198,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  206,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  242,   -1,   -1,
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
"comparaciones : comparaciones opLogico comparacion",
"comparaciones : comparacion",
"comparaciones : comparaciones opLogico error",
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

//#line 672 "gramatica.y"


public int yylex() {
    int value = AnalizadorLexico.yylex();
    yylval = new ParserVal(AnalizadorLexico.refTDS); 
    return value;
}

public void yyerror(String string) {
	//AnalizadorSintactico.agregarError("Parser token error: " + string);
	//System.out.println("token error en linea  "+ AnalizadorLexico.numLinea + ": " +string);
}
//#line 653 "Parser.java"
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
//#line 511 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' (Linea " + 				AnalizadorLexico.numLinea + ")");
        if(val_peek(6).obj == null)
            break;
		ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(4).obj, null));
		ParserVal auxElse= new ParserVal(new Nodo("Else", (Nodo)val_peek(2).obj, null));
		ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,(Nodo)auxElse.obj ));
		yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(6).obj, (Nodo)auxCuerpo.obj));
		}
break;
case 92:
//#line 519 "gramatica.y"
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
//#line 527 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'IF' sin 'ELSE' (Linea " + AnalizadorLexico.numLinea + ")");
	                                                    if(val_peek(4).obj == null)
                                                           break;
                                                        ParserVal auxThen= new ParserVal(new Nodo("Then", (Nodo)val_peek(2).obj, null));
                                                        ParserVal auxCuerpo= new ParserVal(new Nodo("Cuerpo",(Nodo)auxThen.obj ,null));
                                                        yyval= new ParserVal(new Nodo("IF", (Nodo)val_peek(4).obj, (Nodo)auxCuerpo.obj));}
break;
case 94:
//#line 533 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta IF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 95:
//#line 534 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 96:
//#line 535 "gramatica.y"
{AnalizadorSintactico.agregarError("Error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 97:
//#line 536 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 98:
//#line 537 "gramatica.y"
{AnalizadorSintactico.agregarError("warning else vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 99:
//#line 538 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 100:
//#line 539 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 101:
//#line 540 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 102:
//#line 541 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta THEN (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 103:
//#line 542 "gramatica.y"
{AnalizadorSintactico.agregarError("warning if vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 104:
//#line 543 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ENDIF (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 105:
//#line 544 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 106:
//#line 548 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
		ParserVal aux = new ParserVal(new Nodo(val_peek(2).sval));
		((Nodo)aux.obj).setTipo("CADENA");
		yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));}
break;
case 107:
//#line 552 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta PRINT (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 108:
//#line 553 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta '(' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 109:
//#line 554 "gramatica.y"
{AnalizadorSintactico.agregarError("Warning print vacio' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 110:
//#line 555 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ')' (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 111:
//#line 556 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta ';' (Linea " + (AnalizadorLexico.numLinea-1) + ")");}
break;
case 112:
//#line 557 "gramatica.y"
{
	       	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
	                           String lexema = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(2).sval);
                               if(lexema != null){
                                  AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                  ParserVal aux = new ParserVal(new Nodo(lexema));
                                  ((Nodo)aux.obj).setTipo("ID");
                                  yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                               }else{
                                   AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                   AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                   /*stop generacion de arbol*/

                               }
	      }
break;
case 113:
//#line 572 "gramatica.y"
{
                	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
                               	                           String lexema = AnalizadorSintactico.getReferenciaPorAmbito(val_peek(2).sval);

                               if(lexema != null){
                                  AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                  ParserVal aux = new ParserVal(new Nodo(lexema));
                                  ((Nodo)aux.obj).setTipo("CTE");
                                  
                                  yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                               }else{
                                  AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                  AnalizadorLexico.tablaDeSimbolos.remove(val_peek(2).sval);
                                  /*stop generacion de arbol*/
                               }
                               }
break;
case 114:
//#line 588 "gramatica.y"
{
                          	           AnalizadorSintactico.agregarAnalisis("sentencia print (Linea " + AnalizadorLexico.numLinea + ")");
                                        String lexema = AnalizadorSintactico.getReferenciaPorAmbito("-"+val_peek(2).sval);

                                         if(lexema != null){
                                            AnalizadorLexico.tablaDeSimbolos.remove("-"+val_peek(2).sval);                                          
                                            ParserVal aux = new ParserVal(new Nodo(lexema));
                                            ((Nodo)aux.obj).setTipo("CTE");
                                            yyval= new ParserVal(new Nodo("PRINT", (Nodo)aux.obj, null));
                                         }else{
                                            AnalizadorSintactico.agregarError("ID no definido (Linea " + AnalizadorLexico.numLinea + ")");
                                            AnalizadorLexico.tablaDeSimbolos.remove("-"+val_peek(2).sval);
                                            /*stop generacion de arbol*/
                                         }
	      }
break;
case 115:
//#line 605 "gramatica.y"
{AnalizadorSintactico.agregarAnalisis("sentencia 'WHILE' (Linea " + AnalizadorLexico.numLinea + ")");
		 if(val_peek(2).obj == null)
             break;
		yyval= new ParserVal(new Nodo("WHILE", (Nodo)val_peek(2).obj, (Nodo)val_peek(0).obj));}
break;
case 116:
//#line 609 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta WHILE (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 117:
//#line 610 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta condicion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 118:
//#line 611 "gramatica.y"
{AnalizadorSintactico.agregarError("error falta DO (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 119:
//#line 612 "gramatica.y"
{AnalizadorSintactico.agregarError("error WHILE vacio (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 120:
//#line 616 "gramatica.y"
{
				if(val_peek(1).obj == null)
					break;
				yyval = new ParserVal(new Nodo("Cond", (Nodo)val_peek(1).obj, null));}
break;
case 121:
//#line 622 "gramatica.y"
{
				if (val_peek(2).obj != null || val_peek(0).obj != null){
					yyval = new ParserVal (new Nodo(val_peek(1).sval,(Nodo)val_peek(0).obj, (Nodo)val_peek(2).obj ));}
			}
break;
case 122:
//#line 626 "gramatica.y"
{yyval=val_peek(0);}
break;
case 123:
//#line 627 "gramatica.y"
{AnalizadorSintactico.agregarError("opLogico de mas (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 124:
//#line 631 "gramatica.y"
{
		  if(val_peek(2).obj == null || val_peek(0).obj == null){
             break;
          }else{
		  yyval = new ParserVal(new Nodo(val_peek(1).sval,(Nodo) val_peek(2).obj,(Nodo)val_peek(0).obj));
		  }
		}
break;
case 125:
//#line 638 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 126:
//#line 639 "gramatica.y"
{AnalizadorSintactico.agregarError("falta expresion (Linea " + AnalizadorLexico.numLinea + ")");}
break;
case 127:
//#line 642 "gramatica.y"
{yyval = new ParserVal(">");}
break;
case 128:
//#line 643 "gramatica.y"
{yyval = new ParserVal("<");}
break;
case 129:
//#line 644 "gramatica.y"
{yyval = new ParserVal("==");}
break;
case 130:
//#line 645 "gramatica.y"
{yyval = new ParserVal("<>");}
break;
case 131:
//#line 646 "gramatica.y"
{yyval = new ParserVal(">=");}
break;
case 132:
//#line 647 "gramatica.y"
{yyval = new ParserVal("<=");}
break;
case 133:
//#line 648 "gramatica.y"
{
	           AnalizadorLexico.listaDeWarnings.add("WARNING Linea " + AnalizadorLexico.numLinea +": se esperaba comparacion ==.");
	           yyval = new ParserVal("==");
	        }
break;
case 134:
//#line 654 "gramatica.y"
{yyval= new ParserVal("&&");}
break;
case 135:
//#line 655 "gramatica.y"
{yyval= new ParserVal("||");}
break;
case 136:
//#line 659 "gramatica.y"
{AnalizadorSintactico.tipoActual = "LONG";
             yyval = new ParserVal("LONG");
            }
break;
case 137:
//#line 662 "gramatica.y"
{AnalizadorSintactico.tipoActual = "SINGLE";
             yyval = new ParserVal("SINGLE");
             }
break;
//#line 1732 "Parser.java"
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
