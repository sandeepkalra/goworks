/*
 *  Copyright (c) 2012 Sam Harwell, Tunnel Vision Laboratories LLC
 *  All rights reserved.
 *
 *  The source code of this document is proprietary work, and is not licensed for
 *  distribution. For information about licensing, contact Sam Harwell at:
 *      sam@tunnelvisionlabs.com
 */
package org.tvl.goworks.editor.go.formatting;

import org.antlr.netbeans.editor.formatting.CategorySupport;
import org.netbeans.modules.options.editor.spi.PreferencesCustomizer;
import org.openide.util.NbBundle;
import org.tvl.goworks.editor.GoEditorKit;

/**
 *
 * @author Sam Harwell
 */
@NbBundle.Messages({
    "SAMPLE_Comments="
})
public class FormatComments extends javax.swing.JPanel {

    /**
     * Creates new form FormatComments
     */
    public FormatComments() {
        initComponents();

        chkEnableCommentsFormatting.putClientProperty(CategorySupport.OPTION_ID, GoFormatOptions.enableCommentsFormatting);
        chkFormatBlockComments.putClientProperty(CategorySupport.OPTION_ID, GoFormatOptions.formatBlockComments);

        chkAddLeadingStar.putClientProperty(CategorySupport.OPTION_ID, GoFormatOptions.addLeadingStarInComments);
        chkWrapTextAtRightMargin.putClientProperty(CategorySupport.OPTION_ID, GoFormatOptions.wrapTextInComments);
        chkWrapOneLineComments.putClientProperty(CategorySupport.OPTION_ID, GoFormatOptions.wrapOneLineComments);
        chkPreserveNewLines.putClientProperty(CategorySupport.OPTION_ID, GoFormatOptions.preserveNewLinesInComments);
    }

    public static PreferencesCustomizer.Factory getController() {
        return new CategorySupport.Factory(GoEditorKit.GO_MIME_TYPE, "comments", FormatComments.class, //NOI18N
                Bundle.SAMPLE_Comments(), GoPreviewFormatter.INSTANCE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chkEnableCommentsFormatting = new javax.swing.JCheckBox();
        chkFormatBlockComments = new javax.swing.JCheckBox();
        chkAddLeadingStar = new javax.swing.JCheckBox();
        chkWrapTextAtRightMargin = new javax.swing.JCheckBox();
        chkWrapOneLineComments = new javax.swing.JCheckBox();
        chkPreserveNewLines = new javax.swing.JCheckBox();
        lblGeneral = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setName(org.openide.util.NbBundle.getMessage(FormatComments.class, "LBL_Comments")); // NOI18N
        setOpaque(false);

        chkEnableCommentsFormatting.setText("Enable Comments Formatting");

        chkFormatBlockComments.setText("Format Block Comments");

        chkAddLeadingStar.setText("Add Leading Star");

        chkWrapTextAtRightMargin.setText("Wrap Text At Right Margin");

        chkWrapOneLineComments.setText("Wrap One Line Comments");

        chkPreserveNewLines.setText("Preserve New Lines");

        lblGeneral.setText("General");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkEnableCommentsFormatting)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkWrapOneLineComments)
                            .addComponent(chkFormatBlockComments)
                            .addComponent(chkWrapTextAtRightMargin)
                            .addComponent(chkAddLeadingStar)
                            .addComponent(chkPreserveNewLines)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblGeneral)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(chkEnableCommentsFormatting)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkFormatBlockComments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblGeneral)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkAddLeadingStar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkWrapTextAtRightMargin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkWrapOneLineComments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkPreserveNewLines)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkAddLeadingStar;
    private javax.swing.JCheckBox chkEnableCommentsFormatting;
    private javax.swing.JCheckBox chkFormatBlockComments;
    private javax.swing.JCheckBox chkPreserveNewLines;
    private javax.swing.JCheckBox chkWrapOneLineComments;
    private javax.swing.JCheckBox chkWrapTextAtRightMargin;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblGeneral;
    // End of variables declaration//GEN-END:variables
}
