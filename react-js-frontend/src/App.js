import { useNavigate } from 'react-router-dom';
import './App.css';

function App() {

  const navigate = useNavigate();

  async function startNewGame() {
    const response = await fetch("http://localhost:8080/game", { method: "POST" });
    const game = await response.json();
    navigate(`/game/${game.id}`);
  }

  return (
    <div>
      <h1>Welcome to Snake Game</h1>
      <button onClick={startNewGame}>Start New Game</button>
    </div>
  );
}

export default App;
