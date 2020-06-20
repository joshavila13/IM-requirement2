
package scis;


import scis.model.Customers;
import scis.model.Products;
import scis.model.Sales;
import scis.model.SalesDet;
import scis.ui.customers.CustomerColumns;
import scis.ui.products.ProductColumns;
import scis.ui.sales.SalesColumns;
import scis.utility.AppUtils;

import java.sql.*;
import java.util.ArrayList;


/**
 * @author scis
 */
public class Data {
    private static Connection con;
    private static Integer lastid;

    private Data() {
        // private constructor
    }

    public static void setConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String conStr = "jdbc:mysql://localhost:3306/thestore?user=root&password=mysqlmysql";
            con = DriverManager.getConnection(conStr);
            System.out.println("connected");
            lastid = 0;
            setLastCust();
        } catch (Exception e) {
            System.out.println("bad connection");
        }
    }

    public static String saveProduct(Products newP) {
        String s = "";
        PreparedStatement ps, psc;
        String st = "INSERT INTO products(prodid, description, price)  VALUES (?,?,?)";
        try {
            ps = con.prepareStatement(st);
            ps.setString(1, newP.getProdid());
            ps.setString(2, newP.getDescription());
            ps.setDouble(3, newP.getPrice());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
            s = se.getErrorCode() + " " + se.getMessage();
        } catch (Exception e) {
            s = e.getMessage();
        }
        return s;
    }

    public static String saveCustomer(Customers newC) {
        String s = "";
        PreparedStatement ps, psc;
        String st = "INSERT INTO customers(custid, custname, address, telno)  VALUES (?,?,?,?)";
        try {
            lastid = lastid + 1;
            ps = con.prepareStatement(st);
            ps.setInt(1, lastid);
            ps.setString(2, newC.getCustname());
            ps.setString(3, newC.getAddress());
            ps.setString(4, newC.getTelno());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException se) {
            s = se.getErrorCode() + " " + se.getMessage();
        } catch (Exception e) {
            s = e.getMessage();
        }
        return s;
    }

    public static ArrayList<Customers> getCustomers() {
        ArrayList<Customers> ca = new ArrayList<>();
        Customers cust = new Customers();
        try {
            Statement st = con.createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_SENSITIVE);
            ResultSet rs = st.executeQuery("Select * from customers order by custname");
            rs.beforeFirst();
            while (rs.next()) {
                cust = new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5));
                ca.add(cust);
            }
            rs.close();
            st.close();
        } catch (SQLException se) {

        }
        return ca;
    }

    public static ArrayList<Products> getProducts() {
        ArrayList<Products> pa = new ArrayList<>();
        try {
            Statement st = con.createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_SENSITIVE);
            ResultSet rs = st.executeQuery("Select * from products order by description");
            rs.beforeFirst();
            while (rs.next()) {
                Products prod = new Products(rs.getString(1), rs.getString(2), rs.getDouble(3));
                pa.add(prod);
            }
            rs.close();
            st.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return pa;
    }


    public static String saveSales(Sales newS) {

        String s = "";
        PreparedStatement ps, psc;
        String stsa = "INSERT INTO sales(invid, invdate, custid)  VALUES (?,?,?)";
        String stsd = "INSERT INTO salesdetails(invid, prodid, qtysold, unitprice)  VALUES (?,?,?,?)";
        try {
            //update sales header
            ps = con.prepareStatement(stsa);
            //transaction processing
            ps.setString(1, newS.getInvid());
            ps.setDate(2, new java.sql.Date(newS.getInvdate().getTime()));
            ps.setInt(3, newS.getCustid());
            ps.execute();
            //update sales details
            ps.close();
            ps = con.prepareStatement(stsd);
            ArrayList<SalesDet> salesdets = newS.getSalesDet();
            for (SalesDet sdet : salesdets) {
                ps.setString(1, newS.getInvid());
                ps.setString(2, sdet.getProdid());
                ps.setInt(3, sdet.getQtysold());
                ps.setDouble(4, sdet.getUprice());
                ps.execute();
            }
            ps.close();
        } catch (SQLException se) {
            s = se.getErrorCode() + " " + se.getMessage();
        } catch (Exception e) {
            s = e.getMessage();
        }
        return s;
    }

    private static void setLastCust() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select max(custid) from customers");
            rs.beforeFirst();
            while (rs.next()) {
                lastid = rs.getInt(1);
            }
            rs.close();
            st.close();
        } catch (SQLException se) {

        }
    }

    public static Integer getLastid() {
        return lastid;
    }

    public static void DbDone() throws Exception {
        if (con != null) {
            con.close();
            System.out.println("connection closed");
        }
    }

    public static ArrayList<Products> getProducts(String name) {
        return getProducts(name, ProductColumns.DESCRIPTION, Direction.ASC);
    }

    public static ArrayList<Products> getProducts(String name, ProductColumns orderBy, Direction direction) {
        ArrayList<Products> pa = new ArrayList<>();
        boolean isSearchTextEmpty = !AppUtils.isStringOk(name);
        String query;
        if (isSearchTextEmpty) {
            query = String.format("Select * from products order by %s %s", orderBy.getColumnName(), direction.toString());
        } else {
            query = String.format("Select * from products where description LIKE ? OR prodid LIKE ? order by %s %s", orderBy.getColumnName(), direction.toString());
        }

        try {
            PreparedStatement st = con.prepareStatement(query, ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_SENSITIVE);
            if (!isSearchTextEmpty) {
                st.setString(1, "%" + name + "%");
                st.setString(2, "%" + name + "%");
            }

            ResultSet rs = st.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                Products prod = new Products(rs.getString(1), rs.getString(2), rs.getDouble(3));
                pa.add(prod);
            }
            rs.close();
            st.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return pa;
    }

    public static ArrayList<Sales> getSales(String name) {
        return getSales(name, SalesColumns.DATE, Direction.ASC);
    }

    public static ArrayList<Sales> getSales(String name, SalesColumns orderBy, Direction direction) {
        ArrayList<Sales> pa = new ArrayList<>();
        try {

            boolean isSearchTextEmpty = !AppUtils.isStringOk(name);
            String query;
            if (isSearchTextEmpty) {
                query = String.format("Select * from products order by %s %s", orderBy.getColumnName(), direction.toString());
            } else {
                query = String.format("SELECT sc.invid, sc.invdate, cust.custid, cust.custname, (select SUM(sd.unitprice) FROM salesdetails sd WHERE sd.invid = sc.invid) AS amount FROM thestore.sales sc JOIN customers cust USING (custid)" +
                        " WHERE invid LIKE ? ORDER BY %s %s", orderBy.getColumnName(), direction.toString());
            }


            PreparedStatement st = con.prepareStatement(query, ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_SENSITIVE);
            if (!isSearchTextEmpty)
                st.setString(1, "%" + name + "%");

            ResultSet rs = st.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                Customers customers = new Customers(rs.getInt(3), rs.getString(4), null, null, 0);
                Sales sales = new Sales(rs.getString(1), rs.getDate(2), customers);
                sales.setAmount(rs.getDouble(5));
                pa.add(sales);
            }
            rs.close();
            st.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return pa;
    }

    public static ArrayList<Customers> getCustomers(String name) {
        return getCustomers(name, CustomerColumns.NAME, Direction.ASC);
    }

    public static ArrayList<Customers> getCustomers(String name, CustomerColumns orderBy, Direction direction) {
        ArrayList<Customers> ca = new ArrayList<>();
        Customers cust = null;

        try {
            String query;
            boolean isSearchTextEmpty = !AppUtils.isStringOk(name);

            if (isSearchTextEmpty) {
                query = String.format("Select * from customers order by %s %s", orderBy.getColumnName(), direction.toString());
            } else {
                query = String.format("Select * from customers WHERE custname LIKE ? OR address LIKE ? OR telno LIKE ? order by %s %s", orderBy.getColumnName(), direction.toString());
            }

            PreparedStatement st = con.prepareStatement(query);

            if (!isSearchTextEmpty) {
                st.setString(1, "%" + name + "%");
                st.setString(2, "%" + name + "%");
                st.setString(3, "%" + name + "%");
            }

            ResultSet rs = st.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                String namex = rs.getString(2);
                cust = new Customers(rs.getInt(1), namex, rs.getString(3), rs.getString(4), rs.getDouble(5));
                ca.add(cust);
            }
            rs.close();
            st.close();
        } catch (SQLException se) {
        }
        return ca;
    }

    public static ArrayList<Sales> getSales() {
        ArrayList<Sales> pa = new ArrayList<>();
        try {
            Statement st = con.createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_SENSITIVE);
            ResultSet rs = st.executeQuery("SELECT sc.invid, sc.invdate, cust.custid, cust.custname, (select SUM(sd.unitprice) FROM salesdetails sd WHERE sd.invid = sc.invid) AS amount FROM thestore.sales sc JOIN customers cust USING (custid)");
            rs.beforeFirst();
            while (rs.next()) {
                Customers customers = new Customers(rs.getInt(3), rs.getString(4), null, null, 0);
                Sales sales = new Sales(rs.getString(1), rs.getDate(2), customers);
                sales.setAmount(rs.getDouble(5));
                pa.add(sales);
            }
            rs.close();
            st.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return pa;
    }

}