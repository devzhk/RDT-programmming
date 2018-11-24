import java.net.*;
import java.io.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RDTSender {
    static double P_LOSS = 0.3;
    public void main(String args[]) throws Exception{
        String hostname;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the host name:");
        hostname = inFromUser.readLine();
        InetAddress IPAddress = InetAddress.getByName(hostname);

        DatagramSocket udpSender = new DatagramSocket();
        udpSender.connect(IPAddress,2000);

        DatagramPacket pkt;
        DatagramPacket rcvpkt;

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
        byte[] buffer= new byte[1024];//confirm file information
        byte[] filedata = new byte[2048];
        byte[] buf;
        byte[] rcvbuf = new byte[2048];
        DatagramPacket ready = new DatagramPacket(buffer,buffer.length);
        udpSender.receive(ready);
        String rdstr = new String(ready.getData(),0,ready.getLength());
        int p = 0, len;//file pointer
        Packet rdtpkt,rcvPkt;
        DatagramPacket sndpkt;
        Timer timeOut;
        String s;
        int index=1;
        Random random= new Random();
        if (rdstr.equals("READY")){
            long startTime=System.currentTimeMillis();
            System.out.println("Receiver ready\n begin to send the file...");
            while ( p < fileLength ){
                len = fileReader.read(filedata);
                //*Implement RDT3.0
                buf = new byte[2048];
                index = 1 - index;
                System.arraycopy(filedata,0,buf,0,len);
//                s = new String(buf);
//                System.out.println(s.length());
//                System.out.println(s);
                rdtpkt = new Packet(index);
                sndpkt = rdtpkt.makePkt(0,buf);

                //
                System.out.println("Send packet"+rdtpkt.seq);
                if (random.nextDouble() > P_LOSS )
                    udpSender.send(sndpkt);
                else
                    System.out.println("Packet loss");
                timeOut = new Timer();
                timeOut.schedule(new sendTask(sndpkt,udpSender),100,100);//ms
//                udpSender = new DatagramSocket();
//                udpSender.connect(IPAddress,2000);
                //receive
                rcvpkt = new DatagramPacket(rcvbuf,rcvbuf.length);
                udpSender.receive(rcvpkt);
//                System.out.println("Check Point");
                rcvPkt = new Packet(0);
                rcvPkt.extractPkt(rcvpkt.getData());
                while ( rcvPkt.ACK == rdtpkt.seq ){  //rcvPkt.chksum != rcvPkt.computeChksum(rcvPkt.data) ||
                    rcvbuf = new byte[2048];
                    rcvpkt = new DatagramPacket(rcvbuf,rcvbuf.length);
                    udpSender.receive(rcvpkt);
                    System.out.print(rcvPkt.ACK);
                    System.out.print("!=");
                    System.out.println(rcvPkt.seq);
                    System.out.println("Wrong ACK number!");
                    rcvPkt = new Packet(0);
                    rcvPkt.extractPkt(rcvpkt.getData());
                }
                timeOut.cancel();
                p += len;
                System.out.println("Packet Acked");
            }
            System.out.println("Complete pkt transfer");
            fileReader.close();
            long endTime=System.currentTimeMillis();
            long delayTime=endTime-startTime;
            System.out.println("delay :"+delayTime);
        }
        else {
            System.out.println("Receiver not ready for file transferring.");
        }
    }

    class sendTask extends TimerTask{
        DatagramPacket p;
        DatagramSocket udpSender;
        public sendTask(DatagramPacket sndpkt,DatagramSocket udpSender){
            p = sndpkt;
            this.udpSender=udpSender;
        }
        @Override
        public void run(){
            try{
                System.out.println("Sender side Time out! Retransmit the packet and restart timer");
                udpSender.send(p);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
