package de.codecentric.todo.core.impl.persistence.microstream;

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
