package scis.ui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

public class CustomDocumentListener implements DocumentListener {
    private Consumer consumer;

    public CustomDocumentListener(Consumer<DocumentEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        consumer.accept(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        consumer.accept(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        consumer.accept(e);
    }
}
