/*
 *  Copyright (c) 2012 Sam Harwell, Tunnel Vision Laboratories LLC
 *  All rights reserved.
 *
 *  The source code of this document is proprietary work, and is not licensed for
 *  distribution. For information about licensing, contact Sam Harwell at:
 *      sam@tunnelvisionlabs.com
 */
package org.tvl.goworks.project;

import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Sam Harwell
 */
public class GoProjectLogicalView implements LogicalViewProvider {
    // -J-Dorg.tvl.goworks.project.GoProjectLogicalView.level=FINE
    private static final Logger LOGGER = Logger.getLogger(GoProjectLogicalView.class.getName());

    private final GoProject project;

    public GoProjectLogicalView(GoProject project) {
        this.project = project;
    }

    @Override
    public Node createLogicalView() {
        try {
            //Get the Text directory, creating if deleted
            FileObject root = project.getProjectDirectory();

            //Get the DataObject that represents it
            DataFolder rootDataObject = DataFolder.findFolder(root);

            //Get its default node-we'll wrap our node around it to change the
            //display name, icon, etc
            Node realRootFolderNode = rootDataObject.getNodeDelegate();

            //This FilterNode will be our project node
            return new TextNode(realRootFolderNode, project);

        } catch (DataObjectNotFoundException donfe) {
            LOGGER.log(Level.WARNING, "An exception occurred while opening a project.", donfe);
            //Fallback-the directory couldn't be created -
            //read-only filesystem or something evil happened
            return new AbstractNode(Children.LEAF);
        }
    }

    /** This is the node you actually see in the project tab for the project */
    private static final class TextNode extends FilterNode {

        final GoProject project;

        public TextNode(Node node, GoProject project) throws DataObjectNotFoundException {
            super(node, new FilterNode.Children(node),
                    //The projects system wants the project in the Node's lookup.
                    //NewAction and friends want the original Node's lookup.
                    //Make a merge of both
                    new ProxyLookup(new Lookup[]{Lookups.singleton(project),
                        node.getLookup()
                    }));
            this.project = project;
        }

        @Override
        public Action[] getActions(boolean arg0) {
            Action[] nodeActions = {
                CommonProjectActions.newFileAction(),
                CommonProjectActions.copyProjectAction(),
                CommonProjectActions.deleteProjectAction(),
                CommonProjectActions.setAsMainProjectAction(),
                CommonProjectActions.closeProjectAction(),
            };
            return nodeActions;
        }

        @Override
        public Image getIcon(int type) {
            return ImageUtilities.loadImage("org/tvl/goworks/project/ui/resources/icon1.png");
        }

        @Override
        public Image getOpenedIcon(int type) {
            return getIcon(type);
        }

        @Override
        public String getDisplayName() {
            return project.getProjectDirectory().getName();
        }

    }

    @Override
    public Node findPath(Node root, Object target) {
        //leave unimplemented for now
        return null;
    }

}
