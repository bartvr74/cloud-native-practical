create table COCKTAIL (
   ID UUID PRIMARY KEY,
   ID_DRINK TEXT,
   NAME TEXT,
   INGREDIENTS TEXT
);

create table COCKTAIL_SHOPPING_LIST (
    COCKTAIL_ID UUID,
    SHOPPING_LIST_ID UUID
);

alter table COCKTAIL_SHOPPING_LIST add constraint FK_COCKTAIL_ID foreign key (COCKTAIL_ID) references COCKTAIL(ID);
alter table COCKTAIL_SHOPPING_LIST add constraint FK_SHOPPING_LIST foreign key (SHOPPING_LIST_ID) references SHOPPING_LIST(ID);

