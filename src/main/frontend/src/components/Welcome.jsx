import { useNavigate } from '@solidjs/router';
import './Welcome.css';

function Welcome() {

  const navigate = useNavigate();

  const startNewGame = async () => {
    const response = await fetch('http://localhost:8080/game', { method: 'POST' });
    const game = await response.json();
    navigate(`/game/${game.id}`);
  };

  return (
    <div class='welcome-container'>
      <h1 class='welcome-heading'>Welcome to Snake Game</h1>
      <button class='welcome-btn' onClick={startNewGame}>Start New Game</button>
    </div>
  );
}

export default Welcome;
