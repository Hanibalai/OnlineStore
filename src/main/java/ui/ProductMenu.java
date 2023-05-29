package ui;

import repositories.ProductRepo;
import java.util.ArrayList;
import java.util.List;

public class ProductMenu {
    private static final List<String> OPTIONS = new ArrayList<>();

    static {
        OPTIONS.add("1. Create new product");
        OPTIONS.add("2. Delete product by ID");
        OPTIONS.add("3. Get all products");
        OPTIONS.add("4. Get product by ID");
        OPTIONS.add("5. Get all products from a category");
        OPTIONS.add("0. Exit");
    }

    public void work() {
        int option;
        OPTIONS.forEach(System.out::println);
        do {
            option = Console.getOption();
            switch (option) {
                case 1 -> System.out.println(ProductRepo.save(Console.createProduct()));
                case 2 -> System.out.println(ProductRepo.delete(Console.getId("product")));
                case 3 -> Console.UsersView.showResultList(ProductRepo.getAll());
                case 4 -> Console.UsersView.showResult((ProductRepo.getById(Console.getId("product"))));
                case 5 -> Console.UsersView.showResultList(ProductRepo.getFromCategory(Console.getProductCategory()));
                case 9 -> OPTIONS.forEach(System.out::println);
            }
            if (option != 9 && option != 0) {
                System.out.println("9. Show options");
                System.out.println("0. Exit");
            }
        } while (option != 0);
    }
}
