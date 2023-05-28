import { useNavigate, useParams } from "@solidjs/router";
import { createEffect, createSignal } from "solid-js";

function Result() {

    let { gameId } = useParams();
    const navigate = useNavigate();

    const [game, setGame] = createSignal({
      status: "",
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
        const response = await fetch("http://localhost:8080/game", { method: "POST" });
        const game = await response.json();
        navigate(`/game/${game.id}`);
      };


      if (!game) return <div>Loading...</div>
      
      return (
        <div>
            <h1>You {game().status} the game in {game().turn} turns!</h1>
            <button onClick={goBack}>Go back</button>
            <button onClick={startNewGame}>Start New Game</button>
        </div>
    );  
}

export default Result;