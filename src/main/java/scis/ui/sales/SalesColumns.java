package scis.ui.sales;

public enum SalesColumns {
    ID("invid"),
    DATE("invdate"),
    CUSTOMER_ID("custid");

    String columnName;

    private SalesColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
