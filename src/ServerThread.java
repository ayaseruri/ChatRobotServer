import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by wufeiyang on 2017/8/3.
 */
public class ServerThread implements Runnable {

    private Socket mSocket;
    private BufferedReader mBufferedReader;
    private String mTempContent;

    public ServerThread(Socket mSocket) throws IOException {
        this.mSocket = mSocket;
        mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while ((mTempContent = mBufferedReader.readLine()) != null) {
                mSocket.getOutputStream().write((mTempContent + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
