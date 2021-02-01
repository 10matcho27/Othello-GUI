import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class CustomGameView extends JPanel {

    static final int width = 500;//画面の幅
    static final int height = 600;//画面の高さ
    int rectLen = 50;//マスの一片の長さ
    int[][] banStatus = new int[8][8];//各マスの状態。1:black,-1:white,0:empty
    int color = 1; // ターン。初手は黒(1)
    Point click;
    int turnNum = 0;
    int succession = 0;
    Map<Integer, String[]> users = new HashMap<>();
    String[] blackUser = new String[4];
    String[] whiteUser = new String[4];

    public void setUsers(Map<Integer, String[]> users){
        this.users = users;
    }

    public void setBlackUser(String[] blackUser){
        this.blackUser = blackUser;
    }

    public  void setWhiteUser(String[] whiteUser){
        this.whiteUser = whiteUser;
    }

    //コンストラクタ
    public CustomGameView() {
        init();
    }

    //画面描画
    @Override
    public void paintComponent(Graphics g0) {
        //背景
        Graphics2D g = (Graphics2D) g0;
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.green);
        g.fillRect(50, 50, 400, 400);
        //横線
        g.setColor(Color.black);
        for (int row = 50; row <= 450; row = row + rectLen) {
            g.drawLine(50, row, 450, row);
        }
        //縦線
        for (int col = 50; col <= 450; col = col + rectLen) {
            g.drawLine(col, 50, col, 450);
        }

        //駒の描画putArc
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (banStatus[row][col] != 0) {
                    int x = col * 50 + 50;
                    int y = row * 50 + 50;
                    putArc(g, banStatus[row][col], x, y);
                }
            }
        }
        Font font1 = new Font("ＭＳ Ｐゴシック", Font.PLAIN, 25);
        FontMetrics fm = g.getFontMetrics(font1);
        g.setFont(font1);
        String str;
        String strBlack, strWhite;
        int widStrBlack;
        int widStrWhite;
        int width_str;
        //UserName
        strBlack = "Black : " + blackUser[0];
        strWhite = "White : " + whiteUser[0];
        widStrBlack = fm.stringWidth(strBlack);
        widStrWhite = fm.stringWidth(strWhite);
        g.setColor(Color.black);
        g.drawString(strBlack, 15, 25);
        g.setColor(Color.white);
        g.drawString(strWhite, 500 - 15 - widStrWhite, 25);

        if (turnNum == 60||filledAllByOneColor()||succession == 9000) {
            int win = 0;
            int draw = 0;
            int lose = 0;
            if (judgeWin() == 1) {
                //黒
                str = "Winner : Black, " + blackUser[0] + " !!";
                //結果保存？
                //debug
                System.out.println("win" + blackUser[1] + "lose" + whiteUser[3]);
                //
                win = Integer.parseInt(blackUser[1]);
                win ++;
                blackUser[1] = String.valueOf(win);
                lose = Integer.parseInt(whiteUser[3]);
                lose ++;
                whiteUser[3] = String.valueOf(lose);
                //debug
                System.out.println("win" + blackUser[1] + "lose" + whiteUser[3]);
                //
            } else {
                if (judgeWin() == 0) {
                    //同点
                    str = "Draw";
                    //結果保存？
                    //debug
                    System.out.println("draw" + blackUser[2] + "draw" + whiteUser[2]);
                    //
                    lose = Integer.parseInt(blackUser[2]);
                    lose ++;
                    blackUser[2] = String.valueOf(lose);
                    lose = Integer.parseInt(whiteUser[2]);
                    lose ++;
                    whiteUser[2] = String.valueOf(lose);
                    //debug
                    System.out.println("draw" + blackUser[2] + "draw" + whiteUser[2]);
                    //
                } else {
                    //白
                    str = "Winner : White, " + whiteUser[0] + " !!";
                    //結果保存？
                    //debug
                    System.out.println("lose" + blackUser[3] + "win" + whiteUser[1]);
                    //
                    win = Integer.parseInt(whiteUser[1]);
                    win ++;
                    whiteUser[1] = String.valueOf(win);
                    lose = Integer.parseInt(blackUser[3]);
                    lose ++;
                    blackUser[3] = String.valueOf(lose);
                    //debug
                    System.out.println("lose" + blackUser[3] + "win" + whiteUser[1]);
                    //
                }
            }
            width_str = fm.stringWidth(str);
            g.setColor(Color.white);
            g.drawString(str, 250 - width_str / 2, 475);
            //Data.inに書き込み
            fileWriter();
            try {
                dataSyncUpload();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (!putJudge_all(g,color)) {
                str = "There is no space to put for ";
                width_str = fm.stringWidth(str);
                g.setColor(Color.white);
                if (color == 1) {
                    //黒
                    str = str + "Black.";
                    succession += 2000;
                } else {
                    //白
                    str = str + "White.";
                    succession += 7000;
                }
                width_str = fm.stringWidth(str);
                g.drawString(str, 250 - width_str / 2, 475);
                color = color * (-1);
                putJudge_all(g, color);

            }
            else{
                succession = 0;
                //successionは黒白どっちも置く場所が無いときに9000になる。
                g.setColor(Color.darkGray);
                g.fillRect(0, 455, width, 45);
            }
        }

    }

    void putArc(Graphics g, int color, int x, int y) {
        if (color == 1) {
            g.setColor(Color.black);
        }
        if (color == -1) {
            g.setColor(Color.white);
        }
        g.fillArc(x, y, rectLen, rectLen, 0, 360);
    }

    void drawLinePutAble(Graphics g, int x, int y, int color){
        Graphics2D g2 = (Graphics2D)g;
        int startX = (x+1)*50;
        int startY = (y+1)*50;
        BasicStroke bs = new BasicStroke(2);

        if(color == 1){
            g.setColor(Color.black);
        }
        if(color == -1){
            g.setColor(Color.white);
        }
        g2.setStroke(bs);
        g.drawLine(startX,startY,startX+50,startY);
        g.drawLine(startX,startY+50,startX+50,startY+50);
        g.drawLine(startX,startY,startX,startY+50);
        g.drawLine(startX+50,startY,startX+50,startY+50);
    }

    boolean filledAllByOneColor(){
        int black = 0;
        int white = 0;
        for (int row = 0; row < 8; row++) {
            for (int Coma : banStatus[row]) {
                if (Coma == 1) {
                    black++;
                }else{
                    if(Coma == -1){
                        white++;
                    }
                }
            }
        }
        if(black == 0 || white == 0){
            return true;
        }
        return false;
    }

    void init() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                banStatus[row][col] = 0;
            }
        }
        banStatus[3][3] = -1;
        banStatus[4][4] = -1;
        banStatus[3][4] = 1;
        banStatus[4][3] = 1;
        turnNum = 0;
        color = 1;
        //結果読み込み？？
    }

    public void reset() {
        init();
        repaint();
    }

    public void mouseReleased(Point point) {

        Point clicked;
        click = point;
        clicked = gauss(point);

        if (putJudge(clicked, color)) {
            turnNum = turnNum + 1 ;
            color *= (-1);
        }
        repaint();
    }

    //クリックされたマスを特定するメゾット。
    public Point gauss(Point point) {
        Point masu = new Point();
        if (point.x >= 50 && point.x <= 450 && point.y >= 50 && point.y <= 450) {
            masu.x = point.x / 50 - 1;
            masu.y = point.y / 50 - 1;
        }
        return masu;
    }

    //おけるか置けないかの判定
    public boolean putJudge(Point point, int color) {
        int tempX, tempY;
        int x = point.x;
        int y = point.y;
        int status = banStatus[y][x];
        boolean judge = false;

        if (status != 0 || x < 0 || x > 7 || y < 0 || y > 7) {
            //置けない
        } else {
            if (x >= 2) {
                if (banStatus[y][x - 1] * color == -1) {
                    tempX = x - 1;
                    while (tempX >= 0) {
                        if (banStatus[y][tempX]==0){
                            break;
                        }
                        if (banStatus[y][tempX] == color) {
                            judge = true;
                            for (int num = 0; x - num > tempX; num++) {
                                banStatus[y][x - num] = color;
                            }
                            break;
                        }
                        tempX = tempX - 1;
                    }
                }
                if (y >= 2) {
                    if (banStatus[y - 1][x - 1] * color == -1) {
                        tempX = x - 1;
                        tempY = y - 1;
                        while (tempX >= 0 && tempY >= 0) {
                            if (banStatus[tempY][tempX]==0){
                                break;
                            }
                            if (banStatus[tempY][tempX] == color) {
                                judge = true;
                                for (int num = 0; x - num > tempX || y - num > tempY; num++) {
                                    banStatus[y - num][x - num] = color;
                                }
                                break;
                            }
                            tempX = tempX - 1;
                            tempY = tempY - 1;
                        }
                    }
                }
            }
            if (x <= 5) {
                if (banStatus[y][x + 1] * color == -1) {
                    tempX = x + 1;
                    while (tempX <= 7) {
                        if (banStatus[y][tempX]==0){
                            break;
                        }
                        if (banStatus[y][tempX] == color) {
                            judge = true;
                            for (int num = 0; x + num < tempX; num++) {
                                banStatus[y][x + num] = color;
                            }
                            break;
                        }
                        tempX = tempX + 1;
                    }
                }
                if (y >= 2) {
                    if (banStatus[y - 1][x + 1] * color == -1) {
                        tempX = x + 1;
                        tempY = y - 1;
                        while (tempX <= 7 && tempY >= 0) {
                            if (banStatus[tempY][tempX]==0){
                                break;
                            }
                            if (banStatus[tempY][tempX] == color) {
                                judge = true;
                                for (int num = 0; x + num < tempX || y - num > tempY; num++) {
                                    banStatus[y - num][x + num] = color;
                                }
                                break;
                            }
                            tempX = tempX + 1;
                            tempY = tempY - 1;
                        }
                    }
                }
            }
            if (y >= 2) {
                if (banStatus[y - 1][x] * color == -1) {
                    tempY = y - 1;
                    while (tempY >= 0) {
                        if (banStatus[tempY][x]==0){
                            break;
                        }
                        if (banStatus[tempY][x] == color) {
                            judge = true;
                            for (int num = 0; y - num > tempY; num++) {
                                banStatus[y - num][x] = color;
                            }
                            break;
                        }
                        tempY = tempY - 1;
                    }
                }
            }
            if (y <= 5) {
                if (banStatus[y + 1][x] * color == -1) {
                    tempY = y + 1;
                    while (tempY <= 7) {
                        if (banStatus[tempY][x]==0){
                            break;
                        }
                        if (banStatus[tempY][x] == color) {
                            judge = true;
                            for (int num = 0; y + num < tempY; num++) {
                                banStatus[y + num][x] = color;
                            }
                            break;
                        }
                        tempY = tempY + 1;
                    }
                }
                if (x >= 2) {
                    if (banStatus[y + 1][x - 1] * color == -1) {
                        tempX = x - 1;
                        tempY = y + 1;
                        while (tempX >= 0 && tempY <= 7) {
                            if (banStatus[tempY][tempX]==0){
                                break;
                            }
                            if (banStatus[tempY][tempX] == color) {
                                judge = true;
                                for (int num = 0; x - num > tempX || y + num < tempY; num++) {
                                    banStatus[y + num][x - num] = color;
                                }
                                break;
                            }
                            tempX = tempX - 1;
                            tempY = tempY + 1;
                        }
                    }
                }
                if (x <= 5) {
                    if (banStatus[y + 1][x + 1] * color == -1) {
                        tempX = x + 1;
                        tempY = y + 1;
                        while (tempX <= 7 && tempY <= 7) {
                            if (banStatus[tempY][tempX]==0){
                                break;
                            }
                            if (banStatus[tempY][tempX] == color) {
                                judge = true;
                                for (int num = 0; x + num < tempX || y + num < tempY; num++) {
                                    banStatus[y + num][x + num] = color;
                                }
                                break;
                            }
                            tempX = tempX + 1;
                            tempY = tempY + 1;
                        }
                    }
                }
            }
        }
        return judge;
    }

    public int judgeWin() {
        int black = 0;
        int white = 0;
        for (int row = 0; row < 8; row++) {
            for (int Coma : banStatus[row]) {
                if (Coma == 1) {
                    black++;
                }else{
                    if(Coma == -1){
                        white++;
                    }
                }
            }
        }
        if (black > white) {
            return 1;//黒の勝利
        } else {
            if (black == white) {
                return 0;//同点
            } else {
                return -1;//白の勝ち
            }
        }
    }

    public Boolean putJudge_all(Graphics g,int color) {
        int tempX, tempY;
        boolean put = false;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int status = banStatus[y][x];
                if (status != 0 || x < 0 || x > 7 || y < 0 || y > 7) {
                    //置けない
                } else {
                    if (x >= 2) {
                        if (banStatus[y][x - 1] * color == -1) {
                            tempX = x - 1;
                            while (tempX >= 0) {
                                if (banStatus[y][tempX]==0){
                                    break;
                                }
                                if (banStatus[y][tempX] == color) {
                                    put = true;
                                    drawLinePutAble(g,x,y,color);
                                    break;
                                }
                                tempX = tempX - 1;
                            }
                        }
                        if (y >= 2) {
                            if (banStatus[y - 1][x - 1] * color == -1) {
                                tempX = x - 1;
                                tempY = y - 1;
                                while (tempX >= 0 && tempY >= 0) {
                                    if (banStatus[tempY][tempX]==0){
                                        break;
                                    }
                                    if (banStatus[tempY][tempX] == color) {
                                        put = true;
                                        drawLinePutAble(g,x,y,color);
                                        break;
                                    }
                                    tempX = tempX - 1;
                                    tempY = tempY - 1;
                                }
                            }
                        }
                    }
                    if (x <= 5) {
                        if (banStatus[y][x + 1] * color == -1) {
                            tempX = x + 1;
                            while (tempX <= 7) {
                                if (banStatus[y][tempX]==0){
                                    break;
                                }
                                if (banStatus[y][tempX] == color) {
                                    put = true;
                                    drawLinePutAble(g,x,y,color);
                                    break;
                                }
                                tempX = tempX + 1;
                            }
                        }
                        if (y >= 2) {
                            if (banStatus[y - 1][x + 1] * color == -1) {
                                tempX = x + 1;
                                tempY = y - 1;
                                while (tempX <= 7 && tempY >= 0) {
                                    if (banStatus[tempY][tempX]==0){
                                        break;
                                    }
                                    if (banStatus[tempY][tempX] == color) {
                                        put = true;
                                        drawLinePutAble(g,x,y,color);
                                        break;
                                    }
                                    tempX = tempX + 1;
                                    tempY = tempY - 1;
                                }
                            }
                        }
                    }
                    if (y >= 2) {
                        if (banStatus[y - 1][x] * color == -1) {
                            tempY = y - 1;
                            while (tempY >= 0) {
                                if (banStatus[tempY][x]==0){
                                    break;
                                }
                                if (banStatus[tempY][x] == color) {
                                    put = true;
                                    drawLinePutAble(g,x,y,color);
                                    break;
                                }
                                tempY = tempY - 1;
                            }
                        }
                    }
                    if (y <= 5) {
                        if (banStatus[y + 1][x] * color == -1) {
                            tempY = y + 1;
                            while (tempY <= 7) {
                                if (banStatus[tempY][x]==0){
                                    break;
                                }
                                if (banStatus[tempY][x] == color) {
                                    put = true;
                                    drawLinePutAble(g,x,y,color);
                                    break;
                                }
                                tempY = tempY + 1;
                            }
                        }
                        if (x >= 2) {
                            if (banStatus[y + 1][x - 1] * color == -1) {
                                tempX = x - 1;
                                tempY = y + 1;
                                while (tempX >= 0 && tempY <= 7) {
                                    if (banStatus[tempY][tempX]==0){
                                        break;
                                    }
                                    if (banStatus[tempY][tempX] == color) {
                                        put = true;
                                        drawLinePutAble(g,x,y,color);
                                        break;
                                    }
                                    tempX = tempX - 1;
                                    tempY = tempY + 1;
                                }
                            }
                        }
                        if (x <= 5) {
                            if (banStatus[y + 1][x + 1] * color == -1) {
                                tempX = x + 1;
                                tempY = y + 1;
                                while (tempX <= 7 && tempY <= 7) {
                                    if (banStatus[tempY][tempX]==0){
                                        break;
                                    }
                                    if (banStatus[tempY][tempX] == color) {
                                        put = true;
                                        drawLinePutAble(g,x,y,color);
                                        break;
                                    }
                                    tempX = tempX + 1;
                                    tempY = tempY + 1;
                                }
                            }
                        }
                    }
                }
            }
        }
        return put;
    }

    public void fileWriter(){
        try{
            PrintWriter pw = new PrintWriter("src/main/resources/data.in");
            for ( int i=0; i < users.size();i++ ) {
                String[] temp = users.get(i);
                if (temp[0].equals(blackUser[0]) || temp[0].equals(whiteUser[0])) {
                    if(temp[0].equals(blackUser[0])){
                        pw.println(blackUser[0] + "," + blackUser[1] + "," + blackUser[2] + "," + blackUser[3]);
                    }else{
                        pw.println(whiteUser[0] + "," + whiteUser[1] + "," + whiteUser[2] + "," + whiteUser[3]);
                    }
                } else {
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