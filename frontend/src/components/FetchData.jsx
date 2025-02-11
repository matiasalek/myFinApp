import React, { useEffect, useState } from "react";

const FetchData = () => {
    const [data, setData] = useState([]);

    useEffect(() => {
        fetch("https://jsonplaceholder.typicode.com/posts?_limit=5")
            .then((response) => response.json())
            .then((data) => setData(data));
    }, []);

    return (
        <div>
            <h3>Fetched Data</h3>
            <ul>
                {data.map((post) => (
                    <li key={post.id}>{post.title}</li>
                ))}
            </ul>
        </div>
    );
};

export default FetchData;