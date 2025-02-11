import Button from "../components/Button.jsx";
import Counter from "../components/Counter.jsx";
import InputField from "../components/InputField.jsx";
import List from "../components/List.jsx";
import FetchData from "../components/FetchData.jsx";
import React from "react";

const Home = () => {
    return (<div>
        <h1>React Component Playground</h1>
        <h1>Welcome to the Home Page</h1>
        <Button label="Click Me" onClick={() => alert("Button Clicked!")} />
        <Counter />
        <InputField />
        <List items={["Item 1", "Item 2", "Item 3"]} />
        <FetchData />
    </div>);
};

export default Home;
