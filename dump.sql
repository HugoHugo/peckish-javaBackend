--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'SQL_ASCII';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: mca_i18_pantry; Type: SCHEMA; Schema: -; Owner: rab
--

CREATE SCHEMA mca_i18_pantry;


ALTER SCHEMA mca_i18_pantry OWNER TO rab;

SET search_path = mca_i18_pantry, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: iinr; Type: TABLE; Schema: mca_i18_pantry; Owner: belezn1; Tablespace: 
--

CREATE TABLE iinr (
    r_id integer,
    i_id integer,
    amount text DEFAULT 'a pinch'::text
);


ALTER TABLE mca_i18_pantry.iinr OWNER TO belezn1;

--
-- Name: ingredients; Type: TABLE; Schema: mca_i18_pantry; Owner: belezn1; Tablespace: 
--

CREATE TABLE ingredients (
    i_id integer NOT NULL,
    name text,
    type text
);


ALTER TABLE mca_i18_pantry.ingredients OWNER TO belezn1;

--
-- Name: recipes; Type: TABLE; Schema: mca_i18_pantry; Owner: belezn1; Tablespace: 
--

CREATE TABLE recipes (
    r_id integer NOT NULL,
    rname text,
    steps text,
    numingredients integer,
    rating double precision,
    cooktime text,
    serving text,
    url text DEFAULT 'https://wp.stolaf.edu/'::text,
    imageurl text DEFAULT 'https://cdn.pixabay.com/photo/2013/11/24/10/40/dessert-216870_960_720.jpg'::text,
    source text DEFAULT 'AllRecipes.com'::text
);


ALTER TABLE mca_i18_pantry.recipes OWNER TO belezn1;

--
-- Data for Name: iinr; Type: TABLE DATA; Schema: mca_i18_pantry; Owner: belezn1
--

