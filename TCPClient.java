import java.net.*;
import java.io.*;

public class TCPClient {
    public static void main(String args[]) throws Exception {
        String filepath;
        String hostname;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the hostname:");
        hostname = inFromUser.readLine();
        Socket clientSocket = new Socket(hostname, 2000);
        DataOutputStream out2server = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
        String sentence;
        String modifiedsentence;
        while (true) {
            System.out.println("Please enter file name (Type quit to close):\n");
            sentence = inFromUser.readLine();
            out2server.writeBytes(sentence + "\n");
            if (sentence.equals("quit")) {
                System.out.println(clientSocket + "is turned off");
                break;
            }
            System.out.println("Begin to download file:"+sentence+"...\n");
            OutputStream outfile = new FileOutputStream("D:\\socket\\Receiver\\"+sentence);
            byte[] buf = new byte[1000];
            int len = 0;
            while ( (len = inFromServer.read(buf)) != -1){
                modifiedsentence = new String(buf,0,len);
                if (modifiedsentence.endsWith("%end")){
                    outfile.write(buf,0, len - 4);
                    outfile.flush();
                    break;
                }
                else if (modifiedsentence.endsWith("%FND")){
                    System.out.println("File not found");
                    outfile.close();
                    break;
                }
                else {
                    outfile.write(buf, 0, len);
                    outfile.flush();
                }
            }
            outfile.close();
            System.out.println("finish downloading file");
        }
        clientSocket.close();
        System.out.println("socket connection is closed");
    }
}
