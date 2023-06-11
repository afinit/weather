import React from "react";
import { Link } from "react-router-dom";
import { Form, Segment, Button } from "semantic-ui-react";
import "./index.css";

export interface Props {
    coords: string;
    error: string;
}

export default function QueryForm(props: Props) {

    const [localCoords, setLocalCoords] = React.useState('');
    React.useEffect(() => setLocalCoords(props.coords), [props.coords]);

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
                        {props.error && <div style={{ color: 'red' }}>{props.error}</div>}
                        <Button as={Link} to={{ search: `?latlong=${localCoords}` }}>Submit</Button>
                        <Button as={Link} to={{ search: '' }}>Clear</Button>
                    </Form.Group>
                </Form>
            </Segment>
        </div>
    );
}