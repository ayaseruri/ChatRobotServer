package data;

import javafx.util.Pair;
import server.ChatInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wufeiyang on 2017/8/3.
 */
public class Data {

    public static final String SPLIT_MARK = "##";

    private static List<String> mLines;

    static {
        try {
            String dataPath = new File("").getAbsolutePath() + File.separator + "data.txt";
            File dataFile = new File(dataPath);
            if (!dataFile.exists()) {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            }

            mLines = Files.readAllLines(Paths.get(dataPath));

            if (null == mLines) {
                mLines = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ChatInfo processData(String input) {

        Pair<Integer, Line> goodOut = null;

        for (String line : mLines) {
            Line lineData = new Line(line);
            String ask = lineData.getAsk();
            int score = getSimilarScore(input, ask);
            if (null == goodOut) {
                goodOut = new Pair<>(score, lineData);
            } else {
                if (score > goodOut.getKey()) {
                    goodOut = new Pair<>(score, lineData);
                }
            }
        }

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setMine(false);
        chatInfo.setNick("Robot");
        chatInfo.setContent(null == goodOut ? "sorry, i dont know…" : goodOut.getValue().getAnswer());
        return chatInfo;
    }

    private static int getSimilarScore(String input, String data) {
        return 0;
    }


    private static class Line {

        private String[] mDataPart;

        public Line(String mline) {
            mDataPart = mline.split(SPLIT_MARK);
        }

        private long getId() {
            return Long.valueOf(mDataPart[0]);
        }

        public String getAsk() {
            return mDataPart[1];
        }

        public String getAnswer() {
            return mDataPart[2];
        }
    }
}
