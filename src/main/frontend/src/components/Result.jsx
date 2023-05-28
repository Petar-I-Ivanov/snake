import { useNavigate, useParams } from '@solidjs/router';
import { createEffect, createSignal } from 'solid-js';

import './Result.css';

function Result() {

  let { gameId } = useParams();
  const navigate = useNavigate();

  const [game, setGame] = createSignal({
    status: '',
    turn: 0
  });

  createEffect(() => {
    const fetchGame = async () => {
      const response = await fetch(`http://localhost:8080/game/${gameId}`);
      const json = await response.json();
      setGame(json);
    };

    fetchGame();
  }, [gameId]);

  function goBack() {
    navigate(`/`);
  }

  const startNewGame = async () => {
    const response = await fetch('http://localhost:8080/game', { method: 'POST' });
    const game = await response.json();
    navigate(`/game/${game.id}`);
  };


  if (!game) return <div>Loading...</div>

  return (
    <div class='result-container'>

      <h1 class='result-heading'>You <span class='status-span'>{game().status}</span> the game in <span class='turn-span'>{game().turn}</span> turns!</h1>

      <button class='btn' onClick={goBack}>Go back</button>
      <button class='btn' onClick={startNewGame}>Start New Game</button>
    </div>
  );
}

export default Result;