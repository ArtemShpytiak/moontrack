package moontrack.analytics;

@FunctionalInterface
public interface TriConsumer<T, U, W> {
	 void accept(T t, U u, W w);

}
