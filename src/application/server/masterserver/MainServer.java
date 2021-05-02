package application.server.masterserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import application.enity.AddressNet;

public class MainServer {
	public static void main(String[] args) throws IOException {		
		AddressNet addr = new AddressNet(InetAddress.getByName("localhost"), 5000);
		MasterServer server = new MasterServer(addr);
		server.operation();
	}
}
