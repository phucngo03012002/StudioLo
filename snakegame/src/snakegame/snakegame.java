/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package snakegame;

/**
 *
 * @author HP
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class snakegame extends JFrame implements ActionListener, KeyListener {

    JLabel lb = new JLabel();
    boolean loss = false;
    int BOM, dem = 0;
    int maxXY = 100;
    int m = 20, n = 30;
    int start = 0;
    Color background_cl[] = {Color.black, Color.GREEN, Color.GREEN.darker(), Color.red, Color.WHITE};
    int convertX[] = {-1, 0, 1, 0};
    int convertY[] = {0, 1, 0, -1};
    int speed[] = {100, 75, 50, 25, 1};
    private final JButton bt[][] = new JButton[maxXY][maxXY];
    private final JComboBox lv = new JComboBox();
    private final int a[][] = new int[maxXY][maxXY];
    private final int xSnake[] = new int[maxXY * maxXY];
    private final int ySnake[] = new int[maxXY * maxXY];
    private int xFood, yFood;

    private int sizeSnake = 0;
    private int direction = 0;

    private JButton newGame_bt, score_bt;
    private int xWall, yWall;
    private JPanel pn, pn2;
    Container cn;
    Timer timer;

    public snakegame(String s, int k) {
        super(s);
        cn = init(k);
        timer = new Timer(speed[k], new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runSnake(direction);
            }
        });
    }

    public Container init(int k) {
        Container cn = this.getContentPane();
        pn = new JPanel();
        pn.setLayout(new GridLayout(m, n));
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                bt[i][j] = new JButton();
                pn.add(bt[i][j]);
                bt[i][j].setActionCommand(i + " " + j);
                bt[i][j].addActionListener(this);
                bt[i][j].addKeyListener(this);
                bt[i][j].setBorder(null);
                a[i][j] = 0;
            }
        }

        pn2 = new JPanel();
        pn2.setLayout(new FlowLayout());

        newGame_bt = new JButton("Play");
        newGame_bt.addActionListener(this);
        newGame_bt.addKeyListener(this);
        newGame_bt.setFont(new Font("Arial", 1, 17));

        newGame_bt.setBackground(Color.white);

        score_bt = new JButton("0");
        score_bt.addActionListener(this);
        score_bt.addKeyListener(this);
        score_bt.setFont(new Font("Arial", 1, 17));
        score_bt.setBackground(Color.white);

        for (int i = 1; i <= speed.length; i++) {
            lv.addItem("Mode: " + ((i == 1) ? "Too Hard For You ?" : (i == 2) ? "Weak" : (i == 3) ? "Normal" : (i == 4) ? "Okay You Good" : (i == 5) ? "God ?" : null));
        }
        lv.setSelectedIndex(k);
        lv.addKeyListener(this);
        lv.setFont(new Font("Arial", 1, 17));
        lv.setBackground(Color.white);

        pn2.add(newGame_bt);
        pn2.add(lv);
        pn2.add(score_bt);

        a[m / 2][n / 2 - 1] = 1;
        a[m / 2][n / 2] = 1;
        a[m / 2][n / 2 + 1] = 2;
        xSnake[0] = m / 2;
        ySnake[0] = n / 2 - 1;
        xSnake[1] = m / 2;
        ySnake[1] = n / 2;
        xSnake[2] = m / 2;
        ySnake[2] = n / 2 + 1;
        sizeSnake = 3;

        if (lv.getSelectedIndex() >= 2) {
            creatWall();
        }

        creatFood();
        updateColor();
        cn.add(pn);

        cn.add(pn2, "North");
        this.setVisible(true);
        this.setSize(n * 30, m * 30);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        return cn;
    }

    public void updateColor() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                bt[i][j].setBackground(background_cl[a[i][j]]);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    public void runSnake(int k) {
        a[xSnake[sizeSnake - 1]][ySnake[sizeSnake - 1]] = 1;
        xSnake[sizeSnake] = xSnake[sizeSnake - 1] + convertX[k - 1];
        ySnake[sizeSnake] = ySnake[sizeSnake - 1] + convertY[k - 1];

        if (xSnake[sizeSnake] < 0) {
            xSnake[sizeSnake] = m - 1;
        }
        if (xSnake[sizeSnake] == m) {
            xSnake[sizeSnake] = 0;
        }
        if (ySnake[sizeSnake] < 0) {
            ySnake[sizeSnake] = n - 1;
        }
        if (ySnake[sizeSnake] == n) {
            ySnake[sizeSnake] = 0;
        }

        if (a[xSnake[sizeSnake]][ySnake[sizeSnake]] == 1 || a[xSnake[sizeSnake]][ySnake[sizeSnake]] == 4) {
            timer.stop();
            int result = JOptionPane.showConfirmDialog(null, "Your Score: " + score_bt.getText() + "\nDo you want to play again ?.",
                    "Game Over", JOptionPane.YES_NO_OPTION);

            loss = true;
            if (result == JOptionPane.YES_OPTION) {
                snakegame snakegame = new snakegame("Studio Lỏ - Game Rắn", lv.getSelectedIndex());
                this.dispose();
            } else {
                System.exit(0);
            }
            return;
        }

        a[xSnake[start]][ySnake[start]] = 0;
        if (xFood == xSnake[sizeSnake] && yFood == ySnake[sizeSnake]) {
            a[xSnake[start]][ySnake[start]] = 1;
            start--;

            creatFood();
            if (lv.getSelectedIndex() >= 2) {
                creatWall();
            }

            score_bt.setText(String.valueOf(Integer.parseInt(score_bt.getText()) + 1));

        }

        a[xSnake[sizeSnake]][ySnake[sizeSnake]] = 2;

        start++;
        sizeSnake++;
        updateColor();
        for (int i = start; i < sizeSnake; i++) {
            xSnake[i - start] = xSnake[i];
            ySnake[i - start] = ySnake[i];
        }
        sizeSnake -= start;
        start = 0;
    }

    public void creatWall() {
        int w = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 0) {
                    w++;
                }
            }
        }
        int h = (int) ((w - 1) * Math.random() + 1);
        w = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 0) {
                    w++;
                    if (w == h) {
                        xWall = i;
                        yWall = j;
                        a[i][j] = 4;
                        return;
                    }
                }
            }
        }
    }

    public void creatFood() {
        int k = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 0) {
                    k++;
                }
            }
        }
        int h = (int) ((k - 1) * Math.random() + 1);
        k = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 0) {
                    k++;
                    if (k == h) {
                        xFood = i;
                        yFood = j;
                        a[i][j] = 3;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!loss) {
            // TODO Auto-generated method stub
            if (e.getKeyCode() == e.VK_W && direction != 3) {
                direction = 1;
                timer.start();
            } else if (e.getKeyCode() == e.VK_D && direction != 4) {
                direction = 2;
                timer.start();
            } else if (e.getKeyCode() == e.VK_S && direction != 1) {
                direction = 3;
                timer.start();
            } else if (e.getKeyCode() == e.VK_A && direction != 2) {
                direction = 4;
                timer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getActionCommand().equals(newGame_bt.getText())) {
            snakegame snakegame = new snakegame("Studio Lỏ", lv.getSelectedIndex());
            this.dispose();

        }

    }

}