COPY iinr (r_id, i_id, amount) FROM stdin;
0	1	1/2 tsp
0	3	2 cups
0	4	2 Tbsp
0	5	1/2 tsps
0	6	1 pinch
0	7	1.5 cups shredded
1	8	1 pound
1	9	3/4 pound
1	10	1/2 cup
1	11	2 cloves, crushed
1	12	1 (28 ounce) can
1	13	2 (6 ounce) cans
1	14	2 (6.5 ounce) cans
1	0	1/2 cup
1	15	2 tablespoons
1	16	1 1/2 teaspoons
1	17	1/2 teaspoon
1	18	1 teaspoon
1	1	1 tablespoon
1	19	1/4 teaspoon
1	20	4 tablespoons
1	21	12
1	22	16 ounces
14	78	2
2	26	1 tablespoon
2	27	1 , finely chopped
2	11	1 clove, minced
2	28	2 (8 ounce) cans
2	29	1 teaspoon
2	30	
2	31	1 tablespoon
2	0	1 cup
3	32	1 (28 ounce) can
3	12	1 (28 ounce) can
3	9	1 pound
3	33	2 , chopped
3	34	2 , chopped
3	11	5 cloves, chopped
3	15	2 tablespoons
3	35	1 tablespoon
3	29	1/2 teaspoon
3	30	
4	36	1 (16 ounce) package
4	37	2
4	38	1/4 cup
4	39	
4	40	1 (16 ounce) package, shredded
4	4	2 tablespoons, sliced
5	9	1 pound
5	41	1 cup
5	42	1/2 cup
5	43	1 cup
5	44	1 (28 ounce) can, with liquid
5	45	12 ounces, broken into pieces
5	0	1 cup
5	18	1 1/2 teaspoons
5	30	
5	46	1 cup
6	9	1 pound
6	47	1 (1 ounce) package
6	29	1 teaspoon
6	44	1 (28 ounce) can, undrained and chopped
6	0	2 cups
6	36	2 cups
6	25	1/3 cup
6	46	1/2 cup
7	48	1 (8 ounce) package
7	9	1 pound
7	27	1 , chopped
7	49	2 (7 ounce) cans
7	1	1 teaspoon
7	50	1/4 teaspoon
7	51	1/4 teaspoon
7	52	1 cup
7	53	1/2 cup
7	54	1/2 cup
8	55	2 cups
8	56	1/2 cup
8	57	1 , juiced
9	9	1 pound
1	37	1
10	26	1 tablespoon
10	63	4
10	64	1 , chopped
10	65	1 tablespoon
10	11	4 cloves, chopped
10	66	1/4 cup
10	28	1 (15 ounce) can
10	25	2 tablespoons
10	67	1 (6 ounce) can, drained and sliced
11	59	4 pounds
11	34	6
11	11	1 1/2 cloves
11	44	2 (28 ounce) cans
11	68	2 (28 ounce) cans
11	69	1 (28 ounce) can
11	13	2 (16 ounce) cans
11	28	2 (8 ounce) cans
11	70	1/2 cup
11	18	2 tablespoons
12	71	1 pound
12	72	8
12	37	4
12	25	1/2 cup
12	55	1 1/4 cups
12	73	
13	21	12
13	74	5 pounds
13	54	4 cups
13	52	4 cups
13	37	2
13	30	
13	75	1 pound, sliced
13	59	2 , chopped
13	76	2 tablespoons
14	77	6 ounces
14	79	1 1/2 cups
14	80	3 , sliced
14	81	2 tablespoons
14	82	
15	83	1 pound
15	38	1 cup
15	37	2 , beaten
15	84	1 tablespoon
15	60	1 tablespoon
15	39	
15	46	2 cups
15	25	1/2 cup
15	85	3 1/2 cups
15	86	1 (8 ounce) package
16	87	1 pound
16	26	1/3 cup
16	11	1 clove, chopped
16	4	1/4 cup
16	88	2 , quartered and sliced
16	27	1 , chopped
16	89	1 , chopped
16	90	1 (8 ounce) package, sliced
16	29	1 tablespoon
16	91	1 tablespoon
16	30	
17	92	8 ounces
17	26	1/2 cup
17	93	1/2 cup
17	94	1 1/2 teaspoons
17	16	1 1/2 teaspoons
17	29	1 1/2 teaspoons
17	43	3 cups
17	95	15
17	96	3/4 cup
17	53	1/2 cup
17	97	1 (4 ounce) can
18	45	4 ounces
18	4	1/3 cup, divided
18	53	1/2 cup
18	98	1/4 cup
18	99	1/4 cup
18	90	1 (4 ounce) can, drained
18	38	3/4 cup
18	100	1 (8 ounce) package
18	101	2 cups
18	25	1/3 cup
19	102	2 cups
19	1	1/2 teaspoon
19	103	1/4 teaspoon
19	37	3
20	105	20 pounds
20	106	1 (20 ounce) can
20	107	2 (12 fluid ounce) cans
19	0	as needed
\.


--
-- Data for Name: ingredients; Type: TABLE DATA; Schema: mca_i18_pantry; Owner: belezn1
--

