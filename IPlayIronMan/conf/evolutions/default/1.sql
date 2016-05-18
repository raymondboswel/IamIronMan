# ironman schema


# --- !Ups

create table `workout` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `distance` DOUBLE,
    `date` DATETIME,
    `time` INT,
    `pace` INT,
    `speed` DOUBLE,
    `maxSpeed` DOUBLE,
    `type` VARCHAR(20),
    `time_of_day` INT,
    `elevation_gain` INT,
    `elevation_loss` INT
)

# --- !Downs
drop table `workout`