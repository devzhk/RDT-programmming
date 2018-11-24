import java.net.*;
import java.io.*;
import java.util.*;

public class RDTReceiver {
    static double P_CORRUPT = 0.4;
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
        InetAddress address = rdypkt.getAddress();
        int port = rdypkt.getPort();
        DatagramPacket rdpkt = new DatagramPacket(buf,buf.length,rdypkt.getAddress(),rdypkt.getPort());
        rcvSocket.send(rdpkt);


        FileOutputStream fileWriter = new FileOutputStream(filepath);
        int len = 0;
        DatagramPacket rcvpkt;
        DatagramPacket sndpkt;
        Packet sndPkt;
        Packet pkt;
        byte[] sndbuf;
        byte[] savebuf;
        String str;
        Random random = new Random();
        while (len < filelen - 10 ){
            buffer = new byte[4096];
            rcvpkt = new DatagramPacket(buffer,buffer.length);
            rcvSocket.receive(rcvpkt);
            pkt = new Packet(0);
            pkt.extractPkt(rcvpkt.getData());
//            System.out.print(pkt.chksum);
//            System.out.print("=?");
            System.out.print("Receive packet");
            System.out.println(pkt.seq);
            if (random.nextDouble() < P_CORRUPT ) {
                random.nextBytes(pkt.data);
            }
            if (pkt.chksum == pkt.computeChksum(pkt.data))
                System.out.println("ACK");
            while (pkt.chksum != pkt.computeChksum(pkt.data)){
                sndPkt = new Packet(0);
                sndbuf = new byte[10];
                sndpkt = sndPkt.makePkt(pkt.seq,sndbuf,address,port);
                rcvSocket.send(sndpkt);
//                str = new String(pkt.data);
//                System.out.print(pkt.chksum);
//                System.out.print("=?");
//                System.out.println(pkt.computeChksum(pkt.data));
                System.out.println("Packet Corrupted. Wait for resend...");
//                System.out.println("Length"+str.length());
                buffer = new byte[4096];
                rcvpkt = new DatagramPacket(buffer,buffer.length);
                rcvSocket.receive(rcvpkt);
                pkt = new Packet(0);
                pkt.extractPkt(rcvpkt.getData());
                System.out.print("Receive packet");
                System.out.println(pkt.seq);
            }
            sndPkt = new Packet(0);
            sndbuf = new byte[10];
            sndpkt = sndPkt.makePkt(1-pkt.seq,sndbuf,address,port);
            rcvSocket.send(sndpkt);
            System.out.println("Send ACK");
            savebuf = new byte[pkt.data.length];
            System.arraycopy(pkt.data,0,savebuf,0,validLen(pkt.data));

            fileWriter.write(savebuf,0,validLen(pkt.data)-1);
            len += validLen(pkt.data);
//            System.out.println("len :"+ len);
        }
        fileWriter.close();
        System.out.println(filepath+ " has been saved ");
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
