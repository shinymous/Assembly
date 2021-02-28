CREATE TABLE `assembly`.`topic` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(55) NOT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `end_date` DATETIME NOT NULL,
  `closed` BIT(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`));


CREATE TABLE `assembly`.`topic_vote` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `associate_identifier` VARCHAR(11) NOT NULL,
  `topic` BIGINT(20) NOT NULL,
  `vote` SMALLINT(6) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  INDEX `fk_topic_vote_topic_idx` (`topic` ASC),
  CONSTRAINT `fk_topic_vote_topic`
    FOREIGN KEY (`topic`)
    REFERENCES `assembly`.`topic` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