COPY ingredients (i_id, name, type) FROM stdin;
0	water	\N
1	salt	\N
2	pepper	\N
3	elbow macaroni	\N
4	butter	\N
5	dijon mustard	\N
6	cayenne pepper	\N
7	sharp cheddar	\N
8	sweet italian sausage	\N
9	lean ground beef	\N
10	minced onion	\N
11	garlic	\N
12	crushed tomatoes	\N
13	tomato paste	\N
14	canned tomato sauce	\N
15	white sugar	\N
16	dried basil leaves	\N
17	fennel seeds	\N
18	italian seasoning	\N
19	ground black pepper	\N
20	chopped fresh parsley	\N
21	lasagna noodles	\N
22	ricotta cheese	\N
24	mozzarella cheese	\N
25	grated parmesan cheese	\N
26	olive oil	\N
27	onion	\N
28	tomato sauce	\N
29	dried oregano	\N
30	and pepper to taste	\N
31	processed cheese sauce	\N
32	stewed tomatoes	\N
33	yellow onions	\N
34	green bell peppers	\N
35	dried basil	\N
36	macaroni	\N
37	eggs	\N
38	milk	\N
39	black pepper to taste	\N
40	sharp cheddar cheese	\N
41	chopped onions	\N
42	chopped green bell peppers	\N
43	sliced mushrooms	\N
44	whole peeled tomatoes	\N
45	spaghetti	\N
46	shredded mozzarella cheese	\N
47	dry onion soup mix	\N
48	egg noodles	\N
49	tomato sauce with mushrooms	\N
50	black pepper	\N
51	ground cinnamon	\N
52	cottage cheese	\N
53	chopped green onions	\N
54	shredded cheddar cheese	\N
55	heavy cream	\N
56	all-purpose flour	\N
57	lemon	\N
58	dry bread crumbs	\N
59	onions	\N
60	dried parsley	\N
61	condensed tomato soup	\N
62	lemon juice	\N
63	roma (plum) tomato	\N
64	green bell pepper	\N
65	chopped fresh cilantro	\N
66	chopped white onion	\N
67	black olives	\N
68	peeled and diced tomatoes	\N
69	tomato puree	\N
70	vegetable oil	\N
71	dry fettuccini noodles	\N
72	slices bacon	\N
73	black pepper to taste (optional)	\N
74	potatoes	\N
75	bacon	\N
76	margarine	\N
77	dry penne pasta	\N
79	fresh broccoli florets	\N
80	green onions	\N
81	canola oil	\N
82	pepper to taste	\N
83	dry vermicelli pasta	\N
84	garlic salt	\N
85	spaghetti sauce	\N
86	sliced pepperoni sausage	\N
87	farfalle (bow tie) pasta	\N
88	small zucchini	\N
89	tomato	\N
90	mushrooms	\N
91	paprika	\N
92	rotini pasta	\N
93	red wine vinegar	\N
94	garlic powder	\N
95	halved cherry tomatoes	\N
96	crumbled feta cheese	\N
97	chopped black olives	\N
98	sliced green bell pepper	\N
99	sliced red bell peppers	\N
100	cream cheese	\N
101	diced cooked ham	\N
102	durum wheat flour	\N
103	baking powder	\N
105	bone-in ham	\N
106	sliced pineapple	\N
107	beer	\N
78	skinless, boneless chicken breast halves	\N
\.


--
-- Data for Name: recipes; Type: TABLE DATA; Schema: mca_i18_pantry; Owner: belezn1
--

