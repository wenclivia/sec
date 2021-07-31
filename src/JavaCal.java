import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class JavaCal extends JPanel {
    private Font font = new Font(Font.MONOSPACED, Font.BOLD, 14);
    //利用上面第三点介绍的构造函数，我们可以创建一个自定义样式的字体变量f。
//    例如：Font f = new Font("宋体",Font.BOLD,20);
//    上述代码表示我声明了一种自定义字体：宋体、加粗、20pt大小。
    private JPanel jp;//面板
    private double result;
    private boolean start;//判断是否首次输入
    private boolean flag;//判断是否清空显示区的值
    private String lastCommand;//保留运算符
    JTextField display;//显示区域 JTextField为文本框

    public JavaCal() {
//        初始各类值
        result = 0;
        start = true;
        flag = false;
        lastCommand = "=";
//      框架布局  常用的有5种:FlowLayout、BorderLayout、GridLayout、CardLayout、GridBagLayout。一般来说都要放在构造函数或初始化函数中，设置后再加入控件。
        setLayout(new BorderLayout());//或FlowLayout...随后调用add(...)即可。 　　
        display = new JTextField("0.0", 30);//JTextField(String text, int columns) 构造一个用指定文本和列初始化的新TextField。
        display.setFont(font);
        display.setHorizontalAlignment(display.RIGHT);//文本在右边
        display.setEnabled(false);//按钮设置为禁用；

        //初始化清楚用的
        JButton clear = new JButton("clear");//带有文本的按钮
        clear.setFont(font);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //初始操作
                result = 0;
                start = true;
                flag = false;
                lastCommand = "=";
                display.setText("0.0");

            }
        });//接受操作事件的侦听接口
        jp = new JPanel();
        jp.setLayout(new GridLayout(5, 4));

        //实例化监控对象
        NumberAction numAction = new NumberAction();
        CommandAction cmdAction = new CommandAction();
        makeButton("7", numAction);
        makeButton("8", numAction);
        makeButton("9", numAction);
        makeButton("←", numAction);

        makeButton("4", numAction);
        makeButton("5", numAction);
        makeButton("6", numAction);
        makeButton("/", cmdAction);

        makeButton("1", numAction);
        makeButton("2", numAction);
        makeButton("3", numAction);
        makeButton("*", cmdAction);

        makeButton("0", numAction);
        makeButton(".", numAction);
        makeButton("=", cmdAction);
        makeButton("-", cmdAction);

        makeButton("±", cmdAction);
        makeButton("^", cmdAction);
        makeButton("%", cmdAction);
        makeButton("+", cmdAction);

//        将面板，两个按键加入面板，实现界面
        add(display, BorderLayout.NORTH);
        add(jp, BorderLayout.CENTER);
        add(clear,BorderLayout.SOUTH);
    }

    private void makeButton(String buttonName, ActionListener al) {
        JButton jb = new JButton(buttonName);
        jb.setFont(font);
        jp.add(jb);
        jb.addActionListener(al);

    }

    private class NumberAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton jb = (JButton) e.getSource();//获取源对象
            String input = jb.getText();//获取源对象的标签
            if (start) {//首次输入
                if (input.equals(".")) {
                    return;
                }
                if (input.equals("←")) {
                    return;
                }
                //若为0.0，就清零
                if (display.getText().equals("0.0")) {
                    display.setText("");
                }
                start = false;//改变首次输入值标记

            } else {
                if (input.equals("←")) {
                    String s = display.getText();
                    if (Math.abs(Double.valueOf(display.getText())) > 0.0) {
                        s = s.substring(0, s.length() - 1);//s.substring 是截取字符串的意思
//                        截取字符串中一段，一个参数时，参数是指截取的起始位置，两个参数时，第一个是起始位置，第二个是子串的长度
                        if (s.equals("")) {
                            s = "0.0";
                            start = true;
                        }
                        display.setText(s);
                    }
                    return;
                }
                //判断显示区是否有“。”，若又有输入还是“。”就什么不做
                if (display.getText().indexOf(".") != -1) {
                    if (input.equals(".")) {
                        return;
                    }
                }
                //显示区有“-”，又输入“。”。什么不做
                if (display.getText().equals("-")) {
                    if (input.equals(".")) {
                        return;
                    }
                }
                //显示区为“0”，不为“。”，什么不做
                if (display.getText().equals("0")) {
                    if (!input.equals(".")) {
                        return;
                    }
                }
            }
            //点击运算符在输入数字，清空显示区的值
            if (flag) {
                display.setText("");
                flag = false;//还原初始值，不需要清空
            }
            display.setText(display.getText() + input);
        }
    }


    private class CommandAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//actionperformed方法是ActionListener监听事件中的一个重写方法，如果要求按钮执行一些动作，可以在这个方法中添加、修改、删除以及判断。
            JButton jb = (JButton) e.getSource();//获取对象
            String inputCommand = jb.getText();//获得标签
            if (start) {//首次输入
                if (inputCommand.equals("-") || inputCommand.equals("±")) {
                    display.setText("-");//显示区设置为-
                    start = false;//改变首次输入的标记
                }

            } else {
                if (inputCommand.equals("±")) {
                    double x = Double.parseDouble(display.getText());//Double.parseDouble()是把括号里面内容变成double类型的
                    x = -x;
                    display.setText(String.valueOf(x));
                    return;
                }
                if (!flag) {
                    calculator(Double.parseDouble(display.getText()));
                }
                lastCommand = inputCommand;//保存运算符
                flag = true;//之前输入过运算符
            }
        }
    }
    private void calculator(double x){
        if(lastCommand.equals("+")){
            result += x;
        }else  if(lastCommand.equals("-")){
            result -= x;
        }else  if(lastCommand.equals("*")){
            result *= x;
        }else  if(lastCommand.equals("/")){
            result /= x;
        }else  if(lastCommand.equals("%")){
            result %= x;
        }else  if(lastCommand.equals("^")){
            result=Math.pow(result,x);
        }else  if(lastCommand.equals("±")){
            result= -result;
        }else  if(lastCommand.equals("=")){
            result=x;
        }
        display.setText(""+result);
    }
}
class Frame {
    public static void init(JFrame jFrame, Dimension frameSize, String title, String iconFileName, boolean resizable) {
        //系统工具包
        Toolkit tk = Toolkit.getDefaultToolkit();
        //屏幕大小
        Dimension screenSize = tk.getScreenSize();
        //获取宽高
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        //找中心点
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;
        //窗口大小
        int frameWidth = frameSize.width;
        int frameHeight = frameSize.height;
        ;
        //窗体位置
        jFrame.setBounds(centerX - frameWidth / 2, centerY - frameHeight / 2, frameWidth, frameHeight);
        jFrame.setTitle(title);
        if (iconFileName != null) {
            jFrame.setIconImage(tk.getImage(iconFileName));//图标
        }
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(jFrame);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(resizable);// 设置窗体是否可以改变大小
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置程序关闭动作
        jFrame.setVisible(true);// 显示窗体
    }
}

class CalculatorFrame extends JFrame {
    public CalculatorFrame() {
        add(new JavaCal());
        Frame.init(this, new Dimension(800, 600), "简易计算器", null,
                false);
        // 让组件按原始大小显示,并且窗体的大小刚好能够容纳所有的组件
        this.pack();
    }
}

class Calculator {
    public static void main(String[] args) {
        new CalculatorFrame();
    }
}

