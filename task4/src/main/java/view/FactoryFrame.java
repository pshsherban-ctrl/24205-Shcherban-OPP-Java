package view;

import model.*;
import pool.ThreadPool;
import thread.BuildTask;
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
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Главная панель для кастомной отрисовки без единого символа текста
        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                // Задний фон панели
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Рисуем 4 индикатора складов (Кузова, Моторы, Компоненты, Автомобили)
                // Серый — пустой склад, цветной — заполненность
                drawPureBar(g2, 30, 40, bodyStorage.size(), bodyStorage.getCapacity(), Color.CYAN);
                drawPureBar(g2, 30, 100, motorStorage.size(), motorStorage.getCapacity(), Color.ORANGE);
                drawPureBar(g2, 30, 160, accessoryStorage.size(), accessoryStorage.getCapacity(), Color.MAGENTA);
                drawPureBar(g2, 30, 220, autoStorage.size(), autoStorage.getCapacity(), Color.GREEN);

                // Нижний блок: Очередь задач ThreadPool
                // Масштабируем высоту полосы в зависимости от размера очереди
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(30, 400, 390, 20);
                int queueSize = Math.min(threadPool.getTaskCount(), 100);
                int queueWidth = (int) ((queueSize / 100.0) * 390);
                g2.setColor(Color.RED);
                g2.fillRect(30, 400, queueWidth, 20);
            }

            private void drawPureBar(Graphics2D g, int x, int y, int current, int max, Color color) {
                // Рамка (серый контейнер склада)
                g.setColor(Color.GRAY);
                g.fillRect(x, y, 390, 30);

                // Заполнение склада
                if (max > 0) {
                    g.setColor(color);
                    int fillWidth = (int) (((double) current / max) * 390);
                    g.fillRect(x, y, fillWidth, 30);
                }
            }
        };

        // Панель управления скоростями (Ползунки)
        JPanel slidersPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        slidersPanel.setBackground(Color.DARK_GRAY);
        slidersPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        // Добавляем 4 ползунка. В них нет меток и текста — чистая логика значений.
        slidersPanel.add(createPureSlider(100, 4000, 1000, 1)); // Поставщики кузовов
        slidersPanel.add(createPureSlider(100, 4000, 1000, 2)); // Поставщики моторов
        slidersPanel.add(createPureSlider(100, 4000, 1000, 3)); // Поставщики деталей
        slidersPanel.add(createPureSlider(100, 4000, 1000, 4)); // Дилеры

        // Компонуем окно: сверху ползунки, снизу графический Canvas со складами
        setLayout(new BorderLayout());
        add(slidersPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);

        // Таймер перерисовки графики (10 FPS)
        new Timer(100, e -> canvas.repaint()).start();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JSlider createPureSlider(int min, int max, int def, int type) {
        JSlider slider = new JSlider(min, max, def);
        slider.setBackground(Color.DARK_GRAY);
        // КРИТИЧЕСКИ ВАЖНО: Выключаем любые попытки Swing написать цифры на слайдере!
        slider.setPaintLabels(false);
        slider.setPaintTicks(false);

        slider.addChangeListener(e -> {
            int val = slider.getValue();
            if (type == 1) {
                for (Supplier<Body> s : bodySuppliers) s.setDelay(val);
            } else if (type == 2) {
                for (Supplier<Motor> s : motorSuppliers) s.setDelay(val);
            } else if (type == 3) {
                for (Supplier<Accessory> s : accSuppliers) s.setDelay(val);
            } else if (type == 4) {
                for (Dealer d : dealers) d.setDelay(val);
            }
        });
        return slider;
    }
}