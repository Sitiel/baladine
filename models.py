from sqlalchemy import *
from sqlalchemy.orm import relationship, backref

from database import Base


class SQLToJsonModel():
    def toJson(self):
        dict = self.__dict__
        invalid_keys = {"_sa_instance_state"}
        return {x.rsplit('_', 1)[-1]: dict[x] for x in dict if x not in invalid_keys}


# Example of many to many relationship (see /reset in master_controller to use it)
compose = Table('compose', Base.metadata,
                Column('ing_id', Integer, ForeignKey('ingredient.ing_id')),
                Column('recette_id', Integer, ForeignKey('recette.recette_id'))
                )


class ingredient(Base, SQLToJsonModel):
    __tablename__ = "ingredient"
    ing_id = Column(Integer, primary_key=True)
    ing_nom = Column(String(255))
    ing_cout = Column(Float)
    ing_alcohol = Column(Boolean)
    ing_froid = Column(Boolean)

    def __init__(self, nom, cout, alcohol, froid):
        self.ing_nom = nom
        self.ing_cout = cout
        self.ing_alcohol = alcohol
        self.ing_froid = froid


class recette(Base, SQLToJsonModel):
    __tablename__ = "recette"
    recette_id = Column(Integer, primary_key=True)
    ingredients = relationship('ingredient', secondary=compose)
    recette_nom = Column(String(255))

    def __init__(self, nom):
        self.recette_nom = nom


class joueur(Base, SQLToJsonModel):
    __tablename__ = "joueur"
    joueur_id = Column(Integer, primary_key=True)
    joueur_pseudo = Column(String(255))
    joueur_budget = Column(Integer)

    def __init__(self, pseudo, budget):
        self.joueur_pseudo = pseudo
        self.joueur_budget = budget


class zone(Base, SQLToJsonModel):
    __tablename__ = "zone"
    zone_id = Column(Integer, primary_key=True)
    zone_posX = Column(Float)
    zone_posY = Column(Float)
    zone_rayon = Column(Float)

    def __init__(self, posX, posY, rayon):
        self.zone_posX = posX
        self.zone_posY = posY
        self.zone_rayon = rayon


class carte(Base, SQLToJsonModel):
    __tablename__ = "carte"
    carte_id = Column(Integer, primary_key=True)
    carte_largeur = Column(Float)
    carte_longueur = Column(Float)

    def __init__(self, largeur, longueur):
        self.carte_largeur = largeur
        self.carte_longueur = longueur


class transaction(Base, SQLToJsonModel):
    __tablename__ = "transaction"
    transaction_id = Column(Integer, primary_key=True)
    transaction_prix = Column(Float)

    def __init__(self, prix):
        self.transaction_prix = prix


class journee(Base, SQLToJsonModel):
    __tablename__ = "journee"
    jour_id = Column(Integer, primary_key=True)
    jour_date = Column(Date)

    def __init__(self, date):
        self.jour_date = date
