import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

class server extends JFrame{

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Server Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto" , Font.PLAIN , 20);

    public server(){

        try {

            server = new ServerSocket( 8989);
            System.out.println("Server is ready to acccept connection");
            System.out.println("Waiting...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvent();
            startReading();
            // startWriting();

        } catch (Exception e) {
            
            e.printStackTrace();
        }

    }
    
    /**
     * 
     */
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
        
        this.setTitle("Server ");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

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
                    System.out.println("Client terminated the chat");
                    // JOptionPane.showMessageDialog(this,"terminted the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }

                messagArea.append("Server : "+ msg+"\n");
                
            }
            } catch(Exception e){
                    // System.exit(0);
                    System.out.println("Connection is closed.....");
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
        System.out.println("this is server.....going to start server");
        new server();
    }
}