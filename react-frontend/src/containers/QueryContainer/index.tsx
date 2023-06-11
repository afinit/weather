import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import QueryForm from "../../components/QueryForm";

export interface Props {
    coordsInput: string;
    setCoordsInput: React.Dispatch<React.SetStateAction<string>>;
    setCoordsDisplay: React.Dispatch<React.SetStateAction<string | null>>;
}

export default function QueryContainer(
    props: Props
) {
    const [error, setError] = useState('');

    let location = useLocation();
    let queryParams = new URLSearchParams(location.search);
    let coordsInputFromQuery = queryParams.get('latlong');
    const setCoordsInput = props.setCoordsInput
    const setCoordsDisplay = props.setCoordsDisplay

    useEffect(() => {
        const regex = /^-?([1-8]?[0-9](\.[0-9]{1,6})?|90(\.0{1,6})?),\s*-?(180(\.0{1,6})?|((1[0-7][0-9])|([1-9]?[0-9]))(\.[0-9]{1,6})?)$/;
        if (coordsInputFromQuery) {
            if (regex.test(coordsInputFromQuery)) {
                setError('');
                setCoordsInput(coordsInputFromQuery);
                setCoordsDisplay(coordsInputFromQuery);
            } else {
                setError('Invalid latitude-longitude value. Please use the format: "latitude, longitude".');
                setCoordsInput(coordsInputFromQuery);
            }
        } else {
            setCoordsInput('');
            setCoordsDisplay(null);
        }
    }, [coordsInputFromQuery, setCoordsInput, setCoordsDisplay])

    return (
        <QueryForm
            coords={props.coordsInput}
            error={error}
        />
    );
}