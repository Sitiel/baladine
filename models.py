from sqlalchemy import *
from sqlalchemy.orm import relationship, backref

from database import Base
from json_model import *

#----- Relation -----#

# Example of many to many relationship (see /reset in master_controller to use it)
# Relation entre ingredient et recette
compose = Table('compose', Base.metadata,
                Column('ing_id', Integer, ForeignKey('ingredient.ing_id')),
                Column('recette_id', Integer, ForeignKey('recette.recette_id'))
                )

# Relation entre recette et joueur
possede = Table('possede', Base.metadata,
                Column('recette_id', Integer, ForeignKey('recette.recette_id')),
                Column('joueur_id', Integer, ForeignKey('joueur.joueur_id'))
                )

# Relation entre un joueur, un produit et une transaction
participe = Table('participe', Base.metadata,
                Column('joueur_id', Integer, ForeignKey('joueur.joueur_id')),
                Column('recette_id', Integer, ForeignKey('recette.recette_id')),
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

#----- Tables -----#

class ingredient(Base, JsonModel):
    __tablename__ = "ingredient"
    ing_id = Column(Integer, primary_key=True)
    ing_nom = Column(String(255),unique=True)
    ing_cout = Column(Float)
    ing_alcohol = Column(Boolean)
    ing_froid = Column(Boolean)

    def __init__(self, nom, cout, alcohol, froid):
        self.ing_nom = nom
        self.ing_cout = cout
        self.ing_alcohol = alcohol
        self.ing_froid = froid


class recette(Base, JsonModel):
    __tablename__ = "recette"
    recette_id = Column(Integer, primary_key=True)
    recette_nom = Column(String(255))
    #relation vers ingredient
    ingredients = relationship('ingredient', secondary=compose) 

    def __init__(self, nom):
        self.recette_nom = nom


class joueur(Base, JsonModel):
    __tablename__ = "joueur"
    joueur_id = Column(Integer, primary_key=True)
    joueur_pseudo = Column(String(255))
    joueur_budget = Column(Float)
    recettes = relationship('recette', secondary=possede) #relation vers recette
    #represente la carte sur laquelle se trouve le joueur
    carte_id = Column(Integer, ForeignKey('carte.carte_id'))
    carte = relationship("carte", back_populates="joueurs")
    #represente les zones du joueur
    zones = relationship("zone", back_populates="joueur")
    #represente les transactions du joueurs
    transactions = relationship('transaction', secondary=participe)
    recette = relationship('recette', secondary=participe)


    def __init__(self, pseudo, budget):
        self.joueur_pseudo = pseudo
        self.joueur_budget = budget


class zone(Base, JsonModel):
    __tablename__ = "zone"
    zone_id = Column(Integer, primary_key=True)
    zone_posX = Column(Float)
    zone_posY = Column(Float)
    zone_rayon = Column(Float)
    zone_type = Column(String(64))
    #represente la carte sur laquelle se trouve le joueur
    joueur_id = Column(Integer, ForeignKey('joueur.joueur_id'))
    joueur = relationship("joueur", back_populates="zones")

    def __init__(self, posX, posY, rayon, type):
        self.zone_posX = posX
        self.zone_posY = posY
        self.zone_rayon = rayon
        self.zone_type = type


class carte(Base, JsonModel):
    __tablename__ = "carte"
    carte_id = Column(Integer, primary_key=True)
    carte_largeur = Column(Float)
    carte_longueur = Column(Float)
    #represente les journees par carte
    journees = relationship("journee", back_populates="carte")
    #represente les joueurs par carte
    joueurs = relationship("joueur", back_populates="carte")

    def __init__(self, largeur, longueur):
        self.carte_largeur = largeur
        self.carte_longueur = longueur


class transaction(Base, JsonModel):
    __tablename__ = "transaction"
    transaction_id = Column(Integer, primary_key=True)
    transaction_prix = Column(Float)
    #represente la journee pendant laquelle a lieu la transaction
    journee_id = Column(Integer, ForeignKey('journee.jour_id'))
    journee = relationship("journee", back_populates="transactions")
    #represente le joueurs du joueurs
    joueur = relationship('joueur', secondary=participe)
    recette = relationship('recette', secondary=participe)

    def __init__(self, prix):
        self.transaction_prix = prix


class journee(Base, JsonModel):
    __tablename__ = "journee"
    jour_id = Column(Integer, primary_key=True)
    jour_date = Column(Date)
    #represente les transactions de la journee
    transactions = relationship("transaction", back_populates="journee")
    #represente le lien entre la carte et les journees
    carte_id = Column(Integer, ForeignKey('carte.carte_id'))
    carte = relationship("carte", back_populates="journees")
    #represente le lien entre la meteo et les journees
    meteo_id = Column(Integer, ForeignKey('meteo.meteo_id'))
    meteo = relationship("meteo", back_populates="journees")

    def __init__(self, date):
        self.jour_date = date

class meteo(Base, JsonModel):
    __tablename__ = "meteo"
    meteo_id = Column(Integer, primary_key=True)
    meteo_libelle = Column(String(255))
    #represente les journees ayant cette meteo
    journees = relationship("journee", back_populates="meteo")

    def __init__(self, libelle):
        self.meteo_libelle = libelle

