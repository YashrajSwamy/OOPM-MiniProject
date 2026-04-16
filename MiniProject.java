import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

class MiniProject {
    public static void main(String[] args) {
        new Mainscreen();
    }
}

class FoodItem {
    String name;
    int price;
    int quantity;

    FoodItem(String name, int price) {
        this.name = name;
        this.price = price;
        this.quantity = 0;
    }
}

class Mainscreen {

    JFrame f = new JFrame();
    JPanel container = new JPanel();
    JScrollPane scrollPane = new JScrollPane(container);

    Mainscreen() {
        f.setTitle("Prathamesh and Yashraj Shop Register");
        f.setSize(800, 1000);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout());

        container.setBackground(Color.LIGHT_GRAY);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        f.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.setPreferredSize(new Dimension(300, 50));

        JButton addButton    = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> new Customer().openCustomerDialog(f, container));

        deleteButton.addActionListener(e -> {
            Component[] components = container.getComponents();
            for (int i = 0; i < components.length; i++) {
                JPanel card = (JPanel) components[i];
                JCheckBox cb = findCheckBox(card);
                if (cb != null && cb.isSelected()) {
                    container.remove(card);
                }
            }
            container.revalidate();
            container.repaint();
        });

        f.add(buttonPanel, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    JCheckBox findCheckBox(Container parent) {
        Component[] comps = parent.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JCheckBox) return (JCheckBox) comps[i];
            if (comps[i] instanceof Container) {
                JCheckBox found = findCheckBox((Container) comps[i]);
                if (found != null) return found;
            }
        }
        return null;
    }
}

class Customer {

    public void openCustomerDialog(JFrame parent, JPanel container) {
        JDialog d = new JDialog(parent, "Add Customer", true);
        d.setLayout(new GridLayout(4, 0, 5, 5));
        d.setLocationRelativeTo(parent);
        d.setSize(400, 500);
        d.setResizable(false);

        // Name
        JLabel nameLabel = new JLabel("Customer Name");
        JTextField getname = new JTextField(15);
        JPanel namePnl = new JPanel(new FlowLayout());
        namePnl.add(nameLabel);
        namePnl.add(getname);
        d.add(namePnl);

        // Contact
        JLabel contactLabel = new JLabel("Contact");
        JTextField getcontact = new JTextField(15);
        getcontact.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });
        JPanel contactPnl = new JPanel(new FlowLayout());
        contactPnl.add(contactLabel);
        contactPnl.add(getcontact);
        d.add(contactPnl);

        // Address
        JLabel addressLabel = new JLabel("Address");
        JTextArea getaddress = new JTextArea(3, 15);
        JScrollPane addressScroll = new JScrollPane(getaddress);
        JPanel addressPnl = new JPanel(new FlowLayout());
        addressPnl.add(addressLabel);
        addressPnl.add(addressScroll);
        d.add(addressPnl);

        // Buttons
        JButton addBtn    = new JButton("Add");
        JButton cancelBtn = new JButton("Cancel");
        JPanel btnPnl = new JPanel(new FlowLayout());
        btnPnl.add(addBtn);
        btnPnl.add(cancelBtn);
        d.add(btnPnl);

        addBtn.addActionListener(e -> {
            String name    = getname.getText().trim();
            String contact = getcontact.getText().trim();
            String address = getaddress.getText().trim();

            if (name.isEmpty() || contact.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(d, "Please fill all fields!");
                return;
            }
            d.dispose();
            new Dashboard(parent, container, name, contact, address);
        });

        cancelBtn.addActionListener(e -> d.dispose());
        d.setVisible(true);
    }
}

class Dashboard extends JFrame {

    FoodItem[] items = {
        new FoodItem("Vada Pav",      16),
        new FoodItem("Samosa Pav",    20),
        new FoodItem("Misal Pav",     50),
        new FoodItem("Bhaji Pav",     20),
        new FoodItem("Upma",          35),
        new FoodItem("Poha",          35),
        new FoodItem("Sabudana Vada", 40),
        new FoodItem("Idli Sambar",   30),
        new FoodItem("Medu Vada",     40),
        new FoodItem("Sada Dosa",     40),
        new FoodItem("Masala Dosa",   50),
    };

    String[] imagePaths = {
        "imgs\\vadapav.jpg",     "imgs\\samosapav.jpg",
        "imgs\\misalpav.jpg",    "imgs\\bhajipav.jpg",
        "imgs\\upma.jpg",        "imgs\\poha.jpg",
        "imgs\\sabudanavada.jpg",
        "imgs\\idlisambar.jpg",  "imgs\\meduvada.jpg",
        "imgs\\sadadosa.jpg",    "imgs\\masaladosa.jpg",
    };

    JButton[] buttons = new JButton[items.length];
    DefaultTableModel tableModel;
    JTable cart;

    JFrame  mainFrame;
    JPanel  mainContainer;
    String  custName, custContact, custAddress;

    Dashboard(JFrame mainFrame, JPanel mainContainer,
                   String name, String contact, String address) {

        this.mainFrame     = mainFrame;
        this.mainContainer = mainContainer;
        this.custName      = name;
        this.custContact   = contact;
        this.custAddress   = address;

        setTitle("Order - " + name);
        setSize(1000, 620);
        setLocationRelativeTo(mainFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Customer info banner
        JPanel banner = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        banner.setBackground(new Color(230, 245, 255));
        JLabel bannerLabel = new JLabel("Customer: " + name + "   |   Contact: " + contact + "   |   Address: " + address);
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 13));
        banner.add(bannerLabel);
        add(banner, BorderLayout.NORTH);

