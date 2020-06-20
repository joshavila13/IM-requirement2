package scis.utility;

import scis.Data;
import scis.ui.CreateProcessor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.function.Consumer;

public class AppUtils {

    public static void openDB() {
        Data.setConnection();
    }

    public static void closeDB(Component component) {
        try {
            Data.DbDone();
        } catch (Exception e) {
            AppUtils.showErrorDialog(component, "Error", "An Error Happened While closing the DB connection!");
        }
    }

    public static void createPostProcessor(CreateProcessor processor, String entityName, String errorMessage) {
        boolean isErrorNotEmpty = AppUtils.isStringOk(errorMessage);

        if (isErrorNotEmpty) {
            AppUtils.showErrorDialog((Component) processor, "Error", errorMessage);
        } else {
            AppUtils.showInfoDialog((Component) processor, "Success", String.format("A new %s added to the DB!", entityName));
            processor.success();
        }
    }

    public static void showInfoDialog(Component component, String title, String text) {
        JOptionPane.showMessageDialog(component, text, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorDialog(Component component, String title, String text) {
        JOptionPane.showMessageDialog(component, text, title, JOptionPane.ERROR_MESSAGE);
    }

    public static boolean isStringOk(String str) {
        return !(str == null || str.trim().isEmpty());
    }


    private static String DATE_PATTERN = "MM/dd/yyyy";

    public static String generateRandomString() {
        UUID uniqueKey = UUID.randomUUID();
        String unique = uniqueKey.toString();
        String lastPortionOfUUID = unique.substring(unique.lastIndexOf("-") + 1);
        return lastPortionOfUUID;
    }

    public static String formattedDate(Date date, String pattern) {
        if (date == null || pattern == null || pattern.isEmpty()) {
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String dateString = format.format(date);
        return dateString;
    }

    public static String formattedDate(Date date) {
        return formattedDate(date, DATE_PATTERN);
    }


    public static void showErrors(Component component, String title, List<String> errors) {
        StringBuilder sb = new StringBuilder();
        for (String err : errors) {
            sb.append("• ").append(err).append("\n");
        }
        String errorsStr = sb.toString();
        showErrorDialog(component, title, errorsStr);
    }



    public static boolean confirmDialog(Component component, String question) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(component, question, "Confirmation", dialogButton);
        if (dialogResult == JOptionPane.NO_OPTION || dialogResult == JOptionPane.CLOSED_OPTION) {
            return false;
        }

        return true;
    }

    public static void bootDialog(JFrame owner, JPanel panel, String title, Consumer consumer) {
        Dimension size = panel.getSize();
        JDialog jd = new JDialog(owner, title, true);
        jd.add(panel);
        jd.setSize(size);
        jd.setResizable(false);
        jd.pack();
        jd.setLocationRelativeTo(owner);
        jd.setVisible(true);
        consumer.accept(null);
    }

    public static void replacePanel(JFrame owner, JPanel containerPanel, JPanel newAddingPanel) {
        containerPanel.removeAll();
        newAddingPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        containerPanel.add(newAddingPanel);
        owner.revalidate();
        owner.repaint();
    }

    public static <T> DefaultComboBoxModel<T> getModel(List<T> list) {
        if (list == null || list.isEmpty()) {
            return new DefaultComboBoxModel<T>();
        } else {
            DefaultComboBoxModel<T> model = new DefaultComboBoxModel<T>(new Vector<>(list));
            return model;
        }
    }

    public static void resetComponents(Component... components) {
        for (Component cmp : components) {
            if (cmp instanceof JTextField) {
                ((JTextField) cmp).setText("");
            }
            if (cmp instanceof JTextArea) {
                ((JTextArea) cmp).setText("");
            }

            if (cmp instanceof JLabel) {
                ((JLabel) cmp).setText("");
            }
        }
    }

    public static void removeAll(JTable table) {
        TableModel model = table.getModel();

        if (model instanceof DefaultTableModel) {
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) model).removeRow(i);
            }
        }

        if (model instanceof DefaultListModel)
            ((DefaultListModel) model).removeAllElements();
    }

    public static boolean isListEmpty(List entities) {
        return entities == null || entities.isEmpty();
    }
}
