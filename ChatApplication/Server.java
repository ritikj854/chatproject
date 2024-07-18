import java.net.*;
import java.io.*;

class Server{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out; // data ko write karne ke liye iska use kiye hai
    


// constructor
public Server(){
    try {
        server=new ServerSocket(7778);//port-7777
        System.out.println("server is ready to accept connection");
        System.out.println("Waiting...");
       socket= server.accept();//connection accept client
       // abh yeah joh socket mila hain isse hum inputstream nikal sakte hai data ko read karne ke liye
       br=new BufferedReader(new InputStreamReader(socket.getInputStream())); // socket se input stream nika ke inputstreamreader ko de diya
       out=new PrintWriter(socket.getOutputStream());

       startReading();
       startWriting();
        
    } catch (Exception e) {
       e.printStackTrace();
    }

   


}
public void startReading(){
    //thread read karke dega
    Runnable r1=()->{
        System.out.println("reader started");

        try{
        while (true) {
            
            
            String msg=br.readLine();
            if(msg.equals("exit")){
                System.out.println("client terminated the chat");
                socket.close();
                break;
            }
            System.out.println("Client : "+msg);
        
            
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
        }catch(Exception e){
            System.out.println("connection is closed");

        }
        
            
        


    };
    new Thread(r2).start();

}


    public static void main(String[] args){
        System.out.println("this is a server ");
        new Server();
    }
}