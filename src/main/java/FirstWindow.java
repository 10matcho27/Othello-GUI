import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.List;


public class FirstWindow extends JPanel {
    static final int width = 500;//画面の幅
    static final int height = 550;//画面の高さ
    static final int UserLimit = 20;//これぐらいにしておいてほしいなあ
    Map<Integer, String[]> users = new HashMap<>();

    //コンストラクタ
    public  FirstWindow(){
        try {
            dataSync();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //setBlackUser、WhiteUserのメゾットを作成。こっちにプレイや情報を移す。
    //データ登録のクラス作成。

    @Override
    public void paintComponent(Graphics g0) {
        String[] name = new String[UserLimit];
        //背景
        Graphics2D g = (Graphics2D) g0;
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, width, height);

        Font font1 = new Font("ＭＳ Ｐゴシック", Font.PLAIN, 30);
        Font font2 = new Font("ＭＳ Ｐゴシック", Font.PLAIN, 20);
        Font font3 = new Font("ＭＳ Ｐゴシック", Font.PLAIN, 23);
        g.setFont(font1);
        FontMetrics fm = g.getFontMetrics(font1);
        String title = "Othello Game";
        int width_str = fm.stringWidth(title);
        g.setColor(Color.white);
        g.drawString(title, 250 - width_str / 2, 100);

        g.setFont(font2);
        String text1 = "User List";
        int width_str1 = fm.stringWidth(text1);
        g.setColor(Color.white);
        g.drawString(text1, width / 2 - width_str1 / 2 + 20, 150);

        g.setFont(font3);
        int row = 0;
        for(int i=0;i<users.size();i++){
            //System.out.println(users.get(i)[0]);
            g.setColor(Color.pink);
            g.drawString("\n・" + users.get(i)[0] + ", win : " + users.get(i)[1] + ", draw : " + users.get(i)[2] + ", lose : " + users.get(i)[3], width / 2 - width_str1 / 2 -140, (190 + row));
            row += 23;
        }

    }

    public void dataSync() throws GeneralSecurityException, IOException {
        DataSyncInput data = new DataSyncInput("1BQl_z8DSzFL2LEPp0UAK_0vVbfKmJ9oqnZmwrnv3Q_o");
        data.readTest("sheet1!A2:D");
    }

    public void setUsers(Map<Integer, String[]> users){
        this.users = users;
    }

}
