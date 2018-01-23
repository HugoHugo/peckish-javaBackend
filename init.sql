
CREATE TABLE recipes(
	R_id int primary key,
	name text,
	description text,
	steps text,
	numIngredients int,
	difficulty int,
	rating int,
	source text
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


INSERT INTO ingredients VALUES (1, 'Spaghetti');
INSERT INTO ingredients VALUES (2, 'Macaroni');
INSERT INTO ingredients VALUES (3, 'Cheese');
INSERT INTO ingredients VALUES (4, 'Marinara Sauce');
INSERT INTO ingredients VALUES (5, 'POTATOES');
INSERT INTO ingredients VALUES (6, 'Bell Peppers');

INSERT INTO recipes (R_id, name, numIngredients) VALUES (1, 'Mac-n-Cheese', 2);
INSERT INTO IinR VALUES (1,2);
INSERT INTO IinR VALUES (1,3);

INSERT INTO recipes (R_id, name, numIngredients) VALUES (2, 'Student Pasta', 2);
INSERT INTO IinR VALUES (2,1);
INSERT INTO IinR VALUES (2,4);

INSERT INTO recipes (R_id, name, numIngredients) VALUES (3, 'WTF Student Pasta', 4);
INSERT INTO IinR VALUES (3,1);
INSERT INTO IinR VALUES (3,4);
INSERT INTO IinR VALUES (3,5);
INSERT INTO IinR VALUES (3,6);

INSERT INTO recipes (R_id, name, numIngredients) VALUES (4, 'Stuff Andreas doesnt like', 1);
INSERT INTO IinR VALUES (4,5);
