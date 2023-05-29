package ui;

import entities.Order;
import entities.User;
import repositories.CriteriaTestRepo;
import repositories.UserRepo;
import java.util.*;

public class UserMenu {
    private static final List<String> OPTIONS = new ArrayList<>();

    static {
        OPTIONS.add("1. Create new user");
        OPTIONS.add("2. Delete user by ID");
        OPTIONS.add("3. Get all users");
        OPTIONS.add("4. Get user by ID");
        OPTIONS.add("5. Get user orders");
        OPTIONS.add("6. Create new order");
        OPTIONS.add("0. Exit");
    }

    public void work() {
        int option;
        OPTIONS.forEach(System.out::println);
        do {
            option = Console.getOption();
            switch (option) {
                case 1 -> System.out.println(UserRepo.save(Console.createUser()));
                case 2 -> System.out.println(UserRepo.delete(Console.getId("user")));
                case 3 -> Console.UsersView.showResultList(UserRepo.getAll());
                case 4 -> Console.UsersView.showResult(UserRepo.getById(Console.getId("user")));
                case 5 -> Console.UsersView.showResultList(UserRepo.getOrders(Console.getId("user")));
                case 6 -> {
                    User user = UserRepo.getById(Console.getId("user"));
                    if (user == null) System.out.println("User with this ID doesn't exist");
                    else {
                        Order order = Console.createOrder();
                        System.out.println(UserRepo.addOrder(user.getId(), order));
                    }

                }
                //temporary functions just to test how Criteria API works
                case 7 -> Console.UsersView.showResultList(CriteriaTestRepo.getAllUsers());
                case 8 -> Console.UsersView.showResult(CriteriaTestRepo.getUserById(Console.getId("user")));
                case 10 -> {
                    long id = Console.getId("user");
                    System.out.println("Enter new username");
                    String name = Console.getString();
                    Console.UsersView.showResult(CriteriaTestRepo.changeUsername(id, name));
                }
                //end
                case 9 -> OPTIONS.forEach(System.out::println);
            }
            if (option != 9 && option != 0) {
                System.out.println("9. Show options");
                System.out.println("0. Exit");
            }
        } while (option != 0);
    }
}
