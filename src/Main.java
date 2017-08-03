import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wufeiyang on 2017/8/3.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(10012);
        while (true) {
            Socket socket = ss.accept();
            new Thread(new ServerThread(socket)).start();
        }
    }
}
