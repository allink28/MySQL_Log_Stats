import java.io.*;
import java.util.*;

public class Main {

    private static final String INPUT_FILE = "src/main/resources/test";
    private static HashMap<String, Integer> queryCounts = new HashMap<>(512);
    private static HashMap<String, Integer> tableHits = new HashMap<>(64);

    public static void main(String[] args) throws IOException {
        readLineByLine(INPUT_FILE);
        displayResults();
    }

    private static void displayResults() {
        System.out.println(queryCounts.size() + " entries to sort.");
        List<Map.Entry<String, Integer>> results = new ArrayList<>(queryCounts.entrySet());
        results.sort(Map.Entry.comparingByValue());
        results.stream().filter(entry -> entry.getValue() > 1)
                .map(entry -> entry.getValue() + "\t" + entry.getKey()).forEach(System.out::println);

        System.out.println("\n**********\nTable hits:\n");
        List<Map.Entry<String, Integer>> tableResults = new ArrayList<>(tableHits.entrySet());
        tableResults.sort(Map.Entry.comparingByValue());
        tableResults.stream().map(entry -> entry.getValue() + "\t" + entry.getKey()).forEach(System.out::println);
    }

    private static void readLineByLine(String filePath) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(filePath);
             Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            int count = 0;
            while (scanner.hasNextLine()) {
                //Periodically print out line number
                // % 2048 is equivalent of & 0x2047. 11111111111 1+2+4+8+16+32+64+128+256+512+1024
                if ((++count & 2047) == 0)
                    System.out.println(count);
                String line = scanner.nextLine();
                int index = line.indexOf("Query");
                if (index == -1) {
                    continue;
                }

                line = line.substring(index+6).toLowerCase();//Start after "Query "
                countQueries(queryCounts, line);

                index = line.indexOf(" from ", 9); //Search after "select _ "
                if (index == -1) {
                    if (!line.startsWith("insert into")) {
                        continue;
                    }
                    line = parseInsertQuery(line.substring(12));
                } else {
                    line = parseSelectQuery(line.substring(index + 6)); //take out " from "
                }

                countQueries(tableHits, line);
            }
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        }
    }

    static String parseInsertQuery(String line) {
        int index = line.indexOf("values");
        if (index == -1) {
//            System.out.println("Weird insert: " + line);
            return "INSERT\t" + line;
        }
        return "INSERT\t" + line.substring(0, index-1);
    }

    static String parseSelectQuery(String line) {
        int index = line.indexOf(' ');
        if (index == -1) {
//            System.out.println("Weird select: " + line);
            return "SELECT\t" + line;
        }
        String table = line.substring(0, index);
        index = line.indexOf("where", index+2);
        line = line.substring(index+6); //col = 'x' and col2='y' limit 1
        line = line.replaceAll("=[^\\s)]+","");
        return "SELECT\t" + table + " " + line;
    }

    private static void countQueries(HashMap<String, Integer> queryMap, String line) {
        Integer count = queryMap.get(line);
        if (count == null) {
            queryMap.put(line, 1);
        } else {
            queryMap.put(line, ++count);
        }
    }

}
