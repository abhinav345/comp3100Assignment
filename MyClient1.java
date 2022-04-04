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

        String str1="";
        dout.write(("REDY\n").getBytes());
        dout.flush();
        str = din.readLine();
        System.out.println("Server Says: " + str);
        String[] tempInfo = str.split(" ");
        str1 = tempInfo[0];

        String[] JobInfo = str.split(" ");
        System.out.println("GETS Capable " + JobInfo[4] + "  " + JobInfo[5] + " " + JobInfo[6]);
        dout.write(("GETS Capable " + JobInfo[4] + " " + JobInfo[5] + " " + JobInfo[6] + "\n").getBytes());
        dout.flush();
        str = din.readLine();
        System.out.println("Server says " + str);
        String[] GETSInfo = str.split(" ");
        int numServers = Integer.parseInt(GETSInfo[1]);

        dout.write(("OK\n").getBytes());
        dout.flush();

        ArrayList<ArrayList<String>> serverList = new ArrayList<ArrayList<String>>();
        for(int i=0;i<numServers;i++) {
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
            System.out.println("Server says :" + str);
        }

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
        //as of now, tempList has the indexs of all the servers with largest cores.
        //call that index from serverList for largest server
        dout.write(("OK\n").getBytes());
        dout.flush();
        str = din.readLine();
        System.out.println("Server Says: " + str);

        int count=0;
        while(!(str1.equalsIgnoreCase("NONE"))) {
            if(count==0) {
                dout.write(("SCHD " + JobInfo[2] + " " + serverList.get(tempList.get(0)).get(0) + " " + serverList.get(tempList.get(0)).get(1) + "\n").getBytes());
                str = din.readLine();
                System.out.println("Job information back from the server is " + str);
            }
            else {
                dout.write(("REDY\n").getBytes());
                dout.flush();
                str = din.readLine();
                JobInfo = str.split(" ");
                str1 = JobInfo[0];
                if(str1.equalsIgnoreCase("JOBN")) {
                    System.out.println("Server Says: " + str);
                    dout.write(("SCHD " + JobInfo[2] + " " + serverList.get(tempList.get(0)).get(0) + " " + serverList.get(tempList.get(0)).get(1) + "\n").getBytes());
                    str = din.readLine();
                    System.out.println("Job information back from the server is " + str);
                }
            }
            count++;
        }

        dout.write("QUIT\n".getBytes());
        dout.flush();
        str = din.readLine();
        System.out.println("Server Says: " + str);

        din.close();
        dout.close();
        s.close();
    }
}