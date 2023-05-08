import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import RenderGame from "./RenderGame";

function Game() {

    let { gameId } = useParams();

    const [game, setGame] = useState({
        status: "",
        turn: 0,
        map: [...Array(15)].map(e => Array(15))
    });

    useEffect(() => {
        const fetchGame = async () => {
            const response = await fetch(`http://localhost:8080/game/${gameId}`);
            const json = await response.json();
            setGame(json);
        };

        fetchGame();
    }, [gameId]);

    const [error, setError] = useState();

    async function makeAction(e) {
        e.preventDefault();

        const actionValue = e.target.action.value;
        const response = await fetch(`http://localhost:8080/game/${gameId}`, {
            method: 'PUT',
            headers: { 'content-type': 'application/json' },
            body: JSON.stringify({ action: actionValue })
        });

        const json = await response.json();

        console.log(json);

        if (response.status === 200) {
            setGame(json);
            setError(null);
        } else {
            setError(json);
        }
    }

    return (
        <div>
            <RenderGame game={game} />

            <form onSubmit={makeAction}>

                <input
                    type='text'
                    id='action'
                />
                <button>Submit</button>
            </form>
        </div>
    );
}

export default Game;