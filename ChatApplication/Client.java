import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {

    Socket socket;
     BufferedReader br;
    PrintWriter out; 
    // declare component
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);


    public Client(){

        try {
            System.out.println("Sending request to server");

            socket=new Socket("127.0.0.1",7778);
            System.out.println("connection done");

              br=new BufferedReader(new InputStreamReader(socket.getInputStream())); // socket se input stream nika ke inputstreamreader ko de diya
       out=new PrintWriter(socket.getOutputStream());

    createGUI();
    handleEvents();


       startReading();
    //    startWriting();
        


            
        } catch (Exception e) {
           
        }

    }
    private void handleEvents(){

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
                // System.out.println("key released "+e.getKeyCode());
                if(e.getKeyCode()==10){

                    // System.out.println("you have pressed enter button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

          
            
        });
    }
    private void createGUI(){
        //gui code...
        this.setTitle("Client Message[END]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);



        //frame ka layout abh set karenge

        this.setLayout(new BorderLayout());
        //adding the component to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        
    
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);




        this.setVisible(true);
    }
    public void startReading(){
        //thread read karke dega
        Runnable r1=()->{
            System.out.println("reader started");
            try{
            while (true) {
                
                String msg=br.readLine();
                if(msg.equals("exit")){
                    System.out.println("server terminated the chat");
                    JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                // System.out.println("Server : "+msg);
                messageArea.append("Server: "+msg+"\n");
            
                
            }
        }catch(Exception e){
            System.out.println("connection is closed");
                   }
    
        };
        new Thread(r1).start();
    
    }
    public void startWriting(){
        //data ko user se lega then usko send karega client tkh
        Runnable r2=()->{
            System.out.println("writer started");
            try{
            while (true && !socket.isClosed()) {
                
    
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
    
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                      }
                    
                
                
            }
            System.out.println("connection is closed");
        }catch(Exception e){
            e.printStackTrace();
            
        }
    
    
        };
        new Thread(r2).start();
    
    }
    



    public static void main(String[] args) {
        System.out.println("this is client");
        new Client();
    }
    
}
