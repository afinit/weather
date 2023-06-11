import React, { useState } from "react";
import { Form, Segment, Button } from "semantic-ui-react";
import "./index.css";

export interface Props {
    coords: string;
    setCoords: React.Dispatch<React.SetStateAction<string>>;
}



export default function QueryForm(props: Props) {

    const [error, setError] = useState('');
    const [localCoords, setLocalCoords] = useState(props.coords);

    const handleSubmit = () => {
        const regex = /^-?([1-8]?[0-9](\.[0-9]{1,6})?|90(\.0{1,6})?),\s*-?(180(\.0{1,6})?|((1[0-7][0-9])|([1-9]?[0-9]))(\.[0-9]{1,6})?)$/;
    
        if (regex.test(localCoords)) {
          setError('');
          props.setCoords(localCoords);
        } else {
          setError('Invalid latitude-longitude value. Please use the format: "latitude, longitude".');
        }
      };

    return (
        <div className="query-form">
            <Segment>
                <Form>
                    <Form.Group>
                        <Form.Input
                            label="Latitude-Longitude"
                            placeholder="Enter comma-separated latitude-longitude value"
                            value={localCoords}
                            onChange={ e => setLocalCoords(e.target.value) }
                        />
                        {error && <div style={{ color: 'red' }}>{error}</div>}
                        <Button onClick={handleSubmit}>Submit</Button>
                    </Form.Group>
                </Form>
            </Segment>
        </div>
    );
}