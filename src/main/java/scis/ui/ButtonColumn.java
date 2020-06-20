package scis.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonColumn extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener {
    private JTable table;
    private JButton renderButton;
    private JButton editButton;
    private String text;
    private ActionListener actionListener;

    public ButtonColumn(JTable table, int column, ActionListener actionListener) {
        super();
        this.table = table;
        renderButton = new JButton();

        editButton = new JButton();
        editButton.setFocusPainted(false);

        editButton.addActionListener(this);
        this.actionListener = actionListener;
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (hasFocus) {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        } else if (isSelected) {
            renderButton.setForeground(table.getSelectionForeground());
            renderButton.setBackground(table.getSelectionBackground());
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }

        renderButton.setText((value == null) ? "" : value.toString());
        return renderButton;
    }

    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {
        text = (value == null) ? "" : value.toString();
        editButton.setText(text);
        return editButton;
    }

    public Object getCellEditorValue() {
        return text;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        actionListener.actionPerformed(e);
    }
}
