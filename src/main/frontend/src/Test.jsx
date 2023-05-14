import { Router, Routes, Route } from '@solidjs/router';

import App from './App';
import Game from './Game';

function Test() {

    return (
        <Router>
            <Routes>
                <Route path="/test" element={<div>Test</div>} />
                <Route path="/" component={App} />
                <Route path="/game/:gameId" component={Game} />
            </Routes>
        </Router>
    );
}

export default Test;