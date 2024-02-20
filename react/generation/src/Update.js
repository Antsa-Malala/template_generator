import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './Update.css';

const Formulaire = () => {
    const { id } = useParams();
    const [item, setItem] = useState({});
    const [formData, setFormData] = useState({});
    const [columns, setColumns] = useState([]);

  const fetchItems = () => {
    let url = "http://localhost:8080/personnes/"+id;
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
      if (this.readyState === 4) {
        if (this.status === 200) {
          let response = JSON.parse(this.response);
          setItem(response);
        }
      }
    };
    xhttp.open("GET", url, true);
    xhttp.send(null);
};

  useEffect(() => {
    fetchItems();
  }, [id]);

 useEffect(() => {
    const objectKeys = Object.keys(item);
    setColumns(objectKeys);
    setFormData(item);
  }, [item]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const xhr = new XMLHttpRequest();
    let url = "http://localhost:8080/personnes/"+id;

    xhr.open("PUT", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                window.location.href = "/";
            } else {
                console.error('Error adding object');
            }
        }
    };

    xhr.send(JSON.stringify(formData));
};

  return (
    <form onSubmit={handleSubmit}>
      {columns.map((column) => (
        <div key={column}>
          {column.toLowerCase() === 'id' ? null : (
            <>
              <label htmlFor={column}>{column}</label>
              <input
                type="text"
                id={column}
                name={column}
                value={formData[column] || ''}
                onChange={handleChange}
              />
            </>
          )}
        </div>
      ))}

      <button type="submit">Submit</button>
    </form>
  );
};

export default Formulaire;
