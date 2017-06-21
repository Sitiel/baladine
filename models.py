from sqlalchemy import *
from sqlalchemy.orm import relationship, backref

from database import Base


class SQLToJsonModel():
    def toJson(self):
        dict = self.__dict__
        invalid_keys = {"_sa_instance_state"}
        return {x.rsplit('_', 1)[-1]: dict[x] for x in dict if x not in invalid_keys}

#----- Relation -----#

# Example of many to many relationship (see /reset in master_controller to use it)
# Relation entre ingredient et recette
compose = Table('compose', Base.metadata,
                Column('ing_id', Integer, ForeignKey('ingredient.ing_id')),
                Column('recette_id', Integer, ForeignKey('recette.recette_id'))
                )

# Relation entre recette et joueur
possede = Table('possede', Base.metadata,
                Column('recette_id', Integer, ForeignKey('ingredient.ing_id')),
                Column('joueur_id', Integer, ForeignKey('joueur.joueur_id'))
                )

# Relation entre un joueur, un produit et une transaction
participe = Table('participe', Base.metadata,
                Column('joueur_id', Integer, ForeignKey('joueur.joueur_id')),
                Column('recette_id', Integer, ForeignKey('ingredient.ing_id')),
                Column('transaction_id', Integer, ForeignKey('transaction.transaction_id'))
                )

# Relation pour le choix de production du joueur
produit = Table('produit', Base.metadata,
                Column('joueur_id', Integer, ForeignKey('joueur.joueur_id')),
                Column('jour_id', Integer, ForeignKey('journee.jour_id')),
                Column('recette_id', Integer, ForeignKey('recette.recette_id')),
                Column('nombre_prod', Integer),
                Column('prix_vente', Float)
                )

# Relation eentre le joueur et ses zones
#----- Tables -----#

class Ingredient(Base, SQLToJsonModel):
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


class Recette(Base, SQLToJsonModel):
    __tablename__ = "recette"
    recette_id = Column(Integer, primary_key=True)
    recette_nom = Column(String(255))
    #relation vers ingredient
    ingredients = relationship('ingredient', secondary=compose) 

    def __init__(self, nom):
        self.recette_nom = nom


class Joueur(Base, SQLToJsonModel):
    __tablename__ = "joueur"
    joueur_id = Column(Integer, primary_key=True)
    joueur_pseudo = Column(String(255))
    joueur_budget = Column(Integer)
    recettes = relationship('recette', secondary=possede) #relation vers recette
    #represente la carte sur laquelle se trouve le joueur
    carte_id = Column(Integer, ForeignKey('carte.carte_id'))
    carte = relationship("Carte", back_populates="joueurs")
    #represente les zones du joueur
    zones = relationship("Zone", back_populates="joueur")

    def __init__(self, pseudo, budget):
        self.joueur_pseudo = pseudo
        self.joueur_budget = budget


class Zone(Base, SQLToJsonModel):
    __tablename__ = "zone"
    zone_id = Column(Integer, primary_key=True)
    zone_posX = Column(Float)
    zone_posY = Column(Float)
    zone_rayon = Column(Float)
    #represente la carte sur laquelle se trouve le joueur
    joueur_id = Column(Integer, ForeignKey('joueur.joueur_id'))
    joueur = relationship("Zone", back_populates="zones")

    def __init__(self, posX, posY, rayon):
        self.zone_posX = posX
        self.zone_posY = posY
        self.zone_rayon = rayon


class Carte(Base, SQLToJsonModel):
    __tablename__ = "carte"
    carte_id = Column(Integer, primary_key=True)
    carte_largeur = Column(Float)
    carte_longueur = Column(Float)
    #represente les journees par carte
    journees = relationship("Journee", back_populates="carte")
    #represente les joueurs par carte
    joueurs = relationship("Joueurs", back_populates="carte")

    def __init__(self, largeur, longueur):
        self.carte_largeur = largeur
        self.carte_longueur = longueur


class Transaction(Base, SQLToJsonModel):
    __tablename__ = "transaction"
    transaction_id = Column(Integer, primary_key=True)
    transaction_prix = Column(Float)
    #represente la journee pendant laquelle a lieu la transaction
    journee_id = Column(Integer, ForeignKey('journee.jour_id'))
    journee = relationship("Journee", back_populates="transactions")

    def __init__(self, prix):
        self.transaction_prix = prix


class Journee(Base, SQLToJsonModel):
    __tablename__ = "journee"
    jour_id = Column(Integer, primary_key=True)
    jour_date = Column(Date)
    #represente les transactions de la journee
    transactions = relationship("Transaction", back_populates="journee")
    #represente le lien entre la carte et les journees
    carte_id = Column(Integer, ForeignKey('carte.carte_id'))
    carte = relationship("Carte", back_populates="journees")
    #represente le lien entre la meteo et les journees
    meteo_id = Column(Integer, ForeignKey('meteo.meteo_id'))
    meteo = relationship("Meteo", back_populates="journees")

    def __init__(self, date):
        self.jour_date = date

class Meteo(Base, SQLToJsonModel):
    __tablename__ = "meteo"
    meteo_id = Column(Integer, primary_key=True)
    meteo_libelle = Column(String(255))
    #represente les journees ayant cette meteo
    journees = relationship("Journees", back_populates="meteo")

    def __init__(self, libelle):
        self.meteo_libelle = libelle