COPY recipes (r_id, rname, steps, numingredients, rating, cooktime, serving, url, imageurl, source) FROM stdin;
1	World's Best Lasagna Recipe	Directions: In a Dutch oven, cook sausage, ground beef, onion, and garlic over medium heat until well browned. Stir in crushed tomatoes, tomato paste, tomato sauce, and water. Season with sugar, basil, fennel seeds, Italian seasoning, 1 tablespoon salt, pepper, and 2 tablespoons parsley. Simmer, covered, for about 1 1/2 hours, stirring occasionally.Bring a large pot of lightly salted water to a boil. Cook lasagna noodles in boiling water for 8 to 10 minutes. Drain noodles, and rinse with cold water.  In a mixing bowl, combine ricotta cheese with egg, remaining parsley, and 1/2 teaspoon salt.Preheat oven to 375  degrees F (190 degrees C).To assemble, spread 1 1/2 cups of meat sauce in the bottom of a 9x13 inch baking dish.  Arrange 6 noodles lengthwise over meat sauce. Spread with one half of the ricotta cheese mixture. Top with a third of mozzarella cheese slices. Spoon 1 1/2 cups meat sauce over mozzarella, and sprinkle with 1/4 cup Parmesan cheese.  Repeat layers, and top with remaining mozzarella and Parmesan cheese. Cover with foil: to prevent sticking, either spray foil with cooking spray, or make sure the foil does not touch the cheese.Bake in preheated oven for 25 minutes. Remove foil, and bake an additional 25 minutes. Cool for 15 minutes before serving.	21	4.7800000000000002	3 h 15 m	12	http://allrecipes.com/recipe/23600/worlds-best-lasagna/?internalSource=streams&referringId=1&referringContentType=recipe%20hub&clickId=st_recipes_mades	http://images.media-allrecipes.com/userphotos/560x315/3359675.jpg	AllRecipes.com
2	Basic Sauce for Pasta Recipe	Directions: In a large skillet over medium heat, saute' onion and garlic in the olive oil for about 5 minutes. Add tomato sauce, oregano, salt, pepper, cheese sauce and water.  Lower heat and simmer until it thickens; about 30 minutes.	8	4.2999999999999998	45 m	4	http://allrecipes.com/recipe/23601/	http://images.media-allrecipes.com/userphotos/250x250/483.jpg	AllRecipes.com
3	Kay's Spaghetti and Lasagna Sauce Recipe	Directions: Blend the stewed tomatoes and crushed tomatoes in a blender. In a stock pot or large kettle, brown the ground beef with the onions, peppers, garlic. Pour in tomatoes, and reduce heat.  Add sugar, basil and oregano, and simmer about 40 minutes. Season with salt and pepper before serving.	10	4.3600000000000003	1 h	8	http://allrecipes.com/recipe/23602/	http://images.media-allrecipes.com/userphotos/250x250/100868.jpg	AllRecipes.com
4	Baked Macaroni Recipe	Directions: Preheat oven to 350 degrees F (175 degrees C).Bring a large pot of lightly salted water to a boil. Add macaroni and cook for 10 to 13 minutes or until al dente; drain.In a bowl mix together the eggs, milk, and pepper.  In a casserole dish, place about a third of the cooked macaroni in one layer, sprinkle with 1/2 of the cheese, then layer the rest of the macaroni and cover with the remaining cheese.  Coat evenly with the egg-milk mixture.  Lastly, cut slices of butter and place them on top of the cheese.Bake in a preheated oven for 30 minutes or until the cheese is completely melted.	6	3.3700000000000001	45 m	8	http://allrecipes.com/recipe/23603/	http://images.media-allrecipes.com/userphotos/560x315/4469944.jpg	AllRecipes.com
5	Spaghetti Skillet Dinner Recipe	Directions: In a skillet over medium heat, brown the ground beef with onions until no pink shows on the beef; drain.  Mix in green pepper and mushrooms and cook for a few minutes.  Add tomatoes with juice, spaghetti and water; stir.  Add Italian seasoning, salt and pepper.  Cook about 15 minutes or until spaghetti is tender, stirring occasionally.  Add cheese and stir until melted	10	4.1500000000000004	30 m	5	http://allrecipes.com/recipe/23604/	http://images.media-allrecipes.com/userphotos/560x315/1104502.jpg	AllRecipes.com
6	Souper Skillet Pasta Recipe	Directions: In a skillet over medium heat, brown the ground beef until no pink shows; drain.Stir in onion soup mix, oregano, tomatoes and water.  Bring to boil; stir in macaroni.  Simmer covered, stirring occasionally for 20 minutes or until macaroni is tender.  Sprinkle in Parmesan cheese and top with Mozzarella cheese.	8	4.2999999999999998	30 m	6	http://allrecipes.com/recipe/23605/	http://images.media-allrecipes.com/userphotos/560x315/3220557.jpg	AllRecipes.com
7	Company Casserole Recipe	Directions: Preheat oven to 350 degrees F (175 degrees C). Bring a large pot of lightly salted water to a boil. Add pasta and cook for 8 to 10 minutes or until al dente; drain.In a skillet over medium heat, brown the ground beef with the onion until no pink shows; drain. Mix in 1 of the cans of tomato sauce, salt, pepper and cinnamon. Pour into shallow 3 quart casserole baking dish.Pour in noodles in an even layer. Top with cottage cheese; sprinkle with onions and Cheddar cheese. Pour on remaining can of tomato sauce.Bake in a preheated over for 30 minutes.	10	3.9500000000000002	50 m	5	http://allrecipes.com/recipe/23606/	http://images.media-allrecipes.com/userphotos/560x315/965327.jpg	AllRecipes.com
8	White Cream Sauce Recipe	Directions: In a saucepan stir together cream, flour and juice from a lemon.  Cook until thickened.	3	2.0600000000000001	15 m	8	http://allrecipes.com/recipe/23607/	http://images.media-allrecipes.com/userphotos/250x250/266607.jpg	AllRecipes.com
9	Meatball Spaghetti Sauce Recipe	Directions: To make meatballs: Combine in a bowl ground beef, bread crumbs, milk, egg, garlic, onions, salt, parsley and pepper; mix well.  Roll into 1 inch balls and set aside.Heat oil in large skillet over medium heat; add meatballs and cook gently so they stay whole and are lightly browned.To make the sauce: Mix together tomato soup, water, lemon juice, salt, parsley, basil, sage, thyme and cayenne pepper in a large saucepan, add meatballs and simmer for 1 hour.	19	3.9700000000000002	1 h 30 m	6	http://allrecipes.com/recipe/23608/	http://images.media-allrecipes.com/userphotos/560x315/265083.jpg	AllRecipes.com
10	Everything in the Fridge Pasta Sauce Recipe	Directions: Heat olive oil in a large skillet and saute tomato, green peppers, cilantro, garlic and onion for 5 minutes or so to retain crispness. Add tomato sauce and simmer for 10 to 15 minutes. Mix in olives, parmesan cheese.	9	4.4699999999999998	30 m	4	http://allrecipes.com/recipe/23609/	http://images.media-allrecipes.com/userphotos/560x315/709804.jpg	AllRecipes.com
11	Pepe Vandel's Spaghetti Sauce Recipe	Directions: Grind onion, pepper and garlic in meat grinder.  In a large saucepan combine ground vegetables, whole tomatoes, diced tomatoes, tomato puree, tomato paste, tomato sauce, vegetable oil and Italian seasoning. Bring to boil; reduce heat to simmer for 4 to 5 hours.  Stir occasionally.	10	4.2699999999999996	4 h 30 m	96	http://allrecipes.com/recipe/23610/	http://images.media-allrecipes.com/userphotos/560x315/5007106.jpg	AllRecipes.com
12	Fettucine Carbonara Recipe	Directions: Bring a large pot of lightly salted water to a boil. Add fettuccini and cook for 8 to 10 minutes or until al dente; drain.Fry bacon in skillet over medium heat until crispy, remove and drain on paper towel.  Chop with knife into bits.Beat the eggs, cheese and cream in a bowl, then add the bacon.  Pour over the pasta in the pan and toss gently using tongs.Return the pan to a very low heat and cook for 1 to 2 minutes, or until slightly thickened.  Don't overheat or the eggs will scramble.  Season well with black pepper and serve.	6	4.1100000000000003	30 m	8	http://allrecipes.com/recipe/23611/	http://images.media-allrecipes.com/userphotos/560x315/1123724.jpg	AllRecipes.com
13	Perogie Casserole Recipe	Directions: Preheat oven to 400 degrees F (200 degrees C).Bring a large pot of lightly salted water to a boil. Add lasagna and cook for 8 to 10 minutes or until al dente; drain.Roast potatoes in oven for about 1 hour.  Split potatoes and scoop out middle; discard skins.  Mix Cheddar cheese into potatoes.  In a bowl, blend together cottage cheese, eggs, salt and pepper; set aside.Layer lasagna noodles in a 9x13 inch baking dish. Spread cottage cheese mixture over noodles.  Layer noodles again and spread potato and cheese mixture to cover noodles. Smooth top.In a skillet, fry bacon until half way cooked. Spread over top of casserole. Sprinkle onions on top. Dot casserole with margarine.Lower oven temperature to 350 degrees F (175 degrees C) and bake for 45 minutes.	9	4.25	2 h 15 m	7	http://allrecipes.com/recipe/23612/	http://images.media-allrecipes.com/userphotos/560x315/1074175.jpg	AllRecipes.com
14	Steph's Summer Salad Recipe	Directions: Bring a large pot of lightly salted water to a boil. Add penne pasta and cook for 8 to 10 minutes, add broccoli a few minutes before pasta is done; drain.Cook the chicken in a pan, seasoning with lemon pepper as it cooks. When the chicken is no longer pink inside, cut into small pieces. Combine chicken to pasta-broccoli mixture. Stir in oil and sliced green onion and add lemon pepper to taste.	6	4.0899999999999999	45 m	2	http://allrecipes.com/recipe/23613/	http://images.media-allrecipes.com/userphotos/560x315/1274907.jpg	AllRecipes.com
15	Pasghetti Pizza Recipe	Directions: Preheat oven to 400 degrees F (200 degrees C).Bring a large pot of lightly salted water to a boil. Break or cut vermicelli pasta into 2 inch pieces and cook for 8 to 10 minutes or until al dente; drain.Spread pasta in a 9x13 inch baking dish that has been greased with vegetable spray or butter.  In a bowl combine milk, eggs, garlic salt, parsley, pepper and 1 cup of  the mozzarella.  Pour over the pasta and sprinkle with the parmesan cheese.Bake for 15 minutes.  Reduce heat to 350 Degrees F.  Pour spaghetti sauce over pasta, top with pepperoni and remaining mozzarella.  Cook until cheese is completely melted; about 10 minutes.	10	4.3799999999999999	1 h 15 m	8	http://allrecipes.com/recipe/23614/	http://images.media-allrecipes.com/userphotos/560x315/3212508.jpg	AllRecipes.com
16	Pasta Pizzaz Recipe	Directions: Bring a large pot of lightly salted water to a boil. Add farfalle pasta and cook for 8 to 10 minutes or until al dente; drain.In a large skillet over medium heat, melt the butter with the olive oil and saute the garlic, zucchini, onion tomato, mushrooms, oregano, paprika, salt and pepper for 15 to 20 minutes.  Combine the pasta and sauteed vegetables and toss.	11	4.4299999999999997	35 m	4	http://allrecipes.com/recipe/23615/	http://images.media-allrecipes.com/userphotos/560x315/4516065.jpg	AllRecipes.com
17	Greek Pasta Salad III Recipe	Directions: Bring a large pot of lightly salted water to a boil. Add rotini pasta and cook for 8 to 10 minutes or until al dente; drain.Mix together cooked pasta, olive oil, vinegar, garlic powder, basil, oregano, mushrooms, tomatoes, Feta cheese, green onions and olives.  Cover and chill for at least 2 hours, serve cold.	11	4.46	2 h 20 m	10	http://allrecipes.com/recipe/23616/	http://images.media-allrecipes.com/userphotos/250x250/945357.jpg	AllRecipes.com
18	Creamy Pasta Primavera Recipe	Directions: Bring a large pot of lightly salted water to a boil. Add pasta and cook for 8 to 10 minutes or until al dente; drain.In a skillet, melt 1/4 cup of the butter.  Saute green onions, green peppers, red peppers and mushrooms.  Add cream cheese and milk and stir over low heat until cream cheese is melted. Stir in ham and parmesan cheese. Toss spaghetti with remaining butter. Combine with cream cheese mixture; toss lightly.	10	3.96	1 h	6	http://allrecipes.com/recipe/23617/	http://images.media-allrecipes.com/userphotos/250x250/573611.jpg	AllRecipes.com
19	Genuine Egg Noodles Recipe	Directions: Combine flour, salt and baking powder.  Mix in eggs and enough water to make the dough workable.  Knead dough until stiff.  Roll into ball and cut into quarters.  Using 1/4 of the dough at a time, roll flat to about 1/8 inch use flour as needed, top and bottom, to prevent sticking.   Peel up and roll from one end to the other.   Cut roll into 3/8 inch strips.  Noodles should be about 4 to 5 inches long depending on how thin it was originally flattened.  Let dry for 1 to 3 hours.Cook like any pasta or,  instead of drying first cook it fresh but make sure water is boiling and do not allow to stick. It takes practice to do this right.	5	4.2599999999999998	3 h 50 m	5	http://allrecipes.com/recipe/23618/	http://images.media-allrecipes.com/userphotos/560x315/966604.jpg	AllRecipes.com
20	Bone-In Ham Cooked in Beer Recipe	Directions: Preheat oven to 325 degrees F (165 degrees C).  Grease an 18 quart roasting pan.Place the ham, with the fattier side up, in the roaster.  Use toothpicks to secure pineapple rings on the ham.  Pour the beer over the ham.  Place lid on roasting pan.Bake 6 to 8 hours, or until cooked through.Remove the pineapple rings and let sit 15 minutes before slicing.	3	4.5700000000000003	8 h 15 m	30	http://allrecipes.com/recipe/23619/	http://images.media-allrecipes.com/userphotos/250x250/382157.jpg	AllRecipes.com
0	Macaroni and Cheese	Directions: Fill the skillet two-thirds full of water, add the salt, and bring to a boil over medium-high heat. Add the macaroni, turn the heat to medium, and cook, stirring occasionally, until just shy of al dente. This should take about 10 minutes, but check the pasta package for recommended cooking times and aim for the lower end if a range is given. (The macaroni will continue to cook a bit in the sauce.) When the macaroni is ready, biting into a piece should reveal a very thin core of uncooked pasta. Drain the macaroni and return it to the skillet. Turn the heat to low. Add the butter and stir until it melts. Add the evaporated milk, mustard, and cayenne and stir well to combine. Add the cheese in three batches, stirring frequently as each batch is added and waiting until the cheese has melted before adding the next batch. After about 5 minutes total, the sauce will be smooth and noticeably thicker. Serve hot. Leftovers can be refrigerated in a covered container for up to 2 days.	6	4.1900000000000004	20 m	2	https://www.budgetbytes.com/2017/04/will-skillet-mac-cheese/	https://www.budgetbytes.com/wp-content/uploads/2017/04/Will-It-Skillet-Mac-and-Cheese-close.jpg	BudgetBytes
\.


