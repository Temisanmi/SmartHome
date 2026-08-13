-- =========================================================
-- SmartHome initial database schema
-- =========================================================

-- =========================================================
-- Sequences
-- =========================================================

CREATE SEQUENCE user_seq
    START WITH 1
    INCREMENT BY 20;

CREATE SEQUENCE home_seq
    START WITH 1
    INCREMENT BY 20;

CREATE SEQUENCE room_seq
    START WITH 1
    INCREMENT BY 20;

CREATE SEQUENCE device_seq
    START WITH 1
    INCREMENT BY 20;

CREATE SEQUENCE device_log_seq
    START WITH 1
    INCREMENT BY 20;

CREATE SEQUENCE schedule_seq
    START WITH 1
    INCREMENT BY 20;

CREATE SEQUENCE notification_seq
    START WITH 1
    INCREMENT BY 20;


-- =========================================================
-- Users
-- =========================================================

CREATE TABLE app_user (
                          id BIGINT NOT NULL,
                          username VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          created_at TIMESTAMP,
                          otp_code VARCHAR(255),
                          otp_expiry TIMESTAMP,

                          CONSTRAINT pk_app_user PRIMARY KEY (id),
                          CONSTRAINT uk_app_user_username UNIQUE (username),
                          CONSTRAINT uk_app_user_email UNIQUE (email)
);


-- =========================================================
-- Homes
-- =========================================================

CREATE TABLE home (
                      id BIGINT NOT NULL,
                      name VARCHAR(255),
                      address VARCHAR(255),
                      tariff_per_kwh DOUBLE PRECISION,
                      user_id BIGINT,
                      created_at TIMESTAMP,

                      CONSTRAINT pk_home PRIMARY KEY (id),
                      CONSTRAINT uk_home_user UNIQUE (user_id),

                      CONSTRAINT fk_home_user
                          FOREIGN KEY (user_id)
                              REFERENCES app_user (id)
);


-- =========================================================
-- Rooms
-- =========================================================

CREATE TABLE room (
                      id BIGINT NOT NULL,
                      name VARCHAR(255),
                      home_id BIGINT,

                      CONSTRAINT pk_room PRIMARY KEY (id),

                      CONSTRAINT fk_room_home
                          FOREIGN KEY (home_id)
                              REFERENCES home (id)
);


-- =========================================================
-- Base Device
-- =========================================================

CREATE TABLE device (
                        id BIGINT NOT NULL,
                        name VARCHAR(255),
                        online BOOLEAN,
                        room_id BIGINT,
                        power_rating_watts INTEGER,

                        CONSTRAINT pk_device PRIMARY KEY (id),

                        CONSTRAINT fk_device_room
                            FOREIGN KEY (room_id)
                                REFERENCES room (id)
);


-- =========================================================
-- Air Conditioner
-- =========================================================

CREATE TABLE air_conditioner (
                                 id BIGINT NOT NULL,
                                 power_on BOOLEAN,
                                 target_temperature DOUBLE PRECISION,
                                 mode VARCHAR(255),

                                 CONSTRAINT pk_air_conditioner PRIMARY KEY (id),

                                 CONSTRAINT fk_air_conditioner_device
                                     FOREIGN KEY (id)
                                         REFERENCES device (id)
);


-- =========================================================
-- Curtain
-- =========================================================

CREATE TABLE curtain (
                         id BIGINT NOT NULL,
                         open_percentage INTEGER,

                         CONSTRAINT pk_curtain PRIMARY KEY (id),

                         CONSTRAINT fk_curtain_device
                             FOREIGN KEY (id)
                                 REFERENCES device (id)
);


-- =========================================================
-- Gate
-- =========================================================

CREATE TABLE gate (
                      id BIGINT NOT NULL,
                      open BOOLEAN,

                      CONSTRAINT pk_gate PRIMARY KEY (id),

                      CONSTRAINT fk_gate_device
                          FOREIGN KEY (id)
                              REFERENCES device (id)
);


-- =========================================================
-- Refrigerator
-- =========================================================

CREATE TABLE refrigerator (
                              id BIGINT NOT NULL,
                              power_on BOOLEAN,
                              target_temperature DOUBLE PRECISION,
                              current_temperature DOUBLE PRECISION,
                              door_open BOOLEAN,

                              CONSTRAINT pk_refrigerator PRIMARY KEY (id),

                              CONSTRAINT fk_refrigerator_device
                                  FOREIGN KEY (id)
                                      REFERENCES device (id)
);


-- =========================================================
-- Smart Fan
-- =========================================================

