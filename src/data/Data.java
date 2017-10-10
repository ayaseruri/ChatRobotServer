package data;

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

    public static synchronized List<Line> readAllLines() {
        try {
            List<Line> resultLine = new ArrayList<>();
            for (String line : Files.readAllLines(Paths.get(mDataPath))) {
                if ("".equals(line)) {
                    continue;
                }
                resultLine.add(new Line(line));
            }
            return resultLine;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static synchronized void write(Line line, boolean isAppend) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(mDataPath), isAppend));
            bw.write("\n" + line.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void write(List<Line> lines, boolean isAppend) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(mDataPath), isAppend));
            for (Line line : lines) {
                bw.write("\n" + line.toString());
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Line {

        private String[] mDataPart = new String[]{
                UUID.randomUUID().toString(), "", "", "0", "0"
        };

        public Line(String mline) {
            String[] dataPart = mline.split(SPLIT_MARK);
            for (int i = 0; i < dataPart.length; i++) {
                mDataPart[i] = dataPart[i];
            }
        }

        public Line() {
        }

        public String getId() {
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

        public int getSp() {
            return Integer.valueOf(mDataPart[3]);
        }

        public int getVote() {
            return Integer.valueOf(mDataPart[4]);
        }

        public void add() {
            int vote = getVote();
            mDataPart[4] = String.valueOf(vote + 1);
        }

        public void del() {
            int vote = getVote();
            mDataPart[4] = String.valueOf(vote - 1);
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
                    .append(getSp())
                    .append(SPLIT_MARK)
                    .append(getVote());
            return builder.toString();
        }
    }
}
