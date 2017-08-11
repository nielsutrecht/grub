CREATE TABLE IF NOT EXISTS meals (id UUID PRIMARY KEY);
CREATE TABLE IF NOT EXISTS users(id UUID PRIMARY KEY);

CREATE TABLE IF NOT EXISTS user_meals (
    user_id UUID NOT NULL,
    meal_id UUID NOT NULL,
    date DATE NOT NULL,
    time VARCHAR(10) NOT NULL,
    amount DOUBLE NOT NULL,
    PRIMARY KEY (user_id, meal_id, date, time),
    FOREIGN KEY (user_id) references users(id),
    FOREIGN KEY (meal_id) references meals(id),
);

