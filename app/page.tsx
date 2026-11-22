import { TodoList } from "@/components/TodoList";

export default function HomePage() {
    return (
        <main className="page">
            <h1>할 일</h1>
            <TodoList />
        </main>
    );
}
