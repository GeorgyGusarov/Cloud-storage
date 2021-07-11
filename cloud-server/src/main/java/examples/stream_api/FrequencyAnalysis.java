package examples.stream_api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class FrequencyAnalysis {

    /**
     * пример частотного анализа в Stream API
     * задача: каких слов в каком количестве
     * есть текст, хотим узнать слово, в каком количестве, и выстроим это по чистоте встречаемости слов
     */

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:\\Programming\\Cloud storage\\cloud-server\\src\\main\\java\\stream_api\\input2.txt");
        BufferedReader in = new BufferedReader(new FileReader(file));
        System.out.println("Frequently meeting words: ");
        in.lines()
                .flatMap(s -> Stream.of(s.split(" +")))
                .map(s -> {
                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) == '`') {
                            return s.substring(0, i);
                        }
                    }
                    return s;
                })
                .map(s -> s.toLowerCase().replaceAll("[^a-zA-z0-9]", ""))
                .filter(s -> s.length() > 0)
                .collect(HashMap<String, Integer>::new, (map, s) -> map.put(s, map.getOrDefault(s, 0) + 1), HashMap::putAll)
                .entrySet()
                .stream()
//                .sorted(Comparator.comparingInt(Map.Entry::getValue)) // в порядке возрастания с помощью "сахара"
                .sorted((entry1, entry2) -> - entry1.getValue() + entry2.getValue()) // в порядке убывания
                .forEach((entry) -> System.out.println(entry.getKey() + " : " + entry.getValue()));
    }
}
