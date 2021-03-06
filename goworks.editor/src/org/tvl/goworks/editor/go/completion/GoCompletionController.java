/*
 *  Copyright (c) 2012 Sam Harwell, Tunnel Vision Laboratories LLC
 *  All rights reserved.
 *
 *  The source code of this document is proprietary work, and is not licensed for
 *  distribution. For information about licensing, contact Sam Harwell at:
 *      sam@tunnelvisionlabs.com
 */
package org.tvl.goworks.editor.go.completion;

import com.tvl.spi.editor.completion.CompletionItem;
import com.tvl.spi.editor.completion.CompletionProvider;
import com.tvl.spi.editor.completion.CompletionTask;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.antlr.works.editor.antlr4.completion.BaseCompletionController;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;
import org.openide.util.Lookup;

/**
 *
 * @author Sam Harwell
 */
public class GoCompletionController extends BaseCompletionController {
    private final GoCompletionProvider provider;

    public GoCompletionController(JTextComponent component, List<? extends CompletionTask> tasks, List<Integer> queryTypes) {
        super(component, tasks, queryTypes);
        Lookup lookup = MimeLookup.getLookup(DocumentUtilities.getMimeType(component));
        if (lookup != null) {
            Collection<? extends CompletionProvider> providers = lookup.lookupAll(CompletionProvider.class);
            GoCompletionProvider goCompletionProvider = null;
            for (CompletionProvider completionProvider : providers) {
                if (completionProvider instanceof GoCompletionProvider) {
                    goCompletionProvider = (GoCompletionProvider)completionProvider;
                    break;
                }
            }
            this.provider = goCompletionProvider;
        } else {
            this.provider = null;
        }
    }

    @Override
    public void processKeyEvent(KeyEvent evt, CompletionItem bestMatch, boolean isSelected) {
        if (provider != null) {
            if (isSelected && evt.getID() == KeyEvent.KEY_PRESSED && provider.getCompletionSelectors().indexOf(evt.getKeyChar()) >= 0) {
                evt.consume();
                defaultAction(bestMatch, isSelected);
                return;
            }
        }

        super.processKeyEvent(evt, bestMatch, isSelected);
    }

}
