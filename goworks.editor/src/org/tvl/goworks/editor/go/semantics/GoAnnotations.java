/*
 *  Copyright (c) 2012 Sam Harwell, Tunnel Vision Laboratories LLC
 *  All rights reserved.
 *
 *  The source code of this document is proprietary work, and is not licensed for
 *  distribution. For information about licensing, contact Sam Harwell at:
 *      sam@tunnelvisionlabs.com
 */
package org.tvl.goworks.editor.go.semantics;

import java.util.Collection;
import org.antlr.netbeans.semantics.ObjectProperty;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.tvl.goworks.editor.go.codemodel.CodeElementModel;
import org.tvl.goworks.editor.go.codemodel.TypeKind;
import org.tvl.goworks.editor.go.codemodel.VarKind;

/**
 *
 * @author Sam Harwell
 */
public class GoAnnotations {
    public static final ObjectProperty<Boolean> ATTR_LITERAL = new ObjectProperty<>("literal", false);
    public static final ObjectProperty<Boolean> BUILTIN = new ObjectProperty<>("builtin", false);
    public static final ObjectProperty<NodeType> NODE_TYPE = new ObjectProperty<>("nodetype", NodeType.UNDEFINED);
    public static final ObjectProperty<VarKind> VAR_TYPE = new ObjectProperty<>("vartype", VarKind.UNDEFINED);
    public static final ObjectProperty<TypeKind> TYPE_KIND = new ObjectProperty<>("typekind", TypeKind.UNDEFINED);
    public static final ObjectProperty<Boolean> GLOBAL = new ObjectProperty<>("global", false);
    public static final ObjectProperty<TerminalNode> LOCAL_TARGET = new ObjectProperty<>("local-target");
    public static final ObjectProperty<Boolean> RESOLVED = new ObjectProperty<>("resolved", false);
    public static final ObjectProperty<Boolean> QUALIFIED_EXPR = new ObjectProperty<>("qualified-expr", false);
    public static final ObjectProperty<ParserRuleContext> QUALIFIER = new ObjectProperty<>("qualifier");
    public static final ObjectProperty<TerminalNode> UNQUALIFIED_LINK = new ObjectProperty<>("unqualified-link");
    public static final ObjectProperty<Collection<? extends CodeElementModel>> MODELS = new ObjectProperty<>("models");
    public static final ObjectProperty<Boolean> POINTER_RECEIVER = new ObjectProperty<>("pointer-receiver", false);
    public static final ObjectProperty<Boolean> VARIADIC = new ObjectProperty<>("variadic", false);

    public static final ObjectProperty<ParserRuleContext> EXPLICIT_TYPE = new ObjectProperty<>("explicit-type");
    public static final ObjectProperty<ParserRuleContext> IMPLICIT_TYPE = new ObjectProperty<>("implicit-type");
    public static final ObjectProperty<Integer> IMPLICIT_INDEX = new ObjectProperty<>("implicit-index", -1);

    public static final ObjectProperty<String> UNEVALUATED_CONSTANT = new ObjectProperty<>("unevaluated-constant");
    public static final ObjectProperty<Object> EVALUATED_CONSTANT = new ObjectProperty<>("evaluated-constant");

    public static final ObjectProperty<CodeElementReference> EXPR_TYPE = new ObjectProperty<>("expression-type", CodeElementReference.MISSING);
    public static final ObjectProperty<CodeElementReference> CODE_CLASS = new ObjectProperty<>("code-type", CodeElementReference.MISSING);

    public static final ObjectProperty<CodeElementReference> PROP_ELEMENT_REFERENCE =
        new ObjectProperty<>("element-reference");
}
