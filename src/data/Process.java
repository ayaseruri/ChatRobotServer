package data;

import javafx.util.Pair;
import server.ChatInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static data.Data.SPLIT_MARK;

/**
 * Created by wufeiyang on 2017/8/4.
 */
public class Process {

    public static ChatInfo processData(String input) {

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setMine(false);
        chatInfo.setNick("Robot");

        if (input.indexOf(SPLIT_MARK) > 0) {
            String[] inputArray = input.split(SPLIT_MARK);
            Data.Line line = new Data.Line();
            line.setAsk(inputArray[0]);
            line.setAnswer(inputArray[1]);
            Data.write(line);
            chatInfo.setContent("ok, I got it!");
            return chatInfo;
        }

        Pair<Integer, Data.Line> goodOut = null;

        for (String line : Data.readAllLines()) {
            Data.Line lineData = new Data.Line(line);
            String ask = lineData.getAsk();
            int score = getSimilarScore(input, ask);
            if (null == goodOut) {
                goodOut = new Pair<>(score, lineData);
            } else {
                if (lineData.isSp() && input.equals(lineData.getAsk())) {
                    goodOut = new Pair<>(score, lineData);
                    break;
                }

                if (score > goodOut.getKey()) {
                    goodOut = new Pair<>(score, lineData);
                }
            }
        }

        chatInfo.setContent(null == goodOut ? "sorry, i dont know…" : goodOut.getValue().getAnswer());
        return chatInfo;
    }

    private static int getSimilarScore(String input, String data) {
        input = input.replaceAll("\\p{P}", "");
        data = data.replaceAll("\\p{P}", "");

        String[] inputArray = input.split(" ");
        String[] dataArray = data.split(" ");

        List<String> model = new ArrayList<>();

        for (String temp : inputArray) {
            if (!model.contains(temp)) {
                model.add(temp);
            }
        }

        for (String temp : dataArray) {
            if (!model.contains(temp)) {
                model.add(temp);
            }
        }

        int[] inputV = new int[model.size()];
        int[] dataV = new int[model.size()];

        int count;
        for (int i = 0; i < model.size(); i++) {
            count = 0;
            for (String temp : inputArray) {
                if (model.get(i).equals(temp)) {
                    count ++;
                }
            }
            inputV[i] = count;

            count = 0;
            for (String temp : dataArray) {
                if (model.get(i).equals(temp)) {
                    count ++;
                }
            }
            dataV[i] = count;
        }

        int numerator = 0;
        int squareSumInput = 0, squareSumData = 0;
        for (int i = 0; i < inputV.length; i++) {
            numerator += inputV[i] * dataV[i];
            squareSumInput += inputV[i] * inputV[i];
            squareSumData += dataV[i] * dataV[i];
        }

        return (int) (100.0f * numerator / (Math.sqrt(squareSumInput) * Math.sqrt(squareSumData)));
    }
}
