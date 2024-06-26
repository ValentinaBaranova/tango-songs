create table song (
    id uuid primary key,
    name text not null,
    orchestra text not null,
    singers text,
    release_year integer,
    track_id text,
    track_id_requested_at timestamp,
    source_filename text,
    source_loaded_at timestamp
);