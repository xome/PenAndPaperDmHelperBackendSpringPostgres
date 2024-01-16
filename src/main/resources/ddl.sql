create table adventure
(
    id   bigserial,
    name text,
    constraint adventure_pk primary key (id),
    constraint adventure_name_uk unique (name)
);
create table chapter (
    id                              bigserial,
    adventure                       bigint,
    name                            text,
    subheader                       text,
    approximate_duration_in_minutes int,
    constraint chapter_adventure_fk foreign key (adventure) references adventure (id)
        on delete cascade on update cascade,
    constraint chapter_pk primary key (id),
    constraint chapter_adventure_name_uk unique (adventure, name)
);
create table record (
    id         bigserial,
    chapter_id bigint,
    index      integer check ( index >= 0 ),
    type text not null,
    constraint record_chapter_fk foreign key (chapter_id) references chapter (id)
        on delete cascade on update cascade,
    constraint records_pk primary key (id),
    constraint record_uk unique (chapter_id, index)
);
create table picture (
    id                      bigserial,
    record_id               bigint,
    base64                  text,
    file_format             text,
    is_shareable_with_group bool,
    constraint pictures_pk primary key (id),
    constraint pictures_records_fk foreign key (record_id)
        references record (id)
        on delete cascade on update cascade
);
create table environment_lightning (
    id         bigserial,
    record_id  bigint,
    rgb_1      integer check (rgb_1 between 0 and 255),
    rgb_2      integer check (rgb_2 between 0 and 255),
    rgb_3      integer check (rgb_3 between 0 and 255),
    brightness float4 check (brightness between 0 and 1),
    constraint environment_lightning_pk primary key (id),
    constraint environment_lightning_records_fk foreign key (record_id)
        references record (id)
        on delete cascade on update cascade
);
create table chapter_link (
    id         bigserial,
    record_id  bigint,
    chapter_to bigint,
    constraint chapter_link_pk primary key (id),
    constraint chapter_link_records_fk foreign key (record_id)
        references record (id)
        on delete cascade on update cascade,
    constraint chapter_link_chapter_to_fk foreign key (chapter_to)
        references chapter (id)
        on delete cascade on update cascade
);
create table background_music (
    id        bigserial,
    record_id bigint,
    name      text not null,
    base64    text not null,
    constraint background_music_pk primary key (id),
    constraint background_music_records_fk foreign key (record_id)
        references record (id)
        on delete cascade on update cascade
);
create table text (
    id        bigserial,
    record_id bigint,
    text      text,
    constraint text_pk primary key (id),
    constraint text_records_fk foreign key (record_id)
        references record (id)
        on delete cascade on update cascade
);
