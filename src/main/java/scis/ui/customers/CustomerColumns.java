package scis.ui.customers;

public enum CustomerColumns {
    ID("custid"),
    NAME("custname"),
    ADDRESS("address"),
    TELL_NO("telno"),
    BALANCE("balance");

    String columnName;

    private CustomerColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
