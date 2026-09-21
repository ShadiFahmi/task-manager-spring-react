import { useEffect, useState } from 'react'
import './App.css'

function App() {
  const [users, setUsers] = useState([])
  const [tasks, setTasks] = useState([])
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [editingUserId, setEditingUserId] = useState(null)
  const [taskTitle, setTaskTitle] = useState('')
  const [taskDescription, setTaskDescription] = useState('')
  const [taskStatus, setTaskStatus] = useState('TODO')
  const [taskPriority, setTaskPriority] = useState('MEDIUM')
  const [taskDueDate, setTaskDueDate] = useState('')
  const [taskUserId, setTaskUserId] = useState('')
  const [editingTaskId, setEditingTaskId] = useState(null)

  useEffect(() => {
    fetch('http://localhost:8080/api/users')
      .then(response => response.json())
      .then(data => setUsers(data))
      .catch(error => console.error('Error loading users:', error))

    fetch('http://localhost:8080/api/tasks')
      .then(response => response.json())
      .then(data => setTasks(data))
      .catch(error => console.error('Error loading tasks:', error))
  }, [])

  const handleAddUser = async (event) => {
    event.preventDefault()
    const url = editingUserId ? `http://localhost:8080/api/users/${editingUserId}` : 'http://localhost:8080/api/users'
    const method = editingUserId ? 'PUT' : 'POST'
    const response = await fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: name,
        email: email
      })
    })
    const newUser = await response.json()
    if (editingUserId) {
      setUsers(
        users.map((user) =>
          user.id === editingUserId ? newUser : user
        )
      )
    } else {
      setUsers([...users, newUser])
    }

    setName('')
    setEmail('')
    setEditingUserId(null)
    setName('')
    setEmail('')

    console.log(newUser)
  }
  const handleDeleteUser = async (id) => {
    await fetch(`http://localhost:8080/api/users/${id}`, {
      method: 'DELETE'
    })

    setUsers(users.filter((user) => user.id !== id))
  }
  const handleAddTask = async (event) => {
    event.preventDefault()
    const url = editingTaskId ? `http://localhost:8080/api/tasks/${editingTaskId}` : 'http://localhost:8080/api/tasks'

    const method = editingTaskId ? 'PUT' : 'POST'
    const response = await fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        title: taskTitle,
        description: taskDescription,
        status: taskStatus,
        priority: taskPriority,
        dueDate: taskDueDate,
        user: {
          id: Number(taskUserId)
        }
      })
    })

    const newTask = await response.json()

    if (editingTaskId) {
      setTasks(
        tasks.map((task) =>
          task.id === editingTaskId ? newTask : task
        )
      )} 
    else {
      setTasks([...tasks, newTask])
    }

    setTaskTitle('')
    setTaskDescription('')
    setTaskStatus('TODO')
    setTaskPriority('MEDIUM')
    setTaskDueDate('')
    setTaskUserId('')
    setEditingTaskId(null)
  }

  const handleDeleteTask = async (id) => {
    await fetch(`http://localhost:8080/api/tasks/${id}`, {
      method: 'DELETE'
    })

    setTasks(tasks.filter((task) => task.id !== id))
  }
  
  return (
    <div className="app">
      <h1>Task Manager</h1>

      <section>
        <h2>Users</h2>
        <form onSubmit={handleAddUser}>
          <input
            type="text"
            placeholder="Name"
            value={name}
            onChange={(event) => setName(event.target.value)}
          />

          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
          />

          <button type="submit">
            {editingUserId ? 'Update User' : 'Add User'}
          </button>
        </form>
        {users.length === 0 ? (
          <p>No users found.</p>
        ) : (
          <ul>
            {users.map((user) => (
              <li key={user.id}>
                {user.name} — {user.email}
                <button onClick={() => {
                  setEditingUserId(user.id)
                  setName(user.name)
                  setEmail(user.email)}}>
                  Edit
                </button>
                <button onClick={() => handleDeleteUser(user.id)}>
                  Delete
                </button>
              </li>
            ))}
          </ul>
        )}
      </section>

      <section>
        <h2>Tasks</h2>
        <form onSubmit={handleAddTask}>
          <input
            type="text"
            placeholder="Task title"
            value={taskTitle}
            onChange={(event) => setTaskTitle(event.target.value)}
          />

          <input
            type="text"
            placeholder="Description"
            value={taskDescription}
            onChange={(event) => setTaskDescription(event.target.value)}
          />

          <select
            value={taskStatus}
            onChange={(event) => setTaskStatus(event.target.value)}
          >
            <option value="TODO">TODO</option>
            <option value="IN_PROGRESS">IN PROGRESS</option>
            <option value="COMPLETED">COMPLETED</option>
          </select>

          <select
            value={taskPriority}
            onChange={(event) => setTaskPriority(event.target.value)}
          >
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
          </select>

          <input
            type="date"
            value={taskDueDate}
            onChange={(event) => setTaskDueDate(event.target.value)}
          />

          <select
            value={taskUserId}
            onChange={(event) => setTaskUserId(event.target.value)}
          >
            <option value="">Select User</option>

            {users.map((user) => (
              <option key={user.id} value={user.id}>
                {user.name}
              </option>
            ))}
          </select>

          <button type="submit">
            {editingTaskId ? 'Update Task' : 'Add Task'}
          </button>
        </form>

        {tasks.length === 0 ? (
          <p>No tasks found.</p>
        ) : (
          <ul>
            {tasks.map(task => (
              <li key={task.id}>
                <strong>{task.title}</strong>
                {' — '}
                {task.status}
                {' — '}
                {task.priority}
                <button onClick={() => {
                  setEditingTaskId(task.id)
                  setTaskTitle(task.title)
                  setTaskDescription(task.description || '')
                  setTaskStatus(task.status)
                  setTaskPriority(task.priority)
                  setTaskDueDate(task.dueDate || '')
                  setTaskUserId(String(task.user.id))}}>
                  Edit
                </button>
                <button onClick={() => handleDeleteTask(task.id)}>
                  Delete
                </button>
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  )
}

export default App