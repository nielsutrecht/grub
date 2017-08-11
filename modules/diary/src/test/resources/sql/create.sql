CREATE TABLE IF NOT EXISTS ingredients (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    kiloCalories INT NOT NULL);

CREATE UNIQUE INDEX IF NOT EXISTS idxIngredientName ON ingredients(name);

CREATE TABLE IF NOT EXISTS portions (
    ingredient_id UUID,
    name VARCHAR(255) NOT NULL,
    fraction DOUBLE NOT NULL,
    PRIMARY KEY (ingredient_id, name),
    FOREIGN KEY (ingredient_id) references ingredients(id));

