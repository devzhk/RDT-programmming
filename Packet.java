import java.net.DatagramPacket;
import java.net.InetAddress;

public class Packet {
    public int seq;
    public int ACK;
    public int chksum;
    public byte[] data;
    public Packet(int seqno){
        seq = seqno;
    }

    public DatagramPacket makePkt(int ack,byte[] mdata){
        DatagramPacket sndpkt;
        ACK = ack;
        data = mdata;
        chksum = computeChksum(mdata);
        String ackbt = String.valueOf(ACK);
        String seqbt = String.valueOf(seq);
        String chkbt = String.valueOf(chksum);
//        System.out.println("chksum");
//        System.out.println(chksum);
        byte[] snd_pkt = byteMergerAll(ackbt.getBytes(),"$".getBytes(),seqbt.getBytes(),"$".getBytes(),chkbt.getBytes(),"$".getBytes(),data);
        sndpkt = new DatagramPacket(snd_pkt,snd_pkt.length);
        return sndpkt;
    }
    public DatagramPacket makePkt(int ack,byte[] mdata,InetAddress address,int port){
        DatagramPacket sndpkt;
        ACK = ack;
        data = mdata;
        chksum = computeChksum(mdata);
        String ackbt = String.valueOf(ACK);
        String seqbt = String.valueOf(seq);
        String chkbt = String.valueOf(chksum);
        byte[] snd_pkt = byteMergerAll(ackbt.getBytes(),"$".getBytes(),seqbt.getBytes(),"$".getBytes(),chkbt.getBytes(),"$".getBytes(),data);
        sndpkt = new DatagramPacket(snd_pkt,snd_pkt.length,address,port);
        return sndpkt;
    }
    public int computeChksum(byte[] data){
        int ascCode;
        int sum = 0;
        for ( int i= 0 ; i< data.length ; i++){
            ascCode = (int) data[i];
            sum +=ascCode;
        }
        return sum;
    }
    public void extractPkt(byte[] rcvdata){
        String s = new String(rcvdata);
//        System.out.println("String :"+s);
        String[] spt = s.split("\\$");
        ACK = Integer.parseInt(spt[0]);
        seq = Integer.parseInt(spt[1]);
        chksum = Integer.parseInt(spt[2]);
        data = spt[3].getBytes();
    }
    private static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }
}
