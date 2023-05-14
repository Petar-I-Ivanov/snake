import { useNavigate } from '@solidjs/router';

function App() {

  const navigate = useNavigate();

  const startNewGame = async () => {
    const response = await fetch("http://localhost:8080/game", { method: "POST" });
    const game = await response.json();
    navigate(`/game/${game.id}`);
  };

  return (
    <div>
      <h1>Welcome to Snake Game</h1>
      <button onClick={startNewGame}>Start New Game</button>
    </div>
  );
}

export default App;
