import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.security.GeneralSecurityException;
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
            layout.show(contentPane,"userCreatePanel");
        });
        JButton userSelect = new JButton("User Select");
        userSelect.addActionListener(e -> {
            layout.show(contentPane,"userSelectPanel");
        });

        /*
        JButton userDelete = new JButton("User Delete");
        userDelete.addActionListener(e -> {
            layout.show(contentPane,"userDeletePanel");
        });
         */

        panel1.add(userCreate);
        //panel1.add(userDelete);
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
            text.setText("");
            panel1.setUsers(users);
            layout.show(contentPane,"firstWindow");
        });
        JPopupMenu popup1 = new JPopupMenu();
        JMenuItem createdPop = new JMenuItem();
        createdPop.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        createdPop.setText("User Create & Sync Success!");
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

            //Data Sync(upload)
            try {
                dataSyncUpload();
            } catch (GeneralSecurityException generalSecurityException) {
                generalSecurityException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            popup1.show(contentPane,150,0);
            userIn();
            text.setText("");
            panel1.setUsers(users);
            layout.show(contentPane,"firstWindow");
        });
        userCreatePanel.add(text);
        userCreatePanel.add(createButton);
        userCreatePanel.add(backFirstWindow1);

        /*
        settings -userDelete
         */
        /*
        JPanel userDeletePanel = new JPanel();
        AtomicBoolean existPerson = new AtomicBoolean(false);
        JTextField textDel = new JTextField(20);
        JButton deleteButton = new JButton("Delete");
        JButton backFirstWindow2 = new JButton("Back");
        backFirstWindow2.addActionListener(e -> {
            layout.show(contentPane,"firstWindow");
        });
        JPopupMenu popup2 = new JPopupMenu();
        JMenuItem createdPopDel = new JMenuItem();
        createdPop.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
        createdPop.setText("User Delete & Sync Success!");
        createdPop.setBackground(Color.pink);
        popup1.add(createdPopDel);
        deleteButton.addActionListener(e -> {

            //if textDel (person) exist, findUser delete the person from users (HashMap).
            existPerson.set(findUser(textDel.getText(),-1));
            if(existPerson.get()){
                //from users -> to data.in <write>
                String[] emptyUser = new String[4];
                emptyUser[0] = "-empty-";
                emptyUser[1] = "-";
                emptyUser[2] = "-";
                emptyUser[3] = "-";
                int key = users.size()+1;
                users.put(key,emptyUser);
                fileWriter();
                users.remove(key);
                try {
                    dataSyncUpload();
                } catch (GeneralSecurityException eDel) {
                    eDel.printStackTrace();
                } catch (IOException eDel) {
                    eDel.printStackTrace();
                }
                userIn();
                panel1.setUsers(users);
                popup1.show(contentPane,150,0);
                layout.show(contentPane,"firstWindow");
                existPerson.set(false);
            }
        });
        userDeletePanel.add(textDel);
        userDeletePanel.add(deleteButton);
        userDeletePanel.add(backFirstWindow2);
         */

        /*
        settings -userSelect
         */
        AtomicBoolean existBlack = new AtomicBoolean(false);
        AtomicBoolean existWhite = new AtomicBoolean(false);
        JTextField text1 = new JTextField(20);
        JTextField text2 = new JTextField(20);
        JPanel userSelectPanel = new JPanel();
        userSelectPanel.setBounds(50,50,400,400);
        JButton selectButton1 = new JButton("Black select");
        JButton selectButton2 = new JButton("White select");
        userSelectPanel.add(text1);
        userSelectPanel.add(selectButton1);
        userSelectPanel.add(text2);
        userSelectPanel.add(selectButton2);
        JButton toGame = new JButton("Game Start");

        JButton backFirstWindow3 = new JButton("Back");
        toGame.setVisible(false);

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
        userSelectPanel.add(backFirstWindow3);

        backFirstWindow3.addActionListener(e -> {
            userIn();
            panel1.setUsers(users);
            layout.show(contentPane,"firstWindow");
        });

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
        //contentPane.add(userDeletePanel,"userDeletePanel");
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
                    /*
                    if(borw == 2) {
                        whiteUser = users.get(i);
                        //System.out.println(whiteUser[2]);
                    }else{
                        //only for finding person
                        users.remove(i);
                    }
                     */
                    whiteUser = users.get(i);
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
                /*
                if(splitTempList[0] != null && !splitTempList[0].equals("-empty-")) {
                    users.put(count,splitTempList);
                    count++;
                }

                 */
                if(splitTempList[0] != null){
                    users.put(count,splitTempList);
                    count++;
                }
                else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileWriter(){
        try{
            PrintWriter pw = new PrintWriter("src/main/resources/data.in");
            for ( int i=0; i <= users.size(); i++ ) {
                if(users.get(i)!=null) {
                    String[] temp = users.get(i);
                    pw.println(temp[0] + "," + temp[1] + "," + temp[2] + "," + temp[3]);
                }
            }
            pw.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void dataSyncUpload() throws GeneralSecurityException, IOException{
        DataSyncOutput dataOut = new DataSyncOutput("1BQl_z8DSzFL2LEPp0UAK_0vVbfKmJ9oqnZmwrnv3Q_o");
        dataOut.writeTest("sheet1!A2");
    }

}