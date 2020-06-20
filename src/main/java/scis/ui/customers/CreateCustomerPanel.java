package scis.ui.customers;

import scis.Data;
import scis.model.Customers;
import scis.ui.CreateProcessor;
import scis.utility.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class CreateCustomerPanel extends javax.swing.JPanel implements CreateProcessor {

    private boolean isCustomerCreated = false;

    public CreateCustomerPanel() {
        initComponents();
    }

    private void initComponents() {

        btnSaveCustomer = new javax.swing.JButton();
        lblName = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblTellNo = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtTellNo = new javax.swing.JTextField();
        jspAddress = new javax.swing.JScrollPane();
        txtAddress = new javax.swing.JTextArea();

        btnSaveCustomer.setText("Save");
        btnSaveCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCustomerActionPerformed(evt);
            }
        });

        lblName.setText("Name");

        lblAddress.setText("Address");

        lblTellNo.setText("Tell No");

        txtAddress.setColumns(20);
        txtAddress.setRows(5);
        jspAddress.setViewportView(txtAddress);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lblTellNo, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                                                .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnSaveCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtTellNo)
                                                .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                                                .addComponent(jspAddress)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblName))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtTellNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jspAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btnSaveCustomer))
                                                        .addComponent(lblAddress))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblTellNo)
                                                .addGap(0, 0, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveCustomerActionPerformed(java.awt.event.ActionEvent evt) {
        String name = txtName.getText();
        String tellNo = txtTellNo.getText();
        String address = txtAddress.getText();
        List<String> errors = new ArrayList<String>();
        if (!AppUtils.isStringOk(name)) {
            errors.add("Your [Name] field is empty!");
        }

        if (!AppUtils.isStringOk(tellNo)) {
            errors.add("Your [Tell No] field is empty!");
        }

        if (!AppUtils.isStringOk(address)) {
            errors.add("Your [Address] field is empty!");
        }

        if (!errors.isEmpty()) {
            AppUtils.showErrors(this, "Errors", errors);
            return;
        }

        AppUtils.openDB();
        Customers customer = new Customers(Data.getLastid(), name, address, tellNo, -1);
        String errorMessage = Data.saveCustomer(customer);
        AppUtils.createPostProcessor(this, "customer", errorMessage);
        AppUtils.closeDB(this);
        resetEverything();
    }

    public boolean isCustomerCreated() {
        return isCustomerCreated;
    }

    @Override
    public void success() {
        isCustomerCreated = true;
    }

    @Override
    public void resetEverything() {
        AppUtils.resetComponents(txtName, txtTellNo, txtAddress);
    }

    private javax.swing.JButton btnSaveCustomer;
    private javax.swing.JScrollPane jspAddress;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblTellNo;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTellNo;
}
