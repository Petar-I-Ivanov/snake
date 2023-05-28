import { Router, Routes, Route } from '@solidjs/router';

import Welcome from './components/Welcome';
import Game from './components/game/Game';
import Result from './components/Result';

function App() {

    return (
        <Router>
            <Routes>
                <Route path='/' component={Welcome} />
                <Route path='/game/:gameId' component={Game} />
                <Route path='/result/:gameId' component={Result} />
            </Routes>
        </Router>
    );
}

export default App;