--
-- Name: iinr_r_id_key; Type: CONSTRAINT; Schema: mca_i18_pantry; Owner: belezn1; Tablespace: 
--

ALTER TABLE ONLY iinr
    ADD CONSTRAINT iinr_r_id_key UNIQUE (r_id, i_id);


--
-- Name: ingredients_name_key; Type: CONSTRAINT; Schema: mca_i18_pantry; Owner: belezn1; Tablespace: 
--

ALTER TABLE ONLY ingredients
    ADD CONSTRAINT ingredients_name_key UNIQUE (name);


--
-- Name: ingredients_pkey; Type: CONSTRAINT; Schema: mca_i18_pantry; Owner: belezn1; Tablespace: 
--

ALTER TABLE ONLY ingredients
    ADD CONSTRAINT ingredients_pkey PRIMARY KEY (i_id);


--
-- Name: recipes_pkey; Type: CONSTRAINT; Schema: mca_i18_pantry; Owner: belezn1; Tablespace: 
--

ALTER TABLE ONLY recipes
    ADD CONSTRAINT recipes_pkey PRIMARY KEY (r_id);


--
-- Name: iinr_i_id_fkey; Type: FK CONSTRAINT; Schema: mca_i18_pantry; Owner: belezn1
--

ALTER TABLE ONLY iinr
    ADD CONSTRAINT iinr_i_id_fkey FOREIGN KEY (i_id) REFERENCES ingredients(i_id);


