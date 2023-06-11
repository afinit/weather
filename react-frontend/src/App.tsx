import React, { useState } from 'react';
import QueryForm from './components/QueryForm';
import WeatherContainer from './containers/WeatherContainer';

function App() {
  const [coords, setCoords] = useState("");

  return (
    <div className="App">
      <QueryForm
        coords={coords}
        setCoords={setCoords}
      />

      <div>
        <WeatherContainer
          coords={coords}
        />
      </div>
    </div>
  );
}

export default App;
