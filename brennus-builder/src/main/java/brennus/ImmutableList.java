package brennus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ImmutableList<T> implements Iterable<T> {

  public static final <U> ImmutableList<U> empty() {
    return new ImmutableList<U>();
  }

  public static final <U> ImmutableList<U> from(Collection<U> content) {
    return new ImmutableList<U>(new ArrayList<U>(content));
  }

  private final List<T> content;

  private ImmutableList() {
    this.content = Collections.<T>emptyList();
  }

  private ImmutableList(List<T> content) {
    this.content = Collections.unmodifiableList(content);
  }

  @Override
  public Iterator<T> iterator() {
    return content.iterator();
  }

  public ImmutableList<T> append(T o) {
    List<T> newContent = new ArrayList<T>(content);
    newContent.add(o);
    return new ImmutableList<T>(newContent);
  }

  public boolean isEmpty() {
    return content.isEmpty();
  }

  public int size() {
    return content.size();
  }

  public T get(int i) {
    return content.get(i);
  }

  @Override
  public String toString() {
    return content.toString();
  }
}
