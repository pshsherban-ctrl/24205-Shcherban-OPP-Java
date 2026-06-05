package view;

import model.*;
import pool.ThreadPool;
import thread.Dealer;
import thread.Supplier;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FactoryFrame extends JFrame {
    private final Storage<Body> bodyStorage;
    private final Storage<Motor> motorStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Auto> autoStorage;
    private final ThreadPool threadPool;

    private final List<Supplier<Body>> bodySuppliers;
    private final List<Supplier<Motor>> motorSuppliers;
    private final List<Supplier<Accessory>> accSuppliers;
    private final List<Dealer> dealers;

    private final JLabel bodyStockLabel;
    private final JLabel motorStockLabel;
    private final JLabel accessoryStockLabel;
    private final JLabel autoStockLabel;

    public FactoryFrame(Storage<Body> b, Storage<Motor> m, Storage<Accessory> acc, Storage<Auto> auto,
                        ThreadPool pool, List<Supplier<Body>> bSups, List<Supplier<Motor>> mSups,
                        List<Supplier<Accessory>> accSups, List<Dealer> dealers) {
        this.bodyStorage = b;
        this.motorStorage = m;
        this.accessoryStorage = acc;
        this.autoStorage = auto;
        this.threadPool = pool;
        this.bodySuppliers = bSups;
        this.motorSuppliers = mSups;
        this.accSuppliers = accSups;
        this.dealers = dealers;

        setTitle("Factory Emulator");
        setSize(500, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel slidersPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        slidersPanel.setBackground(Color.DARK_GRAY);
        slidersPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel bodyPanel = createSupplierPanel("Body Suppliers", 100, 4000, 1000, bodySuppliers, null, null);
        JPanel motorPanel = createSupplierPanel("Motor Suppliers", 100, 4000, 1000, null, motorSuppliers, null);
        JPanel accessoryPanel = createSupplierPanel("Accessory Suppliers", 100, 4000, 1000, null, null, accSuppliers);
        JPanel dealerPanel = createDealerPanel("Dealers", 100, 4000, 1000, dealers);

        slidersPanel.add(bodyPanel);
        slidersPanel.add(motorPanel);
        slidersPanel.add(accessoryPanel);
        slidersPanel.add(dealerPanel);

        bodyStockLabel = createStockLabel();
        motorStockLabel = createStockLabel();
        accessoryStockLabel = createStockLabel();
        autoStockLabel = createStockLabel();

        addStockLabelToPanel(bodyPanel, "Body stock:", bodyStockLabel);
        addStockLabelToPanel(motorPanel, "Motor stock:", motorStockLabel);
        addStockLabelToPanel(accessoryPanel, "Accessory stock:", accessoryStockLabel);
        addStockLabelToPanel(dealerPanel, "Auto stock:", autoStockLabel);

        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(0, 0, getWidth(), getHeight());

                drawPureBar(g2, 30, 40, bodyStorage.size(), bodyStorage.getCapacity(), Color.CYAN);
                drawPureBar(g2, 30, 100, motorStorage.size(), motorStorage.getCapacity(), Color.ORANGE);
                drawPureBar(g2, 30, 160, accessoryStorage.size(), accessoryStorage.getCapacity(), Color.MAGENTA);
                drawPureBar(g2, 30, 220, autoStorage.size(), autoStorage.getCapacity(), Color.GREEN);

                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(30, 300, 390, 20);
                int queueSize = Math.min(threadPool.getTaskCount(), 100);
                int queueWidth = (int) ((queueSize / 100.0) * 390);
                g2.setColor(Color.RED);
                g2.fillRect(30, 300, queueWidth, 20);
            }

            private void drawPureBar(Graphics2D g, int x, int y, int current, int max, Color color) {
                g.setColor(Color.GRAY);
                g.fillRect(x, y, 390, 30);
                if (max > 0) {
                    g.setColor(color);
                    int fillWidth = (int) (((double) current / max) * 390);
                    g.fillRect(x, y, fillWidth, 30);
                }
            }
        };

        setLayout(new BorderLayout());
        add(slidersPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);

        new Timer(100, e -> {
            canvas.repaint();
            bodyStockLabel.setText(String.valueOf(bodyStorage.size()));
            motorStockLabel.setText(String.valueOf(motorStorage.size()));
            accessoryStockLabel.setText(String.valueOf(accessoryStorage.size()));
            autoStockLabel.setText(String.valueOf(autoStorage.size()));
        }).start();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createStockLabel() {
        JLabel label = new JLabel("0");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, 12));
        return label;
    }

    private void addStockLabelToPanel(JPanel panel, String title, JLabel stockLabel) {
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stockPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        stockPanel.add(titleLabel);
        stockPanel.add(stockLabel);
        panel.add(stockPanel);
    }

    private JPanel createSupplierPanel(String title, int min, int max, int def,
                                       List<Supplier<Body>> bodySup,
                                       List<Supplier<Motor>> motorSup,
                                       List<Supplier<Accessory>> accSup) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider slider = new JSlider(min, max, def);
        slider.setBackground(Color.DARK_GRAY);
        slider.setPaintLabels(false);
        slider.setPaintTicks(false);

        JLabel delayLabel = new JLabel("Delay: " + def + " ms");
        delayLabel.setForeground(Color.WHITE);
        delayLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        delayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        slider.addChangeListener(e -> {
            int val = slider.getValue();
            delayLabel.setText("Delay: " + val + " ms");
            if (bodySup != null) {
                for (Supplier<Body> s : bodySup) s.setDelay(val);
            } else if (motorSup != null) {
                for (Supplier<Motor> s : motorSup) s.setDelay(val);
            } else if (accSup != null) {
                for (Supplier<Accessory> s : accSup) s.setDelay(val);
            }
        });

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(slider);
        panel.add(Box.createVerticalStrut(5));
        panel.add(delayLabel);
        return panel;
    }

    private JPanel createDealerPanel(String title, int min, int max, int def, List<Dealer> dealers) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider slider = new JSlider(min, max, def);
        slider.setBackground(Color.DARK_GRAY);
        slider.setPaintLabels(false);
        slider.setPaintTicks(false);

        JLabel delayLabel = new JLabel("Delay: " + def + " ms");
        delayLabel.setForeground(Color.WHITE);
        delayLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        delayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        slider.addChangeListener(e -> {
            int val = slider.getValue();
            delayLabel.setText("Delay: " + val + " ms");
            for (Dealer d : dealers) d.setDelay(val);
        });

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(slider);
        panel.add(Box.createVerticalStrut(5));
        panel.add(delayLabel);
        return panel;
    }
}