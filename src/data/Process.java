package data;

import javafx.util.Pair;
import server.ChatInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static data.Data.SPLIT_MARK;
import static data.Data.readAllLines;

/**
 * Created by wufeiyang on 2017/8/4.
 */
public class Process {

    private static final int VOTE_SOCRE_DIF = 5;

    public static ChatInfo processData(ChatInfo inputChatInfo) {
        String input = inputChatInfo.getContent();
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setMine(false);
        chatInfo.setNick("Robot");

        if (input.indexOf(SPLIT_MARK) > 0) {
            String[] inputArray = input.split(SPLIT_MARK);
            Data.Line line = new Data.Line();
            line.setAsk(inputArray[0]);
            line.setAnswer(inputArray[1]);
            Data.write(line, true);
            chatInfo.setContent("ok, I got it!");
            return chatInfo;
        } else if (inputChatInfo.isAdd() || inputChatInfo.isDel()) {
            List<Data.Line> allLines = readAllLines();
            for (Data.Line line : allLines) {
                if (line.getId().equals(inputChatInfo.getmAnswerId())) {
                    if (inputChatInfo.isAdd()) {
                        line.add();
                    } else if (inputChatInfo.isDel()) {
                        line.del();
                    }
                    break;
                }
            }
            Data.write(allLines, false);
            return chatInfo;
        }

        Pair<Integer, Data.Line> goodOut = null;

        for (Data.Line lineData : Data.readAllLines()) {
            String ask = lineData.getAsk();
            if (lineData.getSp() > 0 && input.equals(ask)) {
                goodOut = new Pair<>(100, lineData);
                break;
            } else {
                int score = getSimilarScore(input, ask) + lineData.getVote() * VOTE_SOCRE_DIF;
                if (null == goodOut) {
                    goodOut = new Pair<>(score, lineData);
                } else if (score > goodOut.getKey()) {
                    goodOut = new Pair<>(score, lineData);
                }
            }
        }

        if (null == goodOut) {
            chatInfo.setContent("sorry, i dont know…");
        } else {
            chatInfo.setmAnswerId(goodOut.getValue().getId());
            chatInfo.setContent(goodOut.getValue().getAnswer());
        }

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
