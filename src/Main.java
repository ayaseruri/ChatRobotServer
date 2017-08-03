import server.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wufeiyang on 2017/8/3.
 */
public class Main {

    private static List<Socket> mChatRoomSoceks = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(10012);
        while (true) {
            Socket socket = ss.accept();
            new Thread(new ServerThread(socket, mChatRoomSoceks)).start();
        }
    }
}
