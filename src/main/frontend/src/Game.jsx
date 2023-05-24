import { createEffect, createSignal } from "solid-js";
import { useNavigate, useParams } from "@solidjs/router";
import RenderGame from "./RenderGame";

const Game = () => {

  const { gameId } = useParams();
  const navigate = useNavigate();

  const [game, setGame] = createSignal({
    status: "",
    turn: 0,
    map: Array.from({ length: 15 }, () => Array(15))
  });
  const [error, setError] = createSignal("");

  createEffect(() => {
    const fetchGame = async () => {
      const response = await fetch(`http://localhost:8080/game/${gameId}`);
      const json = await response.json();
      setGame(json);
    };

    fetchGame();
  }, [gameId]);

  const makeAction = async (e) => {

    e.preventDefault();

    const actionValue = e.target.action.value;
    const response = await fetch(`http://localhost:8080/game/${gameId}`, {
      method: 'PUT',
      headers: { 'content-type': 'application/json' },
      body: JSON.stringify({ action: actionValue })
    });

    const json = await response.json();

    if (response.status === 200) {

      if (json.status === "WON" || json.status === "LOST") {
        navigate(`/result/${gameId}`);
      }

      setGame(json);
      setError(null);
    }

    if (response.status === 400) {
        setError("Invalid input!");
    }

    if (response.status === 500) {
        setError("Next possition is unavailable.");
    }

    e.target.action.value = "";
  };

  return (
    <div>
      <RenderGame game={game()} />

      <form onSubmit={makeAction}>
        {error && <p>{error}</p>}
        <input type='text' id='action' />
        <button type='submit'>Submit</button>
      </form>
    </div>
  );
};

export default Game;