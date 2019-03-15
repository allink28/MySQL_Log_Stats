import java.io.*;
import java.util.*;

public class HubMain {

    private static final String INPUT_FILE = "src/main/resources/server.log";
    private static HashMap<String, Integer> requestMap = new HashMap<>(1024);

    public static void main(String[] args) throws IOException {
        readLineByLine(INPUT_FILE);
        displayResults();
    }

    private static void displayResults() {
        System.out.println(requestMap.size() + " entries to sort.");
        List<Map.Entry<String, Integer>> results = new ArrayList<>(requestMap.entrySet());
        results.sort(Map.Entry.comparingByKey());
//        results.sort(Map.Entry.comparingByValue());
        results.stream().filter(entry -> entry.getValue() > 1)
                .map(entry -> entry.getKey() + "\t" + entry.getValue()).forEach(System.out::println);

    }

    private static void readLineByLine(String filePath) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(filePath);
             Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            int count = 0;
            while (scanner.hasNextLine()) {
                //Periodically print out line number
                // % 2048 is equivalent of & 0x2047. 11111111111 1+2+4+8+16+32+64+128+256+512+1024
//                if ((++count & 2047) == 0)
//                    System.out.println(count);
                String line = scanner.nextLine();
                int index = line.indexOf("Request received");
//                int index = line.indexOf("PollExchange: 29"); //Succesfull poll responses and responses that they tried to poll too much
//                int index = line.indexOf("x.b.u.Validation:");//x.b.u.Validation: Validation:  66
//                int index = line.indexOf("CollectionPublisherThread: 100");//not quite subscriber pushes
//                int index = line.indexOf("SubscriberPublisherThread:  91");//Subscriber pushes
                if (index == -1) {
                    continue;
                }
                System.out.println(line);
//                line = line.substring(0,29);//Date
                String hourAndMinute = line.substring(11,16);//Hour + minute
                countRequests(requestMap, hourAndMinute);
            }

            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        }
    }


    private static void countRequests(HashMap<String, Integer> requestMap, String hourAndMinute) {
        Integer count = requestMap.get(hourAndMinute);
        if (count == null) {
            requestMap.put(hourAndMinute, 1);
        } else {
            requestMap.put(hourAndMinute, ++count);
        }
    }

}