CREATE TABLE smart_fan (
                           id BIGINT NOT NULL,
                           power_on BOOLEAN,
                           speed INTEGER,

                           CONSTRAINT pk_smart_fan PRIMARY KEY (id),

                           CONSTRAINT fk_smart_fan_device
                               FOREIGN KEY (id)
                                   REFERENCES device (id)
);


-- =========================================================
-- Smart Light
-- =========================================================

CREATE TABLE smart_light (
                             id BIGINT NOT NULL,
                             power_on BOOLEAN,
                             brightness INTEGER,

                             CONSTRAINT pk_smart_light PRIMARY KEY (id),

                             CONSTRAINT fk_smart_light_device
                                 FOREIGN KEY (id)
                                     REFERENCES device (id)
);


-- =========================================================
-- Smart Lock
-- =========================================================

CREATE TABLE smart_lock (
                            id BIGINT NOT NULL,
                            locked BOOLEAN,

                            CONSTRAINT pk_smart_lock PRIMARY KEY (id),

                            CONSTRAINT fk_smart_lock_device
                                FOREIGN KEY (id)
                                    REFERENCES device (id)
);


-- =========================================================
-- Smart TV
-- =========================================================

CREATE TABLE smart_tv (
                          id BIGINT NOT NULL,
                          power_on BOOLEAN,
                          volume INTEGER,
                          current_input VARCHAR(255),

                          CONSTRAINT pk_smart_tv PRIMARY KEY (id),

                          CONSTRAINT fk_smart_tv_device
                              FOREIGN KEY (id)
                                  REFERENCES device (id)
);


-- =========================================================
-- Smart Window
-- =========================================================

CREATE TABLE smart_window (
                              id BIGINT NOT NULL,
                              open BOOLEAN,
                              open_percentage INTEGER,

                              CONSTRAINT pk_smart_window PRIMARY KEY (id),

                              CONSTRAINT fk_smart_window_device
                                  FOREIGN KEY (id)
                                      REFERENCES device (id)
);


-- =========================================================
-- Thermostat
-- =========================================================

CREATE TABLE thermostat (
                            id BIGINT NOT NULL,
                            target_temperature DOUBLE PRECISION,
                            current_temperature DOUBLE PRECISION,

                            CONSTRAINT pk_thermostat PRIMARY KEY (id),

                            CONSTRAINT fk_thermostat_device
                                FOREIGN KEY (id)
                                    REFERENCES device (id)
);


-- =========================================================
-- Water Heater
-- =========================================================

CREATE TABLE water_heater (
                              id BIGINT NOT NULL,
                              power_on BOOLEAN,
                              target_temperature DOUBLE PRECISION,
                              current_temperature DOUBLE PRECISION,

                              CONSTRAINT pk_water_heater PRIMARY KEY (id),

                              CONSTRAINT fk_water_heater_device
                                  FOREIGN KEY (id)
                                      REFERENCES device (id)
);


-- =========================================================
-- Device Logs
-- =========================================================

CREATE TABLE device_log (
                            id BIGINT NOT NULL,
                            device_id BIGINT,
                            event_type VARCHAR(255),
                            old_value VARCHAR(255),
                            new_value VARCHAR(255),
                            timestamp TIMESTAMP,

                            CONSTRAINT pk_device_log PRIMARY KEY (id),

                            CONSTRAINT fk_device_log_device
                                FOREIGN KEY (device_id)
                                    REFERENCES device (id)
);


-- =========================================================
-- Schedules
-- =========================================================

CREATE TABLE schedule (
                          id BIGINT NOT NULL,
                          device_id BIGINT,
                          schedule_type VARCHAR(255),
                          action VARCHAR(255),
                          action_value VARCHAR(255),
                          execute_at TIMESTAMP,
                          recurring_time TIME,
                          days_of_week VARCHAR(255),
                          active BOOLEAN,
                          last_executed_date DATE,

                          CONSTRAINT pk_schedule PRIMARY KEY (id),

                          CONSTRAINT fk_schedule_device
                              FOREIGN KEY (device_id)
                                  REFERENCES device (id)
);


-- =========================================================
-- Notifications
-- =========================================================

CREATE TABLE notification (
                              id BIGINT NOT NULL,
                              user_id BIGINT,
                              message VARCHAR(255),
                              created_at TIMESTAMP,
                              read BOOLEAN,
                              related_device_id BIGINT,

                              CONSTRAINT pk_notification PRIMARY KEY (id),

                              CONSTRAINT fk_notification_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES app_user (id)
);