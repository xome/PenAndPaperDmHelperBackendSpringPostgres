create table chapter
(
    name                         text,
    subheader                    text,
    approximatedurationinminutes float4,
    constraint chapter_pk primary key (name)
);

create table record
(
    chapter_name text references chapter (name) on delete cascade on update cascade,
    position     integer check ( position >= 0 ),
    constraint records_pk primary key (chapter_name, position)
);

create table picture
(
    chapter_name         text,
    position             integer,
    base64               text,
    fileformat           text,
    isshareablewithgroup bool,
    constraint pictures_pk primary key (chapter_name, position),
    constraint pictures_records_fk foreign key (chapter_name, position) references record (chapter_name, position)
        on delete cascade on update cascade
);

create table environment_lightning
(
    chapter_name text,
    position     integer,
    rgb_1        integer check (rgb_1 between 0 and 255),
    rgb_2        integer check (rgb_2 between 0 and 255),
    rgb_3        integer check (rgb_3 between 0 and 255),
    brightness   float4 check (brightness between 0 and 1),
    constraint environment_lightning_pk primary key (chapter_name, position),
    constraint environment_lightning_records_fk foreign key (chapter_name, position) references record (chapter_name, position)
        on delete cascade on update cascade
);

create table chapter_link
(
    chapter_name      text,
    position          integer,
    chapter_name_from text references chapter (name) on delete cascade on update cascade,
    chapter_name_to   text references chapter (name) on delete cascade on update cascade,
    constraint chapter_link_pk primary key (chapter_name, position),
    constraint chapter_link_records_fk foreign key (chapter_name, position) references record (chapter_name, position)
        on delete cascade on update cascade
);

create table background_music
(
    chapter_name text,
    position     integer,
    name         text  not null,
    data         bytea not null,
    constraint background_music_pk primary key (chapter_name, position),
    constraint background_music_records_fk foreign key (chapter_name, position) references record (chapter_name, position)
        on delete cascade on update cascade
);
