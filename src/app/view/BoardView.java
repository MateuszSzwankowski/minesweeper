package app.view;

import app.MinesweeperConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


class BoardView {

    private final JPanel innerPanel;
    private final JScrollPane panel;
    private FieldView[][] fieldViews;

    BoardView(MinesweeperConfig config) {
        int numColumns = config.getNumColumns();
        int numRows = config.getNumRows();
        fieldViews = new FieldView[numRows][numColumns];
        innerPanel = new JPanel();
        innerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        innerPanel.setLayout(new GridLayout(numRows, numColumns));

        makeFieldButtons(numRows, numColumns);

        panel = new JScrollPane(innerPanel);
        panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void makeFieldButtons(int numRows, int numColumns) {
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numColumns; column++) {
                FieldView fieldView = new DefaultFieldView();
                fieldViews[row][column] = fieldView;
                innerPanel.add(fieldView.getButton());
            }
        }
    }

    boolean isScrollBarNeeded() {
        return (panel.getVerticalScrollBar().isVisible() ||
                panel.getHorizontalScrollBar().isVisible());
    }

    JComponent getPanel() {
        return panel;
    }

    FieldView[][] getFieldViews() {
        return fieldViews;
    }
}
