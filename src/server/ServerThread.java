package server;

import com.sun.istack.internal.NotNull;
import data.Data;
import data.Process;
import sun.applet.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wufeiyang on 2017/8/3.
 */
public class ServerThread implements Runnable {

    private Socket mSocket;
    private BufferedReader mBufferedReader;
    private String mTempContent;
    private List<Socket> mChatRoomSoceks;

    public ServerThread(@NotNull Socket mSocket, @NotNull List<Socket> sockets) throws IOException {
        this.mSocket = mSocket;
        mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        mChatRoomSoceks = sockets;
    }

    @Override
    public void run() {
        try {
            while ((mTempContent = mBufferedReader.readLine()) != null) {
                ChatInfo chatInfo = JsonUtil.getGson().fromJson(mTempContent, ChatInfo.class);
                chatInfo.setMine(false);
                if (chatInfo.isChatRoom()) {
                    if (!mChatRoomSoceks.contains(mSocket)) {
                        mChatRoomSoceks.add(mSocket);
                    }

                    for (Socket socket : mChatRoomSoceks) {
                        if (socket != mSocket) {
                            try {
                                socket.getOutputStream().write(getOutput(chatInfo));
                            } catch (IOException e) {
                                e.printStackTrace();
                                if (mChatRoomSoceks.contains(socket)) {
                                    mChatRoomSoceks.remove(socket);
                                }
                            }
                        }
                    }
                } else {
                    mTempContent = chatInfo.getContent();
                    mSocket.getOutputStream().write(getOutput(Process.processData(mTempContent)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getOutput(ChatInfo chatInfo) throws UnsupportedEncodingException {
        return (JsonUtil.getGson().toJson(chatInfo) + "\r\n").getBytes("utf-8");
    }
}
