create table adventure
(
    name text,
    constraint adventure_pk primary key (name)
);

create table chapter
(
    adventure                    text,
    name                         text,
    subheader                    text,
    approximatedurationinminutes float4,
    constraint chapter_adventure_fk foreign key (adventure) references adventure (name)
        on delete cascade on update cascade,
    constraint chapter_pk primary key (adventure, name)
);

create table record
(
    adventure text,
    chapter   text,
    index     integer check ( index >= 0 ),
    constraint record_chapter_fk foreign key (adventure, chapter) references chapter (adventure, name)
        on delete cascade on update cascade,
    constraint records_pk primary key (adventure, chapter, index)
);

create table picture
(
    adventure            text,
    chapter              text,
    index                integer,
    base64               text,
    fileformat           text,
    isshareablewithgroup bool,
    constraint pictures_pk primary key (adventure, chapter, index),
    constraint pictures_records_fk foreign key (adventure, chapter, index)
        references record (adventure, chapter, index)
        on delete cascade on update cascade
);

create table environment_lightning
(
    adventure  text,
    chapter    text,
    index      integer,
    rgb_1      integer check (rgb_1 between 0 and 255),
    rgb_2      integer check (rgb_2 between 0 and 255),
    rgb_3      integer check (rgb_3 between 0 and 255),
    brightness float4 check (brightness between 0 and 1),
    constraint environment_lightning_pk primary key (adventure, chapter, index),
    constraint environment_lightning_records_fk foreign key (adventure, chapter, index)
        references record (adventure, chapter, index)
        on delete cascade on update cascade
);

create table chapter_link
(
    adventure    text,
    chapter      text,
    index        integer,
    chapter_from text,
    chapter_to   text,
    constraint chapter_link_pk primary key (adventure, chapter, index),
    constraint chapter_link_records_fk foreign key (adventure, chapter, index)
        references record (adventure, chapter, index)
        on delete cascade on update cascade,
    constraint chapter_link_chapter_from_fk foreign key (adventure, chapter_from)
        references chapter (adventure, name)
        on delete cascade on update cascade,
    constraint chapter_link_chapter_to_fk foreign key (adventure, chapter_to)
        references chapter (adventure, name)
        on delete cascade on update cascade
);

create table background_music
(
    adventure text,
    chapter   text,
    index     integer,
    name      text  not null,
    data      bytea not null,
    constraint background_music_pk primary key (adventure, chapter, index),
    constraint background_music_records_fk foreign key (adventure, chapter, index)
        references record (adventure, chapter, index)
        on delete cascade on update cascade
);

create table text
(
    adventure text,
    chapter   text,
    index     integer,
    text      text,
    constraint text_pk primary key (adventure, chapter, index),
    constraint text_records_fk foreign key (adventure, chapter, index)
        references record (adventure, chapter, index)
        on delete cascade on update cascade
);
