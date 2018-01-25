DROP TABLE IinR;
DROP TABLE recipes;
DROP TABLE ingredients;

CREATE TABLE recipes(
	R_id int primary key,
	rname text,
	steps text,
	numIngredients int,
	rating double precision,
	cooktime text,
	serving text,
	url text DEFAULT 'https://wp.stolaf.edu/',
	imageURL text DEFAULT 'https://cdn.pixabay.com/photo/2013/11/24/10/40/dessert-216870_960_720.jpg'
);

CREATE TABLE ingredients(
	I_id int primary key,
	name text UNIQUE
);

CREATE TABLE IinR(
	R_id int references recipes(R_id),
	I_id int references ingredients(I_id),
	amount text DEFAULT 'a pinch',
	unique(R_id, I_id)
);


INSERT INTO ingredients VALUES (1, 'spaghetti');
INSERT INTO ingredients VALUES (2, 'elbow macaroni');
INSERT INTO ingredients VALUES (3, 'cheese');
INSERT INTO ingredients VALUES (4, 'marinara sauce');
INSERT INTO ingredients VALUES (5, 'potatoes');
INSERT INTO ingredients VALUES (6, 'bell peppers');

INSERT INTO recipes (R_id, rname, numIngredients) VALUES (1, 'Mac-n-Cheese', 2);
INSERT INTO IinR VALUES (1,2);
INSERT INTO IinR VALUES (1,3);

INSERT INTO recipes (R_id, rname, numIngredients) VALUES (2, 'Student Pasta', 2);
INSERT INTO IinR VALUES (2,1);
INSERT INTO IinR VALUES (2,4);

INSERT INTO recipes (R_id, rname, numIngredients) VALUES (3, 'WTF Student Pasta', 4);
INSERT INTO IinR VALUES (3,1);
INSERT INTO IinR VALUES (3,4);
INSERT INTO IinR VALUES (3,5);
INSERT INTO IinR VALUES (3,6);

INSERT INTO recipes (R_id, rname, numIngredients) VALUES (4, 'Stuff Andreas doesnt like', 1);
INSERT INTO IinR VALUES (4,5);
