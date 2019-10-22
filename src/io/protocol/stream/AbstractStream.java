package io.protocol.stream;

import io.protocol.Packet;

import java.io.*;
import java.net.Socket;

public class AbstractStream {

    private Socket socket;
    private BufferedReader input;
    private boolean closed;
    private DataOutputStream output;
    private boolean debug;

    public AbstractStream(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    public void setDebug(boolean on) {
        this.debug = on;
    }

    public void close() {
        try {
            this.output.flush();
            this.output.close();
            this.input.close();
            this.socket.close();
            this.closed = true;
        } catch (IOException e) {
            System.out.println("Failed to close stream");
        }
    }

    public boolean isOpen() {
        return socket.isConnected() &&  !closed;
    }

    private String readString() throws IOException {
        String read = input.readLine();
        if (debug)
            System.out.println("in: |".concat(read).concat("|"));
        return read;
    }

    private void writeString(String string) throws IOException{
        output.writeBytes(string.concat("\n"));
        if (debug)
            System.out.println("out: |".concat(string).concat("|"));
    }

    public Packet readJSON() throws IOException {
        String payload = readString();
        return Packet.of(payload);
    }

    public void writeJSON(Packet packet) throws IOException {
        writeString(packet.getRawPayload());
    }

    public String getIP() {
        return this.socket.getRemoteSocketAddress().toString().replace("/","").trim();
    }

}
