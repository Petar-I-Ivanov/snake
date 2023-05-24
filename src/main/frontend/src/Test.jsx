import { Router, Routes, Route } from '@solidjs/router';

import App from './App';
import Game from './Game';
import Result from './Result';

function Test() {

    return (
        <Router>
            <Routes>
                <Route path="/test" element={<div>Test</div>} />
                <Route path="/" component={App} />
                <Route path="/game/:gameId" component={Game} />
                <Route path="/result/:gameId" component={Result} />
            </Routes>
        </Router>
    );
}

export default Test;