import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class OthelloMenu extends JFrame implements ActionListener {

    JPanel contentPane;
    CardLayout layout;
    Map<Integer, String[]> users = new HashMap<>();
    String[] blackUser = new String[4];
    String[] whiteUser = new String[4];

    public static void main(String[] args) {
        OthelloMenu frame = new OthelloMenu();
        frame.setTitle("Othello");
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        fileIO();
    }

    public OthelloMenu() {

        /*
        settings -firstWindow
         */
        FirstWindow panel1 = new FirstWindow();
        panel1.setUsers(users);
        JButton userCreate = new JButton("User Create");
        userCreate.addActionListener(e -> {
            //panel1.setUserCreateModeSwitch();
            layout.show(contentPane,"userCreatePanel");
        });
        JButton userSelect = new JButton("User Select");
        userSelect.addActionListener(e -> {
            //panel1.setUserCreateModeSwitch();
            layout.show(contentPane,"userSelectPanel");
        });
        panel1.add(userCreate);
        panel1.add(userSelect);


        /*
        settings -userCreate
         */
        JPanel userCreatePanel = new JPanel();
        JTextField text = new JTextField(20);
        JButton createButton = new JButton("Create");
        JButton backFirstWindow1 = new JButton("Back");
        backFirstWindow1.addActionListener(e -> {
            userIn();
            panel1.setUsers(users);
            layout.show(contentPane,"firstWindow");
        });
        JPopupMenu popup1 = new JPopupMenu();
        JMenuItem createdPop = new JMenuItem();
        createdPop.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        createdPop.setText("User Create Success!");
        createdPop.setBackground(Color.pink);
        popup1.add(createdPop);
        createButton.addActionListener(e -> {
            //System.out.println(text.getText());
            try{
                File file = new File("src/main/resources/data.in");
                FileWriter filewriter = new FileWriter(file, true);
                filewriter.write(text.getText()+",0,0,0\n");
                filewriter.close();
            }catch(IOException ex){
                System.out.println(ex);
            }
            String[] temp = new String[4];
            String temp1 = text.getText();
            temp[0] = temp1;
            temp[1] = "0";
            temp[2] = "0";
            temp[3] = "0";
            users.put(users.size(),temp);
            popup1.show(contentPane,150,0);
            userIn();
            panel1.setUsers(users);
            layout.show(contentPane,"firstWindow");
        });
        userCreatePanel.add(text);
        userCreatePanel.add(createButton);
        userCreatePanel.add(backFirstWindow1);

        /*
        settings -userSelect
         */
        AtomicBoolean existBlack = new AtomicBoolean(false);
        AtomicBoolean existWhite = new AtomicBoolean(false);
        JPanel userSelectPanel = new JPanel();
        userSelectPanel.setBounds(50,50,400,400);
        JButton toGame = new JButton("Game Start");
        JButton backFirstWindow2 = new JButton("Back");
        backFirstWindow2.addActionListener(e -> {
            userIn();
            panel1.setUsers(users);
            layout.show(contentPane,"firstWindow");
        });
        toGame.setVisible(false);
        JButton selectButton1 = new JButton("Black select");
        JButton selectButton2 = new JButton("White select");
        JTextField text1 = new JTextField(20);
        JTextField text2 = new JTextField(20);
        userSelectPanel.add(text1);
        userSelectPanel.add(selectButton1);
        userSelectPanel.add(text2);
        userSelectPanel.add(selectButton2);

        JLabel msg = new JLabel("You should chose black and white correctly.");
        JLabel blank = new JLabel("");
        JLabel msg01 = new JLabel("Then, You can play Othello.");
        msg.setForeground(Color.BLUE);
        msg01.setForeground(Color.BLUE);
        msg.setFont(new Font("Arial", Font.PLAIN, 25));
        msg01.setFont(new Font("Arial",Font.PLAIN,25));
        msg.setHorizontalAlignment(JLabel.CENTER);
        msg01.setHorizontalAlignment(JLabel.CENTER);
        JLabel msg1 = new JLabel("Black OK");
        msg1.setFont(new Font("Arial", Font.PLAIN, 20));
        msg1.setPreferredSize(new Dimension(500, 100));
        msg1.setHorizontalAlignment(JLabel.CENTER);
        msg1.setForeground(Color.BLUE);
        msg1.setVisible(false);
        JLabel msg2 = new JLabel("White OK");
        msg2.setFont(new Font("Arial", Font.PLAIN, 20));
        msg2.setPreferredSize(new Dimension(500, 110));
        msg2.setHorizontalAlignment(JLabel.CENTER);
        msg2.setForeground(Color.BLUE);
        msg2.setVisible(false);

        userSelectPanel.add(msg);
        userSelectPanel.add(blank);
        userSelectPanel.add(msg01);
        userSelectPanel.add(msg1);
        userSelectPanel.add(msg2);

        selectButton1.addActionListener(e -> {
            //System.out.println(text1.getText());
            existBlack.set(findUser(text1.getText(),1));
            if(existBlack.get()){
                msg1.setVisible(true);
                msg.setVisible(false);
                msg01.setVisible(false);
            }
            if(existBlack.get() && existWhite.get()){
               toGame.setVisible(true);
               selectButton1.setVisible(false);
               selectButton2.setVisible(false);
               text1.setVisible(false);
               text2.setVisible(false);
            }
        });

        userSelectPanel.add(toGame);
        CustomGameView gamePanel = new CustomGameView();
        toGame.addActionListener(e -> {
            gamePanel.setUsers(users);
            gamePanel.setBlackUser(blackUser);
            gamePanel.setWhiteUser(whiteUser);
            layout.show(contentPane,"gamePanel");
        });

        selectButton2.addActionListener(e -> {
            //System.out.println(text2.getText());
            existWhite.set(findUser(text2.getText(),2));
            if(existWhite.get()){
                msg2.setVisible(true);
                msg.setVisible(false);
                msg01.setVisible(false);
            }
            if(existBlack.get() && existWhite.get()){
                toGame.setVisible(true);
                selectButton1.setVisible(false);
                selectButton2.setVisible(false);
                text1.setVisible(false);
                text2.setVisible(false);
            }
        });

        userSelectPanel.add(backFirstWindow2);

        /*
        settings -gamePanel
         */
        JButton backFirst = new JButton("Back");
        backFirst.addActionListener(e -> {
            gamePanel.reset();
            userIn();
            panel1.setUsers(users);
            layout.show(contentPane,"firstWindow");
        });
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> gamePanel.reset());
        gamePanel.add(backFirst);
        gamePanel.add(btnReset);
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                gamePanel.mouseReleased(e.getPoint());
            }
        });

        contentPane = new JPanel();
        layout = new CardLayout();
        contentPane.setLayout(layout);

        contentPane.add(panel1,"firstWindow");
        contentPane.add(userCreatePanel,"userCreatePanel");
        contentPane.add(userSelectPanel,"userSelectPanel");
        contentPane.add(gamePanel,"gamePanel");

        Container contentPane = getContentPane();
        contentPane.add(this.contentPane, BorderLayout.CENTER);

        //users
        userIn();
        panel1.setUsers(users);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //
    }

    public boolean findUser(String personName, int borw){
        //System.out.println("out "+personName + " " + users.size());
        for(int i=0; i < users.size();i++ ){
            //System.out.println("in " + users.get(i)[0]);
            if(users.get(i)[0].equals(personName)){
                if(borw == 1){
                    blackUser = users.get(i);
                    //System.out.println(blackUser[2]);
                }else{
                    whiteUser = users.get(i);
                    //System.out.println(whiteUser[2]);
                }
                //users.remove(i);
                return true;
            }
        }
        return false;
    }

    public static void fileIO(){
        File newFile = new File("src/main/resources/data.in");
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return str;
    }

    public void userIn(){
        File file1 = new File("src/main/resources/data.in");
        try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
            String temp;
            int count = 0;
            while ((temp = br.readLine()) != null) {
                //System.out.println(temp);
                String[] splitTempList = (temp).split(",");
                if(splitTempList[0] != null) {
                    users.put(count,splitTempList);
                    count++;
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}