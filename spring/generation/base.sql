drop database template;
CREATE DATABASE template;
CREATE user template WITH PASSWORD 'template';
GRANT ALL PRIVILEGES ON DATABASE "template" to template;
ALTER DATABASE template OWNER TO template;

\q

psql -U template template
template

create table etudiant(
id serial primary key,
nom_etudiant varchar(255),
prenom_etudiant varchar(255),
note double precision,
date_naissance date,
age int
);

INSERT INTO etudiant (nom_etudiant, prenom_etudiant, note, date_naissance, age)
VALUES
  ('Smith', 'John', 15.5, '1995-03-10', 27),
  ('Johnson', 'Mary', 18.2, '1998-07-22', 24),
  ('Brown', 'Peter', 12.8, '1993-05-05', 29),
  ('Davis', 'Sophia', 16.7, '1997-11-15', 25),
  ('Miller', 'Paul', 14.3, '1996-09-28', 26);
