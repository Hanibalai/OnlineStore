package ui;

import java.util.ArrayList;
import java.util.List;

public class MainWindow {

    private final List<String> options;

    public MainWindow() {
        options = new ArrayList<>();
        options.add("1. User");
        options.add("2. Product");
        options.add("0. Exit");
    }

    public void work() {
        options.forEach(System.out::println);
        int option;
        do {
            option = Console.getOption();
            switch (option) {
                case 1 -> new UserMenu().work();
                case 2 -> new ProductMenu().work();
            }
            if (option != 0) options.forEach(System.out::println);
        } while (option != 0);
        Console.close();
    }
}
