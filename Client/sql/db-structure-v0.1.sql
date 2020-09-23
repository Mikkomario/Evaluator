-- Program: E-Valuator
-- Type: Full
-- Version: v0.1-alpha-1

-- Creates a table that records database version updates
CREATE TABLE database_version
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    version VARCHAR(16) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    INDEX dv_version_idx (version),
    INDEX dv_creation_idx (created)

)Engine=InnoDB DEFAULT CHARSET=latin1;

-- Contains used languages
CREATE TABLE `language`
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(2) NOT NULL,

    INDEX language_code_idx (code)

)Engine=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `language` (code) VALUES ('en', 'fi');

-- Contains users profiles
CREATE TABLE `user`
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP

)Engine=InnoDB DEFAULT CHARSET=latin1;

-- Contains test profiles / test results
CREATE TABLE profile
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    language_id INT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT p_u_profile_creator_fk FOREIGN KEY p_u_profile_creator_idx (user_id)
        REFERENCES `user`(id) ON DELETE CASCADE,

    CONSTRAINT p_l_profile_language_fk FOREIGN KEY p_l_profile_language_idx (language_id)
        REFERENCES `language`(id) ON DELETE CASCADE

)Engine=InnoDB DEFAULT CHARSET=latin1;

-- Contains all possible values
CREATE TABLE word
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    text VARCHAR(64) NOT NULL,
    language_id INT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    INDEX w_word_idx (text),

    CONSTRAINT w_l_language_link_fk FOREIGN KEY w_l_language_link_idx (language_id)
        REFERENCES `language`(id) ON DELETE CASCADE

)Engine=InnoDB DEFAULT CHARSET=latin1;

-- Contains general value associations (positive or negative)
CREATE TABLE association_type
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    word_id INT NOT NULL,
    positive BOOLEAN NOT NULL DEFAULT TRUE,
    profile_id INT NOT NULL,

    INDEX at_sign_idx (positive),

    CONSTRAINT at_w_described_word_link_fk FOREIGN KEY at_w_described_word_link_idx (word_id)
            REFERENCES word(id) ON DELETE CASCADE,

    CONSTRAINT at_p_association_creator_fk FOREIGN KEY at_p_association_creator_idx (profile_id)
            REFERENCES profile(id) ON DELETE CASCADE

)Engine=InnoDB DEFAULT CHARSET=latin1;

-- Contains value association strengths (- 0 +)
CREATE TABLE association_strength
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    word_id INT NOT NULL,
    feeling DOUBLE NOT NULL DEFAULT 0.0,
    profile_id INT NOT NULL,

    INDEX as_intensity_idx (feeling),

    CONSTRAINT as_w_described_word_link_fk FOREIGN KEY as_w_described_word_link_idx (word_id)
            REFERENCES word(id) ON DELETE CASCADE,

    CONSTRAINT as_p_association_creator_fk FOREIGN KEY as_p_association_creator_idx (profile_id)
            REFERENCES profile(id) ON DELETE CASCADE

)Engine=InnoDB DEFAULT CHARSET=latin1;

-- Contains means to an end -relationships between values
CREATE TABLE means_relationship
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    means_id INT NOT NULL,
    end_id INT NOT NULL,
    profile_id INT NOT NULL,

    CONSTRAINT mr_w_means_word_link_fk FOREIGN KEY mr_w_means_word_link_idx (means_id)
        REFERENCES word(id) ON DELETE CASCADE,

    CONSTRAINT mr_w_end_word_link_fk FOREIGN KEY mr_w_end_word_link_idx (end_id)
        REFERENCES word(id) ON DELETE CASCADE,

    CONSTRAINT mr_p_relationship_creator_fk FOREIGN KEY mr_p_relationship_creator_idx (profile_id)
        REFERENCES profile(id) ON DELETE CASCADE

)Engine=InnoDB DEFAULT CHARSET=latin1;

-- Lists end values selected by a user
CREATE TABLE end_value
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    word_id INT NOT NULL,
    profile_id INT NOT NULL,

    CONSTRAINT ev_w_word_link_fk FOREIGN KEY ev_w_word_link_idx (word_id)
        REFERENCES word(id) ON DELETE CASCADE,

    CONSTRAINT ev_p_creator_fk FOREIGN KEY ev_p_creator_idx (profile_id)
        REFERENCES profile(id) ON DELETE CASCADE

)Engine=InnoDB DEFAULT CHARSET=latin1;