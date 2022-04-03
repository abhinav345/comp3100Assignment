import java.net.*;
import java.io.*;
import java.util.*;

class MyClient1 {

    public static void main(String args[]) throws Exception {
        Socket s = new Socket("127.0.0.1", 50000);
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

        System.out.println("GETS Capable " + JobInfo[4] + "  " + JobInfo[5] + " " + JobInfo[6]);
        dout.write(("GETS Capable " + JobInfo[4] + " " + JobInfo[5] + " " + JobInfo[6] + "\n").getBytes());
        dout.flush();
        str = din.readLine();
        System.out.println("Server says " + str);

        String[] GETSinfo = str.split(" ");
        int NumServers = Integer.parseInt(GETSinfo[1]);

        System.out.println("Number of servers " + NumServers);

        dout.write("OK\n".getBytes());
        dout.flush();

        //code to store the server(s) data
        ArrayList<ArrayList<String>> serverList = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < NumServers; i++) {
            str = din.readLine();

            String[] serverInfo = str.split(" ");
            String serverName = serverInfo[0];
            String serverID = serverInfo[1];
            String cores = serverInfo[4];
            ArrayList<String> arr = new ArrayList<>();
            arr.add(serverName);
            arr.add(serverID);
            arr.add(cores);
            serverList.add(arr);
            System.out.println(str);
        }

        //code to find the largest server
        int[] arrCores = new int[serverList.size()];
        for(int i=0;i<serverList.size();i++) {
            arrCores[i] = Integer.parseInt(serverList.get(i).get(2));
            Arrays.sort(arrCores);
        }
        ArrayList<Integer> tempList = new ArrayList<Integer>();
        for(int i=0;i<serverList.size();i++) {
            int largest = arrCores[arrCores.length-1];
            if(Integer.parseInt(serverList.get(i).get(2)) == largest) {
                tempList.add(i);
            }
        }
        System.out.println("Largest Server(s) is/are : ");
        for(int i = 0; i<tempList.size();i++) {
            System.out.println(serverList.get(tempList.get(i)));
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
