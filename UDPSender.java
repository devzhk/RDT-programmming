import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class UDPSender {
    public static void main(String[] args) throws Exception{
        String hostname;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the host name:");
        hostname = inFromUser.readLine();
        InetAddress IPAddress = InetAddress.getByName(hostname);

        DatagramSocket udpSender = new DatagramSocket();
        udpSender.connect(IPAddress,2000);

        DatagramPacket pkt;

        String filename;
        System.out.println("Which file to send: <Filename>");
        filename = inFromUser.readLine();
        FileInputStream fileReader = new FileInputStream("D:\\socket\\Sender\\"+filename);
        int fileLength = fileReader.available();
        System.out.println("File to send is "+filename);
        System.out.println("the file length (bytes)" + fileLength);

        byte[] msg;
        msg = (filename+"-"+fileLength).getBytes();
        pkt = new DatagramPacket(msg,msg.length);
        udpSender.send(pkt);
//        send((filename+"-"+fileLength).getBytes(),udpSender);
        byte[] buffer= new byte[4096];
        byte[] filedata = new byte[2048];
        DatagramPacket ready = new DatagramPacket(buffer,buffer.length);
        udpSender.receive(ready);
        String rdstr = new String(ready.getData(),0,ready.getLength());


        int p = 0, len;//file pointer
//        long time1,time2;
        if (rdstr.equals("READY")){
            long startTime=System.currentTimeMillis();
            System.out.println("Receiver get the file name and length\n begin to send the file...");
            while ( p < fileLength ){
//                time1 = System.currentTimeMillis();
//                time2 = time1;
                len = fileReader.read(filedata);
                pkt = new DatagramPacket(filedata,len);
                udpSender.send(pkt);
//                send(filedata,udpSender);
                p += len;
//                while (time2-time1 < 5) time2 = System.currentTimeMillis();
            }
            System.out.println("Complete pkt transfer");
            fileReader.close();
            //TimeUnit.SECONDS.sleep(10);
            long endTime=System.currentTimeMillis();
            long delayTime=endTime-startTime;
            System.out.println("delay :"+delayTime);
        }
        else {
            System.out.println("Receiver not ready for file transferring.");
        }
    }

    private static void send(byte[] msg, DatagramSocket udpSender) throws IOException{
        DatagramPacket pkt = new DatagramPacket(msg,msg.length);
        udpSender.send(pkt);
    }
}
