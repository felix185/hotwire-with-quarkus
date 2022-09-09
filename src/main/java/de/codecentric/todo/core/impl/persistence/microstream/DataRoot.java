package de.codecentric.todo.core.impl.persistence.microstream;

/**
 * Root access for object graph.
 *
 * @author Felix Riess, codecentric AG
 * @since 09 Sep 2022
 */
public class DataRoot {

    private final TodoList todoList = new TodoList();

    public DataRoot() {
        super();
    }

    public TodoList getTodoList() {
        return this.todoList;
    }

    @Override
    public String toString() {
        return "DataRoot{" +
                "todoList=" + this.todoList +
                '}';
    }
}
