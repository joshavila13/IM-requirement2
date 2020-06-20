package scis.ui;

import javax.swing.event.DocumentEvent;
import java.util.List;

public interface ShowProcessor<T> {
    void showEntities(List<T> entities);
    List<T> getEntities();
    void updateTable();
    void searchAction(DocumentEvent documentEvent);
}