        // Item panel
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBackground(Color.WHITE);

        itemPanel.add(sectionLabel("Fast Food"));
        JPanel ffPanel = flowPanel();
        for (int i = 0; i <= 3; i++) ffPanel.add(makeBtn(i));
        itemPanel.add(ffPanel);

        itemPanel.add(sectionLabel("Snacks"));
        JPanel snPanel = flowPanel();
        for (int i = 4; i <= 6; i++) snPanel.add(makeBtn(i));
        itemPanel.add(snPanel);

        itemPanel.add(sectionLabel("South Indian"));
        JPanel siPanel = flowPanel();
        for (int i = 7; i <= 10; i++) siPanel.add(makeBtn(i));
        itemPanel.add(siPanel);

        add(new JScrollPane(itemPanel), BorderLayout.CENTER);

        // Cart panel
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Item");
        tableModel.addColumn("Price");
        tableModel.addColumn("Qty");
        tableModel.addColumn("Total");

        cart = new JTable(tableModel);
        cart.setEnabled(false);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setPreferredSize(new Dimension(350, 0));
        cartPanel.add(new JScrollPane(cart), BorderLayout.CENTER);

        JButton billBtn  = new JButton("Generate Bill");
        JButton clearBtn = new JButton("Clear Cart");
        JPanel  bottomPnl = new JPanel();
        bottomPnl.add(billBtn);
        bottomPnl.add(clearBtn);
        cartPanel.add(bottomPnl, BorderLayout.SOUTH);

        add(cartPanel, BorderLayout.EAST);

        // Button clicks - add qty to cart
        for (int i = 0; i < items.length; i++) {
            int idx = i;
            buttons[i].addActionListener(e -> {
                items[idx].quantity++;
                updateCart();
            });
        }

        billBtn.addActionListener(e  -> generateBill());
        clearBtn.addActionListener(e -> clearCart());

        setVisible(true);
    }

    JButton makeBtn(int i) {
        JButton btn;
        ImageIcon icon = new ImageIcon(imagePaths[i]);
        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            btn = new JButton(items[i].name + " Rs." + items[i].price, new ImageIcon(img));
            btn.setHorizontalTextPosition(JButton.CENTER);
            btn.setVerticalTextPosition(JButton.BOTTOM);
        } else {
            btn = new JButton(items[i].name + " Rs." + items[i].price);
        }
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        buttons[i] = btn;
        return btn;
    }

    JPanel flowPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        p.setBackground(Color.WHITE);
        return p;
    }

    JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 16));
        l.setBorder(new EmptyBorder(10, 10, 2, 0));
        return l;
    }

    void updateCart() {
        tableModel.setRowCount(0);
        int total = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i].quantity > 0) {
                int t = items[i].quantity * items[i].price;
                total += t;
                tableModel.addRow(new Object[]{items[i].name, items[i].price, items[i].quantity, t});
            }
        }
        tableModel.addRow(new Object[]{"", "", "Total", total});
    }

    void clearCart() {
        for (int i = 0; i < items.length; i++) items[i].quantity = 0;
        tableModel.setRowCount(0);
    }

    void generateBill() {
        int total = 0;
        String itemLines = "";
        for (int i = 0; i < items.length; i++) {
            if (items[i].quantity > 0) {
                int t = items[i].quantity * items[i].price;
                total += t;
                itemLines += items[i].name + " x " + items[i].quantity + " = Rs." + t + "\n";
            }
        }

        if (total == 0) {
            JOptionPane.showMessageDialog(this, "Cart is empty! Please add items first.");
            return;
        }

        String date = LocalDate.now().toString();
        int finalTotal = total;
        String finalItemLines = itemLines;

        // Add bill card to main screen
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(775, 100));
        card.setMaximumSize(new Dimension(775, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            new EmptyBorder(5, 15, 5, 15)
        ));

        // Left side - name and date
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setBackground(Color.WHITE);
        JLabel nameInfo = new JLabel(custName);
        nameInfo.setFont(new Font("Arial", Font.BOLD, 14));
        leftPanel.add(nameInfo);
        leftPanel.add(new JLabel(date + "  |  " + custContact));
        card.add(leftPanel, BorderLayout.WEST);

        // Right side - total and checkbox
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        innerPanel.setBackground(Color.WHITE);
        JLabel totalLabel = new JLabel("Rs." + finalTotal);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 15));
        JCheckBox checkBox = new JCheckBox();
        checkBox.setBackground(Color.WHITE);
        innerPanel.add(totalLabel);
        innerPanel.add(checkBox);
        rightPanel.add(innerPanel);
        card.add(rightPanel, BorderLayout.EAST);

        mainContainer.add(card);
        mainContainer.revalidate();
        mainContainer.repaint();

        // Show bill dialog
        JDialog billDialog = new JDialog(this, "Bill", true);
        billDialog.setSize(400, 500);
        billDialog.setLayout(new BorderLayout());
        billDialog.setLocationRelativeTo(this);

        String billText = "Customer : " + custName + "\n"
                        + "Contact  : " + custContact + "\n"
                        + "Address  : " + custAddress + "\n"
                        + "Date     : " + date + "\n"
                        + "-------------------------\n"
                        + finalItemLines
                        + "-------------------------\n"
                        + "TOTAL    : Rs." + finalTotal;

        JTextArea billArea = new JTextArea(billText);
        billArea.setEditable(false);
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        billDialog.add(new JScrollPane(billArea), BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        JPanel okPanel = new JPanel();
        okPanel.add(okButton);
        billDialog.add(okPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            billDialog.dispose();
            dispose();
        });

        billDialog.setVisible(true);
    }
}
