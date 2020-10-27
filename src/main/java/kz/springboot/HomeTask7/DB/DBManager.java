package kz.springboot.HomeTask7.DB;

import java.util.ArrayList;

public class DBManager {

    private static ArrayList<Item> items = new ArrayList<>();

    static {
        items.add(new Item(1L, "IPhone 12", "The iPhone 12 and iPhone 12 mini are Apple's mainstream flagship iPhones for 2020. The phones come in 6.1-inch and 5.4-inch sizes with identical features, including support for faster 5G cellular networks, OLED displays, improved cameras", 400000, 100, 5, "https://mobile-review.com/news/wp-content/uploads/iphone-12-4k-240fps.jpg"));
        items.add(new Item(2L, "MacBook 13 Pro", "Configure your MacBook Pro with these options, only at apple.com: 2.4GHz 8-core Intel Core i9, Turbo Boost up to 5.0GHz, with 16MB shared L3 cache. 32GB or 64GB of 2666MHz DDR4 memory. AMD Radeon Pro 5500M with 4GB of GDDR6 memory.", 1000000, 50, 4, "https://s.appleinsider.ru/2020/05/macosfacetime-750x557.jpg"));
//        items.add(new Item(3L, "Mac mini 10", "The Mac mini (stylized with a lowercase \"mini\") is a desktop computer made by Apple Inc. ... Apple initially marketed it as BYODKM (Bring Your Own Display, Keyboard, and Mouse), pitching it to users switching from a traditional PC.", 570000, 30, 3, "https://www.iphones.ru/wp-content/uploads/2019/01/44FD0338-B699-4C5F-8234-6F86B36F9FA3.jpeg"));
    }

    private static Long id = 3L;

    public static void addItem(Item item) {
        item.setId(id);
        items.add(item);
        id++;
    }

    public static Item getItem(Long id) {

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id))
                return items.get(i);
        }
        return null;
    }

    public static ArrayList<Item> getItems() {
        return items;
    }

}
