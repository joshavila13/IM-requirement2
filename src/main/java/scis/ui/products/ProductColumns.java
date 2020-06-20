package scis.ui.products;

public enum ProductColumns {
    ID("prodid"),
    DESCRIPTION("description"),
    PRICE("price");

    String columnName;

    private ProductColumns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
