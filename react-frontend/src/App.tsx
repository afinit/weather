import React from 'react';
import WeatherContainer from './containers/WeatherContainer';
import QueryContainer from './containers/QueryContainer';
import { BrowserRouter } from 'react-router-dom';

function App() {
    const [coordsInput, setCoordsInput] = React.useState('');
    const [coordsDisplay, setCoordsDisplay] = React.useState<string|null>(null);

    return (
        <BrowserRouter>
            <QueryContainer
                coordsInput={coordsInput}
                setCoordsInput={setCoordsInput}
                setCoordsDisplay={setCoordsDisplay}
            />
            <WeatherContainer
                coordsDisplay={coordsDisplay}
            />
        </BrowserRouter>
    );
}

export default App;
