# ironman schema


# --- !Ups

create table `workout` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `distance` DOUBLE,
    `date` DATETIME,
    `time` INT,
    `pace` INT,
    `speed` DOUBLE,
    `maxSpeed` DOUBLE

)

# --- !Downs
drop table `workout`