import './GameDescribe.css'

function GameDescribe(props) {

    return (
        <div class='describe-container'>
            {console.log(props.game)}
            <h3>Description</h3>
            <p>Number of expand: {getNumberExpand(props.game.status)}</p>
            <p>Turn: {props.game.turn}</p>
            
            {props.game.borderActive && <p>Border food is active!</p>}
            {props.game.growthActive && <p>Growth food is active!</p>}
            {props.game.immunityActive && <p>Immunity food is active!</p>}
        </div>
    );
}

function getNumberExpand(status) {

    const levelMap = {
        'LEVEL_ONE': 1,
        'LEVEL_TWO': 2,
        'LEVEL_THREE': 3,
        'LEVEL_FOUR': 4,
        'LEVEL_FIVE': 5,
        'LEVEL_SIX': 6
    };

    return levelMap[status];
}

export default GameDescribe;