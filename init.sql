create table user
(
    id       bigint auto_increment
        primary key,
    email    varchar(255) null,
    password varchar(255) null
);

create table task
(
    executor_id bigint                              null,
    id          bigint auto_increment
        primary key,
    owner_id    bigint                              null,
    description varchar(255)                        null,
    heading     varchar(255)                        null,
    priority    enum ('HIGH', 'MEDIUM', 'LOW')      null,
    status      enum ('WAITING', 'DONE', 'AT_WORK') null,
    constraint FK3tpwichpu8cwc6n31jwetnmbx
        foreign key (executor_id) references user (id),
    constraint FKjspifx5hvv1mc7ybu4qp2wyns
        foreign key (owner_id) references user (id)
);

INSERT INTO user (email, password) VALUES
('xli@gmail.com', 'Qk5ZiNdF@P'),
('colinhall@yahoo.com', '(gDW^Ei29f'),
('chentodd@simmons.biz', 'gb2^UP8y_j'),
('andresmoody@jenkins.info', 'Tzd0cIuf%A'),
('john23@hotmail.com', '%2xBTn8mXa'),
('ksummers@brown-park.com', '7l1O)mj0+#'),
('qenglish@joseph-anderson.com', '1KkVTMci&0');

INSERT INTO task (executor_id, owner_id, description, heading, priority, status) VALUES
(6, 1, 'First then painting economy unit our. Type ever after nor of. While talk itself religious century size. Soldier form cover think. Herself similar interesting back. Or weight training claim.', 'Defense city challenge reality year.', 'LOW', 'DONE'),
(4, 2, 'Shake either loss nation outside notice prevent. Her north first door. Language book ahead. One analysis part hear bring. And my significant take use know process phone. Summer area as development.', 'Final difficult when entire race.', 'LOW', 'AT_WORK'),
(7, 3, 'Require we government that result also church. Dinner low trade high red. Move ahead return new account but.', 'Foot this talk production.', 'HIGH', 'DONE'),
(2, 5, 'Explore new strategies in market analysis. Develop comprehensive reports on current trends.', 'Market Analysis Overview', 'MEDIUM', 'AT_WORK'),
(3, 6, 'Organize team building activities to enhance group dynamics and productivity.', 'Team Building Initiative', 'HIGH', 'WAITING'),
(1, 7, 'Conduct a thorough review of internal software for potential upgrades.', 'Software Review and Upgrade', 'LOW', 'DONE'),
(5, 2, 'Prepare a detailed proposal for the upcoming environmental sustainability project.', 'Environmental Project Proposal', 'HIGH', 'AT_WORK'),
(4, 2, 'Analyze and report on recent customer feedback to improve service quality.', 'Customer Feedback Analysis', 'MEDIUM', 'WAITING');

