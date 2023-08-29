/*
 * Created by JFormDesigner on Tue Aug 29 13:35:42 CST 2023
 */

package cn.fhyjs.jntm.command;

import java.awt.*;
import javax.swing.*;

/**
 * @author administer
 */
public class FormScreenBackend extends JFrame {
    public FormScreenBackend() {
        initComponents();
    }
    public FormScreenBackend(int x,int y ,int w,int h){
        this();
        this.setBounds(x,y,w,h);
    }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        label1 = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label1 ----
        label1.setText("HELLO MINECRAFT!");
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 11f));
        contentPane.add(label1);
        label1.setBounds(35, 45, 340, 125);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel label1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
