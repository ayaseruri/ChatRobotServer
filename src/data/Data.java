package data;

import javafx.util.Pair;
import server.ChatInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by wufeiyang on 2017/8/3.
 */
public class Data {

    public static final String SPLIT_MARK = "##";

    private static final String mDataPath = new File("").getAbsolutePath() + File.separator + "data.txt";

    static {
        File dataFile = new File(mDataPath);
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized List<String> readAllLines() {
        try {
            return Files.readAllLines(Paths.get(mDataPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static synchronized void write(Line line) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(mDataPath), true));
            bw.write("\n" + line.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Line {

        private String[] mDataPart = new String[]{
                UUID.randomUUID().toString(), "", "", "0"
        };

        public Line(String mline) {
            String[] dataPart = mline.split(SPLIT_MARK);
            for (int i = 0; i < dataPart.length; i++) {
                mDataPart[i] = dataPart[i];
            }
        }

        public Line() {
        }

        private String getId() {
            return mDataPart[0];
        }

        public String getAsk() {
            return mDataPart[1];
        }

        public void setAsk(String ask) {
            mDataPart[1] = ask;
        }

        public String getAnswer() {
            return mDataPart[2];
        }

        public void setAnswer(String answer) {
            mDataPart[2] = answer;
        }

        public boolean isSp() {
            return Integer.valueOf(mDataPart[3]) > 0;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(getId())
                    .append(SPLIT_MARK)
                    .append(getAsk())
                    .append(SPLIT_MARK)
                    .append(getAnswer())
                    .append(SPLIT_MARK)
                    .append(0);
            return builder.toString();
        }
    }
}
