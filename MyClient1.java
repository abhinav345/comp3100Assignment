import java.net.*;
import java.io.*;
import java.util.*;

class MyClient {

	public static void main(String args[]) throws Exception {
		Socket s = new Socket("127.0.0.1",50000);
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
	String str;
	
	dout.write(("HELO\n").getBytes());
	dout.flush();
	str = din.readLine();
	System.out.println("Server Says: " + str);
	
	String username = System.getProperty("user.name");
	dout.write(("AUTH " + username + "\n").getBytes());
	dout.flush();
	str = din.readLine();
	System.out.println("Server Says: " + str);	
	
	dout.write("REDY\n".getBytes());
	dout.flush();
	str = din.readLine();
	System.out.println("Server Says: " + str);
	
	String[] JobInfo = str.split(" ");
	int jobId = Integer.parseInt(JobInfo[2]);
	
	System.out.println("GETS Capable " + JobInfo[4] + "  " + JobInfo[5] + 			" " + JobInfo[6]);
	dout.write(("GETS Capable " + JobInfo[4] + " " + JobInfo[5] + " " + 			JobInfo[6] + "\n").getBytes());
	dout.flush();
	str = din.readLine();
	System.out.println("Server says " + str);

    String[] GETSinfo = str.split(" ");
    int NumServers = Integer.parseInt(GETSinfo[1]);

	System.out.println("Number of servers " + NumServers);
	
	dout.write("OK\n".getBytes());
	dout.flush();

    for(int i = 0;i<NumServers;i++) {
        str = din.readLine();
        System.out.println(str);
    }   
    
    dout.write("OK\n".getBytes());
	dout.flush();
	str = din.readLine();
	System.out.println("Server Says: " + str);
	
	dout.write("QUIT\n".getBytes());
	dout.flush();
	str = din.readLine();
	System.out.println("Server Says: " + str); 
	
	din.close();
	dout.close();
	s.close();
	}
}
