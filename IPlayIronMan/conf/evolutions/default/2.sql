# --- !Ups
ALTER TABLE Workout ADD date DATETIME;

# --- !Downs

ALTER TABLE Workout DROP date;