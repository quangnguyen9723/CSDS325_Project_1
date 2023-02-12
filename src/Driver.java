import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.ListIterator;

public class Driver {
    public static void main(String[] args) throws IOException {
        LinkedList<Integer> list = new LinkedList<>(); {
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);
            list.add(5);
        }

        System.out.println(list);
        ListIterator<Integer> li = list.listIterator();

        while (li.hasNext()) {
            int i = li.next();
            if (i == 3) li.remove();
        }

        System.out.println(list);
    }
}
