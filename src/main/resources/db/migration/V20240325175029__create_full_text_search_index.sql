alter table song
add singer_orchestra_name_search_vector tsvector
generated always as (
  setweight(to_tsvector('simple',singer), 'A')  || ' ' ||
  setweight(to_tsvector('simple',orchestra), 'B') || ' ' ||
  setweight(to_tsvector('spanish',name), 'C') :: tsvector
) stored;

-- add the index
create index idx_search on song using GIN(singer_orchestra_name_search_vector);