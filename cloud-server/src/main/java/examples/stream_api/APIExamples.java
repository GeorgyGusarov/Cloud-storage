package examples.stream_api;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class APIExamples {

    /**
     * примеры Stream API
     * все примеры написаны с помощью лямбды
     */

    public static void main(String[] args) {
        File file = new File("C:\\Programming\\Cloud storage\\cloud-server\\src\\main\\java\\stream_api\\input.txt");

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            in.lines().forEach(System.out::println); // содержимое строк

            System.out.println();

            BufferedReader in1 = new BufferedReader(new FileReader(file));
            in1.lines()
                    .map(String::length) // длинна строк
                    .forEach(System.out::println);

            System.out.println();

            BufferedReader in2 = new BufferedReader(new FileReader(file));
            in2.lines()
                    .map(argument -> argument.replaceAll("e", " "))
                    .forEach(System.out::println); // заменяем все "е" на пустоту

            System.out.println();

            BufferedReader in3 = new BufferedReader(new FileReader(file));
            in3.lines()
                    .flatMap(argument -> Stream.of(argument.split(" "))) // выводим в строку
                    .forEach(argument -> System.out.print(argument + " "));

            System.out.println();

            BufferedReader in4 = new BufferedReader(new FileReader(file));
            List<String> list4 = in4.lines()
                    .flatMap(argument -> Stream.of(argument.split(" "))) // выводим в строку в "листе"
                    .collect(Collectors.toList());
            System.out.println(list4);

            System.out.println();

            BufferedReader in5 = new BufferedReader(new FileReader(file));
            List<String> list5 = in5.lines()
                    .flatMap(argument -> Stream.of(argument.split(" "))) // слитие разрозненных данных в 1 стрим
                    .filter(str -> str.length() > 4) // выводим в строку длинной больше 4
                    .distinct() // выкидывает все повторы
                    .map(String::toUpperCase) // все капсом
                    .map(argument -> argument.replaceAll("E", "O"))
                    .collect(Collectors.toList());
            System.out.println(list5);

//            Optional<String> opt = new Optional<>();
//            opt.orElse("null");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
