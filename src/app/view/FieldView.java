package app.view;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public interface FieldView extends PropertyChangeListener {

    JButton getButton();

    void setSurroundingMinesCount(int minesCount);

    @Override
    void propertyChange(PropertyChangeEvent evt);

}
