import java.io.*;
import java.net.*;
public class TCPServer {
    public static void main(String argv[]) throws IOException{
        ServerSocket ss = new ServerSocket(2000);
        System.out.println("Server set up");
        int client_num=0;
        while (true){
            Socket s = null;
            try{
                s = ss.accept();
                String ip = s.getInetAddress().getHostAddress();
                System.out.println(ip+ " is connecting\n"+ s);
                DataInputStream is = new DataInputStream(s.getInputStream());
                DataOutputStream os = new DataOutputStream(s.getOutputStream());
                System.out.println("thread"+String.valueOf(client_num)+" is created(there are "+String.valueOf(client_num+1)+" clients at the same time");
                client_num+=1;
                Thread thread = new ClientHandle(s, is, os);
                thread.start();
            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}

class ClientHandle extends Thread{
    DataInputStream inFromClient;
    DataOutputStream out2client;
    Socket s;

    public ClientHandle(Socket s, DataInputStream is, DataOutputStream os){
        this.s = s;
        this.inFromClient = is;
        this.out2client = os;
    }
    public void run(){
        String clientSentence;
        String mSentence;
        String filepath;
        InputStream readfile;
        while (true){
            try{
                clientSentence = inFromClient.readLine();
                if (clientSentence.equals("quit")){
                    s.close();
                    System.out.println(s+"is closed");
                    break;
                }
                filepath = "D:\\socket\\Sender\\"+clientSentence;
                long startTime=System.currentTimeMillis();
                try{
                    readfile =new FileInputStream(filepath);
                    byte[] buf = new byte[1000];
                    int len = 0;
                    while ((len= readfile.read(buf))!= -1){
                        out2client.write(buf,0, len);
                        //System.out.println("length:"+String.valueOf(len));
                        out2client.flush();
                    }
                    readfile.close();
                    out2client.writeBytes("%end");

                    System.out.println("finish transfer file");
                    long endTime=System.currentTimeMillis();
                    long delayTime=endTime-startTime;
                    System.out.println("delay :"+delayTime);
                }
                catch (FileNotFoundException fe){
                    fe.printStackTrace();
                    System.out.println("File not found");
                    out2client.writeBytes("%FND");

                }
            }
            catch (IOException e){
                System.out.println("error on server side\n");
                e.printStackTrace();
            }
        }
        try{
            this.inFromClient.close();
            this.out2client.close();
            System.out.println("stream is closed");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
