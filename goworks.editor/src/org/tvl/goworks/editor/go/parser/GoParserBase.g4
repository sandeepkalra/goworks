/*
 * [The "BSD license"]
 *  Copyright (c) 2012 Sam Harwell
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
parser grammar GoParserBase;

options {
    tokenVocab=GoLexerBase;
}

@header {/*
 * [The "BSD license"]
 *  Copyright (c) 2012 Sam Harwell
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tvl.goworks.editor.go.parser;

import java.util.HashSet;
import java.util.Set;
}

@members {
private final Set<String> packageNames = new HashSet<String>();
private boolean checkPackageNames = true;

public static String getPackageName(Token token) {
    if (token == null) {
        return null;
    }

    String name = token.getText();
    if (name != null && token.getType() == StringLiteral) {
        name = name.substring(name.lastIndexOf('/') + 1);
        if (name.startsWith("\"")) {
            name = name.substring(1);
        }

        if (name.endsWith("\"")) {
            name = name.substring(0, name.length() - 1);
        }
    }

    return name;
}

protected void addPackageName(Token token) {
    String name = getPackageName(token);
    if (name == null || name.isEmpty()) {
        return;
    }

    packageNames.add(name);
}

protected boolean isPackageName(Token token) {
    if (!checkPackageNames) {
        return true;
    }

    return token != null && packageNames.contains(token.getText());
}

public boolean isCheckPackageNames() {
    return checkPackageNames;
}

public void setCheckPackageNames(boolean checkPackageNames) {
    this.checkPackageNames = checkPackageNames;
}
}

type
    :   name=typeName
    |   lit=typeLiteral
    |   lp='(' t=type rp=')'
    ;

typeName
    :   qid=qualifiedIdentifier
    ;

typeLiteral
    :   arrType=arrayType
    |   strType=structType
    |   ptrType=pointerType
    |   fnType=functionType
    |   ifaceType=interfaceType
    |   slcType=sliceType
    |   maptype=mapType
    |   chanType=channelType
    ;

arrayType
    :   '[' len=arrayLength ']' elemType=elementType
    ;

arrayLength
    :   expr=expression
    ;

elementType
    :   typ=type
    ;

sliceType
    :   '[' ']' elemType=elementType
    ;

structType
    :   'struct' '{' (fields+=fieldDecl ';')* fields+=fieldDecl? '}'
    ;

fieldDecl
    :   (idList=identifierList fieldType=type | anonField=anonymousField) fieldTag=tag?
    ;

anonymousField
    :   ptr='*'? fieldType=typeName
    ;

tag
    :   StringLiteral
    ;

pointerType
    :   ptr='*' typ=baseType
    ;

baseType
    :   typ=type
    ;

functionType
    :   'func' sig=signature
    ;

signature
    :   params=parameters res=result?
    ;

result
    :   params=parameters
    |   t=type
    ;

parameters
    :   '(' (params=parameterList ','?)? ')'
    ;

parameterList
    :   params+=parameterDecl (',' params+=parameterDecl)*
    ;

parameterDecl
    :   idList=identifierList? ellip='...'? t=type
    ;

interfaceType
    :   'interface' '{' (methods+=methodSpec (';' methods+=methodSpec)* ';'?)? '}'
    ;

methodSpec
    :   name=methodName sig=signature
    |   ifaceName=interfaceTypeName
    ;

methodName
    :   name=IDENTIFIER
    ;

interfaceTypeName
    :   typName=typeName
    ;

mapType
    :   'map' '[' keyTyp=keyType ']' elemType=elementType
    ;

keyType
    :   t=type
    ;

channelType
    :   (   'chan' '<-'?
        |   '<-' 'chan'
        )
        elemType=elementType
    ;

block
    :   '{' (statements+=statement (';' statements+=statement)* ';'?)? '}'
    ;

declaration
    :   cd=constDecl
    |   td=typeDecl
    |   vd=varDecl
    ;

topLevelDecl
    :   decl=declaration
    |   fndecl=functionDecl
    |   methdecl=methodDecl
    ;

constDecl
    :   'const'
        (   consts+=constSpec
        |   '(' (consts+=constSpec (';' consts+=constSpec)* ';'?)? ')'
        )
    ;

constSpec
    :   idList=identifierList (explicitType=type? '=' valueList=expressionList)?
    ;

identifierList
    :   ids+=IDENTIFIER (',' ids+=IDENTIFIER)*
    ;

expressionList
    :   expressions+=expression (',' expressions+=expression)*
    ;

typeDecl
    :   'type'
        (   types+=typeSpec
        |   '(' (types+=typeSpec (';' types+=typeSpec)* ';'?)? ')'
        )
    ;

typeSpec
    :   name=IDENTIFIER t=type
    ;

varDecl
    :   'var'
        (   vars+=varSpec
        |   '(' (vars+=varSpec (';' vars+=varSpec)* ';'?)? ')'
        )
    ;

varSpec
    :   idList=identifierList
        (   varType=type ('=' exprList=expressionList)?
        |   '=' exprList=expressionList
        )
    ;

shortVarDecl
    :   idList=identifierList ':=' exprList=expressionList
    ;

functionDecl
    :   'func' name=IDENTIFIER sig=signature bdy=body?
    ;

body
    :   blk=block
    ;

methodDecl
    :   'func' recv=receiver name=methodName sig=signature bdy=body?
    ;

receiver
    :   '(' name=IDENTIFIER? ptr='*'? typ=baseTypeName ')'
    ;

baseTypeName
    :   name=IDENTIFIER
    ;

operand
    :   lit=literal
    |   qid=qualifiedIdentifier
    |   methodExpr
    |   '(' expression ')'
    ;

literal
    :   basicLiteral
    |   compositeLiteral
    |   functionLiteral
    ;

basicLiteral
    :   INT_LITERAL
    |   FLOAT_LITERAL
    |   IMAGINARY_LITERAL
    |   CharLiteral
    |   StringLiteral
    ;

qualifiedIdentifier
    :   ({isPackageName(_input.LT(1))}? pkg=packageName dot='.')? id=IDENTIFIER
    ;

methodExpr
    :   recvType=receiverType dot='.' name=methodName
    ;

receiverType
    :   t=typeName
    |   '(' ptr='*' t=typeName ')'
    ;

compositeLiteral
    :   literalType literalValue
    ;

literalType
    :   structType
    |   arrayType
    |   '[' '...' ']' elementType
    |   sliceType
    |   mapType
    |   typeName
    ;

literalValue
    :   '{' (elements=elementList ','?)? '}'
    ;

elementList
    :   elements+=element (',' elements+=element)*
    ;

element
    :   (k=key ':')? v=value
    ;

key
    :   field=fieldName
    |   index=elementIndex
    ;

fieldName
    :   field=IDENTIFIER
    ;

elementIndex
    :   index=expression
    ;

value
    :   expr=expression
    |   lit=literalValue
    ;

functionLiteral
    :   typ=functionType bdy=body
    ;

expression
    :   operand
    |   conv=conversion
        -> conversionOrCallExpr
    |   builtinCall
    |   e=expression dot='.' IDENTIFIER
        -> selectorExpr
    |   e=expression '[' expression ']'
        -> indexExpr
    |   e=expression '[' expression? ':' expression? ']'
        -> sliceExpr
    |   e=expression dot='.' lp='(' t=type rp=')'
        -> typeAssertionExpr
    |   e=expression lp='(' (args=argumentList ','?)? rp=')'
        -> callExpr

    |   ('+' | '-' | '!' | '^' | '*' | '&' | '<-') e=expression
        -> unaryExpr

    |   e=expression ('*' | '/' | '%' | '<<' | '>>' | '&' | '&^') right=expression
        -> multExpr
    |   e=expression ('+' | '-' | '|' | '^') right=expression
        -> addExpr
    |   e=expression ('==' | '!=' | '<' | '<=' | '>' | '>=') right=expression
        -> compareExpr
    |   e=expression '&&' right=expression
        -> andExpr
    |   e=expression '||' right=expression
        -> orExpr
    ;

//primaryExpr
//    :   operand
//    |   conversion
//    |   builtinCall
//    |   primaryExpr '.' IDENTIFIER // -> selector
//    |   primaryExpr '[' expression ']' // -> index
//    |   primaryExpr '[' expression? ':' expression? ']' // -> slice
//    |   primaryExpr '.' '(' type ')' // -> typeAssertion
//    |   primaryExpr '(' (argumentList ','?)? ')' // -> call
//    ;

argumentList
    :   exprs=expressionList ellip='...'?
    ;

conversion
    :   t=type '(' e=expression ')'
    ;

statement
    :   declaration
    |   labeledStmt
    |   simpleStmt
    |   goStmt
    |   returnStmt
    |   breakStmt
    |   continueStmt
    |   gotoStmt
    |   fallthroughStmt
    |   block
    |   ifStmt
    |   switchStmt
    |   selectStmt
    |   forStmt
    |   deferStmt
    ;

simpleStmt
    :   emptyStmt
    |   expressionStmt
    |   sendStmt
    |   incDecStmt
    |   assignment
    |   shortVarDecl
    ;

emptyStmt
    :
    ;

labeledStmt
    :   lbl=label ':' stmt=statement
    ;

label
    :   name=IDENTIFIER
    ;

expressionStmt
    :   expression
    ;

sendStmt
    :   chan=channel '<-' e=expression
    ;

channel
    :   e=expression
    ;

incDecStmt
    :   e=expression op=('++' | '--')
    ;

assignment
    :   targets=expressionList op=assignOp values=expressionList
    ;

assignOp
    :   addAssignOp
    |   mulAssignOp
    |   '='
    ;

addAssignOp
    :   '+=' | '-=' | '|=' | '^='
    ;

mulAssignOp
    :   '*=' | '/=' | '^=' | '<<=' | '>>=' | '&=' | '&^='
    ;

ifStmt
    :   'if' (simpleStmt ';')? expression block ('else' (ifStmt | block))?
    ;

switchStmt
    :   exprSwitchStmt
    |   typeSwitchStmt
    ;

exprSwitchStmt
    :   'switch' (simpleStmt ';')? expression? '{' exprCaseClause* '}'
    ;

exprCaseClause
    :   exprSwitchCase ':' (statement (';' statement)* ';'?)?
    ;

exprSwitchCase
    :   'case' expressionList
    |   'default'
    ;

typeSwitchStmt
    :   'switch' (simpleStmt ';')? typeSwitchGuard '{' typeCaseClause* '}'
    ;

// this should reference primaryExpr, but the ".(type)" extension is unambig
typeSwitchGuard
    :   (id=IDENTIFIER ':=')? e=expression dot='.' '(' 'type' ')'
    ;

typeCaseClause
    :   typeSwitchCase ':' (statement (';' statement)* ';'?)?
    ;

typeSwitchCase
    :   'case' typeList
    |   'default'
    ;

typeList
    :   types+=type (',' types+=type)*
    ;

forStmt
    :   'for' (condition | forClause | rangeClause)? block
    ;

condition
    :   expression
    ;

forClause
    :   initStmt? ';' condition? ';' postStmt?
    ;

initStmt
    :   simpleStmt
    ;

postStmt
    :   simpleStmt
    ;

rangeClause
    :   e1=expression (',' e2=expression)? (eq='=' | defeq=':=') 'range' e=expression
    ;

goStmt
    :   'go' expression
    ;

selectStmt
    :   'select' '{' commClause* '}'
    ;

commClause
    :   commCase ':' (statement (';' statement)* ';'?)?
    ;

commCase
    :   'case' (sendStmt | recvStmt)
    |   'default'
    ;

recvStmt
    :   (expression (',' expression)? ('=' | ':='))? recvExpr
    ;

recvExpr
    :   expression
    ;

returnStmt
    :   'return' expressionList?
    ;

breakStmt
    :   'break' label?
    ;

continueStmt
    :   'continue' label?
    ;

gotoStmt
    :   'goto' label
    ;

fallthroughStmt
    :   'fallthrough'
    ;

deferStmt
    :   'defer' expression
    ;

builtinCall
    :   name=IDENTIFIER '(' (args=builtinArgs ','?)? ')'
    ;

builtinArgs
    :   typeArg=type (',' args=expressionList)?
    |   args=expressionList
    ;

sourceFile
    :   pkg=packageClause ';' (importDecls+=importDecl ';')* (decls+=topLevelDecl ';')*
    ;

packageClause
    :   'package' name=packageName              {addPackageName($name.start);}
    ;

packageName
    :   name=IDENTIFIER
    ;

importDecl
    :   'import' (importSpecs+=importSpec | '(' (importSpecs+=importSpec (';' importSpecs+=importSpec)* ';'?)? ')')
    ;

importSpec
    :   dot='.' path=importPath
    |   name=packageName path=importPath        {addPackageName($name.start);}
    |   path=importPath                         {addPackageName($path.start);}
    ;

importPath
    :   path=StringLiteral
    ;
