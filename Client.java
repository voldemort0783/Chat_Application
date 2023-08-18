import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto" , Font.PLAIN , 20);

    public Client(){
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1",8989);
            System.out.println("connection done !!");
            
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvent();
            startReading();
            // startWriting();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void handleEvent(){
        messageInput.addKeyListener(new KeyListener() {


            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
        
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                
                // System.out.println("released"+e.getKeyCode());
                if(e.getKeyCode()==10){
                    String contentToSend = messageInput.getText();
                    messagArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
            
        });
    }

    private void createGUI(){
        // gui code 

        this.setTitle("Client");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        // // heading.setIcon(new ImageIcon("chat.png"));
        

        messagArea.setEditable(false);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        
        

        this.setLayout(new BorderLayout());

        this.add(heading , BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messagArea);
        this.add(jScrollPane , BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);



        this.setVisible(true);
    }



    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("reader started....");
            try{
                while(true){
                
                
                    String msg = br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this,"terminted the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }

                // System.out.println("Server : "+ msg);
                messagArea.append("Server : "+ msg+"\n");
               
                 }
            } catch(Exception e){
                    // System.exit(0);
                    System.out.println("Connection is closed....");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        Runnable r2 = ()->{
            System.out.println("Writter Started...");
            try{
                while(!socket.isClosed()){
                 
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    } 
                
                }
            } catch(Exception e){
                    // System.exit(0);
                    System.out.println("Connection is closed.....");
                }

        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("This is client");
        new Client();
    }
}
