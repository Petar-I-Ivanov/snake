import './Explanation.css';

function Explanation() {

    return (
        <div class='explanation-container'>
            <h3>Explanation</h3>

            <p>W - forward move</p>
            <p>D - right move</p>
            <p>S - backawrd move</p>
            <p>A - left move</p>

            <p>Burger - immunity food</p>
            <p>Sandwich - growth food</p>
            <p>Pizza - border food</p>
            <p>Cloches - normal / poisonous food</p>
        </div>
    );
}

export default Explanation;