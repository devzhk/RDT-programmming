# RDT-programmming
Implement RDT 3.0 protocol on UDP.(Homework for Computer Network, Fall 2018)
## How to run

### Create Directory

- Firstly, just copy the directory `socket` to `D:\`.
- Then we have the directory `D:\socket\Sender\` ;  It contains the file to send;
- And the directory `D:\socket\Receiver\`; The file received will be save in here.
- Here we already have `T.txt`  in the directory.

### CountError

- `CountError.cpp` is used to count the wrong bytes of the file. 

- Input : name of the file you want to check.(eg. T.txt)

### UDP without RDT3.0

- Run `UDPReceiver.java` and`UDPSender.java`;

- Enter the hostname and filename according to the guidance. (Just input the green sentences) 

- Then UDPReceiver will receive the `T.txt` and save it to `D:\socket\Receiver\`; 
- Next, run `CountErrors.cpp` to count the error bits and get error rate.


### TCP file transfer

- Run `TCPServer.java` first and then run `TCPClient.java`

- Enter the hostname and filename according to the guidance. (Just input the green sentences) 


- `T.txt` will be received and saved to  `D:\socket\Receiver\`; 
- Run `CountErrors.cpp`  and enter theto count the error bits and get error rate.


### UDP with RDT3.0
- Run `RDTSenderMain` first and then run `RDTReceiver`(Not `RDTSender`)

- Enter the hostname and filename according to the guidance. (Just input the green sentences) 

- `T.txt` will be received and saved to  `D:\socket\Receiver\`; 

- Run `CountErrors.cpp` to count the error bits and get error rate.
