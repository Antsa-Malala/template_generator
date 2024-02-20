import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import './Liste.css'

const Liste = () => {
    const [columns, setColumns] = useState([]);
    const [data, setData] = useState([]);

    const fetchItems = () => {
        let url = "http://localhost:8080/personnes";
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState === 4) {
                if (this.status === 200) {
                    let response = JSON.parse(this.response);
                    setData(response);

                    if (response.length > 0) {
                        const objectKeys = Object.keys(response[0]);
                        setColumns(objectKeys);
                    }
                }
            }
        };
        xhttp.open("GET", url, true);
        xhttp.send(null);
    };

    const deleteProducts = ( id ) => {
    let url = "http://localhost:8080/personnes/" + id;
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
      if( this.readyState === 4 ){
        if( this.status === 200 ){
              fetchItems();
              console.log("Ok delete");
        }else{
            const errorResponse = JSON.parse(this.response);
            console.error(`Erreur lors de la suppression : ${errorResponse.erreur}`);
        }
        
      }
    };
    xhttp.open( "DELETE" , url , true );
    xhttp.send(null);
  };

    useEffect(() => {
        fetchItems();
    }, []);

    return (
      <div>
      <Link to="/insert">Ajouter</Link>
        <table>
            <thead>
                <tr>
                    {columns.map((column) => (
                        <th key={column}>{column}</th>
                    ))}
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                {data.map((row, index) => (
                    <tr key={index}>
                        {columns.map((column) => (
                            <td key={column}>{row[column]}</td>
                        ))}
                        <td><Link to={"/update/"+row["id"]}>Modifier</Link></td>
                        <td><button onClick={ () => deleteProducts( row["id"] ) }> Supprimer </button></td>
              </tr>
                ))}
            </tbody>
        </table>
      </div>
    );
};

export default Liste;
