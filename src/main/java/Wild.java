import java.util.Collection;
import java.util.Vector;

/**
 * Created by Igor_Dobrovolskiy on 19.07.2017.
 */

public class Wild {
    public Collection<?> contents(){
        Collection<String> stuff = new Vector<>();

        stuff.add("a");
        stuff.add("b");
        stuff.add("see");

        return stuff;
    }
}