--
-- Name: iinr_r_id_fkey; Type: FK CONSTRAINT; Schema: mca_i18_pantry; Owner: belezn1
--

ALTER TABLE ONLY iinr
    ADD CONSTRAINT iinr_r_id_fkey FOREIGN KEY (r_id) REFERENCES recipes(r_id);


--
-- Name: mca_i18_pantry; Type: ACL; Schema: -; Owner: rab
--

REVOKE ALL ON SCHEMA mca_i18_pantry FROM PUBLIC;
REVOKE ALL ON SCHEMA mca_i18_pantry FROM rab;
GRANT ALL ON SCHEMA mca_i18_pantry TO rab;
GRANT ALL ON SCHEMA mca_i18_pantry TO mca_i18_pantry;


--
-- Name: iinr; Type: ACL; Schema: mca_i18_pantry; Owner: belezn1
--

REVOKE ALL ON TABLE iinr FROM PUBLIC;
REVOKE ALL ON TABLE iinr FROM belezn1;
GRANT ALL ON TABLE iinr TO belezn1;
GRANT ALL ON TABLE iinr TO irelan1;
GRANT ALL ON TABLE iinr TO valent1;
GRANT ALL ON TABLE iinr TO radueg1;


--
-- Name: ingredients; Type: ACL; Schema: mca_i18_pantry; Owner: belezn1
--

REVOKE ALL ON TABLE ingredients FROM PUBLIC;
REVOKE ALL ON TABLE ingredients FROM belezn1;
GRANT ALL ON TABLE ingredients TO belezn1;
GRANT ALL ON TABLE ingredients TO irelan1;
GRANT ALL ON TABLE ingredients TO valent1;
GRANT ALL ON TABLE ingredients TO radueg1;


--
-- Name: recipes; Type: ACL; Schema: mca_i18_pantry; Owner: belezn1
--

REVOKE ALL ON TABLE recipes FROM PUBLIC;
REVOKE ALL ON TABLE recipes FROM belezn1;
GRANT ALL ON TABLE recipes TO belezn1;
GRANT ALL ON TABLE recipes TO irelan1;
GRANT ALL ON TABLE recipes TO valent1;
GRANT ALL ON TABLE recipes TO radueg1;


--
-- PostgreSQL database dump complete
--

