/*
 *  Copyright (c) 2012 Sam Harwell, Tunnel Vision Laboratories LLC
 *  All rights reserved.
 *
 *  The source code of this document is proprietary work, and is not licensed for
 *  distribution. For information about licensing, contact Sam Harwell at:
 *      sam@tunnelvisionlabs.com
 */
package org.tvl.goworks.editor.go.navigation;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import org.antlr.netbeans.editor.text.DocumentSnapshot;
import org.antlr.netbeans.parsing.spi.BaseParserData;
import org.antlr.netbeans.parsing.spi.ParseContext;
import org.antlr.netbeans.parsing.spi.ParserDataDefinition;
import org.antlr.netbeans.parsing.spi.ParserResultHandler;
import org.antlr.netbeans.parsing.spi.ParserTask;
import org.antlr.netbeans.parsing.spi.ParserTaskDefinition;
import org.antlr.netbeans.parsing.spi.ParserTaskManager;
import org.antlr.netbeans.parsing.spi.ParserTaskProvider;
import org.antlr.netbeans.parsing.spi.ParserTaskScheduler;
import org.antlr.netbeans.parsing.spi.SingletonParserTaskProvider;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.tvl.goworks.editor.GoEditorKit;
import org.tvl.goworks.editor.go.GoParserDataDefinitions;

/**
 *
 * @author Sam Harwell
 */
public final class NavigatorUIVisibleParserTask implements ParserTask {

    private NavigatorUIVisibleParserTask() {
    }

    @Override
    public ParserTaskDefinition getDefinition() {
        return Definition.INSTANCE;
    }

    @Override
    public void parse(ParserTaskManager taskManager, ParseContext context, DocumentSnapshot snapshot, Collection<? extends ParserDataDefinition<?>> requestedData, ParserResultHandler results)
        throws InterruptedException, ExecutionException {

        if (requestedData.contains(GoParserDataDefinitions.NAVIGATOR_UI_VISIBLE)) {
            BaseParserData<Boolean> data = new BaseParserData<Boolean>(context, GoParserDataDefinitions.NAVIGATOR_UI_VISIBLE, snapshot, GoDeclarationsPanel.getInstance() != null);
            results.addResult(data);
        }
    }

    private static final class Definition extends ParserTaskDefinition {
        private static final Collection<ParserDataDefinition<?>> INPUTS =
            Collections.emptyList();
        private static final Collection<? extends ParserDataDefinition<?>> OUTPUTS =
            Collections.singletonList(GoParserDataDefinitions.NAVIGATOR_UI_VISIBLE);

        public static final Definition INSTANCE = new Definition();

        public Definition() {
            super("Navigator UI Visible Update", INPUTS, OUTPUTS, ParserTaskScheduler.MANUAL_TASK_SCHEDULER);
        }
    }

    @MimeRegistration(mimeType=GoEditorKit.GO_MIME_TYPE, service=ParserTaskProvider.class)
    public static final class Provider extends SingletonParserTaskProvider {

        @Override
        public ParserTaskDefinition getDefinition() {
            return Definition.INSTANCE;
        }

        @Override
        public ParserTask createTaskImpl() {
            return new NavigatorUIVisibleParserTask();
        }

    }

}
