/*
 * @Author (Derek Shaheen)
 * @since 8/19/2017
 */

package networkinformation;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class NetworkInformation {
    
    public static void main(String[] args) throws IOException {
        Scanner scnr = new Scanner(System.in);
        boolean breakVal = false;
        while(!breakVal) {
            showMenu();
            switch(scnr.nextInt()) {
                case 1: System.out.println(getLocalIP()); break;
                case 2: System.out.println(getExternalIP()); break;
                case 3: System.out.println(getNetworkInterfaces(false)); break;
                case 4: System.out.println(getNetworkInterfaces(true)); break;
                case 5: writeToFile(); break;
                case 0: breakVal = true; break;
                default: System.out.println("Invalid, please try again!"); break;
            }
        }
        System.out.println(") Exiting. Made by -> Derek Shaheen 2017");
    }
    
    public static void showMenu() {
        System.out.println("1) Return Local IP");
        System.out.println("2) Return External IP");
        System.out.println("3) View All Network Interfaces");
        System.out.println("4) View Active Network Interfaces");
        System.out.println("5) Write information to file");
        System.out.println("0) Exit");
        System.out.print(") Enter a value for network information: ");
    }
    
    public static String getLocalIP() {
        InetAddress localIP;
        try {
            localIP = InetAddress.getLocalHost();
            return "Local IP: " + toString(localIP);
        } catch (UnknownHostException ex) {
            return "Failed to get local IP";
        }
    }
    
    public static String getExternalIP() throws IOException {
        Pattern pattern = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b"); // Regex to validate IPv4 address // Credit for this pattern goes to this page: http://www.regular-expressions.info/ip.html accessed 8/16/17.
        URL externalIP;
        String pageResult;
        Matcher patternMatcher;
        try {
            externalIP = new URL("http://api.ipify.org/");
            BufferedReader inFile = new BufferedReader(new InputStreamReader(externalIP.openStream()));
            while ((pageResult = inFile.readLine()) != null) { // There should only be 1 line, but lets check the whole buffer just in case.
                patternMatcher = pattern.matcher(pageResult);
                if (patternMatcher.find()) { // check each line against the IPv4 pattern
                    return "External IP: " + pageResult;
                }
            }   
        } catch (MalformedURLException | UnknownHostException ex) {
            return "Error: " + ex;
        }
        return "Invalid Result: " + pageResult; // if no lines from the webpage match our regex, then we did not return an IP - invalid result.
    }
    
    public static String getNetworkInterfaces(boolean onlyUp) throws SocketException {
        String buffer = "";
        Enumeration<NetworkInterface> netInterface = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface interf : Collections.list(netInterface)) {
            if ((onlyUp) && (interf.isUp())) { buffer += "\nAdapter: " + interf.getName() + "\tDisplay name: " + interf.getDisplayName();
            } else if (!onlyUp) { buffer += "\nAdapter: " + interf.getName() + "\tDisplay name: " + interf.getDisplayName(); }
        }
        return buffer;
    }
    
    public static void writeToFile() {
        String fileName = "NetworkInformation.txt";
        try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {
            writer.println(getLocalIP());
            writer.println(getExternalIP());
            writer.println(getNetworkInterfaces(false));
            writer.close();
            System.out.println("File written successfully -> " + System.getProperty("user.dir") + "\\" + fileName);
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }
    
    public static String toString(Object o) {
        return o + " ";
    }
    
}
