
package com.raven.swing;

import javax.swing.JComboBox;

public class ComboBox extends JComboBox{
    
    public ComboBox(){
        setUI(new ModernComboBoxUI());
    }
}
