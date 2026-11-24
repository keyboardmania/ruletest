"use client";

import { FormEvent, useId, useState } from "react";
import styles from "./TodoList.module.css";

type Todo = {
    id: string;
    title: string;
    completed: boolean;
};

export function TodoList() {
    const titleInputId = useId();
    const [title, setTitle] = useState("");
    const [todos, setTodos] = useState<Todo[]>([]);
    const trimmedTitle = title.trim();

    function createTodo(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        if (trimmedTitle.length === 0) {
            return;
        }

        setTodos((currentTodos) => [
            ...currentTodos,
            {
                id: crypto.randomUUID(),
                title: trimmedTitle,
                completed: false,
            },
        ]);
        setTitle("");
    }

    function toggleTodoCompleted(todoId: string) {
        setTodos((currentTodos) =>
            currentTodos.map((todo) => {
                if (todo.id !== todoId) {
                    return todo;
                }

                return {
                    ...todo,
                    completed: !todo.completed,
                };
            }),
        );
    }

    function deleteTodo(todoId: string) {
        setTodos((currentTodos) => currentTodos.filter((todo) => todo.id !== todoId));
    }

    return (
        <section className={styles.panel}>
            <form className={styles.form} onSubmit={createTodo}>
                <label className={styles.label} htmlFor={titleInputId}>
                    할 일
                </label>
                <div className={styles.controls}>
                    <input
                        id={titleInputId}
                        className={styles.input}
                        value={title}
                        onChange={(event) => setTitle(event.target.value)}
                        placeholder="할 일을 입력하세요"
                    />
                    <button
                        className={styles.addButton}
                        type="submit"
                        disabled={trimmedTitle.length === 0}
                    >
                        등록
                    </button>
                </div>
            </form>
            <ul className={styles.list}>
                {todos.length === 0 ? (
                    <li className={styles.empty}>등록된 할 일이 없습니다.</li>
                ) : (
                    todos.map((todo) => (
                        <li
                            key={todo.id}
                            className={todo.completed ? styles.completedItem : styles.openItem}
                        >
                            <label className={styles.todoLabel}>
                                <input
                                    type="checkbox"
                                    checked={todo.completed}
                                    onChange={() => toggleTodoCompleted(todo.id)}
                                />
                                <span>{todo.title}</span>
                            </label>
                            <button
                                className={styles.deleteButton}
                                type="button"
                                onClick={() => deleteTodo(todo.id)}
                            >
                                삭제
                            </button>
                        </li>
                    ))
                )}
            </ul>
        </section>
    );
}
