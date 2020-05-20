import java.io.*; 
import java.util.*; 
import java.net.*; 

public class Bank {
	public static void main(String[] args) throws IOException  
    {    
    	 // Vector to store active clients 
    	static Vector<ClientHandler> ar = new Vector<>(); 
      
    	// counter for clients 
    	static int i = 0; 
        
        // server is listening on port 1234 
        ServerSocket ss = new ServerSocket(1234); 
          
        Socket s; 

		// Bank run infinite loop
        // for getting 
        // client request 
        while (true)  
        { 
		//When request come its assign a new thread to handle comms
  		// Accept the incoming request 
        s = ss.accept();
        System.out.println("New client request received : " + s); 

		// obtain input and output streams 
        DataInputStream dis = new DataInputStream(s.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
        System.out.println("Creating a new handler for this client..."); 
  
        // Create a new handler object for handling this request. 
        ClientHandler mtch = new ClientHandler(s,"client " + i, dis, dos); 

        // Create a new Thread with this object. 
        Thread t = new Thread(mtch); 
        // add this client to active clients vector 
        ar.add(mtch); 
  
        // start the thread. 
        t.start(); 
  
        // increment i for new client. 
        // i is used for naming only, and can be replaced 
        // by any naming scheme 
        i++; 
		
}
// ClientHandler class 
class ClientHandler implements Runnable  
{ 
    Scanner scn = new Scanner(System.in); 
    private String name; 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
      
    // constructor 
    public ClientHandler(Socket s, String name, 
                            DataInputStream dis, DataOutputStream dos) { 
        this.dis = dis; 
        this.dos = dos; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
    } 
  
    @Override
    public void run() { 
  
        String received; 
        PrintWriter outF = new PrintWriter(new BufferedWriter(new FileWriter("theFile" + this.name + ".txt")));


        mc.dos.writeUTF("Hello from server - "+this.name+" : ");
        while (true)  
        { 
            try
            { 
                // receive the string 
                received = dis.readUTF(); 
                  
                System.out.println(received); 
                  
                if(received.equals("logout")){ 
                    this.isloggedin=false; 
                    this.s.close(); 
                    break; 
                } 
                //Server prints the file on the screen,
                System.out.println(recieved);

				//Server saves the file in a local system,
   				outF.write(received);

            } catch (IOException e) { 
                  
                e.printStackTrace(); 
            } 
              
        } 
        //Server appends a new line
        outF.newLine();

 		//Server sends the updated file back to the client.
 		mc.dos.writeUTF(outF);
 		outF.flush();
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
        mc.dos.writeUTF("Bye from server - "+this.name+" : ");
    } 
} 

