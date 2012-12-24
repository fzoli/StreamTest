package streamtest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypedList<E> extends ArrayList<E> {

    private final Class<E> type;

    public TypedList(Class<E> type) {
        this.type = type;
    }
    
    public TypedList(Collection<? extends E> c, Class<E> type) {
        super(c);
        this.type = type;
    }

    @Override
    public E[] toArray() {
        return toArray(this, type);
    }
    
    public static <E> E[] toArray(final List<E> ls, Class<E> clazz) {
        synchronized(ls) {
            E[] array = (E[]) Array.newInstance(clazz, ls.size());
            for (int i = 0; i < array.length; i++) {
                array[i] = ls.get(i);
            }
            return array;
        }
    }

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        strings.add("alma");
        strings.add("körte");
        
        System.out.println(TypedList.toArray(strings, String.class)[0]);
        
        TypedList<String> s = new TypedList<>(strings, String.class);
        System.out.println(s.toArray()[1]);
        
        TypedList<String> strings2 = new TypedList<>(String.class);
        strings2.add("barack");
        System.out.println(strings2.toArray()[0]);
    }
    
}
