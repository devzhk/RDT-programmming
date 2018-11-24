import java.io.*;
import java.net.*;
import java.util.*;


public class UDPReceiver {
    static double P_CORRUPT = 0.3;
    static double P_LOSS = 0.2;
    public static void main(String args[]) throws Exception{
        DatagramSocket rcvSocket = new DatagramSocket(2000);
        System.out.println("ready on port 2000");
        byte[] buffer = new byte[2048];
        DatagramPacket rdypkt = new DatagramPacket(buffer,buffer.length);
        rcvSocket.receive(rdypkt);
        String rdstr = new String(rdypkt.getData(),0,rdypkt.getLength());
        StringTokenizer rdtoken = new StringTokenizer(rdstr,"-");
        String filename  = rdtoken.nextToken();
        String filepath = "D:\\socket\\Receiver\\"+filename;
        String lenstr = rdtoken.nextToken();
        int filelen = Integer.parseInt(lenstr);
        System.out.println("save at:    "+filepath);
        System.out.println("file length:"+filelen);
        byte[] buf = "READY".getBytes();
        DatagramPacket rdpkt = new DatagramPacket(buf,buf.length,rdypkt.getAddress(),rdypkt.getPort());
        rcvSocket.send(rdpkt);


        FileOutputStream fileWriter = new FileOutputStream(filepath);
        int len = 0;
        DatagramPacket rcvpkt;
        Random random = new Random();
        while (len < filelen - 10 ){
//            buffer = new byte[1024];
//            System.out.println(buffer[1]);
            rcvpkt = new DatagramPacket(buffer,buffer.length);
            rcvSocket.receive(rcvpkt);
//            System.out.println(buffer[1]);
            System.out.println("len :"+ len);
            if (random.nextDouble() < P_CORRUPT ) {
                byte[] corruptdata = new byte[rcvpkt.getLength()];
                random.nextBytes(corruptdata);
                fileWriter.write(corruptdata, 0, rcvpkt.getLength());
            }
            else {
                fileWriter.write(rcvpkt.getData(),0,rcvpkt.getLength());
            }
            len += validLen(rcvpkt.getData());

        }
        fileWriter.close();
        System.out.println(filepath+ "saved");
    }
    public static int validLen(byte[] buf){
        int i = 0;
        if (null == buf || 0 == buf.length)
            return i ;
        for (; i < buf.length; i++) {
            if (buf[i] == '\0')
                break;
        }
        return i + 1;
    }
}
