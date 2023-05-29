package ui;

import entities.Order;
import entities.Product;
import entities.User;
import repositories.ProductRepo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

class Console {
    private static final BufferedReader reader;

    static {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    static User createUser() {
        System.out.println("Enter a username");
        String name = getString();
        System.out.println("Enter an email");
        String email = getString();
        System.out.println("Enter user address");
        System.out.println("country:");
        String country = getString();
        System.out.println("city:");
        String city = getString();
        System.out.println("street:");
        String street = getString();
        System.out.println("house:");
        String house = getString();
        User.UserAddress userAddress = new User.UserAddress(country, city, street, house);
        return new User(name, email, userAddress);
    }

    static Product createProduct() {
        System.out.println("Enter product name");
        String name = Console.getString();
        System.out.println("Enter product price");
        BigDecimal price = Console.getDecimal();
        return new Product(name, price, getProductCategory());
    }

    static Product.Category getProductCategory() {
        Product.Category[] categories = Product.Category.values();
        System.out.println("Select a product category from the list\n(enter the number)");
        for (int i = 0; i < categories.length; i++)
            System.out.println(i + 1 + ". '" + categories[i] + "'");
        int categoryNumber;
        while ((categoryNumber = getNumber()) > categories.length || categoryNumber < 1)
            System.out.println("Wrong number");
        return categories[categoryNumber - 1];
    }

    static Order createOrder() {
        int option = 1;
        Order order = new Order();
        while (true) {
            if (option == 1) {
                ProductRepo.getFromCategory(getProductCategory()).forEach(System.out::println);
                System.out.println("Select an item to add to the order");
                order.getProducts().add(ProductRepo.getById(getId("product")));
                System.out.println("Product is added");
                System.out.println("0. Save order");
                System.out.println("1. Add new item to order");
            } else if (option == 0) break;
            else System.out.println("Wrong input\nTry again");
            option = getOption();
        }
        return order;
    }

    static long getId(String entity) {
        System.out.println("Enter " + entity + " ID");
        return getNumber();
    }

    static int getOption() {
        System.out.println("Select an option");
        return getNumber();
    }

    static int getNumber() {
        while (true) {
            try {
                return Integer.parseInt(getString());
            } catch (NumberFormatException e) {
                System.out.println("Wrong input\nTry again");
            }
        }
    }

    static BigDecimal getDecimal() {
        while (true) {
            try {
                return new BigDecimal(getString());
            } catch (NumberFormatException e) {
                System.out.println("Wrong input\nTry again");
            }
        }
    }

    static String getString() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("IO exception.\n" + e.getMessage());
        }
        return "";
    }

    static void close() {
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("IO exception.\n" + e.getMessage());
        }
    }

    public static class UsersView {
        static void showResult(Object o) {
            System.out.println("==============================================");
            System.out.println("Result:");
            System.out.println("\t" + o);
            System.out.println("==============================================");
        }

        static void showResultList(List<?> resultList) {
            System.out.println("==============================================");
            System.out.println("Result:");
            for (Object o : resultList) System.out.println("\t" + o);
            System.out.println("==============================================");
        }
    }
}
