CREATE DATABASE cine_albertosernaa;
USE cine_albertosernaa;

CREATE TABLE generos (
	id_genero CHAR(3) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE peliculas (
    id_pelicula CHAR(10) PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    duracion INT,
    año_lanzamiento INT,
    edad_recomendada VARCHAR (3),
    id_genero CHAR(3),
    FOREIGN KEY (id_genero) REFERENCES generos(id_genero)
);

INSERT INTO generos (id_genero, nombre) VALUES
('G1', 'Acción'),
('G2', 'Comedia'),
('G3', 'Terror'),
('G4', 'Ciencia Ficción'),
('G5', 'Infantil');

INSERT INTO peliculas (id_pelicula, titulo, duracion, año_lanzamiento, edad_recomendada, id_genero) VALUES
('P1', 'Scarface', 170, '1983', '+18', 'G1'),
('P2', 'Torrente', 97, '1998', '+18', 'G2'),
('P3', 'Annabelle', 98, '2014', '+16', 'G3'),
('P4', 'Harry Potter', 152, '2005', '+12', 'G4'),
('P5', 'Toy Story 4', 100, '2019', '+7', 'G5'),
('P6', 'Interstellar', 169, '2014', '+13', 'G4'),
('P7', 'Creed', 122, '2019', '+18', 'G1'),
('P8', 'Shrek', 90, '2001', '+7', 'G5');