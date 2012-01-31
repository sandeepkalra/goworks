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
package org.tvl.goworks.editor.go.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.antlr.netbeans.editor.completion.AbstractAnchor;
import org.antlr.netbeans.editor.completion.Anchor;
import org.antlr.netbeans.editor.text.DocumentSnapshot;
import org.antlr.netbeans.editor.text.TrackingPositionRegion;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.netbeans.api.annotations.common.NonNull;
import org.openide.util.Parameters;
import org.tvl.goworks.editor.go.parser.GoParserBase.topLevelDeclContext;

/**
 *
 * @author Sam Harwell
 */
public class GoParserAnchorListener extends BlankGoParserBaseListener {
    private final Stack<Integer> anchorPositions = new Stack<Integer>();
    private final List<Anchor> anchors = new ArrayList<Anchor>();
    private final DocumentSnapshot snapshot;
    private final AtomicBoolean cancel;

    public GoParserAnchorListener(DocumentSnapshot snapshot) {
        Parameters.notNull("snapshot", snapshot);
        this.snapshot = snapshot;
        this.cancel = null;
    }

    public GoParserAnchorListener(DocumentSnapshot snapshot, AtomicBoolean cancel) {
        Parameters.notNull("snapshot", snapshot);
        this.snapshot = snapshot;
        this.cancel = cancel;
    }

    public List<Anchor> getAnchors() {
        return anchors;
    }

    private void checkCancellation() {
        boolean cancelled;
        if (cancel != null) {
            cancelled = cancel.get();
        } else {
            cancelled = Thread.interrupted();
        }

        if (cancelled) {
            throw new CancellationException();
        }
    }

    @Override
    public void enterEveryRule(ParserRuleContext<Token> ctx) {
        checkCancellation();
    }

    @Override
    public void exitEveryRule(ParserRuleContext<Token> ctx) {
        checkCancellation();
    }

    @Override
    public void enterRule(topLevelDeclContext ctx) {
        enterAnchor(ctx);
    }

    @Override
    public void exitRule(topLevelDeclContext ctx) {
        exitAnchor(ctx, ctx.ruleIndex);
    }

    private void enterAnchor(ParserRuleContext<Token> ctx) {
        anchorPositions.push(ctx.getStart().getStartIndex());
    }

    private void exitAnchor(ParserRuleContext<Token> ctx, int anchorId) {
        int start = anchorPositions.pop();
        int stop = ctx.getStop() != null ? ctx.getStop().getStopIndex() + 1 : snapshot.length();
        TrackingPositionRegion.Bias trackingMode = ctx.getStop() != null ? TrackingPositionRegion.Bias.Exclusive : TrackingPositionRegion.Bias.Forward;
        anchors.add(createAnchor(ctx, start, stop, trackingMode, anchorId));
    }

    private Anchor createAnchor(ParserRuleContext<Token> ctx, int start, int stop, TrackingPositionRegion.Bias trackingMode, int rule) {
        TrackingPositionRegion trackingSpan = snapshot.createTrackingRegion(start, stop - start, trackingMode);
            return new TemplateAnchor(trackingSpan, rule);
//        }
    }

    public static class TemplateAnchor extends AbstractAnchor {
        private TemplateAnchor(@NonNull TrackingPositionRegion span, int rule) {
            super(span, rule);
        }

        @Override
        protected List<String> getRuleNames() {
            return Arrays.asList(GoParser.ruleNames);
        }
    }

}
