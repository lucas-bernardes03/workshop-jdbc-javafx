CREATE TABLE star (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(60) DEFAULT NULL,
  StellarClass varchar(15) DEFAULT NULL,
  Mass double DEFAULT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE planet (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(60) NOT NULL,
  Type varchar(20) NOT NULL,
  Diameter double NOT NULL,
  Mass double NOT NULL,
  Gravity double NOT NULL,
  OrbitalSpeed double NOT NULL,
  StarId int(11) NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (StarId) REFERENCES star (id)
);

CREATE TABLE satellite (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(60) NOT NULL,
  PlanetId int(11) NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (PlanetId) REFERENCES planet (id)
);

INSERT INTO star (Name, StellarClass, Mass) VALUES 
  ('Sun', 'G', 1.0),
  ('Betelgeuse', 'M', 14.0),
  ('Saiph', 'B', 15.5),
  ('Vega', 'A', 2.1);

INSERT INTO planet (Name, Type, Diameter, Mass, Gravity, OrbitalSpeed, StarId) VALUES 
  ('Mercury','Rocky',4880,0.55,3.7,47.36,1),
  ('Venus','Rocky',12103,0.81,8.87,35.02,1),
  ('Earth','Rocky',12740,1,9.8,29.78,1),
  ('Mars','Rocky',6780,0.1,3.7,24.07,1),
  ('Jupiter','Gas Giant',139822,317.8,24.79,13.07,1),
  ('Saturn','Gas Giant',116464,95.15,10.44,9.68,1),
  ('Uranus','Ice Giant',50724,14.53,8.69,6.8,1),
  ('Neptune','Ice Giant',49244,17.14,11.15,5.43,1),
  ('Pluto','Dwarf',2376,0.002,0.620,4.73,1);
  
INSERT INTO satellite (Name, PlanetId) VALUES
  ('Moon',3),
  ('Phobos',4),
  ('Deimos',4),
  ('Io',5),
  ('Europa',5),
  ('Ganymede',5),
  ('Callisto',5),
  ('Titan',6),
  ('Rhea',6),
  ('Iapetus',6),
  ('Dione',6),
  ('Tethys',6),
  ('Enceladus',6),
  ('Mimas',6),
  ('Titania',7),
  ('Oberon',7),
  ('Umbriel',7),
  ('Ariel',7),
  ('Triton',8),
  ('Proteus',8),
  ('Charon',9);
  