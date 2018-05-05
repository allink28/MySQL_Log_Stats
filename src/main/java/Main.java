import java.io.*;
import java.util.*;

public class Main {

    private static final String INPUT_FILE = "src/main/resources/general.log.3";
    private static HashMap<String, Integer> queryCounts = new HashMap<>(512);

    public static void main(String[] args) throws IOException {
        readLineByLine(INPUT_FILE);
        displayResults();
    }

    private static void displayResults() {
        System.out.println(queryCounts.size() + " entries to sort.");
        List<Map.Entry<String, Integer>> results = new ArrayList<>(queryCounts.entrySet());
        results.sort(Map.Entry.comparingByValue());
//        System.out.println("\n\n");
//        for (int i = results.size()-1; i > -1; --i) {
//            Map.Entry<String, Integer> entry = results.get(i);
//            System.out.println(entry.getValue() + "\t" + entry.getKey());
//        }
        results.stream().map(entry -> entry.getValue() + "\t" + entry.getKey()).forEach(System.out::println);
    }

    private static void readLineByLine(String filePath) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(filePath);
             Scanner scanner = new Scanner(inputStream, "UTF-8")){

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int index = line.indexOf("Query");
                if (index == -1)
                    continue;
                // & 0x255 is equivalent of % 256. 11111111 1+2+4+8+16+32+64+128
                countQueries(line.substring(index+6));
            }
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        }
    }

    private static void countQueries(String line) {
        Integer count = queryCounts.get(line);
        if (count == null) {
            queryCounts.put(line, 1);
        } else {
            queryCounts.put(line, ++count);
        }
    }

}
